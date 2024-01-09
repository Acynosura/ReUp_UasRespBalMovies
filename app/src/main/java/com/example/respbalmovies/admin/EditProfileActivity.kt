package com.example.respbalmovies.admin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import com.example.respbalmovies.data.DataFilm
import com.example.respbalmovies.databinding.ActivityEditProfileBinding
import com.example.respbalmovies.sharedPreferences.prefData
import com.example.respbalmovies.sharedPreferences.prefData.Companion.EMAIL
import com.example.respbalmovies.sharedPreferences.prefData.Companion.PHONES
import com.example.respbalmovies.sharedPreferences.prefData.Companion.SHAREDPREF
import com.example.respbalmovies.sharedPreferences.prefData.Companion.UID
import com.example.respbalmovies.sharedPreferences.prefData.Companion.USERNAME
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditProfileBinding
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var storageRef : StorageReference
    private val dataFilmCollectionRef = firestore.collection("datafilm")
    private val UserFilmCollectionRef = firestore.collection("users")
    private var updateId = ""
    private val dataFilmListLiveData: MutableLiveData<List<DataFilm>> by lazy {
        MutableLiveData<List<DataFilm>>()
    }

//    companion object {
//        const val SHAREDPREF = "shared_pref"
//        const val UID = "uid"
//        const val USERNAME = "username"
//        const val EMAIL = "email"
//        const val ROLES = "roles"
//        const val PHONES = "phones"
//        const val IMAGES = "image"
//    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        storageRef = FirebaseStorage.getInstance().reference.child("datafilm")
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences(SHAREDPREF, Context.MODE_PRIVATE)

        val ID = sharedPref.getString(UID, "")
        val USERNAME = sharedPref.getString(USERNAME, "")
        val PHONE = sharedPref.getString(PHONES, "")
        val EMAIL = sharedPref.getString(EMAIL, "")

        Log.d("UserDataDebug", "${ID}, ${USERNAME}, ${PHONE}, ${EMAIL}")



        with(binding){
            binding.btnUpdateProfiles.isVisible = true
            binding.etUsernameProfiles.setText(USERNAME.toString())
            binding.etPhonesProfiles.setText(PHONE.toString())
            binding.etEmailProfiles.setText(EMAIL.toString())


            btnUpdateProfiles.setOnClickListener {
                if (validateInput()) {
                    val new_username = binding.etUsernameProfiles.text.toString()
                    val new_email = binding.etEmailProfiles.text.toString()
                    val new_phone = binding.etPhonesProfiles.text.toString()

                    if (ID != null) {
                        updateDataUser(ID, new_username, new_phone, new_email)
                    }
                    updateId = ""
                    setEmptyField()
                    val IntentToHome = Intent(this@EditProfileActivity, AdminHomeActivity::class.java)
                    IntentToHome.putExtra("NOTIF", "update")
                    startActivity(IntentToHome)
                    finish()
                }
            }
        }
        binding.btBackInput.setOnClickListener{
            startActivity(Intent(this@EditProfileActivity, AdminHomeActivity::class.java))
            finish()
        }
    }

    private fun updateDataUser(id: String, new_username:String, new_phone:String, new_email:String) {
        val userDocRef = UserFilmCollectionRef.document(id)

        val updateData = hashMapOf(
            "username" to new_username,
            "phone" to new_phone,
            "email" to new_email
        )

        userDocRef.update(updateData as Map<String, Any>)
            .addOnSuccessListener {
                val sharedPref = this?.getSharedPreferences(prefData.SHAREDPREF, Context.MODE_PRIVATE)
                if (sharedPref != null) {
                    with(sharedPref.edit()) {
                        putString(com.example.respbalmovies.sharedPreferences.prefData.UID, id)
                        putString(com.example.respbalmovies.sharedPreferences.prefData.USERNAME, new_username)
                        putString(com.example.respbalmovies.sharedPreferences.prefData.EMAIL, new_email)
                        putString(com.example.respbalmovies.sharedPreferences.prefData.PHONES, new_phone)
                        apply()
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@EditProfileActivity, "Update failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setEmptyField() {
        binding.etUsernameProfiles.setText("")
        binding.etUsernameProfiles.setText("")
        binding.etPhonesProfiles.setText("")
    }

    private fun validateInput(): Boolean {
        with(binding) {
            if(etEmailProfiles.text.toString()!="" && etPhonesProfiles.text.toString()!="" && etUsernameProfiles.text.toString()!=""){
                return true
            }else{
                return false
            }
        }

    }

    private fun getFileName(uri: Uri): String {
        return uri.path?.lastIndexOf('/').let { uri.path?.substring(it!!)!! }
    }
}