package hu.aut.android.kotlinshoppinglist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.widget.EditText
import hu.aut.android.kotlinshoppinglist.data.ShoppingItem
import kotlinx.android.synthetic.main.dialog_create_item.view.*
import java.util.*
/*
Ez a dialógus ablak szolgál az új Shipping Item felvitelére, és a meglevő Shopping Item módosítására
 */

class ShoppingItemDialog : DialogFragment() {

    private lateinit var shoppingItemHandler: ShoppingItemHandler
    //Shopping Item elemek text-ben, ide szükséges a bővítés a Shopping Item új adattagja esetén
    private lateinit var etItem: EditText
    private lateinit var etPrice: EditText
    private lateinit var etQuantity: EditText
    private lateinit var etShop: EditText
    private lateinit var etWhy: EditText

    interface ShoppingItemHandler {
        fun shoppingItemCreated(item: ShoppingItem)

        fun shoppingItemUpdated(item: ShoppingItem)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ShoppingItemHandler) {
            shoppingItemHandler = context
        } else {
            throw RuntimeException("The Activity does not implement the ShoppingItemHandler interface")
        }
    }
/*Új Shopping Item felvitelekor ez hívódik meg. A felirat a New Item lesz*/
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("New Item")

        initDialogContent(builder)

        builder.setPositiveButton("OK") { dialog, which ->
            // keep it empty
        }
        return builder.create()
    }

    private fun initDialogContent(builder: AlertDialog.Builder) {
        /*etItem = EditText(activity)
        builder.setView(etItem)*/

        //dialog_create_item.xml-el dolgozunk
        val rootView = requireActivity().layoutInflater.inflate(R.layout.dialog_create_item, null)
        //Shopping Item tagok az xml-ből (EditText elemek)
        //Itt is szükséges a bővítés új Shopping Item adattag esetén
        etItem = rootView.etName
        etPrice = rootView.etPrice
        etQuantity=rootView.etQuantity
        etShop=rootView.etShop
        etWhy=rootView.etWhy
        builder.setView(rootView)
        //Megnézzük, hogy kapott-e argumentumot (a fő ablakból), ha igen, akkor az adattagokat beállítjuk erre
        // tehát az Edittext-ek kapnak értéket, és az ablak címét beállítjuk
        val arguments = this.arguments
        if (arguments != null &&
                arguments.containsKey(MainActivity.KEY_ITEM_TO_EDIT)) {
            val item = arguments.getSerializable(
                    MainActivity.KEY_ITEM_TO_EDIT) as ShoppingItem
            //Itt is szükséges a bővítés új Shopping Item adattag esetén
            etItem.setText(item.name)
            etPrice.setText(item.price.toString())
            etQuantity.setText(item.quantity.toString())
            etShop.setText(item.shop)
            etWhy.setText(item.why)
            builder.setTitle("Edit shopping list")
        }
    }


    override fun onResume() {
        super.onResume()

        val dialog = dialog as AlertDialog
        val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)
         //OK gomb a dialógus ablakon
        //vizsgálja az eseménykezelője, hogy a dialógus ablak kapott-e paramétereket
        //Ha kapott, akkor a handleItemEdit() hívódik meg (edit)
        //Ha nem kapott, akor a handleItemCreate() hívódik meg (create)
        positiveButton.setOnClickListener {
            if (etItem.text.isNotEmpty()) {
                val arguments = this.arguments
                if (arguments != null &&
                        arguments.containsKey(MainActivity.KEY_ITEM_TO_EDIT)) {
                    handleItemEdit()
                } else {
                    handleItemCreate()
                }

                dialog.dismiss()
            } else {
                etItem.error = "This field can not be empty"
            }
        }
    }
    //Új elem esetén hvódik meg, egy új ShoppingItem-et hoz létre
    //az itemId azért null, mert a DB adja a kulcsot
    //Itt is szükséges a bővítés új Shopping Item adattag esetén
    private fun handleItemCreate() {
        shoppingItemHandler.shoppingItemCreated(ShoppingItem(
                null,
                etItem.text.toString(),
                etPrice.text.toString().toInt(),
                false,
                etQuantity.text.toString().toInt(),
                etShop.text.toString(),
                etWhy.text.toString()
        ))
    }
    //Edit esetén hívódik meg, az edit-et csinálja, paraméterként átadja az adatokat
    //Itt is szükséges a bővítés új Shopping Item adattag esetén
    private fun handleItemEdit() {
        val itemToEdit = arguments?.getSerializable(
                MainActivity.KEY_ITEM_TO_EDIT) as ShoppingItem
        itemToEdit.name = etItem.text.toString()
        itemToEdit.price = etPrice.text.toString().toInt()
        itemToEdit.quantity=etQuantity.text.toString().toInt()
        itemToEdit.shop=etShop.text.toString()
        itemToEdit.why=etWhy.text.toString()

        shoppingItemHandler.shoppingItemUpdated(itemToEdit)
    }
}
