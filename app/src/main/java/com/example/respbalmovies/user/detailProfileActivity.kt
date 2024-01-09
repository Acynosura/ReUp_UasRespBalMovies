package com.example.respbalmovies.user

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.respbalmovies.databinding.ActivityDetailProfileBinding
import com.example.respbalmovies.sharedPreferences.prefData

class detailProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences(prefData.SHAREDPREF, Context.MODE_PRIVATE)
        val ID = sharedPref.getString(prefData.UID, "")
        val USERNAME = sharedPref.getString(prefData.USERNAME, "")
        val PHONE = sharedPref.getString(prefData.PHONES, "")
        val EMAIL = sharedPref.getString(prefData.EMAIL, "")

        binding.txtUsernameProfiles.text = USERNAME
        binding.txtEmailProfile.text = EMAIL
        binding.txtPhonesProfile.text = PHONE
    }
}