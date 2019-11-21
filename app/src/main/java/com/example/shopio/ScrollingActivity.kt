package com.example.shopio
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.GridLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.shopio.adapter.ShopAdapter
import com.example.shopio.data.AppDatabase
import com.example.shopio.data.Shop
import com.example.shopio.touch.ShopRecylcerTouchCallback
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.shop_row.*
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import java.util.*

class ScrollingActivity : AppCompatActivity(), ShopDialog.ShopHandler {

    companion object: AppCompatActivity(){
        val KEY_TODO = "KEY_TODO"
        val KEY_STARTED = "KEY_STARTED"
        val TAG_TODO_EDIT = "TAG_TODO_EDIT"
        val TAG_TODO_DIALOG = "TAG_TODO_DIALOG"
    }


    lateinit var shopAdapter: ShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        setSupportActionBar(toolbar)

        initRecyclerView()

        fab.setOnClickListener {
            showAddShopDialog()
        }

        fabDeleteAll.setOnClickListener {
            shopAdapter.deleteAllShops()
        }

        if(!wasStartedBefore()) {

            MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.fab)
                .setPrimaryText("New item")
                .setSecondaryText("Click here to create new items")
                .show()

            /*MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.fabDeleteAll)
                .setPrimaryText("Clear the List")
                .setSecondaryText("Click here to delete all items")
                .show()*/
            saveWasStarted()
        }
    }

    fun saveWasStarted() {
        var sharedPref =
            PreferenceManager.getDefaultSharedPreferences(this)
        var editor = sharedPref.edit()
        editor.putBoolean(KEY_STARTED, true)
        editor.apply()
    }

    fun wasStartedBefore(): Boolean {
        var sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPref.getBoolean(KEY_STARTED, false)
    }

    private fun initRecyclerView() {
        Thread {
            var shops = AppDatabase.getInstance(this@ScrollingActivity).shopDao().getAllShop()

            runOnUiThread {
                shopAdapter = ShopAdapter(this, shops)
                recyclerShop.adapter = shopAdapter

                var itemDecorator = DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
                recyclerShop.addItemDecoration(itemDecorator)

                //recyclerShop.layoutManager = GridLayoutManager(this,
                //    2)

                val callback = ShopRecylcerTouchCallback(shopAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerShop)
            }

        }.start()

    }

    fun showAddShopDialog() {
        ShopDialog().show(supportFragmentManager, TAG_TODO_DIALOG)
    }

    var editIndex: Int = -1

    fun showEditShopDialog(shopToEdit: Shop, idx: Int) {
        editIndex = idx
        val editDialog = ShopDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_TODO, shopToEdit)
        editDialog.arguments = bundle

        editDialog.show(supportFragmentManager, TAG_TODO_EDIT)
    }


    fun saveShop(shop: Shop) {
        Thread {
            var newId = AppDatabase.getInstance(this@ScrollingActivity).shopDao().insertShop(shop)
            shop.shopId = newId

            runOnUiThread {
                shopAdapter.addShop(shop)
            }

        }.start()
    }

    override fun shopCreated(item: Shop) {
        saveShop(item)
    }

    override fun shopUpdated(item: Shop) {
        Thread{
            AppDatabase.getInstance(
                this@ScrollingActivity).shopDao().updateShop(item)
            runOnUiThread{
                shopAdapter.updateShopOnPosition(item, editIndex);
            }
        }.start()
    }




}