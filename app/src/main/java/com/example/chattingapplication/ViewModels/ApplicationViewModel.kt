package com.example.chattingapplication.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.chattingapplication.Events.Event
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// QUESTION why dont we do this error handling and the authentication of data in the model side why do it in the viewModel ? which is used for data transfer?


@HiltViewModel
class ApplicationViewModel @Inject constructor(val authObject: FirebaseAuth) : ViewModel() {

// create two variables for the event object and for the inProgressStatus

    var mutableStateOfInProgressStatus = mutableStateOf(false);
    var mutableStateOfEventObject = mutableStateOf(Event<String?>(null))


    fun signUpUser(name: String, number: String, email: String, password: String) {
        mutableStateOfInProgressStatus.value = true;
// this will create a new user in the firebase database
        var authResult = authObject.createUserWithEmailAndPassword(email, password);



        if (authResult.isSuccessful) {
//  for now print a log message
            Log.d("TAG", "the user has been registered in firebase")
// TODO: Create system to save user in the database too

        } else {
//  handle the exception and print out a stack trace and show user an error message
            handleException(authResult.exception)
        }

    }

//  why are we creating a exceptionhandler here? shouldnt we use throwables to throw it up the stack and make a global exception handler to handle all those specific type of exceptions?

    fun handleException(exception: Exception? = null, customMessage: String = "") {

        exception?.printStackTrace()

        var errorMessage = exception?.localizedMessage ?: "";

        var message = if (customMessage.isNullOrEmpty()) errorMessage else customMessage;

        // all of this process is for the event object to be created

        mutableStateOfEventObject.value = Event<String>(message);
        mutableStateOfInProgressStatus.value = false;


    }


}