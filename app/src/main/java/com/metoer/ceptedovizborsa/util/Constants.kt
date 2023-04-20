package com.metoer.ceptedovizborsa.util

object Constants {
    const val WEBSOCKET_ID: Int = 1453
    const val BINANCE_CHART_LIMIT = 1000
    const val BINANCE_WEB_SOCKET_BASE_URL = "wss://stream.binance.com:9443/ws/"
    const val BINANCE_WEB_SOCKET_COIN_BASE_URL = "wss://ws.coincap.io/trades/binance"
    const val CURRENCY_BASE_URL = "https://www.tcmb.gov.tr/kurlar/"
    const val RATES_BASE_URL = "https://api.coincap.io/v2/rates"
    const val COINCAP_BASE_URL = "https://api.coincap.io/v2/"
    const val BINANCE_CHART_BASE_URL = "https://api.binance.com/"
    const val COIN_MARKET_URL = "https://api.coincap.io/v2/assets"
    const val PAGE_TICKER_URL = "api/v3/ticker/24hr"
    const val HEADER_DATA = "_"
    const val IMAGE_URL = "https://wise.com/public-resources/assets/flags/rectangle/"
    const val IMAGE_URL2 = "https://wise.com/public-resources/assets/flags/rectangle/"
    const val SPINNER1_STATE_KEY = "mySpinner1"
    const val SPINNER2_STATE_KEY = "mySpinner2"
    const val COIN_BUNDLE_KEY_STRING = "coinData"
    const val SHARED_PREFENCES_KEY = "coinmoinoSharedPref"
    const val IS_FIRST_OPEN_APP = "is_first_open_app"
    const val FIREBASE_COLLECTION_NAME = "CoinMoinoStarter"

    const val API_KEY1 = "6b43de33a-b199-4072-b96a-e4a033ad1a35"
    const val API_KEY2 = "822c604b-436a-40d7-920b-9274a4bb8040"
    const val API_KEY3 = "cff18fc1-8a6e-4c99-906e-7eb3138cf36c"
    const val API_KEY4 = "ad334be6-0d70-4df5-90b8-75f6b5f9290a"
    const val API_KEY5 = "d471d0e3-c049-44bf-bdc9-dbd4bd590896"


    const val TODAY_XML = "today.xml"
    const val LIMIT_100 = "?limit=100"
    const val AUTHORIZATION = "Authorization: 9be33ccd-1b0b-4009-ac01-0aed7af968f6"
    const val MARKET_COIN_DATA = "markets?&limit=200&exchangeId=binance"
    const val AUTHORIZATIN_HEADER = "Authorization"
    const val QUERY_QUOTE_SYMBOL = "quoteSymbol"
    const val CANDLES_COIN_DATA_GET_ENDPOINT = "candles?exchange=binance"
    const val INTERVAL_QUERY = "interval"
    const val BASE_ID_QUERY = "baseId"
    const val QUOTE_ID = "quoteId"
    const val LIMIT_QUERY = "limit"
    const val TICKER_ENDPOINT = "api/v3/ticker?"
    const val SYMBOL_QUERY = "symbol"
    const val TICKER_WINDOWSSIZE = "windowSize"
    const val CHART_BINANCE_ENDPOINT = "api/v3/uiKlines?"

    const val MINIMUM_DEPTH_WIDTH = 5

}