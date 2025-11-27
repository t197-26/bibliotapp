package app.cincodev.bibliotapp

import com.google.firebase.Timestamp

data class Loan(
    var id: String = "",
    val bookTitle: String = "",      // Título do Livro
    val userName: String = "",       // Quem pegou emprestado
    val loanDate: Timestamp? = null, // Data do empréstimo
    val returnDate: Timestamp? = null, // Data de devolução prevista
    val status: String = "ativo"
) {
    // Construtor vazio para o Firebase
    constructor() : this("", "", "", null, null, "")
}