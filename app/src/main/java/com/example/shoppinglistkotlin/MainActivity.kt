package com.example.shoppinglistkotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var productRepository: ProductRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private var hand: String = "paper"
    private var cHand: String = "rock"

    private var playerHandInt: Int = 1
    private var computerHandInt: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Rock, Paper, Scissors Kotlin"

        //deleteGameList()

        productRepository = ProductRepository(this)
        initViews()
    }

    private fun deleteGameList() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                productRepository.deleteAllProducts()
            }
        }
    }

    private fun initViews() {
        btnRock.setOnClickListener { onRockClick() }
        btnPaper.setOnClickListener { onPaperClick() }
        btnScissors.setOnClickListener { onScissorsClick() }
    }

    private fun addProduct(playerHandIndex: Int, computerHandIndex: Int) {
        mainScope.launch {
            val currentTime = Calendar.getInstance().time
            val product = Product(

                computerHand = computerHandIndex,
                playerHand = playerHandIndex,
                winner = winlose.text.toString(),
                date = currentTime
            )

            withContext(Dispatchers.IO) {
                productRepository.insertProduct(product)
            }
        }
    }

    private fun computer() {
        computerHandInt = (1..3).random()

        cHand = when (computerHandInt) {
            1 -> "rock"
            2 -> "paper"
            else -> "scissors"
        }

        playerHandInt = when (hand) {
            "rock" -> 1
            "paper" -> 2
            else -> 3
        }

        var playerResourceID: Int
        var computerResourceID: Int

        playerResourceID = when (hand) {
            "rock" -> R.drawable.rock
            "paper" -> R.drawable.paper
            else -> R.drawable.scissors
        }

        computerResourceID = when (cHand) {
            "rock" -> R.drawable.rock
            "paper" -> R.drawable.paper
            else -> R.drawable.scissors
        }

        playerHand.setImageResource(playerResourceID)
        computerHand.setImageResource(computerResourceID)

        if(cHand == hand)
            winlose.text = "Draw"
        if(cHand == "rock" && hand == "paper" || cHand == "paper" && hand == "scissors" || cHand == "scissors" && hand == "rock")
            winlose.text = "You win!"
        if(hand == "rock" && cHand == "paper" || hand == "paper" && cHand == "scissors" || hand == "scissors" && cHand == "rock")
            winlose.text = "Computer wins!"

        addProduct(playerResourceID, computerResourceID)
    }

    private fun onRockClick(){
        hand = "rock"
        computer()
    }

    private fun onPaperClick(){
        hand = "paper"
        computer()
    }

    private fun onScissorsClick(){
        hand = "scissors"
        computer()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.history_fab -> {
                val profileActivityIntent = Intent(this, GameHistory::class.java)
                startActivity(profileActivityIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    companion object {
        const val PRODUCT_EXTRA = "PRODUCT_EXTRA"
    }
}