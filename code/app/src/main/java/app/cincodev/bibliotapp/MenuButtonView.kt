package app.cincodev.bibliotapp

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView

class MenuButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val iconImage: ImageView
    private val labelText: TextView
    private val cardView: CardView

    init {
        LayoutInflater.from(context).inflate(R.layout.component_menu_button, this, true)
        iconImage = findViewById(R.id.icon_image)
        labelText = findViewById(R.id.label_text)
        cardView = findViewById(R.id.card_view)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.MenuButtonView)
            val icon = typedArray.getDrawable(R.styleable.MenuButtonView_iconSrc)
            val text = typedArray.getString(R.styleable.MenuButtonView_labelText)
            val tint = typedArray.getColor(R.styleable.MenuButtonView_iconTint, Color.BLACK)
            val bgColor = typedArray.getColor(R.styleable.MenuButtonView_cardBackground, Color.WHITE)

            icon?.let { iconImage.setImageDrawable(it) }
            iconImage.setColorFilter(tint)
            labelText.text = text
            cardView.setCardBackgroundColor(bgColor)

            typedArray.recycle()
        }
    }

    fun setLabel(text: String) {
        labelText.text = text
    }

    fun setIcon(@DrawableRes iconRes: Int) {
        iconImage.setImageResource(iconRes)
    }

    fun setIconTint(@ColorInt color: Int) {
        iconImage.setColorFilter(color)
    }

    fun setCardBackground(@ColorInt color: Int) {
        cardView.setCardBackgroundColor(color)
    }
}
