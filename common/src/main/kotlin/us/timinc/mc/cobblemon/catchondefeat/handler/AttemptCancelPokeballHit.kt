package us.timinc.mc.cobblemon.catchondefeat.handler

import com.cobblemon.mod.common.api.events.pokeball.ThrownPokeballHitEvent
import net.minecraft.server.level.ServerPlayer
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.CustomProperties.CATCH_ON_DEFEAT
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.CustomProperties.PREVENT_REGULAR_CAPTURE
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.TranslationComponents.cantCatch
import us.timinc.mc.cobblemon.catchondefeat.CatchOnDefeat.config
import us.timinc.mc.cobblemon.timcore.AbstractHandler

object AttemptCancelPokeballHit : AbstractHandler<ThrownPokeballHitEvent>() {
    override fun handle(evt: ThrownPokeballHitEvent) {
        val pokemon = evt.pokemon.pokemon
        if ((PREVENT_REGULAR_CAPTURE.pokemonMatcher(
                pokemon, true
            ) || config.preventRegularCapture) && (CATCH_ON_DEFEAT.pokemonMatcher(
                pokemon, true
            ) || config.everybodysCaughtThisWay)
        ) {
            (evt.pokeBall.owner as? ServerPlayer)?.sendSystemMessage(cantCatch(pokemon))
            evt.cancel()
        }
    }
}