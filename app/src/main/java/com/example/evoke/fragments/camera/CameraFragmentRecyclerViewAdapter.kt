package com.example.evoke.fragments.camera

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.example.evoke.R
import com.example.evoke.models.ProductModel
import com.example.evoke.network.VolleyService
import com.google.gson.Gson
import org.json.JSONObject
import com.bumptech.glide.request.RequestOptions
import com.example.evoke.databinding.FragmentCameraBinding
import com.example.evoke.fragments.camera.CameraFragment.Companion.appContext
import com.squareup.picasso.Picasso


class CameraFragmentRecyclerViewAdapter(
    private val mContext: Context?,
    private var values: ArrayList<ProductModel>,
    val listener: (String) -> Unit,
    val binding: FragmentCameraBinding
) : RecyclerView.Adapter<CameraFragmentRecyclerViewAdapter.ViewHolder>() {
    init {
        var quickViewProduct: ProductModel = ProductModel(1, "2", "3", "4", 2, 1, "e")
        binding.quickViewProduct = quickViewProduct
    }

    var stop: Int = 1;
    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values[position], position, listener)
//        holder?.textView?.text = values[position]

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutId: Int = 0
        layoutId = R.layout.list_items
        val itemView = LayoutInflater.from(mContext).inflate(layoutId, parent, false)
        return ViewHolder(
            itemView
        )
    }

    public fun swapDataSet(list: ArrayList<ProductModel>) {
        this.values = list
        notifyDataSetChanged()
    }


    fun addToDataSet(newValue: String?) {
        if (newValue != null && !customContains(newValue)) {

            findInAPI(newValue)
            return
        }
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var textView: TextView? = null
        var imageView: ImageView

        init {
            textView = itemView?.findViewById(R.id.item_txt_name)
            imageView = itemView?.findViewById(R.id.item_image)!!
        }

        fun bind(item: ProductModel, pos: Int, listener: (String) -> Unit) = with(itemView) {
            textView?.text = item.title
            val options = RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round)
            Glide.with(this).load(item.image).apply(options).into(imageView)

            itemView.setOnClickListener {
                listener(item.url)
            }
        }

    }

    private fun customContains(newValue: String): Boolean {
        for (i in values) {
            if (newValue == i.item) {
                return true
            }
        }
        return false
    }


    private fun findInAPI(rawValue: String?) {

        if (stop == 1) {
            stop = -1

            val url: String = "http://94.182.189.118/api/product/$rawValue"
            val request = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener<JSONObject> { response ->
                    var gson = Gson()

                    var testModel = gson.fromJson(response.toString(), ProductModel::class.java)

//                not add if already in set
                    if (!customContains(testModel.item)) {
                        this.values.add(0, testModel)
                        notifyDataSetChanged()
                        binding.invalidateAll()
                        showpreivew(testModel)

                    }
                    stop = 1


                },
                Response.ErrorListener { error ->
                    //                Log.d(TAG, error.message)
                    stop = 1
                })


            VolleyService.requestQueue.add(request)
            VolleyService.requestQueue.start()

        }
    }

    private fun showpreivew(product: ProductModel) {
//        CameraFragment.cha(product)
        binding.quickViewProduct = product
        binding.invalidateAll()

        Picasso.get().load(product.image).into(binding.previewImageView)

        binding.ConsGred.setOnClickListener { v: View? ->
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(product.url)
            appContext.startActivity(openURL)
        }
    }

    companion object {
        private const val TAG = "Recycler Adapter"
    }
}