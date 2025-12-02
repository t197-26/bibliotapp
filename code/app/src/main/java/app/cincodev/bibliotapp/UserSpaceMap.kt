package app.cincodev.bibliotapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class UserSpaceMap : AppCompatActivity() {

    lateinit var arrowBackButtonView: ImageButton

    private val nomesEstantes = listOf(
        "Literatura Brasileira",
        "Literatura Estrangeira",
        "Tecnologia e Informática",
        "História Geral",
        "História do Brasil",
        "Geografia e Geopolítica",
        "Biologia e Ciências Naturais",
        "Matemática e Lógica",
        "Física e Química",
        "Filosofia",
        "Psicologia",
        "Artes e Música",
        "Saúde e Medicina",
        "Direito e Legislação",
        "Administração e Economia"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_space_map)


        arrowBackButtonView = findViewById(R.id.SpaceMapArrowBack)
        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val totalEstantes = 15

        for (i in 1..totalEstantes) {
            val estanteId = resources.getIdentifier("estante$i", "id", packageName)
            val botaoEstante = findViewById<Button>(estanteId)

            val nomeEstante = nomesEstantes[i - 1]
            botaoEstante.text = nomeEstante

            botaoEstante?.setOnClickListener {
                animarClique(botaoEstante)
                mostrarPopupEstante(i)
            }
        }

    }

    private fun mostrarPopupEstante(numeroEstante: Int) {
        val nome = nomesEstantes[numeroEstante - 1]

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Estante selecionada")
        builder.setMessage("Você clicou na estante: $nome")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
    private fun animarClique(botao: Button) {
        val corOriginal = botao.background
        botao.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))


        Handler(Looper.getMainLooper()).postDelayed({
            botao.background = corOriginal
        }, 150)
    }
}
