package com.example.memorama

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity: AppCompatActivity() {
    private lateinit var cards: MutableList<Int>
    private lateinit var gridLayout: GridLayout
    private var conteo: Int = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Cargar el arreglo de las cartas con las imágenes
        cards = mutableListOf(
            R.drawable.imagen1,
            R.drawable.imagen2,
            R.drawable.imagen3,
            R.drawable.imagen4,
            R.drawable.imagen5,
            R.drawable.imagen6,
            R.drawable.imagen7,
            R.drawable.imagen8
        )
        cards.addAll(cards)

        // Shuffle the cards
        cards.shuffle()

        // Set up the card grid
        gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        for (i in 0 until gridLayout.childCount) {
            val imageView = gridLayout.getChildAt(i) as ImageView
            imageView.setImageResource(R.drawable.card_back)
            imageView.setOnClickListener { voltearCarta(imageView, cards[i]) }
        }
    }

    private var firstCard: ImageView? = null
    private var secondCard: ImageView? = null

    private fun voltearCarta(imageView: ImageView, card: Int) {
        if (firstCard == null) {
            firstCard = imageView
            imageView.setImageResource(card)
        } else if (secondCard == null && imageView != firstCard) {
            secondCard = imageView
            imageView.setImageResource(card)

            // Para checar si la primera carta es igual a la segunda seleccionada
            if (firstCard!!.drawable.constantState == secondCard!!.drawable.constantState) {
                firstCard = null
                secondCard = null
                this.conteo++
                val conteoTexto = findViewById<TextView>(R.id.conteo);
                conteoTexto.setText("Puntuación: " + this.conteo.toString());
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    firstCard!!.setImageResource(R.drawable.card_back)
                    secondCard!!.setImageResource(R.drawable.card_back)
                    firstCard = null
                    secondCard = null
                }, 1000)
            }
        }

        // Check if all the cards are matched
        var allMatched = true
        for (i in 0 until gridLayout.childCount) {
            val imageView = gridLayout.getChildAt(i) as ImageView
            if (imageView.drawable.constantState == resources.getDrawable(R.drawable.card_back).constantState) {
                allMatched = false
            }
        }
        if (allMatched) {
            this.conteo = 0;
            val inflater = layoutInflater
            val layout = inflater.inflate(R.layout.ganador, null)

            val text = layout.findViewById<TextView>(R.id.ganador)
            text.text = "FELICIDADES, GANASTE!"

            val toast = Toast(applicationContext)
            toast.duration = Toast.LENGTH_SHORT
            toast.view = layout
            toast.show()
            Handler(Looper.getMainLooper()).postDelayed({
                recreate()
            }, 2000)
        }
    }
}

