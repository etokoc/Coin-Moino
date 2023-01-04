package com.metoer.ceptedovizborsa.util

class CreateApiKeyUtil {
    companion object {
        private var startIndex = 0
        private val keyList =
            arrayListOf(Constants.API_KEY1,Constants.API_KEY2, Constants.API_KEY3, Constants.API_KEY4,Constants.API_KEY5)

        fun getKey(): String {
            ++startIndex
            if (startIndex >= keyList.size)
                startIndex = 0
            return keyList[startIndex]
        }
    }
}