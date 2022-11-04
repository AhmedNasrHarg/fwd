package com.udacity.project4.authentication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    val SIGN_IN_REQUEST_CODE = 1
    lateinit var loginBtn: Button
     var currentUserName = ""
     var currentUserEmail = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        loginBtn = findViewById(R.id.login_btn)
//         TODO: Implement the create account and sign in using FirebaseUI, use sign in using email and sign in using Google
        loginBtn.setOnClickListener {
            //todo: we can wait for getCurrentUserInfo() to get data, or sharedPreference.getString() to be in if().
//            getCurrentUserInfo()
            val sharedPreference =  getSharedPreferences("auth_user", Context.MODE_PRIVATE)
            if(sharedPreference.getString("username","")!! == "" && sharedPreference.getString("email","")!! == ""){
                println(sharedPreference.getString("username","")!!)
//                Toast.makeText(this,sharedPreference.getString("username","")!!,Toast.LENGTH_SHORT).show()
                launchSignInFlow()
            }else{
                startActivity(Intent(this,RemindersActivity::class.java))
            }
        }
//          TODO: If the user was authenticated, send him to RemindersActivity

//          TODO: a bonus is to customize the sign in flow to look nice using :
        //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout

    }

    private fun getCurrentUserInfo() {
        val sharedPreference =  getSharedPreferences("auth_user", Context.MODE_PRIVATE)
        currentUserName = sharedPreference.getString("username","")!!
        currentUserEmail = sharedPreference.getString("email","")!!
    }

    private fun saveUserToSharedPreferences(name:String, email:String) {
        val sharedPreference =  getSharedPreferences("auth_user", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("username",name)
        editor.putString("email",email)
        editor.commit()
        Toast.makeText(this,"$name 000",Toast.LENGTH_SHORT).show()
        Toast.makeText(this,sharedPreference.getString("username121","")!!,Toast.LENGTH_SHORT).show()
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .build(),
            SIGN_IN_REQUEST_CODE
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in
      Toast.makeText(this,"Success: ${FirebaseAuth.getInstance().currentUser?.displayName}",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,RemindersActivity::class.java))
                Log.i("SignIn", "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
                currentUserName = FirebaseAuth.getInstance().currentUser?.displayName!!
                currentUserEmail = FirebaseAuth.getInstance().currentUser?.email!!
                saveUserToSharedPreferences(currentUserName,currentUserEmail)
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                Toast.makeText(this,"Login Failed",Toast.LENGTH_SHORT).show()
                // response.getError().getErrorCode() and handle the error.
                Log.i("SignIn", "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }
}
