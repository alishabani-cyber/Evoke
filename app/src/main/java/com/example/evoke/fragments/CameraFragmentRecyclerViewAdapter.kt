package com.example.evoke.fragments

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.evoke.R
import com.example.evoke.models.ProductModel
import com.example.evoke.utils.VolleyService
import com.google.gson.Gson
import org.json.JSONObject


class CameraFragmentRecyclerViewAdapter(private val mContext: Context?, private var values: ArrayList<ProductModel>, val listener: (String) -> Unit) :
    RecyclerView.Adapter<CameraFragmentRecyclerViewAdapter.ViewHolder>() {


    override fun getItemCount(): Int {
        return values.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(values[position].title, position, listener)
//        holder?.textView?.text = values[position]

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutId: Int = 0
        layoutId = R.layout.list_items
        val itemView = LayoutInflater.from(mContext).inflate(layoutId, parent, false)
        return ViewHolder(itemView)
    }

    public fun swapDataSet(list: ArrayList<ProductModel>) {
        this.values = list
        notifyDataSetChanged()
    }


    fun addToDataSet(newValue: String?) {
        if(newValue != null && customContains(newValue)) {

            findInAPI(newValue)
        }
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        var textView: TextView? = null
        init {
            textView = itemView?.findViewById(R.id.item_txt)

        }

        fun bind(item: String, pos: Int, listener: (String) -> Unit) = with(itemView) {
            textView?.text = item

            itemView.setOnClickListener {
                listener(item)
            }
        }

    }

    private fun customContains(newValue: String): Boolean {
        for (i in values) {
            if (newValue == i.item) {
                return false
            }
        }
        return true
    }


    private fun findInAPI(rawValue: String?) {

        val url : String = "http://94.182.189.118/api/product/036000291452"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                var gson = Gson()

                var testModel = gson.fromJson(response.toString(), ProductModel::class.java)

                if(!customContains(testModel.item)) {
                    Log.d(TAG, "ADD NEW VALUE TO SET")
                    this.values.add(0, testModel)
                    Log.d(TAG, "Size of value $this.values.size")
                    notifyDataSetChanged()

                }


            },
            Response.ErrorListener { error ->
                Log.d(TAG, error.message)
            })


        VolleyService.requestQueue.add(request)
        VolleyService.requestQueue.start()


    }

    companion object {
        private const val TAG = "Recycler Adapter"
    }
}