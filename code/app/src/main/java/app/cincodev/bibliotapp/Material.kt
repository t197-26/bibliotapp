package app.cincodev.bibliotapp

data class Material(
    val id: String = "",
    val titulo: String = "",
    val autor: String = "",
    val material: String = "",      // tipo do livro
    val isbn: String = "",
    val idioma: String = "",
    val edicao: String = "",
    val publicacao: String = "",
    val cdu: String = "",
    val capa: String = ""           // imagem em BASE64
)
