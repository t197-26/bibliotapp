package app.cincodev.bibliotapp

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserDigitalizations : AppCompatActivity() {
    lateinit var arrowBackButtonView: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_digitalizations)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        arrowBackButtonView = findViewById(R.id.LoginArrowBack)
        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val lista = arrayOf(
            DigitalizacaoItem(
                id = "92hfofofdf",
                material_id="default",
                registro = "A Revolução Informacional [Impresso]",
                paginas = "30-40",
                status = "Digitalizando",
                requisitante = "Teste"
            ),
            DigitalizacaoItem(
                id = "s9h0boahslf",
                material_id="default",
                registro = "A Revolução Informacional [Impresso]",
                paginas = "30-40",
                status = "Digitalizando",
                requisitante = "Teste"
            ),
            DigitalizacaoItem(
                id = "ji2dbw0asd",
                material_id="default",
                registro = "A Revolução Informacional [Impresso]",
                paginas = "30-40",
                status = "Digitalizando",
                requisitante = "Teste"
            ),
            DigitalizacaoItem(
                id = "09ohxb98dh",
                material_id="default",
                registro = "A Revolução Informacional [Impresso]",
                paginas = "30-40",
                status = "Digitalizando",
                requisitante = "Teste"
            ),
        )

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerDigitalizacoes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DigitalizacoesAdapter(this, lista)

        super.onStart()
    }
}