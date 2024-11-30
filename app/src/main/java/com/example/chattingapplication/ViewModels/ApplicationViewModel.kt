package com.example.chattingapplication.ViewModels

import android.net.Uri
import android.util.Log
import androidx.activity.result.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.semantics.error
import androidx.concurrent.futures.await
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.chattingapplication.Events.Event
import com.example.chattingapplication.Models.ChatData
import com.example.chattingapplication.Models.ChatUser
import com.example.chattingapplication.Models.Message
import com.example.chattingapplication.Models.Status
import com.example.chattingapplication.Models.UserProfileModel
import com.example.chattingapplication.Utilites.Constants.CHATS
import com.example.chattingapplication.Utilites.Constants.MESSAGE
import com.example.chattingapplication.Utilites.Constants.STATUS
import com.example.chattingapplication.Utilites.Constants.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject
import com.google.firebase.firestore.ktx.toObjects as toObjectsKtx

// QUESTION why don't we do this error handling and the authentication of data in the model side why do it in the viewModel ? which is used for data transfer?

//TODO: Replace the firebase storage of images with the supabase storage dependency
//TODO: Get the project running
//TODO: make an apk of the project
//TODO: Write a description for this project on github
//TODO: write a message containing how mr jalees mukaram told you to learn jetpackcompose and you created this proejct
//TODO: Create a tree like structure of the project paths like the sign up path and the login path and the relevent classes and functiosn and their flow respectivley
//TODO: USE greptile for the free mind maps

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    val fireBaseUser: FirebaseAuth,
    val fireStoredb: FirebaseFirestore,
    val firebaseImageStorage: FirebaseStorage,
    val supabaseClient: SupabaseClient
) : ViewModel() {

    var bugFixChatId = mutableStateOf("");
    // create two variables for the event object and for the inProgressStatus
    var inProgressChats = mutableStateOf(false);
    var inProgress = mutableStateOf(false);
    var mutableStateOfEventObject = mutableStateOf(Event<String?>(null))
    var mutableSignUpComplete = mutableStateOf(false);
    var userSignedIn = mutableStateOf(false);
    var chats = mutableStateOf<List<ChatData>>(listOf())
    var chatMessages = mutableStateOf<List<Message>>(listOf())
    var inProgressChatMessage = mutableStateOf(false);
    var currentChatMessageListener: ListenerRegistration? = null;

    // ? My Understanding: if the user is already created the nwe pass a real userDataObject ot it so that instead of creating a new UserProfileModel object in the createOrUpdate we can just pass the availaible one
    var mutableUserDataObject = mutableStateOf<UserProfileModel?>(null);
    var status = mutableStateOf<List<Status>>(listOf());
    var inProgressStatus = mutableStateOf(false);


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

        fireStoredb.collection(USER_NODE).whereEqualTo("number", number).get()
            .addOnSuccessListener {
                if (it.isEmpty) {
//? do everything as earlier
                    println("SignUp Process has started")
// this will create a new user in the firebase database
                    fireBaseUser.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {

                            if (it.isSuccessful) {
//  for now print a log message
                                userSignedIn.value = true;
                                println("the user has been registered in firebase")
                                Log.d("TAG", "the user has been registered in firebase")
// TODO: Create system to save user in the database too
                                mutableSignUpComplete.value = true;
                                createOrUpdateUser(name, number);

                            } else {
//  handle the exception and print out a stack trace and show user an error message
                                handleException(
                                    it.exception,
                                    customMessage = "Sign Up Failed"
                                )
                                println("else block sinde the onComplete listneer has been triggered")
                            }


                        }


                } else {
                    inProgress.value = false;
                    handleException(customMessage = "User is already registered")
                }

            }


    }


    fun loginUser(email: String, password: String) {


        if (email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill all fields")
        } else {
            inProgress.value = true
            fireBaseUser.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    fireBaseUser.currentUser?.uid?.let {
                        mutableSignUpComplete.value = true;
                        getUserData(it);

                    }
                } else {
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
//            query ot the database for retrieval
            fireStoredb.collection(USER_NODE).document(authId).get().addOnSuccessListener {
                // place a check inside the success block for if the user exists there
                if (it.exists()) {
                    // update user date
                    println("the exists check was triggered")
                    fireStoredb.collection(USER_NODE).document(authId).set(userData)
                        .addOnSuccessListener {
                            getUserData(authId);
                            inProgress.value = false
                        }
                        .addOnFailureListener {
                            handleException(it, "Cannot Update User")
                        }

                } else {
                    inProgress.value = false
                    println("the else condition for the createUserDataOrUpdate was triggered and the getUserData method is called further")
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
                println("the user data if not null block has run")
                inProgress.value = false;
                populateChats()
                populateStatus()
            }
        }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {

        println("the handle exception block has run")
        exception?.printStackTrace()

        var errorMessage = exception?.localizedMessage ?: "";

        var message = if (customMessage.isNullOrEmpty()) errorMessage else customMessage;

        // all of this process is for the event object to be created

        mutableStateOfEventObject.value = Event<String>(message);
        inProgress.value = false;

        println(errorMessage)
        println(message)


    }


    fun uploadProfileImage(uri: Uri) {
        viewModelScope.launch {
            uploadImage(uri) { publicUrl ->
                // Switch to the main thread if needed

                createOrUpdateUser(imageUrl = publicUrl)
            }
        }
    }


    suspend fun uploadImage(uri: Uri, onSuccess: (String) -> Unit) {
        return withContext(Dispatchers.IO) {
            val fileName = UUID.randomUUID().toString()
            val response = supabaseClient.storage
                .from("AnyUserBucket")
                .upload(fileName, uri)


            // Get the public URL
            val publicUrl = supabaseClient.storage
                .from("AnyUserBucket")
                .publicUrl(response.path)
            onSuccess.invoke(publicUrl)
            println(publicUrl)

        }
    }

    fun logout() {
        fireBaseUser.signOut()
        userSignedIn.value = false
        mutableUserDataObject.value = null
//        the error should be shown on the screeen
        mutableStateOfEventObject.value = Event("Logged Out")
        depopulateMessage()
        currentChatMessageListener = null
    }

    fun populateMessages(chatId: String) {
        inProgressChatMessage.value = true
        currentChatMessageListener =
            fireStoredb.collection(CHATS).document(chatId).collection(MESSAGE)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        handleException(error)

                    }
//* The chatMessages list gets a list of sorted Message objects.
                    if (value != null) {
                        chatMessages.value = value.documents.mapNotNull {
                            it.toObject<Message>()
                        }.sortedBy { it.timestamp }
                        println(chatMessages.value)
                        inProgressChatMessage.value = false;
                    }

                }
    }

    fun depopulateMessage() {
        chatMessages.value = listOf()
        currentChatMessageListener = null
    }

    fun populateChats() {
        inProgressChats.value = true
        fireStoredb.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId", mutableUserDataObject.value?.userId),
                Filter.equalTo("user2.userId", mutableUserDataObject.value?.userId)
            )
        ).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error)
            }
            if (value != null) {
                chats.value = value.documents.mapNotNull {
                    it.toObject<ChatData>()
                }
                inProgressChats.value = false;

            }
        }

    }

    fun onAddChatClick(number: String) {
        if (number.isEmpty() or number.first().isDigit()) {
    println("the is number check is being passed")
            fireStoredb.collection(CHATS).where(
                Filter.or(
                    Filter.and(
                        Filter.equalTo("user1.number", number),
                        Filter.equalTo("user1.number", mutableUserDataObject.value?.number)
                    ),
                    Filter.and(
                        Filter.equalTo("user1.number", mutableUserDataObject.value?.number),
                        Filter.equalTo("user1.number", number)
                    )
                )
            ).get().addOnSuccessListener {
                if (it.isEmpty) {
                    println("on add chat click first successCondition has been triggered")
                    fireStoredb.collection(USER_NODE).whereEqualTo("number", number).get()
                        .addOnSuccessListener { it1 ->
                            if (it1.isEmpty) {
                                handleException(customMessage = "number not found")
                            } else {
                                println("on add chat click second successCondition has been triggered")
// !! the bugs caused in this proejct might be because of the misuse of hte toObject and toObjects method in this project
                                var chatPartner = it1.toObjectsKtx<UserProfileModel>()[0]
                                var id = fireStoredb.collection(CHATS).document().id;
                                var chat = ChatData(
                                    chatId = id,
                                    ChatUser(
                                      userId =   mutableUserDataObject.value?.userId,
                                     name =    mutableUserDataObject.value?.name,
                                      number =   mutableUserDataObject.value?.number,
                                      imageUrl =   mutableUserDataObject.value?.imageUrl
                                    ),
                                    ChatUser(
                                     userId =    chatPartner.userId,
                                     name =    chatPartner.name,
                                     number =    chatPartner.number,
                                       imageUrl =  chatPartner.imageUrl
                                    )
                                )
                                println(chat.toString())

                                fireStoredb.collection(CHATS).document(id).set(chat);


                            }
                        }
                        .addOnFailureListener {
                            handleException(it);
                        }

                }
            }.addOnFailureListener {
                println("on add chat click first failureCondition has been triggered")
            }


        }
        else{
            println("the number check condition has failed")
        }
    }


    fun onSendReply(chatId: String, message: String) {
        val time = Calendar.getInstance().time.toString()
        val message = Message(mutableUserDataObject.value?.userId, message, time)
        fireStoredb.collection(CHATS).document(chatId).collection(MESSAGE).document()
            .set(message);


    }

    fun uploadStatus(uri: Uri) {
        viewModelScope.launch {
            uploadImage(
                uri
            ) {
                createStatus(it.toString())
            }
        }
    }

    fun createStatus(imageUrl: String) {
        val newStatus = Status(
            ChatUser(
                userId = mutableUserDataObject.value?.userId,
                name = mutableUserDataObject.value?.name,
                imageUrl = mutableUserDataObject.value?.imageUrl,
                number = mutableUserDataObject.value?.number,
            ),
            imageUrl = imageUrl,
            timeStamp = System.currentTimeMillis()
        )
        fireStoredb.collection(STATUS).document().set(newStatus)
    }

    fun populateStatus() {
//   *  after 24 hours the status should be removed
        val timeDelta = 24L * 60 * 60 * 1000
        val cutOff = System.currentTimeMillis() - timeDelta
        inProgressStatus.value = true;
        fireStoredb.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId", mutableUserDataObject.value?.userId),
                Filter.equalTo("user2.userId", mutableUserDataObject.value?.userId)

            )
        ).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error)

            }
            if (value != null) {
                val currentConnections = arrayListOf(mutableUserDataObject.value?.userId)
                val chats = value.toObjectsKtx<ChatData>()
                chats.forEach { chat ->
                    if (chat.user1.userId == mutableUserDataObject.value?.userId) {
                        currentConnections.add(chat.user2.userId)
                    } else {
                        currentConnections.add(chat.user1.userId)
                    }

                    fireStoredb.collection(STATUS).whereGreaterThan("timeStamp", cutOff)
                        .whereIn("user.userId", currentConnections)
                        .addSnapshotListener { value, error ->
                            if (error != null) {
                                handleException(error)
                            }
                            if (value != null) {
                                status.value = value.toObjectsKtx()
                                inProgress.value = false;
                            }
                        }
                }
            }
        }

    }


}


