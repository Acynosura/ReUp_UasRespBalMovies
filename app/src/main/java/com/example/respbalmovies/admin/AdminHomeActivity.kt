package com.example.respbalmovies.admin

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.respbalmovies.account.CardLogRegActivity
import com.example.respbalmovies.data.DataFilm
import com.example.respbalmovies.databinding.ActivityAdminHomeBinding
import com.example.respbalmovies.sharedPreferences.prefData.Companion.EMAIL
import com.example.respbalmovies.sharedPreferences.prefData.Companion.PHONES
import com.example.respbalmovies.sharedPreferences.prefData.Companion.SHAREDPREF
import com.example.respbalmovies.sharedPreferences.prefData.Companion.UID
import com.example.respbalmovies.sharedPreferences.prefData.Companion.USERNAME
import com.example.respbalmovies.swipeToDeleteCallback
import com.google.firebase.firestore.FirebaseFirestore

class AdminHomeActivity : AppCompatActivity() {

    //    INISIASI VARIABEL YANG DI BUTUHKAN
    private val firestore = FirebaseFirestore.getInstance()
    private val dataFilmCollectionRef = firestore.collection("datafilm")
    private val usersCollectionRef = firestore.collection("users")
    private lateinit var binding: ActivityAdminHomeBinding
    private var updateId = ""
    private var username: String? = null
    private var email: String? = null
    private var roles: String? = null
    private var showToast = ""
    private var currentid = ""
    private val dataFilmListLiveData: MutableLiveData<List<DataFilm>> by lazy {
        MutableLiveData<List<DataFilm>>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)

        val sharedPref = getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE)
        val ID = sharedPref.getString(UID, "")
        val USERNAME = sharedPref.getString(USERNAME, "")
        val PHONE = sharedPref.getString(PHONES, "")
        val EMAIL = sharedPref.getString(EMAIL, "")
        binding.txtUsernameAdmin.setText(USERNAME)

        Log.d("UserDataDebug", "${ID}, ${USERNAME}, ${PHONE}, ${EMAIL}")

        setContentView(binding.root)
        observeDataFilmChanges()
        getAlldataFilm()

        binding.containerInfo.setOnClickListener{
            startActivity(
                Intent(this@AdminHomeActivity, EditProfileActivity
            ::class.java)
            )
        }

        binding.imgImageAdmin.setOnClickListener{
            val sharedPref = this@AdminHomeActivity?.getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE)
            val editor = sharedPref?.edit()
            editor?.clear()?.apply()
            startActivity(
                Intent(this@AdminHomeActivity, CardLogRegActivity
            ::class.java)
            )
            finish()
        }


        showToast = intent.getStringExtra("NOTIF").toString()
        if (showToast=="create") {
            Toast.makeText(
                this,
                "Data Succesfully Added",
                Toast.LENGTH_SHORT
            ).show()
        }else if(showToast=="update"){
            Toast.makeText(
                this,
                "Data Succesfully Updated",
                Toast.LENGTH_SHORT
            ).show()
        }

        with(binding) {
            btnAdd.setOnClickListener{
                val intentToInput = Intent(this@AdminHomeActivity, InputActivity::class.java)
                intentToInput.putExtra("COMMAND", "ADD")
                startActivity(intentToInput)
            }
        }

    }

    private fun getAlldataFilm() {
        observedataFilms()
    }

    private fun observedataFilms() {
        dataFilmListLiveData.observe(this) { datafilm ->
            if (datafilm.isNotEmpty()) { // Periksa apakah daftar catatan tidak kosong
                Log.d(ContentValues.TAG, "observedataFilms: ${datafilm.size}")
                binding.rvRvnotes.isVisible = true
                binding.textEmpty.isVisible = false
                val recyclerAdapter = DataFilmAdapter(datafilm)

                val swipeToDeleteCallback = object : swipeToDeleteCallback() {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition
                        val deletedItem = recyclerAdapter.getDataAtPosition(position)
                        if (deletedItem != null) {
                            deleteDataFilm(deletedItem)
                        }
                        if (deletedItem != null) {
                            Toast.makeText(
                                this@AdminHomeActivity,
                                "${deletedItem.tittle} deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }else{
                            Toast.makeText(
                                this@AdminHomeActivity,
                                "data deleted",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                binding.rvRvnotes.apply {
                    layoutManager = LinearLayoutManager(this@AdminHomeActivity)
                    setHasFixedSize(true)
                    adapter = recyclerAdapter
                }

                val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
                itemTouchHelper.attachToRecyclerView(binding.rvRvnotes)

            }else{
                binding.rvRvnotes.isVisible = false
                binding.textEmpty.isVisible = true
            }
        }
    }

    private fun observeDataFilmChanges() {
        dataFilmCollectionRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                Log.d("MainActivity", "Data not found: ", error)
                return@addSnapshotListener
            }
            val datafilm = snapshots?.toObjects(DataFilm::class.java)
            if (datafilm != null) {
                dataFilmListLiveData.postValue(datafilm)
            }
        }
    }
    private fun addFilm(datafilm: DataFilm) {
        dataFilmCollectionRef.add(datafilm)
            .addOnSuccessListener { documentReference ->
                val createdDataFilmId = documentReference.id
                datafilm.id = createdDataFilmId
                documentReference.set(datafilm)
                    .addOnFailureListener {
                        Log.d("MainActivity", "Error updating data ID: ", it)
                    }
            }
            .addOnFailureListener {
                Log.d("MainActivity", "Error adding data: ", it)
            }
    }
    private fun updateDataFilm(datafilm: DataFilm) {
        datafilm.id = updateId
        dataFilmCollectionRef.document(updateId).set(datafilm)
            .addOnFailureListener {
                Log.d("MainActivity", "Error updating data: ", it)
            }
    }
    private fun deleteDataFilm(datafilm: DataFilm) {
        if (datafilm.id.isEmpty()) {
            Log.d("MainActivity", "Error deleting: data ID is empty!")
            return
        }
        dataFilmCollectionRef.document(datafilm.id).delete()
            .addOnFailureListener {
                Log.d("MainActivity", "Error deleting data: ", it)
            }
    }
}