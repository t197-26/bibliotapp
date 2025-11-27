package app.cincodev.bibliotapp

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.io.encoding.Base64

class DigitalizacoesAdapter(private val context: Context, private val dataSet: MutableList<DigitalizationRequestWithMaterial>) :
    RecyclerView.Adapter<DigitalizacoesAdapter.ViewHolder>() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

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

        viewHolder.titulo.text = item.material.nome
        viewHolder.dataPedido.text = "Pedido em: ${item.request.abertoEm ?: ""}"
        viewHolder.paginas.text = "Páginas: ${item.request.paginas}"
        viewHolder.status.text = item.request.status

        val capaBase64 = item.material.imagemUrl
        if (capaBase64.isNotEmpty()) {
            val bitmap = decodeBase64ToBitmap(capaBase64)
            if (bitmap != null) {
                viewHolder.imgCapa.setImageBitmap(bitmap)
            }
        }


        when (item.request.status) {
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

        viewHolder.acao.setOnClickListener {
            item.onClickCancelButton()
        }
    }

    private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun getItemCount() = dataSet.size
}
