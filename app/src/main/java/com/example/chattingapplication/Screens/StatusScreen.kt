package com.example.chattingapplication.Screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chattingapplication.ScreenRoutes
import com.example.chattingapplication.Utilites.UtilityComposables.CommonProgressBar
import com.example.chattingapplication.Utilites.UtilityComposables.CommonRow
import com.example.chattingapplication.Utilites.UtilityComposables.CommonScreenTitle
import com.example.chattingapplication.Utilites.UtilityComposables.DividerCommon
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
        val statuses = viewModel.status.value
        val userData = viewModel.mutableUserDataObject.value
        val myStatuses = statuses.filter {
            it.user.userId == userData?.userId
        }
        val otherStatuses = statuses.filter {
            it.user.userId != userData?.userId
        }
        val launcher = rememberLauncherForActivityResult(contract= ActivityResultContracts.GetContent()) {
            uri ->
            uri?.let {
                viewModel.uploadStatus(uri = uri)
            }
        }
        Scaffold(
            floatingActionButton = {
                FAB {
                    launcher.launch("image/*")
                }
            },
            content = {
                Column(modifier = Modifier.fillMaxSize().padding(it)) {
                    CommonScreenTitle(txt = "Status")
                    if (statuses.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "No Statuses Available")
                        }
                    } else {
                        if (myStatuses.isNotEmpty()) {
                            CommonRow(
                                imageUrl = myStatuses[0].user.imageUrl,
                                name = myStatuses[0].user.name
                            ) {
                                viewModel.bugFixStatusId.value = myStatuses[0].user.userId!!
                                println(viewModel.bugFixStatusId.value)
                                println("print statment")
                                ScreenRoutes.SingleStatusRoute.createRoute(myStatuses[0].user.userId!!)
                                navController.navigate(ScreenRoutes.SingleStatusRoute.route)
                            }
                            DividerCommon()
                            val uniqueUsers = otherStatuses.map { it.user }.toSet().toList()
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(uniqueUsers) { user ->
                                    CommonRow(imageUrl = user.imageUrl, name = user.name){
                                        viewModel.bugFixStatusId.value = user.userId!!
                                        println(viewModel.bugFixStatusId.value)
                                        println("print statment")
                                        ScreenRoutes.SingleStatusRoute.createRoute(user.userId)
                                        navController.navigate(ScreenRoutes.SingleStatusRoute.route)
                                    }
                                }
                            }
                        }

                    }
                }
            },
            bottomBar = {
                BottomNavigationMenu(
                    selectedScreen = BottomNavigationItem.STATUSLIST,
                    navController = navController
                )
            }
        )


    }


}


@Composable
fun FAB(onFabClick: () -> Unit) {
    FloatingActionButton(
        onClick = onFabClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Edit,
            contentDescription = "Add Status",
            tint = Color.White
        )
    }

}

