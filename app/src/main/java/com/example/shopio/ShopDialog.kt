package com.example.shopio


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.shopio.data.Shop
import kotlinx.android.synthetic.main.new_shop_dialog.view.*
import java.util.*

class ShopDialog : DialogFragment() {

    interface ShopHandler {
        fun shopCreated(item: Shop)
        fun shopUpdated(item: Shop)
    }

    private lateinit var shopHandler: ShopHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ShopHandler) {
            shopHandler = context
        } else {
            throw RuntimeException(
                "The activity does not implement the ShopHandlerInterface")
        }
    }

    private lateinit var etShopDate: EditText
    private lateinit var etShopText: EditText
    private lateinit var etShopPrice: EditText
    private lateinit var etShopDetails: EditText
    private lateinit var spnCategory: Spinner


    var isEditMode = false


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())


        builder.setTitle("New Item")

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.new_shop_dialog, null
        )



// Create an ArrayAdapter using the string array and a default spinner layout

        spnCategory = rootView.spn_catagory
        etShopDate = rootView.etDate
        etShopText = rootView.etShop
        etShopPrice = rootView.etPrice
        etShopDetails = rootView.etDetails
        builder.setView(rootView)

        isEditMode = ((arguments != null) && arguments!!.containsKey(ScrollingActivity.KEY_TODO))

        if (isEditMode) {
            println("Edit Mode")
            builder.setTitle("Edit Item")
            var shop: Shop = (arguments?.getSerializable(ScrollingActivity.KEY_TODO) as Shop)

            etShopDate.setText(shop.createDate)
            etShopDate.isEnabled=false
            etShopText.setText(shop.shopText)
            etShopPrice.setText(shop.price)
            etShopDetails.setText(shop.details)
            if(shop.category == "Food") {
                spnCategory.setSelection(0)
            }else if(shop.category == "Clothes"){
                spnCategory.setSelection(1)
            }else{
                spnCategory.setSelection(2);
            }
        }

        builder.setPositiveButton("OK") {
                dialog, witch -> // empty
        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etShopText.text.isNotEmpty() && etShopPrice.text.isNotEmpty()) {
                if(isEditMode) {
                    handleShopEdit()
                }else{
                    handleShopCreate()
                }

                dialog.dismiss()
            } else {
                if(etShopText.text.isEmpty()) {
                    etShopText.error = "This field can not be empty"
                }
                if(etShopPrice.text.isEmpty()) {
                    etShopPrice.error = "This field can not be empty"
                }

            }
        }
    }

    private fun handleShopCreate() {
        shopHandler.shopCreated(
            Shop(
                null,
                Date(System.currentTimeMillis()).toString(),
                 etShopText.text.toString(), spnCategory.selectedItem.toString(), etShopPrice.text.toString(), etShopDetails.text.toString(),
                false
            )
        )
    }

    private fun handleShopEdit() {
        val shopToEdit = arguments?.getSerializable(
            ScrollingActivity.KEY_TODO
        ) as Shop
        shopToEdit.createDate = etShopDate.text.toString()
        shopToEdit.shopText = etShopText.text.toString()
        shopToEdit.price = etShopPrice.text.toString()
        shopToEdit.category = spnCategory.selectedItem.toString()
        shopToEdit.details = etShopDetails.text.toString()

        shopHandler.shopUpdated(shopToEdit)
    }
}