package com.osman.kirazapp.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.osman.kirazapp.R

class HomeActivity : AppCompatActivity() {
    lateinit var iconSozlesme:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        iconSozlesme=findViewById(R.id.iconContract)

        iconSozlesme.setOnClickListener {
            val intent = Intent(this@HomeActivity,ScrollingActivity::class.java)
            startActivity(intent)
        }






    }




}