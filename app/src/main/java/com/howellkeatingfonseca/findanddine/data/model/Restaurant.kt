package com.howellkeatingfonseca.findanddine.data.model

data class Restaurant(
    val id: String,
    val name: String,
    val address: String,
    val city: String? = null,
    val cuisine: String? = null,
    val latitude: Double,
    val longitude: Double,
    val hours: String? = null,
    val isFavourite: Boolean = false
)
