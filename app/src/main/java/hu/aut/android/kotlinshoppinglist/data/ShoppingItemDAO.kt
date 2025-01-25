package hu.aut.android.kotlinshoppinglist.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
/*
Itt az adatbázis műveletek találhatóak.
Új adattagkor (új ShippingItem adattag), nem szükséges módosítani itt.
 */
@Dao
interface ShoppingItemDAO {

    //Az összes listázása
    @Query("SELECT * FROM shoppingitem")
    fun findAllItems(): List<ShoppingItem>

    //Egy elem beszúrása
    @Insert
    fun insertItem(item: ShoppingItem): Long
    //Egy törlése
    @Delete
    fun deleteItem(item: ShoppingItem)
    //Egy módosítása
    @Update
    fun updateItem(item: ShoppingItem)

}
