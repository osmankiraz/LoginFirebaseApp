package com.osman.kirazapp.Login

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
//import com.osman.kalemapp.Login.databinding.ActivityScrollingBinding
import com.osman.kirazapp.R
import com.osman.kirazapp.databinding.ActivityScrollingBinding

class ScrollingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScrollingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setImageResource(R.drawable.ic_done)
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Sözlemeşmen admin onayı bekliyor", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            onBackPressed()
        }
    }
}