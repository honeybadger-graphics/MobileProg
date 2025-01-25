package hu.aut.android.kotlinshoppinglist.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import hu.aut.android.kotlinshoppinglist.MainActivity
import hu.aut.android.kotlinshoppinglist.R
import hu.aut.android.kotlinshoppinglist.adapter.ShoppingAdapter.ViewHolder
import hu.aut.android.kotlinshoppinglist.data.AppDatabase
import hu.aut.android.kotlinshoppinglist.data.ShoppingItem
import hu.aut.android.kotlinshoppinglist.touch.ShoppingTouchHelperAdapter
import kotlinx.android.synthetic.main.row_item.view.*
import java.util.*

class ShoppingAdapter : RecyclerView.Adapter<ViewHolder>, ShoppingTouchHelperAdapter {
    /* ShoppingItem elemek listája*/
    private val items = mutableListOf<ShoppingItem>()
    private val context: Context

    constructor(context: Context, items: List<ShoppingItem>) : super() {
        this.context = context
        this.items.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.row_item, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*Itt kérjük le az egyes ShoppingItem elemek adattagjait, itt is szükséges az adattaggal a bővítés*/
        holder.tvName.text = items[position].name
        holder.tvPrice.text = items[position].price.toString() + "Ft"
        holder.cbBought.isChecked = items[position].bought
        holder.tvQuantity.text = items[position].quantity.toString() +"db"
        holder.tvShop.text = items[position].shop
        holder.tvWhy.text = items[position].why
        /*Delete gomb eseménykezeője (a főoldalon)*/
        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }
        /*Edit gomb eseménykezelője (a főoldalon), megnyitja az edit dialógust, átadja az adott ShoppingItem-et neki*/
        holder.btnEdit.setOnClickListener {
            (holder.itemView.context as MainActivity).showEditItemDialog(
                    items[holder.adapterPosition])
        }
        /*Checkbox eseménykezelője, állítja a checkbox értékét, azaz a ShoppingItem-nek, az isChecked adattagját.
        Az adatbázisban is frissíti
         */
        holder.cbBought.setOnClickListener {
            items[position].bought = holder.cbBought.isChecked
            val dbThread = Thread {
                //Itt frissíti a DB-ben
                AppDatabase.getInstance(context).shoppingItemDao().updateItem(items[position])
            }
            dbThread.start()
        }
    }
    /*Új elem hozzáadásakor hívódik meg*/
    fun addItem(item: ShoppingItem) {
        items.add(item)
        notifyItemInserted(items.lastIndex)
    }
    /*Elem törlésekor hívódik meg. Az adatbázisból törli az elemet (DAO-n keresztül)*/
    fun deleteItem(position: Int) {
        val dbThread = Thread {
            AppDatabase.getInstance(context).shoppingItemDao().deleteItem(
                    items[position])
            (context as MainActivity).runOnUiThread{
                items.removeAt(position)
                notifyItemRemoved(position)
            }
        }
        dbThread.start()
    }
    /*Update-kor hívódik meg*/
    fun updateItem(item: ShoppingItem) {
        val idx = items.indexOf(item)
        items[idx] = item
        notifyItemChanged(idx)
    }

    override fun onItemDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)

        notifyItemMoved(fromPosition, toPosition)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /*a ShoppingItem elemek, ide kell a bővítés új taggal*/
        /*Itt a gombokat, checkboxot is lekérjük*/
        val tvName: TextView = itemView.tvName
        val tvPrice: TextView = itemView.tvPrice
        val tvQuantity: TextView = itemView.tvQuantity
        val tvWhy: TextView = itemView.tvWhy
        val tvShop: TextView = itemView.tvShop
        val cbBought: CheckBox = itemView.cbBought
        val btnDelete: Button = itemView.btnDelete
        val btnEdit: Button = itemView.btnEdit
    }
}