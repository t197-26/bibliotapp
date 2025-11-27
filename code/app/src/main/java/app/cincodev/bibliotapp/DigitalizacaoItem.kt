package app.cincodev.bibliotapp
data class DigitalizacaoItem(
    // O id do documento de digitalização na coleção "digitalizacoes" no Firestore
    val id:String,
    // O id do material que foi pedido a digitalização
    val material_id:String,
    val requisitante: String,
    val paginas: String,
    val registro: String,
    var status: String,
    var abertoEm: String? = null,
)