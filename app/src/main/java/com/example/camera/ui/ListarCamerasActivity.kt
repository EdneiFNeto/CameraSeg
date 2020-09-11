package com.example.camera.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.example.camera.R
import com.example.camera.async.base.BaseSelect
import com.example.camera.model.User
import com.example.camera.ui.base.BaseActivity
import com.example.camera.ui.base.interfaces.CallbackClick
import com.example.camera.util.CallBack
import com.example.camera.util.ExecuteTaskUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_listar_cameras.*
import java.util.*

class ListarCamerasActivity : BaseActivity() {

    private var broadcastManager: LocalBroadcastManager? = null
    private lateinit var mReceiver: Receiver
    private var dialog: AlertDialog? = null
    private var ids = arrayListOf(R.id.item_refresh)
    private lateinit var adapter: MyAdapter
    private var TAG = "ListarCamerasActivityLog"
    private val images = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_cameras)
        SelectUser(this, arrayListOf(), resources.getString(R.string.class_user))
            .execute()
    }

    override fun onResume() {
        super.onResume()

        adapter = MyAdapter(this, images)
        recycleViewImage.adapter = adapter
        showIcons()

        mReceiver = Receiver()
        broadcastManager = LocalBroadcastManager.getInstance(this)
        broadcastManager?.registerReceiver(
            mReceiver,
            IntentFilter(resources.getString(R.string.action_get_user))
        )
    }


    private fun showIcons() {
        showNavigationIcon(R.drawable.ic_action_left_arrow, object : CallbackClick {
            override fun onClick() {
                finish()
            }
        })

        for (id in ids) {
            icons(id, true, object : CallbackClick {
                override fun onClick() {
                    when (id) {
                        R.id.item_refresh -> showImages()
                    }
                }
            })
        }
    }

    private fun showImages() {

        images.clear()
        Log.e(TAG, "User id ${user?.id}")

        var dialogBuilder = MaterialAlertDialogBuilder(this)
        val myView = LayoutInflater.from(this).inflate(R.layout.dialog, null)
        myView.findViewById<TextView>(R.id.titleLoader).text =
            resources.getString(R.string.update_image)
        dialogBuilder.setView(myView)
        dialogBuilder.setCancelable(false)
        dialog = dialogBuilder.create()
        dialog?.show()

        val storage = FirebaseStorage.getInstance()
        val listRef = storage.reference.child("images/${user?.id}")

        Log.e(TAG, "list Refr $listRef")

        listRef.listAll()
            .addOnSuccessListener { listResult ->

                listResult.prefixes.forEach { prefix ->
                    Log.e(TAG, "Prefix $prefix")
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
            .addOnCompleteListener {
                Log.e(TAG, "addOnCompleteListener")
                dialog?.dismiss()
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

    override fun onPause() {
        super.onPause()
        if (dialog?.isShowing == true)
            dialog?.dismiss()

        if (mReceiver != null) {
            broadcastManager?.unregisterReceiver(mReceiver)
        }
    }


    inner class Receiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.e(TAG, "Data ${intent?.data}")
            Log.e(TAG, "Action ${intent?.action}")

            when (intent?.action) {
                resources.getString(R.string.action_get_user) -> {
                    if (intent.hasExtra(resources.getString(R.string.extra_success))) {
                        showImages()
                    }
                }
            }
        }

    }


}
