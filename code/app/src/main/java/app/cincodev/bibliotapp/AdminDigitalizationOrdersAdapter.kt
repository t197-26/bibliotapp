package app.cincodev.bibliotapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminDigitalizationOrdersAdapter(private val dataSet: MutableList<DigitalizacaoItem>) :
    RecyclerView.Adapter<AdminDigitalizationOrdersAdapter.ViewHolder>() {

    lateinit var fb: FirebaseFirestore

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val requisitante: TextView = view.findViewById(R.id.dataRequisitante)
        val paginas: TextView = view.findViewById(R.id.dataPaginas)
        val registro: TextView = view.findViewById(R.id.dataRegistro)
        val status : TextView = view.findViewById(R.id.dataStatus)

        val menuBotao: ImageButton = view.findViewById(R.id.menuItemDigitalizacao)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_digitalization_order, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        fb = Firebase.firestore

        viewHolder.requisitante.text = dataSet[position].requisitante
        viewHolder.paginas.text = dataSet[position].paginas
        viewHolder.registro.text = dataSet[position].registro
        viewHolder.status.text = dataSet[position].status
        viewHolder.menuBotao.setOnClickListener { anchorView ->
            val context = anchorView.context
            val inflater = LayoutInflater.from(context)
            val popupView = inflater.inflate(R.layout.custom_menu_digitalizacao, null)

            val popupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )

            popupWindow.elevation = 12f

            popupView.findViewById<Button>(R.id.btnFinalizar).setOnClickListener {
                updateDigitalizacao(dataSet[position].id, "Enviado")
                notifyItemChanged(position)
                popupWindow.dismiss()
            }

            popupView.findViewById<Button>(R.id.btnRecusar).setOnClickListener {
                updateDigitalizacao(dataSet[position].id, "Recusado")
                notifyItemChanged(position)
                popupWindow.dismiss()
            }

            popupView.findViewById<Button>(R.id.btnIniciar).setOnClickListener {
                updateDigitalizacao(dataSet[position].id, "Digitalizando")
                notifyItemChanged(position)
                popupWindow.dismiss()
            }

            popupView.findViewById<Button>(R.id.btnMapa).setOnClickListener {
                val intent = Intent(context, UserSpaceMap::class.java)
                Toast.makeText(context, "Mapa", Toast.LENGTH_SHORT).show()
                context.startActivity(intent)
                popupWindow.dismiss()
            }

            popupWindow.showAsDropDown(anchorView, -85, 0)
        }
    }

    override fun getItemCount() = dataSet.size

    private fun updateDigitalizacao(id:String, novoStatus:String) {
        fb.collection("digitalizacoes")
            .document(id)
            .update(mapOf(
                "status" to novoStatus
            ))
    }
}