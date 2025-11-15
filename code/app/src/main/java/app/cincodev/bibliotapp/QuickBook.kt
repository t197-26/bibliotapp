package app.cincodev.bibliotapp

import android.graphics.Bitmap

data class QuickBook(
    val id:String,
    val capa: Bitmap?,
    val titulo:String,
    val devolucao:String
)