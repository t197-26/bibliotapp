package app.cincodev.bibliotapp

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class UserDigitalizations : AppCompatActivity() {
    lateinit var arrowBackButtonView: ImageButton
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val digitalizationOrders: MutableList<DigitalizacaoItem> = mutableListOf()

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

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerDigitalizacoes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DigitalizacoesAdapter(this, digitalizationOrders)

        getSharedPreferences("bibliotapp_shared_preferences", MODE_PRIVATE).let {
            val matricula = it.getString("matricula", "")
            if (matricula == null) return

            db.collection("digitalizacoes")
                .whereEqualTo("requisitante", matricula)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        digitalizationOrders.add(
                            DigitalizacaoItem(
                                document.id,
                                document["material_id"] as String,
                                document["requisitante"] as String,
                                document["paginas"] as String,
                                document["registro"] as String,
                                document["status"] as String,
                            )
                        )
                    }

                    recyclerView.adapter?.notifyItemRangeChanged(0, documents.size())
                }
                .addOnFailureListener { exception ->
                    Log.w("Firestore", "Error getting documents: ", exception)
                }
        }

        super.onStart()
    }
}