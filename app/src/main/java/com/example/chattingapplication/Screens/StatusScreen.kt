package com.example.chattingapplication.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chattingapplication.Utilites.UtilityComposables.CommonProgressBar
import com.example.chattingapplication.ViewModels.ApplicationViewModel
import com.example.chattingapplication.ui.BottomNavigationItem
import com.example.chattingapplication.ui.BottomNavigationMenu

@Composable
fun StatusScreenComposable(viewModel: ApplicationViewModel, navController: NavController) {

    Text(text = "All Status Page")

//    TODO: Look into this question
//? look into wether the data in the composable be kept inside the else condition i see the reason that the form should not be editable during the loading but then why isnt this done in sign up screen
    if (viewModel.inProgress.value) {

        CommonProgressBar();
    } else {

//        This is a different ui block
        BottomNavigationMenu(
            selectedScreen = BottomNavigationItem.STATUSLIST,
            navController = navController
        )


    }


}

