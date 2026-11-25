package com.howellkeatingfonseca.findanddine.data.model

data class Rating(
    val id: Int = 0,
    val restaurantId: String,
    val stars: Int,
    val visitDate: Long,
    val note: String = "",
    val rank: Int = 0
) {
    companion object {
        const val MIN_STARS = 1
        const val MAX_STARS = 5
    }
}
