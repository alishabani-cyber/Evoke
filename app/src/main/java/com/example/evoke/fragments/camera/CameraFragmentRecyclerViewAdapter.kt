package com.example.evoke.fragments.camera

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.evoke.R
import com.example.evoke.models.ProductModel
import com.bumptech.glide.request.RequestOptions


class CameraFragmentRecyclerViewAdapter(
    private val mContext: Context?,
    private var values: ArrayList<ProductModel>,
    private val listener: (String) -> Unit
) : RecyclerView.Adapter<CameraFragmentRecyclerViewAdapter.ViewHolder>() {

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
}