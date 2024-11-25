package com.example.chattingapplication.Utilites.UtilityComposables


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
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
            .clickable(enabled = false) { },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        CircularProgressIndicator()
    }


}


@Composable
fun CheckUserSignedInStatus(viewModel: ApplicationViewModel, navController: NavController) {
    var signedInStatus = remember {
        mutableStateOf(false);
    }

    var signedIn = viewModel.userSignedIn.value;


    if (signedIn && !signedInStatus.value) {
        signedInStatus.value = true;
        navController.navigate(ScreenRoutes.ChatListRoute.route) {
            popUpTo(0);
        };

    }

}


@Composable
fun DividerCommon() {
    HorizontalDivider(
        thickness = 1.dp, color = Color.DarkGray, modifier = Modifier
            .alpha(0.3f)
            .padding(top = 8.dp, bottom = 8.dp)
    )
}


@Composable
fun ImageCommon(
    data: String?,
    modifier: Modifier = Modifier.wrapContentSize(),
    contentScale: ContentScale = ContentScale.Crop
) {
//TODO : add coil compose dependency
    var painter = rememberAsyncImagePainter(model = data)
    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
    )

}


@Composable
fun CommonScreenTitle(txt: String) {
    Text(
        text = txt,
        fontWeight = FontWeight.Bold,
        fontSize = 35.sp,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun CommonRow(imageUrl: String?, name: String?, onItemClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .clickable {
                onItemClick.invoke();
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        ImageCommon(
            data = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Red)
        )
        Text(
            text = name ?: "---",
            fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp)
        )
    }
}