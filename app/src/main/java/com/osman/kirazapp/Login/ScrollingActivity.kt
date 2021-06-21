package com.osman.kirazapp.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
//import com.osman.kalemapp.Login.databinding.ActivityScrollingBinding
import com.osman.kirazapp.R
import com.osman.kirazapp.databinding.ActivityScrollingBinding

class ScrollingActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var mUser: FirebaseUser
    lateinit var mRef: DatabaseReference
    var isWaiting:Boolean?=null

    private lateinit var binding: ActivityScrollingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        mRef= FirebaseDatabase.getInstance().reference
        mUser= mAuth.currentUser!!

        //mAuth.updateCurrentUser(mUser.update)
        setupAuthListener()

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        //binding.fab.setImageResource(R.drawable.ic_send)


        mRef.child("users").child(mUser.uid).child("verified").get().addOnSuccessListener {

            if(it.value!!.equals("waiting")){
                binding.fab.setImageResource(R.drawable.ic_waiting)
                isWaiting=true
            }else{
                binding.fab.setImageResource(R.drawable.ic_send)
                isWaiting=false
            }



        }

//        if(mRef.child("users").child(mUser.uid).child("verified").getva{
//            binding.fab.setImageResource(R.drawable.ic_done)
//            Log.e("osmankiraz123","waiting eqaul done oldu")
//        }else{
//            binding.fab.setImageResource(R.drawable.ic_send)
//            Log.e("osmankiraz123","waiting eqaul send oldu")
//        }




        binding.fab.setOnClickListener { view ->

            if(isWaiting==true){
                Snackbar.make(view, "Sözlemeşmen admin onayı bekliyor", Snackbar.LENGTH_LONG)
                    .setAction("Kapat",object :View.OnClickListener{
                        override fun onClick(v: View?) {
                            onBackPressed()

                        }}).show()


            }else{
                mRef.child("users").child(mUser.uid).child("verified").setValue("waiting")
                binding.fab.setImageResource(R.drawable.ic_waiting)
                Snackbar.make(view, "Sözlemeşmen admin onayı bekliyor", Snackbar.LENGTH_LONG)
                    .setAction("Kapat",object :View.OnClickListener{
                        override fun onClick(v: View?) {
                            onBackPressed()

                        }

                    }).show()




            }



        }
    }


    private fun setupAuthListener() {
        mAuthListener=object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user= FirebaseAuth.getInstance().currentUser
                if(user == null){
                    var intent= Intent(this@ScrollingActivity,LoginActivity::class.java).addFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION)
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