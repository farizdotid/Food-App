package org.codejudge.application.model.ui

data class NearbyPlace(
    val id: String = "",
    val image: String = "",
    val storeName: String = "-",
    val storeAddress: String = "-",
    val rating: Double = 0.0,
    val isOpen: Boolean = false
)
