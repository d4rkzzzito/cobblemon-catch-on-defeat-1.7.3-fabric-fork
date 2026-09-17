package us.timinc.mc.cobblemon.catchondefeat.event

import com.cobblemon.mod.common.api.events.Cancelable
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.server.level.ServerPlayer

interface JoinDefeatEvent {
    val pokemon: Pokemon
    val player: ServerPlayer

    class Pre(override val player: ServerPlayer, override val pokemon: Pokemon) : JoinDefeatEvent, Cancelable()

    class Post(override val player: ServerPlayer, override val pokemon: Pokemon) : JoinDefeatEvent
}