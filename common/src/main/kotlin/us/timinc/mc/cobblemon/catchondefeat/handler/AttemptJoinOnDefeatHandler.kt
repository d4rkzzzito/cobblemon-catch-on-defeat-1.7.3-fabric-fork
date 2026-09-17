package us.timinc.mc.cobblemon.catchondefeat.handler

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.events.CobblemonEvents.POKEMON_CAPTURED
import com.cobblemon.mod.common.api.events.battles.BattleFaintedEvent
import com.cobblemon.mod.common.api.events.pokemon.PokemonCapturedEvent
import com.cobblemon.mod.common.api.pokeball.PokeBalls
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.getPlayer
import net.minecraft.server.level.ServerPlayer
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.CustomProperties.CATCH_ON_DEFEAT
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.CustomProperties.DEFEAT_JOIN_CHANCE
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.CustomProperties.MUST_BE_SOLOED
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.Events.JOIN_DEFEAT_POST
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.Events.JOIN_DEFEAT_PRE
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.Holders.JOIN_CONFIRM
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.Network.sendClientPacket
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.TranslationComponents.joinedTeam
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.TranslationComponents.ranAway
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.TranslationComponents.thereCanOnlyBeOne
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.config
import us.timinc.mc.cobblemon.catchondefeat.event.JoinDefeatEvent
import us.timinc.mc.cobblemon.catchondefeat.network.JoinConfirmReceipt
import us.timinc.mc.cobblemon.timcore.AbstractHandler
import java.util.*
import kotlin.random.Random.Default.nextFloat

object AttemptJoinOnDefeatHandler : AbstractHandler<BattleFaintedEvent>() {

    override fun handle(evt: BattleFaintedEvent) {
        val pokemon = evt.killed.effectedPokemon

        if (!evt.battle.isPvW || !pokemon.isWild()) return

        val excluded = config.excludedLabels.map { it.lowercase() }
        if (excluded.isNotEmpty()) {
            val speciesLabels = pokemon.species.labels.map { it.lowercase() }
            if (speciesLabels.any { it in excluded }) return
        }

        val hasExplicitNo = pokemon.persistentData.contains("catch_on_defeat") &&
                !pokemon.persistentData.getBoolean("catch_on_defeat")

        if (hasExplicitNo && !config.everybodysCaughtThisWay) return

        val players = evt.battle.playerUUIDs.mapNotNull(UUID::getPlayer)

        val mustBeSoloed = MUST_BE_SOLOED.pokemonMatcher(pokemon, true)
        if (players.size > 1 && (config.thereCanOnlyBeOnePlayerInBattle || mustBeSoloed)) {
            for (player in players) {
                player.sendSystemMessage(
                    thereCanOnlyBeOne(pokemon)
                )
            }
            return
        }

        val chance = DEFEAT_JOIN_CHANCE.getValue(pokemon) ?: config.defaultJoinChance
        val roll = nextFloat() * 100F
        if (roll > chance) {
            for (player in players) {
                player.sendSystemMessage(
                    ranAway(pokemon)
                )
            }
            return
        }

        val player = players.random()

        JOIN_DEFEAT_PRE.postThen(
            JoinDefeatEvent.Pre(
                player, pokemon
            ), {
                return
            }, {}
        )

        val clonedPokemon = pokemon.clone()

        if (config.alwaysAcceptJoin) {
            finishJoin(player, clonedPokemon)
        } else {
            val receipt = JoinConfirmReceipt.Data(clonedPokemon)
            val packetId = JOIN_CONFIRM.hangReceipt(player, receipt)
            val packet = receipt.toPacket(packetId)
            sendClientPacket(packet, player)
        }
    }

    fun finishJoin(player: ServerPlayer, pokemon: Pokemon, silently: Boolean = false) {
        val storage = Cobblemon.storage.getParty(player)
        if (config.heal) pokemon.heal()
        storage.add(pokemon)
        if (config.countsAsCapture) {
            POKEMON_CAPTURED.emit(
                PokemonCapturedEvent(
                    pokemon, player, EmptyPokeBallEntity(
                        PokeBalls.POKE_BALL, player.level()
                    )
                )
            )
        }

        if (!silently) {
            player.sendSystemMessage(
                joinedTeam(pokemon)
            )
        }

        JOIN_DEFEAT_POST.emit(
            JoinDefeatEvent.Post(player, pokemon)
        )
    }
}