package com.example.chattingapplication.Utilites.UtilityComposables


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.chattingapplication.ScreenRoutes
import com.example.chattingapplication.ViewModels.ApplicationViewModel
import java.util.Stack

@Composable
fun CommonProgressBar() {
    Row(
        modifier =
        Modifier
            .alpha(0.5f)
            .fillMaxSize()
            .background(color = Color.LightGray)
            .clickable(enabled = false) { }
            , verticalAlignment = Alignment.CenterVertically
        , horizontalArrangement = Arrangement.Center

    ) {
        CircularProgressIndicator()
    }


}


@Composable
fun CheckUserSignedInStatus(viewModel:ApplicationViewModel, navController: NavController){
    var signedInStatus = remember{
        mutableStateOf(false);
    }

    var signedIn = viewModel.userSignedIn.value;


    if(signedIn && !signedInStatus.value){
        signedInStatus.value = true;
        navController.navigate(ScreenRoutes.ChatListRoute.route){
            popUpTo(0);
        };

    }
}