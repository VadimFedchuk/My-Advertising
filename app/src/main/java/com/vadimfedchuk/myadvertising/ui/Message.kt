package com.vadimfedchuk.myadvertising.ui

data class Message(
                   val isMyMessage: Boolean,
                   val message: String,
                   val date: String,
                   val nameCompanion: String?,
                   val photoCompanion: String?
)