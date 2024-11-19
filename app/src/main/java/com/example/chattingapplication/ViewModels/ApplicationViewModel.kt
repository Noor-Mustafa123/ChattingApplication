package com.example.chattingapplication.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.chattingapplication.Events.Event
import com.example.chattingapplication.Models.UserProfileModel
import com.example.chattingapplication.Utilites.Constants.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// QUESTION why don't we do this error handling and the authentication of data in the model side why do it in the viewModel ? which is used for data transfer?

//! Check the profile screen for some questions
//! ask it to create mind maps of how many possible paths the signup take
//! why are we creating a separate composable method just to check the signinstatus while there is already a userSignedIn" is created already in the view model



@HiltViewModel
class ApplicationViewModel @Inject constructor(
    val fireBaseUser: FirebaseAuth,
    val fireStoredb: FirebaseFirestore
) : ViewModel() {


// create two variables for the event object and for the inProgressStatus

    var inProgress = mutableStateOf(false);
    var mutableStateOfEventObject = mutableStateOf(Event<String?>(null))
    var mutableSignUpComplete = mutableStateOf(false);
    var userSignedIn = mutableStateOf(false);

    // ? My Understanding: if the user is already created the nwe pass a real userDataObject ot it so that instead of creating a new UserProfileModel object in the createOrUpdate we can just pass the availaible one
    var mutableUserDataObject = mutableStateOf<UserProfileModel?>(null);

    //    ! understand how this checking of user signin status is working i dont get it
    init {
        val currentUser = fireBaseUser.currentUser
        userSignedIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it);
        }
    }


    fun signUpUser(name: String, number: String, email: String, password: String) {
        inProgress.value = true;
//    Check Placed for duplicate data for singup
        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please Fill ALL Fields")
            return
        }

        fireStoredb.collection(USER_NODE).whereEqualTo("number",number).get().addOnSuccessListener {
            if (it.isEmpty) {
//? do everything as earlier
                println("SignUp Process has started")
// this will create a new user in the firebase database
                var authResult = fireBaseUser.createUserWithEmailAndPassword(email, password);

                if (authResult.isSuccessful) {
//  for now print a log message
                    userSignedIn.value = true;
                    println("the user has been registered in firebase")
                    Log.d("TAG", "the user has been registered in firebase")
// TODO: Create system to save user in the database too
                    mutableSignUpComplete.value = true;
                    createOrUpdateUser(name, number);

                } else {
//  handle the exception and print out a stack trace and show user an error message
                    handleException(authResult.exception, customMessage = "Sign Up Failed")
                    println("else block has been triggered")
                }
            } else {
                inProgress.value = false;
                handleException(customMessage = "User is already registered")
            }

        }


    }


    fun loginUser(email: String , password: String){


        if(email.isEmpty() or password.isEmpty()){
            handleException(customMessage = "Please fill all fields")
        }
        else{
            inProgress.value = true
        fireBaseUser.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                fireBaseUser.currentUser?.uid?.let {
                    mutableSignUpComplete.value = true;
                    getUserData(it);
                }
            }
            else{
                handleException(it.exception, customMessage = "Log in failed")
            }
         }

        }
    }


    fun createOrUpdateUser(name: String? = null, number: String? = null, imageUrl: String? = null) {
//* A user with email and password is already created in firebase so we only need to create the profile one
        var authId = fireBaseUser.currentUser?.uid;

        var userData = UserProfileModel(
            userId = authId,
            name = name ?: mutableUserDataObject.value?.name,
            number = number ?: mutableUserDataObject.value?.number,
            imageUrl = imageUrl ?: mutableUserDataObject.value?.imageUrl
        )

        authId?.let {
            inProgress.value = true
//            query ot the database for connection
            fireStoredb.collection(USER_NODE).document(authId).get().addOnSuccessListener {
                // place a check inside the success block for if the user exists there
                if (it.exists()) {
                    // update user data
                } else {
                    inProgress.value = false
                    fireStoredb.collection(USER_NODE).document(authId).set(userData);
                    getUserData(authId);
                }
            }.addOnFailureListener {
                handleException(it, "Cannot Retrieve User")

            }
        }


    }


    fun getUserData(authId: String) {
        inProgress.value = true;
        fireStoredb.collection(USER_NODE).document(authId).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error, "Cannot Retrieve User");
                inProgress.value = false;
            }
            if (value != null) {
                var UserData: UserProfileModel? = value.toObject<UserProfileModel>()
                mutableUserDataObject.value = UserData;
                inProgress.value = false;
            }
        }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {

        exception?.printStackTrace()

        var errorMessage = exception?.localizedMessage ?: "";

        var message = if (customMessage.isNullOrEmpty()) errorMessage else customMessage;

        // all of this process is for the event object to be created

        mutableStateOfEventObject.value = Event<String>(message);
        inProgress.value = false;


    }


}