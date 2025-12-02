package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.Timestamp
import java.util.Date

class UserProcessingBooking : AppCompatActivity() {

    lateinit var fb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_processing_booking)

        // Instância do Firebase
        fb = Firebase.firestore

        // sp = SharedPreferences com BOOK_ID e EXEMPLAR_ID
        val sp = getSharedPreferences("arquivo", MODE_PRIVATE)
        // another_prefs = SharedPreferences com matricula
        val another_prefs = getSharedPreferences("bibliotapp_shared_preferences", MODE_PRIVATE)

        // ID do material cujo exemplar foi reservado
        val bookId = sp.getString("BOOK_ID", "") ?: ""
        // ID do exemplar reservado
        val exemplarId = sp.getString("EXEMPLAR_ID", "") ?: ""

        // Recuperar matrícula do usuário que solicitou a reserva
        val matricula = another_prefs.getString("matricula", null)

        if (matricula == null) {
            Log.i("UserBooking", "Erro: matrícula não encontrada")
            startActivity(Intent(this, UserFailedBooking::class.java))
            return
        }

        reservar(bookId, exemplarId, matricula)
    }

    private fun reservar(bookId: String, exemplarId: String, matricula: String) {

        // Referência do exemplar reservado
        val exemplarRef = fb.collection("materiais")
            .document(bookId)
            .collection("exemplares")
            .document(exemplarId)

        // Atualizar status do exemplar reservado
        exemplarRef.update("status", "Emprestado")
            .addOnSuccessListener {

                val agora = Timestamp.now()

                val quinzeDiasMs = 15L * 24L * 60L * 60L * 1000L
                val devolverEm = Timestamp(Date(agora.toDate().time + quinzeDiasMs))

                // Registrar o empréstimo
                val emprestimo = hashMapOf(
                    "devolver_em" to devolverEm,
                    "emprestado_em" to agora,
                    "exemplares_id" to exemplarId,
                    "materiais_id" to bookId,
                    "users_id" to matricula
                )

                fb.collection("emprestimos")
                    .add(emprestimo)
                    .addOnSuccessListener {
                        startActivity(Intent(this, UserSuccesfulBooking::class.java))
                    }
                    .addOnFailureListener { e ->
                        Log.i("UserBooking", "Erro ao criar empréstimo", e)
                        startActivity(Intent(this, UserFailedBooking::class.java))
                    }

            }
            .addOnFailureListener { e ->
                Log.i("UserBooking", "Erro ao atualizar exemplar", e)
                startActivity(Intent(this, UserFailedBooking::class.java))
            }
    }
}
