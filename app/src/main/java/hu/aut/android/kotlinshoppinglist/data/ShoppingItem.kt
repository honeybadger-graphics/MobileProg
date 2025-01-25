package hu.aut.android.kotlinshoppinglist.data

import java.io.Serializable

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey

/*
Adatbázis táblát készti el.
Táblanév:shoppingitem.
Oszlopok:itemId, name,  price, bought.
@PrimaryKey(autoGenerate = true): elsődleges kulcs, automatikusan generálva.
Ide szükséges a bővítés új adattal.
 */
@Entity(tableName = "shoppingitem")
data class ShoppingItem(@PrimaryKey(autoGenerate = true) var itemId: Long?,
                        @ColumnInfo(name = "name") var name: String,
                        @ColumnInfo(name = "price") var price: Int,
                        @ColumnInfo(name = "bought") var bought: Boolean,
                        @ColumnInfo(name = "quantity") var quantity: Int,
                        @ColumnInfo(name = "shop") var shop: String,
                        @ColumnInfo(name = "why") var why: String

) : Serializable
