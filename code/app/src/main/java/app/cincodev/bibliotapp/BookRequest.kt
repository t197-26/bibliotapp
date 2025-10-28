package app.cincodev.bibliotapp

data class BookRequest(
    val id: String,
    val bookTitle: String,
    val bookCoverUrl: String,
    val requestDate: String,
    val pages: String,
    val status: RequestStatus
)