package com.example.chattingapplication.Models


//* This model is to create a user in the userProfile not to save the user data like email and password this is being mapped because it is going to be saved in the firebase datbase
data class UserProfileModel(
    val userId: String? = "",
    val name: String? = "",
    val number: String? = "",
    val imageUrl: String? = ""
) {
    fun toMap() : Map<String,String?> {
       return mapOf(
            "userId" to userId,
            "name" to name,
            "number" to number,
            "imageUrl" to imageUrl
        )
    }

}