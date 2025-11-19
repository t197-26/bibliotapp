package app.cincodev.bibliotapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminDigitalizationOrders : AppCompatActivity() {
    lateinit var arrowBackButtonView: ImageButton
    lateinit var homeButton: ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var fb: FirebaseFirestore

    lateinit var dataset: MutableList<DigitalizacaoItem>
    lateinit var adapter: AdminDigitalizationOrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_digitalization_orders)

        fb = Firebase.firestore

        arrowBackButtonView = findViewById(R.id.AdminDigitalizationOrdersArrowBack)
        homeButton = findViewById(R.id.AdminDigitalizationOrdersBottomBarHomeImageView)
        recyclerView = findViewById(R.id.recycler)

        dataset = mutableListOf()
        adapter = AdminDigitalizationOrdersAdapter(dataset)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        arrowBackButtonView.setOnClickListener {
            startActivity(Intent(this, AdminHome::class.java))
        }
        homeButton.setOnClickListener {
            startActivity(Intent(this, AdminHome::class.java))
        }
        loadDigitalizationOrders()
    }

    private fun loadDigitalizationOrders() {
        fb.collection("digitalizacoes")
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {

                    dataset.clear()

                    for (doc in snapshot) {
                        dataset.add(
                            DigitalizacaoItem(
                                doc.id,
                                doc.getString("material_id") ?: "",
                                doc.getString("requisitante") ?: "",
                                doc.getString("paginas") ?: "",
                                doc.getString("registro") ?: "",
                                doc.getString("status") ?: ""
                            )
                        )
                    }
                    adapter.notifyDataSetChanged()
                }
            }

    }
}
