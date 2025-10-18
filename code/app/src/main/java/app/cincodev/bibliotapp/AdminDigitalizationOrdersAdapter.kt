package app.cincodev.bibliotapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class AdminDigitalizationOrdersAdapter(private val dataSet: Array<DigitalizacaoItem>) :
    RecyclerView.Adapter<AdminDigitalizationOrdersAdapter.ViewHolder>() {

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

            // Optional: add elevation for shadow
            popupWindow.elevation = 12f

            // Handle button clicks
            popupView.findViewById<Button>(R.id.btnFinalizar).setOnClickListener {
                Toast.makeText(context, "Finalizar", Toast.LENGTH_SHORT).show()
                popupWindow.dismiss()
            }

            popupView.findViewById<Button>(R.id.btnRecusar).setOnClickListener {
                Toast.makeText(context, "Recusar", Toast.LENGTH_SHORT).show()
                popupWindow.dismiss()
            }

            popupView.findViewById<Button>(R.id.btnIniciar).setOnClickListener {
                dataSet[position].status = "Em andamento"
                notifyItemChanged(position)
                Toast.makeText(context, "Iniciar", Toast.LENGTH_SHORT).show()
                popupWindow.dismiss()
            }

            popupView.findViewById<Button>(R.id.btnMapa).setOnClickListener {
                Toast.makeText(context, "Mapa", Toast.LENGTH_SHORT).show()
                popupWindow.dismiss()
            }

            // Show the popup anchored below the button
            popupWindow.showAsDropDown(anchorView, -85, 0)
        }


    }

    override fun getItemCount() = dataSet.size

}