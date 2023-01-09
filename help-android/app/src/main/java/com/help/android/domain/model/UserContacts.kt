package com.help.android.domain.model

data class UserContacts(
    val phoneNumber: String,
    val vkLink: String? = null,
    val tgLink: String? = null,
)

val fakeUserContacts = UserContacts("+1 234 567 89 00")
