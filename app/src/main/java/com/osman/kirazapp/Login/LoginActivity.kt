package com.osman.kirazapp.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.osman.kirazapp.R

class LoginActivity : AppCompatActivity() {
    lateinit var tvKaydol:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvKaydol=findViewById(R.id.tvKaydol)
        tvKaydol.setOnClickListener {
            val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}