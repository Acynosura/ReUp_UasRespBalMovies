package com.example.respbalmovies.admin

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.respbalmovies.R
import com.example.respbalmovies.data.DataFilm
import com.example.respbalmovies.swipeToDeleteCallback
import com.google.firebase.firestore.FirebaseFirestore

//INISIASI RECYCLE VIEW yang ada di adminHome
//DATA AMBIL DARI FIREBASE

class DataFilmAdapter(val dataFilm: List<DataFilm>?): RecyclerView.Adapter<DataFilmAdapter.MyViewHolder>() {
    private val firestore = FirebaseFirestore.getInstance()
    private val dataFilmCollectionRef = firestore.collection("datafilm")
    private lateinit var binding: AdminHomeActivity
    private var updateId = ""
    private val dataFilmListLiveData: MutableLiveData<List<DataFilm>> by lazy {
        MutableLiveData<List<DataFilm>>()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tittle = view.findViewById<TextView>(R.id.txt_tittle_rvnotes)
        val image = view.findViewById<ImageView>(R.id.img_image_rv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rvnotes, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (dataFilm != null) {
            return dataFilm.size
        }
        return 0
    }

    fun getDataAtPosition(position: Int): DataFilm? {
        return dataFilm?.get(position)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tittle.text = dataFilm?.get(position)?.tittle
        Glide.with(holder.itemView.context)
            .load(dataFilm?.get(position)?.image_url)
            .centerCrop()
            .into(holder.image)


        holder.itemView.setOnClickListener {
            val intentToUpdate = Intent(holder.itemView.context, InputActivity::class.java)
            intentToUpdate.putExtra("ID", dataFilm?.get(position)?.id)
            intentToUpdate.putExtra("JUDUL", dataFilm?.get(position)?.tittle)
            intentToUpdate.putExtra("SUTRADARA", dataFilm?.get(position)?.director)
            intentToUpdate.putExtra("DURASI", dataFilm?.get(position)?.duration)
            intentToUpdate.putExtra("DESKRIPSI", dataFilm?.get(position)?.description)
            intentToUpdate.putExtra("HARGA", dataFilm?.get(position)?.price)
            intentToUpdate.putExtra("GENRE", dataFilm?.get(position)?.genre)
            intentToUpdate.putExtra("IMAGE", dataFilm?.get(position)?.image_url)
            intentToUpdate.putExtra("COMMAND", "UPDATE")
            holder.itemView.context.startActivity(intentToUpdate)
        }

        val swipeToDeleteCallback = object : swipeToDeleteCallback(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
            }
        }
    }
}