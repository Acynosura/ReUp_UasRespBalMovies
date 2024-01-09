package com.example.respbalmovies.user

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.respbalmovies.R
import com.example.respbalmovies.admin.AdminHomeActivity
import com.example.respbalmovies.data.DataFilm
import com.example.respbalmovies.swipeToDeleteCallback
import com.google.firebase.firestore.FirebaseFirestore



//INISIASI RECYCLE VIEW yang ada di PUBLICHOME
//DATA AKAN DI AMBIL DARI FIREBASE
//TAMPILAN RECYCLENYA BEDA DARI ADMIN


class dataFilmAdapter(val dataFilm: List<DataFilm>?): RecyclerView.Adapter<dataFilmAdapter.MyViewHolder>() {
    private val firestore = FirebaseFirestore.getInstance()
    private val dataFilmCollectionRef = firestore.collection("datafilm")
    private lateinit var binding: AdminHomeActivity
    private var updateId = ""
    private val dataFilmListLiveData: MutableLiveData<List<DataFilm>> by lazy {
        MutableLiveData<List<DataFilm>>()
    }

    private fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(adapterPosition, itemViewType)
        }
        return this
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tittle = view.findViewById<TextView>(R.id.txt_tittle_items)
        val image = view.findViewById<ImageView>(R.id.img_image_items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return MyViewHolder(view).listen{ data, _->
            intentDetail(parent, dataFilm!![data])
        }
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

        val swipeToDeleteCallback = object : swipeToDeleteCallback(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
            }
        }
    }

    private fun intentDetail(parrent: ViewGroup, item:DataFilm){
        val intentToDetail = Intent(parrent.context.applicationContext, filmDescActivity::class.java)

        intentToDetail.putExtra("ID", item.id)
        intentToDetail.putExtra("TITTLE", item.tittle)
        intentToDetail.putExtra("GENRE", item.genre)
        intentToDetail.putExtra("DIRECTOR", item.director)
        intentToDetail.putExtra("DURATION", item.duration)
        intentToDetail.putExtra("DESCRIPTION", item.description)
        intentToDetail.putExtra("IMAGE", item.image_url)

        Toast.makeText(parrent.context.applicationContext, "${item.tittle}, ${item.genre}, ${item.director}, ${item.duration}, ${item.image_url}", Toast.LENGTH_SHORT).show()

        parrent.context.startActivity(intentToDetail)
    }
}