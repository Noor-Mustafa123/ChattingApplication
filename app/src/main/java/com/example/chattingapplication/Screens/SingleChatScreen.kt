package com.example.chattingapplication.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chattingapplication.Models.Message
import com.example.chattingapplication.Utilites.UtilityComposables.DividerCommon
import com.example.chattingapplication.Utilites.UtilityComposables.ImageCommon
import com.example.chattingapplication.ViewModels.ApplicationViewModel


//Bug all of the items on the single chat screen are showing on the left side


@Composable
fun SingleChatScreenComposable(
    viewModel: ApplicationViewModel,
    navController: NavController,
    chatId: String
) {

    var reply by rememberSaveable {
        mutableStateOf("")
    }

    val onSendReply = {
        viewModel.onSendReply(chatId, reply);
        reply = ""

    }
    val myUser = viewModel.mutableUserDataObject.value
    println(viewModel.chats.value)
    println(chatId)
    val currentChat = viewModel.chats.value.first { it.chatId == chatId }
    val chatUser =
        if (myUser?.userId == currentChat.user1.userId) currentChat.user2 else currentChat.user1



    Scaffold(
        bottomBar = {
            ReplyBox(reply = reply, onReplyChange = { reply = it }, onSendReply = onSendReply)
        }
    ) { innerPadding ->
        // Screen content with padding
        Column(modifier = Modifier.padding(innerPadding)) {


            LaunchedEffect(key1 = Unit) {
                viewModel.populateMessages(chatId)
            }

            BackHandler {
                viewModel.depopulateMessage()
            }

            Column {
                ChatHeader(name = chatUser.name ?: "", imageUrl = chatUser.imageUrl ?: "") {
                    navController.popBackStack()
                    viewModel.depopulateMessage()
                }
            }

            MessageBox(
                modifier = Modifier,
                chatMessages = viewModel.chatMessages.value,
                currentUserId = myUser?.userId ?: ""
            )
//            ReplyBox(reply = reply, onReplyChange = { reply = it }, onSendReply = onSendReply)
        }
    }
}



@Composable
fun MessageBox(modifier: Modifier, chatMessages: List<Message>, currentUserId: String) {
    LazyColumn(modifier = modifier) {
        items(chatMessages) { msg ->

                val alignment = if (msg.sendBy == currentUserId) Alignment.End else Alignment.Start

                val color =
                    if (msg.sendBy == currentUserId) Color(0xFF68C400) else Color(0xFFC0C0C0)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = alignment
                ) { Text(
                    text = msg.message ?: "",
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .padding(12.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )}


        }
    }
}

@Composable
fun ChatHeader(name: String, imageUrl: String, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .clickable {
                    onBackClick.invoke()
                }
                .padding(8.dp)
        )
        ImageCommon(
            data = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
        )
        Text(text = name, fontWeight = FontWeight.Bold, modifier = Modifier.padding(4.dp))

    }
}


@Composable
fun ReplyBox(reply: String, onReplyChange: (String) -> Unit, onSendReply: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        DividerCommon()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(value = reply, onValueChange = onReplyChange, maxLines = 3)
            Button(onClick = onSendReply) {
                Text(text = "Send")
            }
        }
    }
}








