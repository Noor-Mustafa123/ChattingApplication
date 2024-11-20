package com.example.chattingapplication.Screens

import android.inputmethodservice.Keyboard
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chattingapplication.ScreenRoutes
import com.example.chattingapplication.Utilites.UtilityComposables.CommonProgressBar
import com.example.chattingapplication.Utilites.UtilityComposables.DividerCommon
import com.example.chattingapplication.Utilites.UtilityComposables.ImageCommon
import com.example.chattingapplication.ViewModels.ApplicationViewModel
import com.example.chattingapplication.ui.BottomNavigationItem
import com.example.chattingapplication.ui.BottomNavigationMenu

@Composable
fun UserProfileScreenComposable(navController: NavController, viewModel: ApplicationViewModel) {

    if (viewModel.inProgress.value) {
        CommonProgressBar()
    } else {

        val userData = viewModel.mutableUserDataObject.value;
        var name by rememberSaveable {
            mutableStateOf(userData?.name ?: "")
        }
        var number by rememberSaveable {
            mutableStateOf(userData?.number ?: "")
        }

        ProfileScreenContent(
            name = name,
            number = number,
            atBack = {
                navController.navigate(route = ScreenRoutes.ChatListRoute.route)
            },
            atSave = {
                viewModel.createOrUpdateUser(name = name, number = number)
            },
            viewModel = viewModel,
            modifier = Modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            onNameChange = { name = it },
            onNumberChange = { name = it },
        ) {
            viewModel.logout();
            navController.navigate(route = ScreenRoutes.ChatListRoute.route)
        }

        BottomNavigationMenu(
            selectedScreen = BottomNavigationItem.PROFILEPAGE,
            navController = navController
        )


    }
}


//       This will be a different ui block
@Composable
fun ProfileScreenContent(
    atBack: () -> Unit,
    atSave: () -> Unit,
    viewModel: ApplicationViewModel,
    modifier: Modifier,
    name: String,
    number: String,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onLogout: () -> Unit
) {

    var imageUrl = viewModel.mutableUserDataObject.value?.imageUrl;

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Back", modifier = Modifier.clickable {
                atBack.invoke()
            })
            Text(text = "Save", modifier = Modifier.clickable {
                atSave.invoke()
            })
        }
        DividerCommon();
        ProfilePicture(viewModel = viewModel, imageURL = imageUrl)

        DividerCommon()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name", modifier = Modifier.width(100.dp))
            TextField(
                value = name, onValueChange = onNameChange, colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Number", modifier = Modifier.width(100.dp))
            TextField(
                value = number, onValueChange = onNumberChange, colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
        }
        DividerCommon()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Logout", modifier = Modifier.clickable {
                onLogout.invoke();
            })
        }
    }
}


//because the image will take inputs and a lot of logic we will create a sepereate composable for that

@Composable
fun ProfilePicture(imageURL: String?, viewModel: ApplicationViewModel) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),

        ) { uri ->
        uri?.let {
            viewModel.uploadProfileImage(uri)
        }
    }
    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .padding(8.dp)
                .clickable {
                    launcher.launch("image/*")
                }, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape, modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            ) {
                ImageCommon(imageURL)
            }
        }
        Text(text = "Change Profile Picture")
    }
    if (viewModel.inProgress.value) {
        CommonProgressBar();
    }
}




