package com.example.camera.ui

import android.content.Intent
import android.os.Bundle
import com.example.camera.R
import com.example.camera.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onResume() {
        super.onResume()
        button_google.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
