package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserProcessingBooking : AppCompatActivity() {

    lateinit var fb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_processing_booking)

        // Instância do Firebase
        fb = Firebase.firestore

        val x = getSharedPreferences("arquivo", MODE_PRIVATE)
        val bookId = x.getString("BOOK_ID", "") ?: ""
        val exemplarId = x.getString("EXEMPLAR_ID", "") ?: ""

        updateDados(bookId, exemplarId)

    }

    private fun updateDados(bookId: String, exemplarId: String) {

        fb.collection("materiais")
            .document(bookId)
            .collection("exemplares")
            .document(exemplarId)
            .update("status", "Emprestado")
            .addOnSuccessListener {
                startActivity(Intent(this, UserSuccesfulBooking::class.java))
            }
            .addOnFailureListener {
                startActivity(Intent(this, UserFailedBooking::class.java))
            }
    }


}
