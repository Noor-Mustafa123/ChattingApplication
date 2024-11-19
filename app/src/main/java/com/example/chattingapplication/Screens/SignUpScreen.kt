package com.example.chattingapplication.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chattingapplication.R
import com.example.chattingapplication.ScreenRoutes
import com.example.chattingapplication.Utilites.UtilityComposables.CheckUserSignedInStatus
import com.example.chattingapplication.Utilites.UtilityComposables.CommonProgressBar
import com.example.chattingapplication.ViewModels.ApplicationViewModel


@Composable
fun SignUpScreenComposable(navController: NavController, viewModel: ApplicationViewModel) {

        CheckUserSignedInStatus(viewModel,navController);


    var nameState = remember() {
        mutableStateOf(TextFieldValue())
    }
    var numberState = remember() {
        mutableStateOf(TextFieldValue())
    }
    var emailState = remember() {
        mutableStateOf(TextFieldValue())
    }
    var passwordState = remember() {
        mutableStateOf(TextFieldValue())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.sofa),
            contentDescription = null,
            modifier = Modifier
                .width(200.dp)
                .padding(top = 16.dp)
                .padding(8.dp)
        )
        Text(
            text = "Sign Up",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(8.dp)
        )


        OutlinedTextField(value = nameState.value, onValueChange = {
            nameState.value = it
            println(nameState.value);
        }, modifier = Modifier.padding(8.dp), label = {
            Text(text = "Name")
        })
        OutlinedTextField(value = numberState.value, onValueChange = {
            numberState.value = it
            println(numberState.value);
        }, modifier = Modifier.padding(8.dp), label = {
            Text(text = "Phone Number")
        })

        OutlinedTextField(value = emailState.value, onValueChange = {
            emailState.value = it
            println(emailState.value);
        }, modifier = Modifier.padding(8.dp), label = {
            Text(text = "Email")
        })

        OutlinedTextField(value = passwordState.value, onValueChange = {
            passwordState.value = it
            println(passwordState.value);
        }, modifier = Modifier.padding(8.dp), label = {
            Text(text = "Password")
        })

        Button(onClick = {
// * forwarded the saved state data to the view model
            viewModel.signUpUser(
                name = nameState.value.text,
                number = numberState.value.text,
                password = passwordState.value.text,
                email = emailState.value.text
            )
        }, modifier = Modifier.padding(16.dp)) {
            Text(text = "Sign Up")
        }


        Text(
            text = "Already have an account? Log In here ->",
            color = Color.Blue,
            modifier = Modifier.clickable {
                navController.navigate(ScreenRoutes.LoginRoute.route);
                Log.d("TAG", "User has been redirected to login screen")
            })

    }
//  ???????  when will the function be reset that the progress bar will be shown and when will it run again to check if the progress bar need to be removed now ??????
    if(viewModel.inProgress.value){
//        show progress bar composable
        println("check has been triggered")
        CommonProgressBar();
    }

}