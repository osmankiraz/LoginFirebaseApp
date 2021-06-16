package com.osman.kalemapp.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.osman.kalemapp.R

class RegisterActivity : AppCompatActivity() {
    lateinit var spinnerMedeniHal: Spinner
    lateinit var adapter: ArrayAdapter<CharSequence>

    lateinit var radioGroup: RadioGroup
    lateinit var radioButton: RadioButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        radioGroup = findViewById(R.id.radioGroup)


        spinnerMedeniHal = findViewById(R.id.spnMedeniHal)
        adapter = ArrayAdapter.createFromResource(
            this,
            R.array.medeni_hal,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMedeniHal.adapter = adapter
        spinnerMedeniHal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(
                    this@RegisterActivity,
                    "you selected ${parent!!.getItemAtPosition(position).toString()}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }


    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.rbErkek ->
                    if (checked) {
                        Log.e("osman", "erkek seçildi")
                    }
                R.id.rbKadin ->
                    if (checked) {
                        Log.e("osman", "kadın seçildi")
                    }
            }
        }
    }


}