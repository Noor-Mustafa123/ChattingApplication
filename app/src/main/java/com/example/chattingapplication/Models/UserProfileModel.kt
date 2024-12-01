package com.example.chattingapplication.Models


//* This model is to create a user in the userProfile not to save the user data like email and password this is being mapped because it is going to be saved in the firebase datbase
data class UserProfileModel(
    val userId: String? = "",
    val name: String? = "",
    val number: String? = "",
    val imageUrl: String? = ""
) {
    fun toMap(): Map<String, String?> {
        return mapOf(
            "userId" to userId,
            "name" to name,
            "number" to number,
            "imageUrl" to imageUrl
        )
    }

}


data class ChatData(
    val chatId: String? = "",
//    dont understand this expression
    val user1: ChatUser = ChatUser(),
    val user2: ChatUser = ChatUser()
)

data class ChatUser(
    val userId: String? = "",
    val name: String? = "",
    val imageUrl: String? = "",
    val number: String? = ""
)

data class Message(
    var sendBy: String? = "",
    val message: String? = "",
    val timestamp: String? = ""
)

data class Status(
    val user: ChatUser = ChatUser(),
    val imageUrl: String? = "",
    val timeStamp: Long?
){
    // No-argument constructor required for Firebase deserialization
    constructor() : this(ChatUser(), null, 0)
}