package com.example.chattingapplication.Screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.chattingapplication.ViewModels.ApplicationViewModel
import com.example.chattingapplication.ui.BottomNavigationItem
import com.example.chattingapplication.ui.BottomNavigationMenu

@Composable
fun UserProfileScreenComposable(navController: NavController,viewModel:ApplicationViewModel){

    Text(text = "User Profile Page")

    BottomNavigationMenu(selectedScreen = BottomNavigationItem.ALLCHATLIST, navController = navController)

}