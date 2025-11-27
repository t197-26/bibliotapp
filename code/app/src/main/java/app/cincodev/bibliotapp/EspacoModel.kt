package app.cincodev.bibliotapp

data class EspacoModel(
    var id: String = "",
    val nome: String = "",
    var status: String = "disponivel",
    val capacidade: Int = 0
) {
    // Construtor vazio exigido pelo Firebase
    constructor() : this("", "", "disponivel", 0)
}