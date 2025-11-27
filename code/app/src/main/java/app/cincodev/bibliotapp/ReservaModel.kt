package app.cincodev.bibliotapp

import com.google.firebase.Timestamp

data class ReservaModel(
    var id: String = "",
    val spaceName: String = "",
    val startTime: Timestamp? = null,
    val endTime: Timestamp? = null,
    val userId: String = "",
    val matricula: String = ""
) {

    constructor() : this("", "", null, null, "", "")
}