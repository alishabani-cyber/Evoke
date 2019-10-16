package com.example.evoke.fragments.camera

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso


@BindingAdapter("loadImage")
fun loadImage(view: ImageView, imageUrl :String? ) {
    Picasso.get().load(imageUrl).into(view)
}
