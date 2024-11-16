package com.example.chattingapplication.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chattingapplication.ViewModels.ApplicationViewModel

@Composable
fun LoginScreenComposable(navController: NavController, viewModel: ApplicationViewModel) {

//Companion object are like objects which are singleton availabe around the project for use
    Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.TopCenter) {
        Text(text = "Hello this is the loginScreen");
    }
}