package com.example.evoke.fragments.camera

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.evoke.models.ProductModel
import com.example.evoke.network.VolleyRequestSingleton
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.json.JSONObject
import kotlin.collections.ArrayList

class CameraViewModel : ViewModel() {
    private val TAG = "CameraViewModel"
    private var start: Int = 1;

/*

    init {
        var quickViewProduct: ProductModel = ProductModel(1, "1", "1", "1", 1, 1, "1")
        var quickViewProduct2: ProductModel = ProductModel(2, "2", "2", "2", 2, 2, "2")
        var quickViewProduct3: ProductModel = ProductModel(3, "3", "3", "3", 3, 3, "3")

        var p : List<ProductModel> = mutableListOf(
            quickViewProduct,
            quickViewProduct2,
            quickViewProduct3
        )
        var t = MutableLiveData<List<ProductModel>>()
        t.value = p
        _productList.value = t
    }
*/

    var _productList = MutableLiveData<ArrayList<ProductModel>>(arrayListOf())
//    var productList: LiveData<ArrayList<ProductModel>>
//        get() = _productList
//        set(value) {
//            Log.d(TAG, "new data are ${value.value}")
//            _productList.value = value.value
//            Log.d(TAG, "productList setter hasbeen called ")
//        }



    public fun addNewProduct(value :ProductModel) {
        // if the value is null then initializer it with an empty list
        if (_productList.value == null) {
            _productList.value = arrayListOf<ProductModel>()
        }
        // add the new value to list
        _productList.value?.add(0, value)
//        Timber.i("added values are ${_productList.value}")
        // force live data to notify changes
        _productList.value = _productList.value
//        productList.value = _productList
    }


    fun addToProduct(newValue: String?, context: Context) {
        if (newValue != null && !customContains(newValue)) {

            findInAPI(newValue, context)
            return
        }
    }

    private fun customContains(newValue: String): Boolean {
        for (i in _productList.value!!) {
            if (newValue == i.item) {
                return true
            }
        }
        return false
    }


    private fun findInAPI(rawValue: String?, context : Context) {
        // if start is 1 then it can run then turn start to -1 so

        if (start == 1) {
            start = -1

            val url: String = "http://94.182.189.118/api/product/$rawValue"
            val request = JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener<JSONObject> { response ->
                    val gson = Gson()
                    val testModel = gson.fromJson(response.toString(), ProductModel::class.java)

                    // not add if already in set
                    if (!customContains(testModel.item)) {
                        addNewProduct(testModel)
                    }
                    start = 1


                },
                Response.ErrorListener { error ->
                    //                Log.d(TAG, error.message)
                    start = 1
                })


            VolleyRequestSingleton.getInstance(context).addToRequestQueue(request)

        }
    }


}