package com.osman.kirazapp.Login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.osman.kirazapp.Models.Users
import com.osman.kirazapp.R

class RegisterActivity : AppCompatActivity() {
    lateinit var spinnerMedeniHal: Spinner
    lateinit var adapter: ArrayAdapter<CharSequence>

    lateinit var radioGroup: RadioGroup
    lateinit var radioButton: RadioButton
    lateinit var buttonKayıt: Button

    lateinit var emailET: EditText
    lateinit var sifreET: EditText
    lateinit var adSoyadET: EditText
    lateinit var yasET: EditText

    lateinit var cinsiyetString: String
    lateinit var medeniHalString: String
    lateinit var yasString: String

    lateinit var mAuth: FirebaseAuth
    lateinit var mRef: DatabaseReference
    lateinit var mAuthListener:FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        radioGroup = findViewById(R.id.radioGroup)
        spinnerMedeniHal = findViewById(R.id.spnMedeniHal)
        buttonKayıt = findViewById(R.id.btnKaydol)

        emailET = findViewById(R.id.etEmailRegister)
        sifreET = findViewById(R.id.etRegisterPassWord)
        adSoyadET = findViewById(R.id.etRegisterName)
        yasET = findViewById(R.id.etYas)
        yasString = yasET.text.toString()

        cinsiyetString = "belirtilmedi"
        yasString=""

        setupAuthListener()
        mAuth = FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().reference


        // watcher tanımlamaları
        emailET.addTextChangedListener(watcher)
        sifreET.addTextChangedListener(watcher)
        adSoyadET.addTextChangedListener(watcher)





        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.medeni_hal,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMedeniHal.adapter = adapter

        // spinner işlem yapıldığında olacaklar
        spinnerMedeniHal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                medeniHalString = parent!!.getItemAtPosition(position).toString()
                Log.e("osmankiraz123", "medeni hal seçildi " + medeniHalString)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }


    }

    // WATCHER
    var watcher: TextWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.length > 5) {
                if (emailET.text.toString().length > 5 && adSoyadET.text.toString().length > 5 && sifreET.text.toString().length > 5) {
                    buttonKayıt.isEnabled = true
                    buttonKayıt.setTextColor(
                        ContextCompat.getColor(
                            this@RegisterActivity!!,
                            R.color.beyaz
                        )
                    )
                    buttonKayıt.setBackgroundColor(R.drawable.register_button_aktif)
                } else {
                    buttonKayıt.isEnabled = false
                    buttonKayıt.setTextColor(
                        ContextCompat.getColor(
                            this@RegisterActivity!!,
                            R.color.sonukmavi
                        )
                    )
                    buttonKayıt.setBackgroundColor(R.drawable.register_button)
                }

            } else {
                buttonKayıt.isEnabled = false
                buttonKayıt.setTextColor(
                    ContextCompat.getColor(
                        this@RegisterActivity!!,
                        R.color.sonukmavi
                    )
                )
                buttonKayıt.setBackgroundColor(R.drawable.register_button)
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }

    // RADIO BUTTONUNA BASILINCA OLACAKLAR
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            // Check which radio button was clicked
            when (view.getId()) {
                R.id.rbErkek ->
                    if (checked) {
                        cinsiyetString = "erkek"
                    }
                R.id.rbKadin ->
                    if (checked) {
                        cinsiyetString = "kadin"
                    }
            }
        }
    }

    // email değerinin doğru bir e mail degeri olduğunu kontrol ediyoruz
    fun isValidEmail(kontrolEdilecekEmail: String): Boolean {
        if (kontrolEdilecekEmail == null) {
            return false
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(kontrolEdilecekEmail).matches()
    }

    // KAYIT BUTONUNA BASILINCA OLACAKLAR
    fun kayitButonu(view: View) {
        var gelenEmail = emailET.text.toString()
        var sifre = sifreET.text.toString()
        var adiSoyadi = adSoyadET.text.toString()
        var yas = yasET.text.toString()

        //cinsiyetString
        //medeniHalString

        if (isValidEmail(gelenEmail)) {

            //var credential=EmailAuthProvider.getCredential(emailET.text.toString(),sifreET.text.toString())
            mAuth.createUserWithEmailAndPassword(gelenEmail, sifre)
                .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                    override fun onComplete(p0: Task<AuthResult>) {
                        if (p0!!.isSuccessful) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Kullanıcı oluşturuldu",
                                Toast.LENGTH_SHORT
                            ).show()
                            var userID = mAuth.currentUser!!.uid.toString()
                            var kaydedilecekKullanici = Users(
                                gelenEmail,
                                sifre,
                                adiSoyadi,
                                yas,
                                cinsiyetString,
                                medeniHalString,
                                userID
                            )
                            Log.e(
                                "osmankiraz123",
                                "kaydedilecek kullanıcı:: $kaydedilecekKullanici"
                            )
                            Log.e("osmankiraz123", "almaya çalıştığımız userıd::  $userID")
                            mRef.child("users").child(userID).setValue(kaydedilecekKullanici)
                                .addOnCompleteListener(object : OnCompleteListener<Void> {
                                    override fun onComplete(p0: Task<Void>) {
                                        if (p0!!.isSuccessful) {
                                            Log.e(
                                                "osmankiraz123",
                                                "kullanıcı database kaydedildi ve yeni intent başlatıldı"
                                            )
                                            val intent = Intent(
                                                this@RegisterActivity,
                                                HomeActivity::class.java
                                            )
                                            startActivity(intent)
                                        } else {
                                            mAuth.currentUser!!.delete().addOnCompleteListener(object :OnCompleteListener<Void>{
                                                override fun onComplete(p0: Task<Void>) {
                                                    Log.e(
                                                        "osmankiraz123",
                                                        " kullanıcı kaydedilirken hata oluştu ve mAuth Silindi hata sebebi ${p0!!.exception}"
                                                    )
                                                    Toast.makeText(this@RegisterActivity,"Kullanıcı Kaydedilemedi",Toast.LENGTH_SHORT).show()

                                                }

                                            })

                                        }
                                    }
                                }).addOnFailureListener(object : OnFailureListener {
                                    override fun onFailure(p0: Exception) {
                                        Log.e(
                                            "osmankiraz123",
                                            "kullanıcı kaydedilirken hata oluştu hata sebebi " + p0.toString()
                                        )
                                    }
                                })

                            Log.e("osmankiraz123", "onCompleteListerner sonu")


                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Kullanıcı oluşturulamadı" + p0!!.exception,
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("osmankiraz123", "hata şu" + p0.exception)
                        }
                    }

                })


        } else {
            Toast.makeText(this, "Lütfen geçerli email giriniz", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setupAuthListener() {
        mAuthListener=object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user=FirebaseAuth.getInstance().currentUser
                if(user != null){
                    var intent=Intent(this@RegisterActivity,HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
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