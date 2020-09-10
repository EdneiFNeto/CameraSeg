package com.example.camera.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.camera.R
import com.example.camera.ui.base.BaseActivity
import com.example.camera.ui.base.interfaces.CallbackClick
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_listar_cameras.*
import java.util.*

class ListarCamerasActivity : BaseActivity() {

    private var ids = arrayListOf(R.id.item_refresh)
    private lateinit var adapter: MyAdapter
    private var TAG = "ListarCamerasActivityLog"
    private val images = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_cameras)
    }

    override fun onResume() {
        super.onResume()

        adapter = MyAdapter(this, images)
        recycleViewImage.adapter = adapter
        showIcons()
        showImages()
    }

    private fun showIcons() {
        showNavigationIcon(R.drawable.ic_action_left_arrow, object : CallbackClick{
            override fun onClick() {
                finish()
            }
        })
        for(id in ids){
            icons(id, true, object:CallbackClick{
                override fun onClick() {
                    when(id){
                        R.id.item_refresh->showImages()
                    }
                }
            })
        }
    }

    private fun showImages() {
        images.clear()
        val storage = FirebaseStorage.getInstance()
        val listRef = storage.reference.child("images/user")

        listRef.listAll()
            .addOnSuccessListener { listResult ->

                listResult.prefixes.forEach { prefix ->
                    Log.e(TAG, "Prfix $prefix")
                }

                listResult.items.forEach { item ->
                    Log.e(TAG, "item $item")
                    item.downloadUrl.addOnSuccessListener { uri ->
                        Log.e(TAG, "Uri $uri")
                        images.add(uri.toString())
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "Exception  $it")
            }

            .addOnCompleteListener { result->
                Log.e(TAG, "result $result")
            }
    }

    inner class MyAdapter(private val context: Context, private val images: ArrayList<String>) :
        RecyclerView.Adapter<MyAdapter.Holder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(
                LayoutInflater.from(context).inflate(
                    R.layout.layout_images,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int {
            return images.size
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.add(images[position])
        }

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val image = itemView.findViewById<ImageView>(R.id.cardImage)

            fun add(string: String) {
                Picasso.get()
                    .load(string)
                    .into(image)
            }
        }

    }
}
