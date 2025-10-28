package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminDigitalizationOrders : AppCompatActivity() {
    lateinit var arrowBackButtonView: ImageButton
    lateinit var homeButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_digitalization_orders)

        arrowBackButtonView = findViewById(R.id.AdminDigitalizationOrdersArrowBack)
        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        homeButton = findViewById(R.id.AdminDigitalizationOrdersBottomBarHomeImageView)
        homeButton.setOnClickListener {
            startActivity(Intent(this, AdminHome::class.java))
        }



        val dataset = arrayOf(
            DigitalizacaoItem("2357911", "10-30", "111111", "Em fila"),
            DigitalizacaoItem("357012", "47-50", "222222", "Em fila"),
            DigitalizacaoItem("5832523", "2-3", "3333333", "Concluído")
        )

        val customAdapter = AdminDigitalizationOrdersAdapter(dataset)

        val recyclerView: RecyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = customAdapter
    }
}