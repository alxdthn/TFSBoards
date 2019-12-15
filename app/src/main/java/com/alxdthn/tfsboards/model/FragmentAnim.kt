package com.alxdthn.tfsboards.model

import androidx.annotation.AnimRes

data class FragmentAnim(
	@AnimRes val enter: Int,
	@AnimRes val exit: Int,
	@AnimRes val popEnter: Int,
	@AnimRes val popExit: Int
)