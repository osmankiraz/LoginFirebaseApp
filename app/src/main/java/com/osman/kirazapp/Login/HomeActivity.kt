package com.osman.kirazapp.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.osman.kirazapp.R
import com.osman.kirazapp.admin.UserListActivity

class HomeActivity : AppCompatActivity() {
    lateinit var iconSozlesme:ImageView
    lateinit var iconCikis:ImageView
    lateinit var iconSettings:ImageView
    lateinit var tvDeneme:TextView

    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener:FirebaseAuth.AuthStateListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        iconSozlesme=findViewById(R.id.iconContract)
        iconCikis=findViewById(R.id.iconExit)
        iconSettings=findViewById(R.id.iconSettings)
        tvDeneme=findViewById(R.id.tvAyar)

        mAuth = FirebaseAuth.getInstance()
        mAuth.currentUser
        setupAuthListener()



        iconSozlesme.setOnClickListener {
            val intent = Intent(this@HomeActivity,ScrollingActivity::class.java)
            startActivity(intent)
        }
        iconCikis.setOnClickListener{
            mAuth.signOut()
        }
        iconSettings.setOnClickListener {
            val intent = Intent(this@HomeActivity,SettingsActivity::class.java)
            startActivity(intent)
        }








    }




    private fun setupAuthListener() {
        mAuthListener=object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user=FirebaseAuth.getInstance().currentUser
                if(user == null){
                    var intent=Intent(this@HomeActivity,LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                    finish()
                }else{

                }

            }

        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthListener)
    }

    override fun onStop() {
        super.onStop()
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener)
        }
    }

}