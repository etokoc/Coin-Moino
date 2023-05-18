package com.metoer.ceptedovizborsa.util

import android.content.Context
import com.metoer.ceptedovizborsa.R

object GlobalThemeUtil {
    private var theme = GlobalThemeEnum.DEFAULT_THEME
    fun getTheme(context: Context): Pair<Int, Int> {
        val themeId: Int = SharedPrefencesUtil(context).getLocal("app_saved_theme", Int) as Int
        return when (themeId) {
            GlobalThemeEnum.DEFAULT_THEME.id -> {
                theme = GlobalThemeEnum.DEFAULT_THEME
                Pair<Int, Int>(R.style.DefaultRiseColorsTheme, R.style.DefaultDropColorsTheme)
            }

            GlobalThemeEnum.CLASSIC_THEME.id -> {
                theme = GlobalThemeEnum.CLASSIC_THEME
                Pair<Int, Int>(R.style.ClassicRiseColorsTheme, R.style.ClassicDropColorsTheme)
            }

            GlobalThemeEnum.COLOR_BLIND_THEME.id -> {
                theme = GlobalThemeEnum.COLOR_BLIND_THEME
                Pair<Int, Int>(R.style.ColorBlindRiseColorsTheme, R.style.ColorBlindDropColorsTheme)
            }

            else -> {
                Pair(0, 0)
            }
        }
    }

    fun getThemeColor(context: Context, colorIsDrop: Boolean): Int {
        var colorId: Int = R.color.black
        when (theme) {
            GlobalThemeEnum.DEFAULT_THEME -> {
                if (colorIsDrop) {
                    colorId = R.color.coinValueDrop
                } else {
                    colorId = R.color.coinValueRise
                }
            }

            GlobalThemeEnum.CLASSIC_THEME -> {
                if (colorIsDrop) {
                    colorId = R.color.classicDropColor
                } else {
                    colorId = R.color.classicRiseColor
                }
            }

            GlobalThemeEnum.COLOR_BLIND_THEME -> {
                if (colorIsDrop) {
                    colorId = R.color.colorBlindDropColor
                } else {
                    colorId = R.color.colorBlindRiseColor
                }
            }
        }
        return colorId
    }

    fun changeTheme(context: Context, themeId: GlobalThemeEnum) {
        theme = themeId
        SharedPrefencesUtil(context).addLocal("app_saved_theme", theme.id)
    }
}