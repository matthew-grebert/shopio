package com.example.shopio.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.shopio.DetailActivity
import com.example.shopio.R
import com.example.shopio.ScrollingActivity
import com.example.shopio.ShopDialog
import com.example.shopio.data.AppDatabase
import com.example.shopio.data.Shop
import com.example.shopio.touch.ShopTouchHelperCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_splash.view.*
import kotlinx.android.synthetic.main.new_shop_dialog.view.*
import kotlinx.android.synthetic.main.shop_row.view.*
import java.util.*
import kotlin.concurrent.thread
import android.widget.Button
import android.widget.Toast


class ShopAdapter : RecyclerView.Adapter<ShopAdapter.ViewHolder>, ShopTouchHelperCallback {


    var shopList = mutableListOf<Shop>()

    val context: Context

    constructor(context: Context, listShops: List<Shop>) {
        this.context = context

        shopList.addAll(listShops)

        /*for (i in 0..20) {
            shopList.add(Shop("2019", "Shop $i", false))
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val shopRow = LayoutInflater.from(context).inflate(
            R.layout.shop_row, parent, false
        )
        return ViewHolder(shopRow)
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var shop = shopList.get(holder.adapterPosition)

        holder.cbShop.text = shop.shopText
        holder.cbShop.isChecked = shop.done
        holder.tvDate.text = shop.createDate
        holder.etPrice.text = "$" + shop.price

        if(shop.category == "Food"){
            holder.ivCatagory.setImageResource(R.drawable.apple)
        }else if(shop.category == "Clothes"){
            holder.ivCatagory.setImageResource(R.drawable.tshirt)
        }else{
            holder.ivCatagory.setImageResource(R.drawable.computer)
        }

        holder.btnDelete.setOnClickListener {
            deleteShop(holder.adapterPosition)
        }


        holder.cbShop.setOnClickListener(){
            shop.done = holder.cbShop.isChecked
            updateShop(shop)
        }

        holder.btnEdit.setOnClickListener {
            (context as ScrollingActivity).showEditShopDialog(
                shop, holder.adapterPosition
            )
        }

        holder.btnDetails.setOnClickListener{
            Toast.makeText(context as ScrollingActivity, shop.details, Toast.LENGTH_LONG).show()
        }
    }

    fun updateShop(shop: Shop){
        Thread{
            AppDatabase.getInstance(context).shopDao().updateShop(shop)
        }.start()
    }




    fun updateShopOnPosition(shop: Shop, index: Int) {
        shopList.set(index, shop)
        notifyItemChanged(index)
    }

    fun deleteShop(index: Int){
        Thread{
            AppDatabase.getInstance(context).shopDao().deleteShop(shopList[index])
            (context as ScrollingActivity).runOnUiThread {
                shopList.removeAt(index)
                notifyItemRemoved(index)
            }
        }.start()
    }

    fun deleteAllShops() {
        Thread {
            AppDatabase.getInstance(context).shopDao().deleteAllShop()

            (context as ScrollingActivity).runOnUiThread {
                shopList.clear()
                notifyDataSetChanged()
            }
        }.start()
    }

    fun addShop(shop: Shop){
        shopList.add(shop)
        notifyItemInserted(shopList.lastIndex)
    }


    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(shopList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onDismissed(position: Int) {
        deleteShop(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbShop = itemView.cbShop
        val ivCatagory = itemView.ivCatagory
        val tvDate = itemView.tvDate
        val etPrice = itemView.tvPrice
        val btnDelete = itemView.btnDelete
        val btnEdit = itemView.btnEdit
        val btnDetails = itemView.btnDetails
        val view = itemView

    }
}
