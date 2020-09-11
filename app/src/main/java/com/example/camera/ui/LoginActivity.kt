package com.example.camera.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.camera.R
import com.example.camera.ui.base.BaseActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
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
        googleLogin()
    }


    private fun googleLogin() {

        initGoogle()
        getUserLoginGoogle(null)

        sign_in_button.setSize(SignInButton.SIZE_STANDARD)
        sign_in_button.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    private fun initGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

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
            getUserLoginGoogle(account)
        } catch (e: ApiException) {
            e.printStackTrace()
            Log.e(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun getUserLoginGoogle(account: GoogleSignInAccount?) {

        val c = GoogleSignIn.getLastSignedInAccount(this)
        if(account?.idToken != null){
            Log.i(TAG, "**Success login google**")
            Log.i(TAG, "Display name ${c?.displayName}")
            Log.i(TAG, "Email ${c?.email}")
            Log.i(TAG, "ID ${c?.id}")
            Log.i(TAG, "Photo Url ${c?.photoUrl}")
            Log.i(TAG, "Token ${c?.idToken}")
            Log.i(TAG, "Account Token ${account?.idToken}")
            Log.i(TAG, "Account id ${account?.id}")
            try {
                firebaseAuthWithGoogle(account?.idToken.toString())
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }else{
            signOut()
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.i(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Log.i(TAG, "User ${user?.displayName}")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this, OnCompleteListener<Void> {
                Log.e(TAG, "Disconetted $it")
            })
    }

    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener(this) {
                Log.e(TAG, "revoke $it")
            }
    }

}
