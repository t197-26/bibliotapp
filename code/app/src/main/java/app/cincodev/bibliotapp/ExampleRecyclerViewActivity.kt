package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ExampleRecyclerViewActivity : AppCompatActivity() {
    lateinit var buttonDigitalizacoes : MenuButtonView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example_recyclerview)

        val dataset = arrayOf("January", "February", "March")
        val customAdapter = ExampleCustomAdapter(dataset)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = customAdapter

        buttonDigitalizacoes = findViewById(R.id.menuButtonDigitalizacoes)

        buttonDigitalizacoes.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
}