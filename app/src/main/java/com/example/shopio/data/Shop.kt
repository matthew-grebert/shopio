package com.example.shopio.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "shop")
data class Shop(
    @PrimaryKey(autoGenerate = true) var shopId: Long?,
    @ColumnInfo(name = "createDate") var createDate: String,
    @ColumnInfo(name = "shopText") var shopText: String,
    @ColumnInfo(name = "catagory") var category: String,
    @ColumnInfo(name = "price") var price: String,
    @ColumnInfo(name = "details") var details: String,
    @ColumnInfo(name = "done") var done: Boolean
) : Serializable