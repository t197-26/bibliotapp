package app.cincodev.bibliotapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DigitalizacoesAdapter(private val dataSet: Array<DigitalizacaoItem>) :
    RecyclerView.Adapter<DigitalizacoesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgCapa: ImageView = view.findViewById(R.id.imgCover)
        val titulo: TextView = view.findViewById(R.id.txtTitulo)
        val dataPedido: TextView = view.findViewById(R.id.txtData)
        val paginas: TextView = view.findViewById(R.id.txtPaginas)
        val status: TextView = view.findViewById(R.id.txtStatus)
        val acao: ImageView = view.findViewById(R.id.imgAction)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_digitalizacao, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = dataSet[position]

        viewHolder.titulo.text = item.registro
        viewHolder.dataPedido.text = "Pedido em: ${item.registro}"
        viewHolder.paginas.text = "Páginas: ${item.paginas}"
        viewHolder.status.text = item.status


        when (item.status) {
            "Em fila" -> {
                viewHolder.status.setBackgroundColor(Color.parseColor("#BBDEFB")) // Light Blue
                viewHolder.status.setTextColor(Color.BLACK)
            }
            "Digitalizando" -> {
                viewHolder.status.setBackgroundColor(Color.parseColor("#FFF59D")) // Yellow
                viewHolder.status.setTextColor(Color.BLACK)
            }
            "Enviada" -> {
                viewHolder.status.setBackgroundColor(Color.parseColor("#A5D6A7")) // Green
                viewHolder.status.setTextColor(Color.BLACK)
            }
            "Recusada" -> {
                viewHolder.status.setBackgroundColor(Color.parseColor("#EF9A9A")) // Red
                viewHolder.status.setTextColor(Color.BLACK)
            }
            else -> {
                viewHolder.status.setBackgroundColor(Color.LTGRAY)
                viewHolder.status.setTextColor(Color.BLACK)
            }
        }
    }

    override fun getItemCount() = dataSet.size
}
