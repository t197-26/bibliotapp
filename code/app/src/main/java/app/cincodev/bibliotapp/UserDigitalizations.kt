package app.cincodev.bibliotapp

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.firebase.firestore.FirebaseFirestore

class UserDigitalizations : AppCompatActivity() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    lateinit var arrowBackButtonView: ImageButton
    lateinit var chipEmFila: Chip
    lateinit var chipCancelado: Chip
    lateinit var chipDigitalizando: Chip
    lateinit var chipRecusado: Chip
    lateinit var recyclerView: RecyclerView
    private val digitalizationRequestsWithMaterial: MutableList<DigitalizationRequestWithMaterial> =
        mutableListOf()

    private var filteredByStatusRequestsWithMaterial: MutableList<DigitalizationRequestWithMaterial> = mutableListOf()
    private var filteredBySearchRequestsWithMaterial: MutableList<DigitalizationRequestWithMaterial> = mutableListOf()

    private var selectedFilter: String? = null

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
        chipEmFila = findViewById(R.id.chipEmFila)
        chipRecusado = findViewById(R.id.chipRecusada)
        chipCancelado = findViewById(R.id.chipCancelado)
        chipDigitalizando = findViewById(R.id.chipDigitalizando)
        arrowBackButtonView.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        chipEmFila.setOnClickListener {
            handleOnStatusChipFilterClick("Em fila")
        }
        chipRecusado.setOnClickListener {
            handleOnStatusChipFilterClick("Recusado")
        }
        chipCancelado.setOnClickListener {
            handleOnStatusChipFilterClick("Cancelado")
        }
        chipDigitalizando.setOnClickListener {
            handleOnStatusChipFilterClick("Digitalizando")
        }

        recyclerView = findViewById(R.id.recyclerDigitalizacoes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DigitalizacoesAdapter(this, digitalizationRequestsWithMaterial)

        getSharedPreferences("bibliotapp_shared_preferences", MODE_PRIVATE).let {
            val matricula = it.getString("matricula", "")
            if (matricula == null) return

            db.collection("digitalizacoes")
                .whereEqualTo("requisitante", matricula)
                .get()
                .addOnSuccessListener { digitalizationRequestsDocuments ->
                    db.collection("materiais")
                        .get()
                        .addOnSuccessListener { materialDocuments ->
                            val digitalizationRequests =
                                digitalizationRequestsDocuments.map { document ->
                                    DigitalizacaoItem(
                                        document.id,
                                        document["material_id"] as String,
                                        document["requisitante"] as String,
                                        document["paginas"] as String,
                                        document["registro"] as String,
                                        document["status"] as String,
                                        document["abertoEm"] as String?,
                                    )
                                }
                            val materials = materialDocuments.map { document ->
                                Material(
                                    document.id,
                                    document["titulo"] as String,
                                    document["material"] as String,
                                    document["isbn"] as String,
                                    document["autor"] as String,
                                    document["capa"] as String,
                                )
                            }

                            for (digitalizationRequest in digitalizationRequests) {
                                for (material in materials) {
                                    if (material.id == digitalizationRequest.material_id) {
                                        digitalizationRequestsWithMaterial.add(
                                            DigitalizationRequestWithMaterial(
                                                digitalizationRequest,
                                                material,
                                                fun() {
                                                    if (digitalizationRequest.status != "Em fila") return

                                                    val builder =
                                                        AlertDialog.Builder(this@UserDigitalizations)
                                                    builder.setTitle("Cancelar Digitalização")
                                                    builder.setMessage("Ao cancelar, o seu pedido sai da fila de digitalização. Um novo pedido para o mesmo livro irá para o final da fila.")

                                                    builder.setPositiveButton("Confirmar") { dialog, _ ->
                                                        db.collection("digitalizacoes")
                                                            .document(digitalizationRequest.id)
                                                            .update(
                                                                mapOf(
                                                                    "status" to "Cancelado"
                                                                )
                                                            ).addOnSuccessListener {
                                                                Toast.makeText(this, "Pedido cancelado com sucesso", Toast.LENGTH_SHORT).show()

                                                                val index = digitalizationRequestsWithMaterial.indexOfFirst { (request, material, onClickCancelButton) -> request.id == digitalizationRequest.id }
                                                                digitalizationRequestsWithMaterial[index].request.status = "Cancelado"

                                                                recyclerView.adapter?.notifyItemRangeChanged(index, 1)
                                                            }.addOnFailureListener {
                                                                Toast.makeText(this, "Ocorreu um erro e não foi possível cancelar seu pedido de digitalização", Toast.LENGTH_SHORT).show()
                                                        }

                                                        dialog.dismiss()
                                                    }

                                                    builder.setNegativeButton("Cancelar") { dialog, _ ->
                                                        dialog.dismiss()
                                                    }

                                                    val dialog = builder.create()
                                                    dialog.show()
                                                })
                                        )
                                    }
                                }
                            }

                            recyclerView.adapter?.notifyItemRangeChanged(
                                0,
                                digitalizationRequestsWithMaterial.size
                            )
                        }
                }
                .addOnFailureListener { exception ->
                    Log.w("Firestore", "Error getting documents: ", exception)
                }
        }

        super.onStart()
    }

    fun handleOnStatusChipFilterClick(holdingChipStatus: String) {
        if (selectedFilter == holdingChipStatus) {
            selectedFilter = null
            filteredByStatusRequestsWithMaterial.clear()
            recyclerView.swapAdapter(DigitalizacoesAdapter(this@UserDigitalizations, digitalizationRequestsWithMaterial), false)
            return
        }

        selectedFilter = holdingChipStatus
        filteredByStatusRequestsWithMaterial = digitalizationRequestsWithMaterial.filter { it -> it.request.status == holdingChipStatus }.toMutableList()
        recyclerView.swapAdapter(DigitalizacoesAdapter(this@UserDigitalizations, filteredByStatusRequestsWithMaterial), false)
    }

    fun handleOnSearchChange(search: String) {
        val dataset = if (selectedFilter != null) filteredByStatusRequestsWithMaterial else digitalizationRequestsWithMaterial
        if (search.isEmpty()) {
            filteredBySearchRequestsWithMaterial.clear()
            recyclerView.swapAdapter(DigitalizacoesAdapter(this@UserDigitalizations, dataset), false)
            return
        }

        val filteredDataset = dataset.filter { it -> it.material.nome.lowercase().contains(search.lowercase()) }
    }
}