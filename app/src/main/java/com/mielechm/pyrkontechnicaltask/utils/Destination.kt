package com.mielechm.pyrkontechnicaltask.utils

import kotlinx.serialization.Serializable

class Destination {

    @Serializable
    data object GuestsListView

    @Serializable
    data class GuestDetailsView(val name: String)
}