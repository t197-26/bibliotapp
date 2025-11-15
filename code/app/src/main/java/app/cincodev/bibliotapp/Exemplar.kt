package app.cincodev.bibliotapp

data class Exemplar(
    val id:String,
    val suporte: String,
    val registro: String,
    val disponibilidade: String,
    var status: String
)
