package com.osman.kirazapp.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.osman.kirazapp.Models.Users
import com.osman.kirazapp.R

class SettingsActivity : AppCompatActivity() {
    lateinit var spinnerMedeniHal: Spinner
    lateinit var adapter: ArrayAdapter<CharSequence>

    lateinit var etEmail:EditText
    lateinit var etPassword:EditText
    lateinit var etName:EditText
    lateinit var etAge:EditText

    lateinit var emailString: String
    lateinit var sifreString: String
    lateinit var adiString: String
    lateinit var cinsiyetString: String
    lateinit var medeniHalString: String
    lateinit var yasString: String

    lateinit var radioGroup: RadioGroup
    lateinit var radioButtonErkek: RadioButton
    lateinit var radioButtonKadin: RadioButton
    lateinit var buttonDuzenle: Button

    lateinit var okunanKullaniciBilgileri:Users

     var erkekSeciliMi: Boolean?=null
     var bekarSeciliMi: Boolean?=null


    lateinit var mAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var mUser:FirebaseUser
    lateinit var mRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        spinnerMedeniHal=findViewById(R.id.spnMedeniHalSettings)
        etEmail=findViewById(R.id.etEmailSettings)
        etName=findViewById(R.id.etNameSettings)
        etPassword=findViewById(R.id.etPasswordSettings)
        etAge=findViewById(R.id.etYasSettings)

        radioGroup=findViewById(R.id.radioGroupSettings)
        radioButtonErkek=findViewById(R.id.rbErkekSt)
        radioButtonKadin=findViewById(R.id.rbKadinSt)
        buttonDuzenle=findViewById(R.id.btnDuzenle)

        mAuth = FirebaseAuth.getInstance()
        mRef=FirebaseDatabase.getInstance().reference
        mUser= mAuth.currentUser!!

        //mAuth.updateCurrentUser(mUser.update)
        setupAuthListener()


        etEmail.addTextChangedListener(watcher)
        etPassword.addTextChangedListener(watcher)
        etName.addTextChangedListener(watcher)

        kullaniciBilgileriniGetir()




        adapter=ArrayAdapter.createFromResource(this,R.array.medeni_hal,android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMedeniHal.adapter=adapter

        // spinner i??lem yap??ld??????nda olacaklar
        spinnerMedeniHal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                medeniHalString = parent!!.getItemAtPosition(position).toString()

                if(medeniHalString.equals("bekar")){
                    bekarSeciliMi=true
                }else{bekarSeciliMi=false}

                Log.e("osmankiraz123", "medeni hal se??ildi " +medeniHalString )

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        //           D??ZENLE BUTONU TIKLANILINCA
        buttonDuzenle.setOnClickListener {

                        // MA??L DE??????T??R??L??NCE OLACAKLAR
            if (!emailString!!.equals(etEmail.text.toString())) {

                if (isValidEmail(etEmail.text.toString())) {
                    mRef.child("users").orderByChild("email").addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var mailKullanimdaMi=false
                            for (ds in snapshot.children){
                                var okunanEmailDegerleri=ds!!.getValue(Users::class.java)!!.email
                                if (okunanEmailDegerleri!!.equals(etEmail.text.toString())){
                                    Toast.makeText(this@SettingsActivity,"Yazm???? oldu??unuz mail kullan??l??yor",Toast.LENGTH_SHORT).show()
                                    mailKullanimdaMi=true
                                    break
                                }
                            }
                            if (mailKullanimdaMi == false) {

                                var credential=EmailAuthProvider.getCredential(emailString,sifreString)
                                mUser.reauthenticate(credential).addOnCompleteListener {  Log.e("osmankiraz123","re auth yap??ld??")}

                                mUser.updateEmail(etEmail.text.toString()).addOnCompleteListener {
                                    if (it.isSuccessful){
                                        mRef.child("users").child(mUser.uid).child("email").setValue(etEmail.text.toString())
                                        Toast.makeText(this@SettingsActivity,"Mailiniz de??i??tirildi",Toast.LENGTH_SHORT).show()
                                    }
                                }.addOnFailureListener {
                                    Log.e("osmankiraz123","mail de??i??tirilirken hata :::: :"+it.toString())
                                    Toast.makeText(this@SettingsActivity,"Mail hatas??"+it.toString(),Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                } else {
                    Toast.makeText(this@SettingsActivity,"L??tfen ge??erli bir mail adresi giriniz",Toast.LENGTH_LONG).show()
                }

            }
            //    ????FRE DE????????T??L??NCE OLACAKLAR
            if(!sifreString.equals(etPassword.text.toString())){
                var credential=EmailAuthProvider.getCredential(emailString,sifreString)
                mUser.reauthenticate(credential).addOnCompleteListener { Log.e("osmankiraz123","re auth yap??ld??") }
                mUser.updatePassword(etPassword.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        mRef.child("users").child(mUser.uid).child("password").setValue(etPassword.text.toString())
                        Toast.makeText(this@SettingsActivity,"??ifreniz de??i??tirildi",Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Log.e("osmankiraz123","??ifre de??i??tirilirken hata :::: :"+it.toString())
                    Toast.makeText(this@SettingsActivity,"??ifre hatas??"+it.toString(),Toast.LENGTH_SHORT).show()
                }
            }

                        // ADI SOYADI DE??????T??R??L??CE OLACAKLAR
            if (!adiString!!.equals(etName.text.toString())){
                mRef.child("users").child(mUser.uid).child("name_lastname").setValue(etName.text.toString())
            }

                        // YAS DEG????M???? ??SE
            if (!yasString!!.equals(etAge.text.toString())){
                mRef.child("users").child(mUser.uid).child("age").setValue(etAge.text.toString())
            }

                    // C??NS??YET DE??????T??R??L??NCE OLACAKLAR
            if (!cinsiyetString.equals(okunanKullaniciBilgileri.gender.toString())) {

                if (erkekSeciliMi==true) {
                    mRef.child("users").child(mUser.uid).child("gender").setValue(radioButtonErkek.text.toString())
                } else {
                    mRef.child("users").child(mUser.uid).child("gender").setValue(radioButtonKadin.text.toString())
                }
            }

                    // MEDEN?? HAL DE??????T??R??L??NCE OLACAKLAR
            if (!medeniHalString.equals(okunanKullaniciBilgileri.merital_status.toString())) {
                if (bekarSeciliMi==true){
                    mRef.child("users").child(mUser.uid).child("merital_status").setValue("bekar")
                }else{
                    mRef.child("users").child(mUser.uid).child("merital_status").setValue("evli")
                }


            }




            //Toast.makeText(this@SettingsActivity,"De??i??iklikler yap??ld??",Toast.LENGTH_LONG).show()
            //var intent=Intent(this@SettingsActivity,HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            //startActivity(intent)

        }



    }

    // WATCHER
    var watcher: TextWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.length > 4) {
                if (etEmail.text.toString().length > 4 && etName.text.toString().length > 4 && etPassword.text.toString().length > 4) {
                    buttonDuzenle.isEnabled = true
                    buttonDuzenle.setTextColor(
                        ContextCompat.getColor(
                            this@SettingsActivity!!,
                            R.color.beyaz
                        )
                    )
                    buttonDuzenle.setBackgroundColor(R.drawable.register_button_aktif)
                } else {
                    buttonDuzenle.isEnabled = false
                    buttonDuzenle.setTextColor(
                        ContextCompat.getColor(
                            this@SettingsActivity!!,
                            R.color.sonukmavi
                        )
                    )
                    buttonDuzenle.setBackgroundColor(R.drawable.register_button)
                }

            } else {
                buttonDuzenle.isEnabled = false
                buttonDuzenle.setTextColor(
                    ContextCompat.getColor(
                        this@SettingsActivity!!,
                        R.color.sonukmavi
                    )
                )
                buttonDuzenle.setBackgroundColor(R.drawable.register_button)
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }

    // email de??erinin do??ru bir e mail degeri oldu??unu kontrol ediyoruz
    fun isValidEmail(kontrolEdilecekEmail: String): Boolean {
        if (kontrolEdilecekEmail == null) {
            return false
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(kontrolEdilecekEmail).matches()
    }

    // KULLANICININ VER??TABANINDAN VER??LER??N?? GET??REN FONKS??YON
    private fun kullaniciBilgileriniGetir() {
        mRef.child("users").child(mUser.uid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.getValue() != null) {
                    okunanKullaniciBilgileri=snapshot.getValue(Users::class.java)!!
                    etEmail.setText(okunanKullaniciBilgileri!!.email)
                    emailString=okunanKullaniciBilgileri!!.email.toString()
                    etPassword.setText(okunanKullaniciBilgileri!!.password)
                    sifreString=okunanKullaniciBilgileri!!.password.toString()
                    etAge.setText(okunanKullaniciBilgileri!!.age)
                    yasString=okunanKullaniciBilgileri!!.age.toString()
                    etName.setText(okunanKullaniciBilgileri!!.name_lastname)
                    adiString=okunanKullaniciBilgileri!!.name_lastname.toString()

                    if (okunanKullaniciBilgileri!!.merital_status.equals("bekar")){
                        spinnerMedeniHal.setSelection(0)
                        medeniHalString="bekar"
                    }else{
                        spinnerMedeniHal.setSelection(1)
                        medeniHalString="evli"
                    }

                    if (okunanKullaniciBilgileri!!.gender.equals("Erkek")){
                         radioGroup.check(R.id.rbErkekSt)
                        erkekSeciliMi=true
                        cinsiyetString="Erkek"
                    }else{
                        radioGroup.check(R.id.rbKadinSt)
                        erkekSeciliMi=false
                        cinsiyetString="Kad??n"
                    }


                } else {

                }

            }

            override fun onCancelled(error: DatabaseError) {


            }
        })

    }

    //  RADIO BUTONUNA BASILINCA OLACAKLAR C??NS??YET SE????M??
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            // Check which radio button was clicked
            Log.e("osmankiraz123","radio butonu t??klanm????m?? checked :"+checked)
            when (view.getId()) {
                R.id.rbErkekSt ->
                    if (checked) {
                        erkekSeciliMi=true
                        cinsiyetString="Erkek"
                    }
                R.id.rbKadinSt ->
                    if (checked) {
                        erkekSeciliMi=false
                        cinsiyetString="Kad??n"
                    }
            }
        }
    }

    private fun setupAuthListener() {
        mAuthListener=object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var user=FirebaseAuth.getInstance().currentUser
                if(user == null){
                    var intent= Intent(this@SettingsActivity,LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
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