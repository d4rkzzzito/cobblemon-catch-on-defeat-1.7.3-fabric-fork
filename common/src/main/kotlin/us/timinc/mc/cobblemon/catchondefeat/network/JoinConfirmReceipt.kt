package us.timinc.mc.cobblemon.catchondefeat.network

import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.events.storage.ReleasePokemonEvent
import com.cobblemon.mod.common.pokemon.Pokemon
import io.wispforest.owo.network.ClientAccess
import io.wispforest.owo.network.ServerAccess
import net.minecraft.network.chat.Component
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.Holders.JOIN_CONFIRM
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.Network.sendServerPacket
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.TranslationComponents.wasReleased
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.config
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.debugger
import us.timinc.mc.cobblemon.catchondefeat.handler.AttemptJoinOnDefeatHandler
import us.timinc.mc.cobblemon.catchondefeat.screen.ConfirmJoinScreen
import us.timinc.mc.cobblemon.timcore.Holder
import java.util.*

object JoinConfirmReceipt {
    @JvmRecord
    data class Packet(
        val id: UUID,
        val name: Component,
    ) {
        fun accept() {
            sendServerPacket(Response(id, true))
        }

        fun reject() {
            sendServerPacket(Response(id, false))
        }
    }

    @JvmRecord
    data class Response(
        val id: UUID,
        val accepted: Boolean,
    )

    class Data(
        val pokemon: Pokemon,
    ) : Holder.ReceiptPacketMaker<Packet> {
        override fun toPacket(id: UUID) = Packet(id, pokemon.species.translatedName)
    }

    fun handleClient(data: Packet, clientAccess: ClientAccess) {
        if (config.alwaysAcceptJoin) {
            data.accept()
            return
        }
        clientAccess.runtime().setScreen(ConfirmJoinScreen(data))
    }

    fun handleServer(data: Response, serverAccess: ServerAccess) {
        try {
            val receipt = JOIN_CONFIRM.pullReceipt(data.id, serverAccess.player)
            if (!data.accepted) {
                receipt.player.sendSystemMessage(wasReleased(receipt.data.pokemon.species.translatedName))
                if (config.rejectsCountAsRelease) {
                    AttemptJoinOnDefeatHandler.finishJoin(receipt.player, receipt.data.pokemon, true)

                    val storage = receipt.data.pokemon.storeCoordinates.get()?.store ?: return
                    val pokemon = receipt.data.pokemon
                    CobblemonEvents.POKEMON_RELEASED_EVENT_PRE.postThen(
                        event = ReleasePokemonEvent.Pre(receipt.player, pokemon, storage),
                        ifSucceeded = {
                            storage.remove(pokemon)
                            CobblemonEvents.POKEMON_RELEASED_EVENT_POST.post(
                                ReleasePokemonEvent.Post(
                                    receipt.player,
                                    pokemon,
                                    storage
                                )
                            )
                        }
                    )
                }
                return
            }
            AttemptJoinOnDefeatHandler.finishJoin(receipt.player, receipt.data.pokemon)
        } catch (e: Error) {
            debugger.debug(e.message ?: "An error occurred while handling JoinConfirmReceipt on server.", true)
        }
    }
}