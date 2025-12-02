package app.cincodev.bibliotapp

data class DigitalizationRequestWithMaterial(
    val request: DigitalizacaoItem,
    val material: Material,
    val onClickCancelButton: () -> Unit
)