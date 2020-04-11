package com.example.shoppinglistkotlin

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "product_table")
data class Product(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,

    @ColumnInfo(name = "computerHand")
    var computerHand: Int,

    @ColumnInfo(name = "playerHand")
    var playerHand: Int,

    @ColumnInfo(name = "winner")
    var winner: String

) : Parcelable