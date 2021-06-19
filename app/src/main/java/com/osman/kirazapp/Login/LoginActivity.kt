package com.osman.kirazapp.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.osman.kirazapp.Models.Users
import com.osman.kirazapp.R

class LoginActivity : AppCompatActivity() {
    lateinit var tvKaydol:TextView
    lateinit var etEmail:EditText
    lateinit var etPassword:EditText
    lateinit var btnGiris:Button

    lateinit var mAuth:FirebaseAuth
    lateinit var mRef:DatabaseReference
    lateinit var mAuthListener:FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tvKaydol=findViewById(R.id.tvKaydol)
        etEmail=findViewById(R.id.etEmail)
        etPassword=findViewById(R.id.etPassword)
        btnGiris=findViewById(R.id.btnGiris)

        setupAuthListener()
        mAuth= FirebaseAuth.getInstance()

        mRef=FirebaseDatabase.getInstance().reference



        init()




        tvKaydol.setOnClickListener {
            val intent = Intent(this@LoginActivity,RegisterActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }


    }



    fun init(){
        etEmail.addTextChangedListener(watcher)
        etPassword.addTextChangedListener(watcher)

        btnGiris.setOnClickListener {
            oturumAcacakKullaniciDenetle(etEmail.text.toString(),etPassword.text.toString())
        }

    }

    private fun oturumAcacakKullaniciDenetle(emailP: String, sifreP: String) {

        mRef.child("users").orderByChild("email").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot!!.children){
                    var okunanKullanici=ds.getValue(Users::class.java)
                    if (okunanKullanici!!.email.equals(emailP)){
                        oturumAc(okunanKullanici,sifreP)
                        break
                    }else{
                        Toast.makeText(this@LoginActivity,"Mail bulunamadı ",Toast.LENGTH_SHORT).show()
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {


            }

        })

    }

    private fun oturumAc(okunanKullanici: Users, sifreP: String) {

        var girisYapacakEmail=okunanKullanici.email.toString()
        mAuth.signInWithEmailAndPassword(girisYapacakEmail,sifreP).addOnCompleteListener(object :OnCompleteListener<AuthResult>{
            override fun onComplete(p0: Task<AuthResult>) {
                    if (p0.isSuccessful){
                        Toast.makeText(this@LoginActivity,"Oturum Açıldı",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@LoginActivity,"Hatalı Şifre/Mail ",Toast.LENGTH_SHORT).show()
                    }

            }

        })

    }

    //         WATCHER               //
    var watcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(etEmail.text.toString().length>=6 && etPassword.text.toString().length>=6){
                btnGiris.isEnabled=true
                btnGiris.setTextColor(
                    ContextCompat.getColor(
                        this@LoginActivity!!,
                        R.color.beyaz
                    )
                )
                btnGiris.setBackgroundColor(R.drawable.register_button_aktif)
            }else{
                btnGiris.isEnabled=false
                btnGiris.setTextColor(
                    ContextCompat.getColor(
                        this@LoginActivity!!,
                        R.color.sonukmavi
                    )
                )
                btnGiris.setBackgroundColor(R.drawable.register_button)
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }

    private fun setupAuthListener() {
        mAuthListener=object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user=FirebaseAuth.getInstance().currentUser
                if(user != null){
                    var intent=Intent(this@LoginActivity,HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
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