package com.example.respbalmovies.user

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.respbalmovies.databinding.ActivityFilmDescBinding

class filmDescActivity : AppCompatActivity() {
    companion object {
        var judul_film = ""
        var gambar = ""
    }

    private lateinit var binding : ActivityFilmDescBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFilmDescBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val ID = intent.getStringExtra("ID")
        val TITTLE = intent.getStringExtra("TITTLE")
        val GENRE = intent.getStringExtra("GENRE")
        val DIRECTOR = intent.getStringExtra("DIRECTOR")
        val DURATION = intent.getStringExtra("DURATION")
        val DESCRIPTION = intent.getStringExtra("DESCRIPTION")
        val IMAGE = intent.getStringExtra("IMAGE")


        Toast.makeText(this, "${TITTLE}, ${GENRE}, ${DIRECTOR}, ${DURATION}, ${IMAGE}", Toast.LENGTH_SHORT).show()

        Glide.with(this)
            .load(IMAGE)
            .centerCrop()
            .into(binding.imgBgimgDetail)

        Glide.with(this)
            .load(IMAGE)
            .centerCrop()
            .into(binding.imgImageDetail)

        binding.txtGenreDetail.text = "Genre : ${GENRE}"
        binding.txtTittleDetail.text = TITTLE
        binding.txtDirectorDetail.text = "Directed by : " + DIRECTOR
        binding.txtDurationDetail.text = DURATION
        binding.txtDescriptionDetail.text = DESCRIPTION

    }
}