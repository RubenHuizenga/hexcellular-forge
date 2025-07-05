package com.proton.rubenhuizenga.hexcellular.action

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import com.proton.rubenhuizenga.hexcellular.StateStorage
import com.proton.rubenhuizenga.hexcellular.getProperty

object OpObserveProperty : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment) =
		listOf(StateStorage.getProperty(env.world, args.getProperty(0, argc)))
}