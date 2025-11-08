package app.cincodev.bibliotapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserBookDetail : AppCompatActivity() {

    lateinit var arrowBackButtonView: ImageButton

    // Firebase
    lateinit var fb:FirebaseFirestore

    // Campos do detalhamento de material
    lateinit var etBookTitle: TextView
    lateinit var etBookMaterial: TextView
    lateinit var bookIdioma: TextView
    lateinit var bookISBN: TextView
    lateinit var bookAutor: TextView
    lateinit var bookCDU: TextView
    lateinit var bookEdicao: TextView
    lateinit var bookPublicacao: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_book_detail)

        // Instância do Firebase
        fb = Firebase.firestore

        // Campos de detalhamento de material
        etBookTitle = findViewById(R.id.bookTitle)
        etBookMaterial = findViewById(R.id.bookMaterial)
        bookIdioma = findViewById(R.id.bookIdioma)
        bookISBN = findViewById(R.id.bookISBN)
        bookAutor = findViewById(R.id.bookAutor)
        bookCDU = findViewById(R.id.bookCDU)
        bookEdicao = findViewById(R.id.bookEdicao)
        bookPublicacao = findViewById(R.id.bookPublicacao)

        arrowBackButtonView = findViewById(R.id.userBookDetailArrowBack)
        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Chamada dos detalhes do material
        getBookDetails();

        val bookExemplarMenu: ImageView = findViewById(R.id.bookExemplarMenu)


        bookExemplarMenu.setOnClickListener {
            showPopupMenu(bookExemplarMenu)
        }

    }

    // Função para chamada da informações
    private fun getBookDetails() {

        fb.collection("materiais")
            .document("default")
            .get()
            .addOnSuccessListener { result ->

                etBookTitle.setText(result.get("titulo").toString())
                etBookMaterial.setText(result.get("material").toString())
                bookIdioma.setText(result.get("idioma").toString())
                bookISBN.setText(result.get("isbn").toString())
                bookAutor.setText(result.get("autor").toString())
                bookCDU.setText(result.get("cdu").toString())
                bookEdicao.setText(result.get("edicao").toString())
                bookPublicacao.setText(result.get("publicacao").toString())

            }
    }


    private fun showPopupMenu(anchorView: ImageView) {
        // Inflate custom popup layout
        val inflater = LayoutInflater.from(this)
        val popupView = inflater.inflate(R.layout.custom_menu_exemplar, null)

        // Create PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true // dismiss on outside touch
        )

        popupWindow.elevation = 16f // adds shadow

        // Setup button listeners inside popup
        popupView.findViewById<Button>(R.id.btnReservar).setOnClickListener {
            confirmarReserva(context = this)
            popupWindow.dismiss()
        }

        popupView.findViewById<Button>(R.id.btnDigitalizar).setOnClickListener {
            popupWindow.dismiss()
            startActivity(Intent(this, UserDigitalizationOrder::class.java))
        }

        popupView.findViewById<Button>(R.id.btnVerNoMapa).setOnClickListener {
            popupWindow.dismiss()
            startActivity(Intent(this, UserSpaceMap::class.java))
        }

        // Show the popup anchored to the menu button
        // You can adjust offsets (x, y) as needed
        popupWindow.showAsDropDown(anchorView, -150, -350)
    }

    private fun confirmarReserva(context: android.content.Context) {
        val dialogView = LayoutInflater.from(context)
            .inflate(R.layout.dialog_confirmation_reservation_book, null)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val btnCancelar = dialogView.findViewById<Button>(R.id.btnCancelar)
        val btnConfirmar = dialogView.findViewById<Button>(R.id.btnConfirmar)

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirmar.setOnClickListener {
            startActivity(Intent(this, UserProcessingBooking::class.java))
            dialog.dismiss()
        }

        dialog.show()
    }
}
