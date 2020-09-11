package com.example.camera.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.camera.R
import com.example.camera.async.base.BaseInsert
import com.example.camera.async.base.BaseSelect
import com.example.camera.model.User
import com.example.camera.ui.base.BaseActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import java.util.ArrayList


class LoginActivity : BaseActivity() {

    private val RC_SIGN_IN: Int = 1
    val TAG = "LoginActitivyLog"
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onResume() {
        super.onResume()
        auth = FirebaseAuth.getInstance()
        init()
    }


    private fun init() {

        initGoogle()
        getUserLoginGoogle(null)

        sign_in_button.setSize(SignInButton.SIZE_STANDARD)
        sign_in_button.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG, "Data $data")
        when (requestCode) {
            RC_SIGN_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        try {
            val account = task?.getResult(ApiException::class.java)
            Log.i(TAG, "Token:" + account?.idToken)
            getUserLoginGoogle(account?.idToken.toString())
        } catch (e: ApiException) {
            e.printStackTrace()
            Log.e(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun getUserLoginGoogle(idToken : String?) {

        val c = GoogleSignIn.getLastSignedInAccount(this)
        if (idToken != null) {
            try {
                firebaseAuthWithGoogle(idToken, c)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        } else {
            mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, OnCompleteListener<Void> {
                    Log.i("signOut", "Disconnected")
                })
        }
    }

    private fun firebaseAuthWithGoogle(
        idToken: String,
        c: GoogleSignInAccount?
    ) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.i(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    var id = c?.id?.toBigInteger()
                    val users = arrayListOf(
                        User(
                            id = id?.toLong(),
                            name = user?.displayName,
                            email = user?.email,
                            token = idToken
                        )
                    )
                    SaveUser(this, users, resources.getString(R.string.class_user)).execute()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }


    inner class SaveUser(private val context: Context, users: ArrayList<User>, model: String) :
        BaseInsert<User>(context, users, model) {
        override fun onPostExecute(result: List<User>?) {
            super.onPostExecute(result)
            Log.i(TAG, "Save user $result")
            startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
    }
}

