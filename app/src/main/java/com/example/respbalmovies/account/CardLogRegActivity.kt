package com.example.respbalmovies.account

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.respbalmovies.R
import com.example.respbalmovies.admin.AdminHomeActivity
import com.example.respbalmovies.sharedPreferences.prefData
import com.google.android.material.tabs.TabLayout

class CardLogRegActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: LoginRegisterAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_log_reg)
        tabLayout = findViewById(R.id.tl_navbar_logregis)
        viewPager2 = findViewById(R.id.vp_container_logregis)

        adapter = LoginRegisterAdapter(supportFragmentManager, lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("Login"))
        tabLayout.addTab(tabLayout.newTab().setText("Register"))

        viewPager2.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

    }

    override fun onStart() {
        super.onStart()
        val sharedPref = getSharedPreferences(prefData.SHAREDPREF, Context.MODE_PRIVATE)
        if(sharedPref.getBoolean(prefData.IS_LOGIN, false)){
            startActivity(Intent(this@CardLogRegActivity, AdminHomeActivity ::class.java))
            finish()
        }
    }
}