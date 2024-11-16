package com.example.chattingapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp



// This is done to give dagger hilt application level access so that we can use it application wide
@HiltAndroidApp
class LiveChatApplication : Application()