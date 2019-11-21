package com.example.shopio.data

import androidx.room.*

@Dao
interface ShopDao {

    @Query("SELECT * FROM shop")
    fun getAllShop() : List<Shop>

    @Insert
    fun insertShop(shop: Shop) : Long

    @Delete
    fun deleteShop(shop: Shop)

    @Update
    fun updateShop(shop: Shop)

    @Query("DELETE FROM shop")
    fun deleteAllShop()

}