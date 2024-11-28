package com.example.chattingapplication.Utilites.hiltComponents

import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DaggerComponents {
    @Provides
    fun provideFireBaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun provideFireStore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideFirebaseImageStore(): FirebaseStorage = Firebase.storage

    //Supabase api Client
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://idwclgkdqicufqpvvjwj.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imlkd2NsZ2tkcWljdWZxcHZ2andqIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzI0Njg2NjQsImV4cCI6MjA0ODA0NDY2NH0.LoOVOIiuOXIzGWTmXUdKEG3chdgiA-TVjYVEWYpddBc"
        ) {
            var Storage = io.github.jan.supabase.storage.Storage
            install(Postgrest)
            install(Auth) {
                flowType = FlowType.PKCE
                scheme = "app"
                host = "supabase.com"
            }
            install(Storage)
        }
    }


}