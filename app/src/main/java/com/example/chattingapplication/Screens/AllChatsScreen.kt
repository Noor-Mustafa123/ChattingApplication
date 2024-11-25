package com.example.chattingapplication.Screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chattingapplication.ScreenRoutes
import com.example.chattingapplication.Utilites.UtilityComposables.CommonProgressBar
import com.example.chattingapplication.Utilites.UtilityComposables.CommonRow
import com.example.chattingapplication.Utilites.UtilityComposables.CommonScreenTitle
import com.example.chattingapplication.ViewModels.ApplicationViewModel
import com.example.chattingapplication.ui.BottomNavigationItem
import com.example.chattingapplication.ui.BottomNavigationMenu


@Composable
fun AllChatScreenComposable(navController: NavController, viewModel: ApplicationViewModel) {

    if (viewModel.inProgressChats.value) {
        CommonProgressBar();
    } else {
        val chats = viewModel.chats.value;
        val userData = viewModel.mutableUserDataObject.value;
        val showDialog = remember {
            mutableStateOf(false);
        }
        val onFabClick = {
            showDialog.value = true
        }
        val onDismiss = {
            showDialog.value = false
        }
        val onAddChat: (String) -> Unit = {
            viewModel.onAddChatClick(it);
            showDialog.value = false
//            TODO: Further setting here
        }
        Scaffold(
            floatingActionButton = {

                showAddChatAlert(
                    showDialog = showDialog.value,
                    onAddChat = onAddChat,
                    onDismiss = onDismiss,
                    onFabClick = onFabClick
                )
            },
            bottomBar = {
                BottomNavigationMenu(
                    selectedScreen = BottomNavigationItem.ALLCHATLIST,
                    navController = navController
                )
            }
        ) { innerPadding ->
            // Screen content with padding
            Column(modifier = Modifier.padding(innerPadding)) {

                CommonScreenTitle("Chats")

                if (chats.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "No Chats Available")
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(chats) { chat ->
                            val chatUser = if (chat.user1.userId == userData?.userId) {
                                chat.user2
                            } else {
                                chat.user1
                            }
                            CommonRow(imageUrl = chatUser.imageUrl, name = chatUser.name) {
                                chat.chatId?.let {

                                    navController.navigate(
                                        ScreenRoutes.SingleChatRoute.createRoute(
                                            id = it
                                        )
                                    )

                                }
                            }
                        }
                    }
                }

            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun showAddChatAlert(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onFabClick: () -> Unit,
    onAddChat: (String) -> Unit
) {
//    this will be the phone number of the user
    val addChatMember = remember { mutableStateOf("") }

//    find an alternate because this is deprecated maybe thats why it is causing this error
    if (showDialog) {
        AlertDialog(onDismissRequest = {
            onDismiss.invoke()
            addChatMember.value = "";
        },
            confirmButton = {

                Button(onClick = { onAddChat(addChatMember.value) }) {
                    Text("Add Chat")
                }
            },
            title = {
                Text(text = "Create New Chat", fontSize = 18.sp)
            },
            text = {
                OutlinedTextField(
                    value = addChatMember.value,
                    onValueChange = {
                        addChatMember.value = it;
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            })
    }



    FloatingActionButton(
        onClick = { onFabClick.invoke() },
        containerColor = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(bottom = 40.dp),
        shape = CircleShape
    ) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.White)
    }


}

