package ui.colorPicker

import com.outsidesource.oskitcompose.color.HsvColor
import com.outsidesource.oskitkmp.interactor.Interactor

data class ColorPickerViewState(
    val color: HsvColor = HsvColor.Blue,
    val colors: Map<String, HsvColor> = mapOf(
        "1" to HsvColor.Red,
        "2" to HsvColor.Green,
        "3" to HsvColor.Blue,
    )
)

class ColorPickerViewInteractor : Interactor<ColorPickerViewState>(
    initialState = ColorPickerViewState()
) {

    fun colorChanged(color: HsvColor) {
        update { state -> state.copy(color = color) }
    }

    fun multiColorChanged(key: String, color: HsvColor) {
        update { state -> state.copy(colors = state.colors.toMutableMap().apply { this[key] = color }) }
    }
}