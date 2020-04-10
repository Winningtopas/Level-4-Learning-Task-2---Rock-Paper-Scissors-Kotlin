package com.example.shoppinglistkotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.your_game_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val products = arrayListOf<Product>()
    private lateinit var productRepository: ProductRepository
    private val productAdapter = ProductAdapter(products)
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private var hand: String = "paper"
    private var cHand: String = "rock"

    private var computerHandInt: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Rock, Paper, Scissors Kotlin"

        productRepository = ProductRepository(this)
        initViews()
    }

    private fun initViews() {
        ///////    rvShoppingList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        ///////     rvShoppingList.adapter = productAdapter
        ///////      rvShoppingList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))

        //createItemTouchHelper().attachToRecyclerView(rvShoppingList)
  ///////      createItemTouchHelper().attachToRecyclerView(rvShoppingList)

        getShoppingListFromDatabase()

        fab.setOnClickListener { addProduct() }

        btnRock.setOnClickListener { onRockClick() }
        btnPaper.setOnClickListener { onPaperClick() }
        btnScissors.setOnClickListener { onScissorsClick() }

        //updateUI()
    }

   // private fun updateUI() {
        //tvLastThrow.text = getString(R.string.last_throw, lastThrow)

        //when (hand) {
            //"rock" -> playerHand.setImageResource(R.drawable.rock)
            //"paper" -> playerHand.setImageResource(R.drawable.paper)
            //"scissors" -> playerHand.setImageResource(R.drawable.scissors)
        //}
    //}

    private fun computer() {
        computerHandInt = (1..3).random()

        when (computerHandInt) {
            1 -> cHand = "rock"
            2 -> cHand = "paper"
            3 -> cHand = "scissors"
        }

        when (cHand) {
        "rock" -> computerHand.setImageResource(R.drawable.rock)
        "paper" -> computerHand.setImageResource(R.drawable.paper)
        "scissors" -> computerHand.setImageResource(R.drawable.scissors)
        }

        if(cHand == hand)
            winlose.text = "Draw"
        if(cHand == "rock" && hand == "paper" || cHand == "paper" && hand == "scissors" || cHand == "scissors" && hand == "rock")
            winlose.text = "You win!"
        if(hand == "rock" && cHand == "paper" || hand == "paper" && cHand == "scissors" || hand == "scissors" && cHand == "rock")
            winlose.text = "Computer wins!"


    }



    private fun onRockClick(){
        hand = "rock"
        Toast.makeText(this, hand + " " + cHand + computerHandInt, Toast.LENGTH_SHORT).show()
        playerHand.setImageResource(R.drawable.rock)
        computer()

    }

    private fun onPaperClick(){
        hand = "paper"
        Toast.makeText(this, hand, Toast.LENGTH_SHORT).show()
        playerHand.setImageResource(R.drawable.paper)
        computer()

    }

    private fun onScissorsClick(){
        hand = "scissors"
        Toast.makeText(this, hand, Toast.LENGTH_SHORT).show()
        playerHand.setImageResource(R.drawable.scissors)
        computer()

    }


    private fun validateFields(): Boolean {
        return if (etProduct.text.toString().isNotBlank() && etQuantity.text.toString().isNotBlank()) {
            true
        } else {
            Toast.makeText(this, "Please fill in the fields", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun addProduct() {
        if (validateFields()) {
            mainScope.launch {
                val product = Product(
                    name = etProduct.text.toString(),
                    quantity = etQuantity.text.toString().toInt()
                )

                withContext(Dispatchers.IO) {
                    productRepository.insertProduct(product)
                }

                getShoppingListFromDatabase()
            }
        }
    }

   /* private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val productToDelete = products[position]
                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        productRepository.deleteProduct(productToDelete)
                    }
                    getShoppingListFromDatabase()
                }
            }
        }
        return ItemTouchHelper(callback)
    }
*/

    private fun getShoppingListFromDatabase() {
        mainScope.launch {
            val shoppingList = withContext(Dispatchers.IO) {
                productRepository.getAllProducts()
            }
            products.clear()
            products.addAll(shoppingList)
            productAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    private fun deleteShoppingList() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                productRepository.deleteAllProducts()
            }
            getShoppingListFromDatabase()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete_shopping_list -> {
                deleteShoppingList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
