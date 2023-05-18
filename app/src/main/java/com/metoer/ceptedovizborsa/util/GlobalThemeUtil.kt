package com.metoer.ceptedovizborsa.util

import android.content.Context
import com.metoer.ceptedovizborsa.R

object GlobalThemeUtil {
    private var theme = GlobalThemeEnum.DEFAULT_THEME
    private var themeDirection: Boolean = false
    fun getTheme(context: Context): Pair<Int, Int> {
        val themeId: Int = SharedPrefencesUtil(context).getLocal("app_saved_theme", Int) as Int
        themeDirection =
            SharedPrefencesUtil(context).getLocal("app_saved_theme_direction", Boolean) as Boolean
        return when (themeId) {
            GlobalThemeEnum.DEFAULT_THEME.id -> {
                theme = GlobalThemeEnum.DEFAULT_THEME
                if (!themeDirection) {
                    Pair<Int, Int>(R.style.DefaultRiseColorsTheme, R.style.DefaultDropColorsTheme)
                } else {
                    Pair<Int, Int>(R.style.DefaultDropColorsTheme, R.style.DefaultRiseColorsTheme)
                }
            }

            GlobalThemeEnum.CLASSIC_THEME.id -> {
                theme = GlobalThemeEnum.CLASSIC_THEME
                if (!themeDirection) {
                    Pair<Int, Int>(R.style.ClassicRiseColorsTheme, R.style.ClassicDropColorsTheme)
                } else {
                    Pair<Int, Int>(R.style.ClassicDropColorsTheme, R.style.ClassicRiseColorsTheme)
                }
            }

            GlobalThemeEnum.COLOR_BLIND_THEME.id -> {
                theme = GlobalThemeEnum.COLOR_BLIND_THEME
                if (!themeDirection) {
                    Pair<Int, Int>(
                        R.style.ColorBlindRiseColorsTheme,
                        R.style.ColorBlindDropColorsTheme
                    )
                } else {
                    Pair<Int, Int>(
                        R.style.ColorBlindDropColorsTheme,
                        R.style.ColorBlindRiseColorsTheme
                    )
                }
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
                colorId = if (colorIsDrop) {
                    if (!themeDirection) {
                        R.color.coinValueDrop
                    } else {
                        R.color.coinValueRise
                    }
                } else {
                    if (!themeDirection) {
                        R.color.coinValueRise
                    } else {
                        R.color.coinValueDrop
                    }
                }
            }

            GlobalThemeEnum.CLASSIC_THEME -> {
                colorId = if (colorIsDrop) {
                    if (!themeDirection) {
                        R.color.classicDropColor
                    } else {
                        R.color.classicRiseColor
                    }
                } else {
                    if (!themeDirection) {
                        R.color.classicRiseColor
                    } else {
                        R.color.classicDropColor
                    }
                }
            }

            GlobalThemeEnum.COLOR_BLIND_THEME -> {
                colorId = if (colorIsDrop) {
                    if (!themeDirection) {
                        R.color.colorBlindDropColor
                    } else {
                        R.color.colorBlindRiseColor

                    }
                } else {
                    if (!themeDirection) {
                        R.color.colorBlindRiseColor
                    } else {
                        R.color.colorBlindDropColor

                    }
                }
            }
        }
        return colorId
    }

    fun changeTheme(context: Context, themeId: GlobalThemeEnum) {
        theme = themeId
        SharedPrefencesUtil(context).addLocal("app_saved_theme", theme.id)
    }

    fun setDirection(context: Context, directionRadioIsChecked: Boolean) {
        themeDirection = directionRadioIsChecked
        SharedPrefencesUtil(context).addLocal("app_saved_theme_direction", themeDirection)
    }
}