package hu.aut.android.kotlinshoppinglist.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHandler(context: Context) :  SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$ID INTEGER PRIMARY KEY," +
                "$NAME TEXT," +
                "$SHOP TEXT," +
                "$WHY TEXT,"+
                "$PRICE INTEGER,"+
                "$QUANTITY INTEGER,"+
                "$BOUGHT INTEGER);"
        db.execSQL(CREATE_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }
    fun addShopItem(shopItem: ShoppingItem) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, shopItem.name)
        values.put(SHOP, shopItem.shop)
        values.put(WHY, shopItem.why)
        values.put(PRICE, shopItem.price)
        values.put(QUANTITY, shopItem.quantity)
        values.put(BOUGHT, shopItem.bought)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    fun getTask(_id: Int): ShoppingItem {
        val tasks = ShoppingItem(0,"",0,false,0,"","")
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    tasks.itemId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    tasks.name = cursor.getString(cursor.getColumnIndex(NAME))
                    tasks.shop = cursor.getString(cursor.getColumnIndex(SHOP))
                    tasks.why = cursor.getString(cursor.getColumnIndex(WHY))
                    tasks.price = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRICE)))
                    tasks.quantity = Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                        QUANTITY)))
                    tasks.bought =cursor.getString(cursor.getColumnIndex(ID)).toBoolean()
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        return tasks
    }
    fun task(): List<ShoppingItem> {
            val taskList = ArrayList<ShoppingItem>()
            val db = writableDatabase
            val selectQuery = "SELECT * FROM $TABLE_NAME"
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        val tasks = ShoppingItem(0,"",0,false,0,"","")
                        tasks.itemId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                        tasks.name = cursor.getString(cursor.getColumnIndex(NAME))
                        tasks.shop = cursor.getString(cursor.getColumnIndex(SHOP))
                        tasks.why = cursor.getString(cursor.getColumnIndex(WHY))
                        tasks.price = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PRICE)))
                        tasks.quantity = Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                            QUANTITY)))
                        tasks.bought =cursor.getString(cursor.getColumnIndex(ID)).toBoolean()
                        taskList.add(tasks)
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }
            return taskList
        }
    fun updateTask(shopItem: ShoppingItem): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, shopItem.name)
        values.put(SHOP, shopItem.shop)
        values.put(WHY, shopItem.why)
        values.put(PRICE, shopItem.price)
        values.put(QUANTITY, shopItem.quantity)
        values.put(BOUGHT, shopItem.bought)
        val success = db.update(TABLE_NAME, values, "$ID=?", arrayOf(shopItem.itemId.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }
    fun deleteTask(_id: Int): Boolean {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, "$ID=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    companion object {
        private const val DB_VERSION = 2
        private const val DB_NAME = "shopping.db"
        private const val TABLE_NAME = "shoppingitem"
        private const val ID = "d"
        private const val NAME = "name"
        private const val SHOP = "shop"
        private const val WHY = "why"
        private const val  PRICE ="price"
        private const val  QUANTITY = "quantity"
        private  const val BOUGHT = "bought"
    }
}