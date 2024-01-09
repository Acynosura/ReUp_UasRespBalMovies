package com.example.respbalmovies.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.respbalmovies.R
import com.example.respbalmovies.databinding.ActivityPublicHomeBinding

class publicHomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPublicHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPublicHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        replaceFragment(homeFragment())

        with(binding){

            bottomNav.setOnItemSelectedListener{
                when(it.itemId){
                    R.id.home_menu -> replaceFragment(homeFragment())
                    R.id.profile_menu -> replaceFragment(profileFragment())
                    R.id.bookmark_menu -> replaceFragment(bookmarkFragment())
                    else->{}
                }
                true
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}