package com.example.camera.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.camera.R
import com.example.camera.ui.base.BaseActivity

class SplachActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splach)

        Handler().postDelayed(Runnable {
            startActivity(Intent(this, LoginActivity::class.java))
        }, 3000)
    }
}
