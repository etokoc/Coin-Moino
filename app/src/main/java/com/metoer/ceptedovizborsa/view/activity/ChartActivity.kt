package com.metoer.ceptedovizborsa.view.activity

import android.app.ActionBar
import android.app.Dialog
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Paint
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.tabs.TabLayout
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.db.CoinBuyItem
import com.metoer.ceptedovizborsa.data.response.coin.candles.BinanceRoot
import com.metoer.ceptedovizborsa.data.response.coin.markets.MarketData
import com.metoer.ceptedovizborsa.databinding.ActivityChartBinding
import com.metoer.ceptedovizborsa.databinding.CustomSpinnerLayoutBinding
import com.metoer.ceptedovizborsa.util.*
import com.metoer.ceptedovizborsa.util.EditTextUtil.editTextFilter
import com.metoer.ceptedovizborsa.viewmodel.activity.ChartViewModel
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinPortfolioViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_chart.*
import okhttp3.WebSocket
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class ChartActivity : BaseActivity(), AdapterView.OnItemClickListener {

    private var _binding: ActivityChartBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var dataMarket: MarketData

    private val viewModel: ChartViewModel by viewModels()
    private val coinPortfolioViewModel: CoinPortfolioViewModel by viewModels()
    private var moreTimeList = arrayListOf<String>()
    lateinit var binanceSocket: WebSocket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        loadLocale()
        super.onResume()
        supportLocale()
        dataMarket = intent.getSerializableExtra("send") as MarketData
        binding.apply {
            edittextTotal.filters = editTextFilter()
            edittextUnit.filters = editTextFilter()
            edittextUnit.hint = getString(R.string.miktar, dataMarket.baseSymbol)
            edittextTotal.hint = getString(R.string.toplam, dataMarket.quoteSymbol)
        }
        moreTimeList = arrayListOf(
            getString(R.string.coin_time_1_minute),
            getString(R.string.coin_time_5_minute),
            getString(
                R.string.coin_time_30_minute,
            ),
            getString(R.string.coin_time_2_hour),
            getString(R.string.coin_time_8_hour),
            getString(R.string.coin_time_12_hour),
            getString(R.string.coin_time_1_week),
            getString(R.string.coin_time_1_month),
        )
        initTabLayout()
        initListeners()
        calculateCoin()
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)

        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(
            configuration,
            baseContext.resources.displayMetrics
        )
        val sharedPref = SharedPrefencesUtil(applicationContext)
        sharedPref.addLocal("My_Lang", language)
    }

    private fun loadLocaleString(): String {
        val prefs = SharedPrefencesUtil(applicationContext)
        val language = prefs.getLocal("My_Lang", String)
        return language.toString()
    }

    private fun loadLocale() {
        setLocale(loadLocaleString())
    }

    var changeValueController: Double = 0.0
    private fun initListeners() {
        binding.apply {
            fillCandleData()
            dataMarket.baseSymbol?.let { base ->
                dataMarket.quoteSymbol?.let { quote ->
                    viewModel.getChartFromBinanceData(base.uppercase(), quote.uppercase(), interval)
//                    viewModel.getBinanceChartWebSocket(
//                        baseSymbol = base.lowercase(),
//                        quoteSymbol = quote.lowercase(),
//                        param = interval
//                    )
                    viewModel.getTickerFromBinanceData(base.uppercase(), quote.uppercase(), "1d")
                        .observe(this@ChartActivity) { tickerData ->
                            val percent = tickerData?.priceChangePercent?.toDouble()
                            if (percent != null) {
                                if (percent > 0) {
                                    textViewPercent.textColors(
                                        R.color.coinValueRise
                                    )
                                } else if (percent < 0) {
                                    textViewPercent.textColors(
                                        R.color.coinValueDrop
                                    )
                                } else {
                                    textViewPercent.textColors(
                                        R.color.appGray
                                    )
                                }
                            }
                            textViewPercent.text = getString(
                                R.string.coin_exchange_parcent_text,
                                tickerData?.priceChangePercent?.let {
                                    NumberDecimalFormat.numberDecimalFormat(
                                        it, "0.##"
                                    )
                                },
                                "%"
                            )
                            coinValueTextView.text =
                                tickerData?.lastPrice?.let {
                                    NumberDecimalFormat.numberDecimalFormat(
                                        it,
                                        "###,###,###,###.########"
                                    )
                                }
                            textViewVolume.text = getString(
                                R.string.volume_base_text,
                                base.uppercase(),
                                tickerData?.volume?.toDouble()?.let {
                                    MoneyCalculateUtil.volumeShortConverter(
                                        it,
                                        this@ChartActivity
                                    )
                                }
                            )
                            textViewVolumeQuote.text = getString(
                                R.string.volume_base_text,
                                quote.uppercase(),
                                tickerData?.quoteVolume?.toDouble()?.let {
                                    MoneyCalculateUtil.volumeShortConverter(
                                        it,
                                        this@ChartActivity
                                    )
                                }
                            )
                            textViewHighest.text = getString(
                                R.string.high_price_text,
                                tickerData?.highPrice?.let {
                                    NumberDecimalFormat.numberDecimalFormat(
                                        it,
                                        "###,###,###,###.########"
                                    )
                                }
                            )
                            textViewLowestPrice.text = getString(
                                R.string.low_price_text,
                                tickerData?.lowPrice?.let {
                                    NumberDecimalFormat.numberDecimalFormat(
                                        it,
                                        "###,###,###,###.########"
                                    )
                                }
                            )
                        }
                    binanceSocket =
                        viewModel.getBinanceTickerWebSocket(baseSymbol = base, quoteSymbol = quote)
                    viewModel.getBinanceSocketTickerListener()
                        ?.observe(this@ChartActivity) { tickerData ->
                            val percent = tickerData?.priceChangePercent?.toDouble()
                            if (changeValueController < (tickerData?.lastPrice?.toDouble()
                                    ?: 0.0)
                            ) {
                                coinValueTextView.textColors(R.color.coinValueRise)
                            } else if (changeValueController > (tickerData?.lastPrice?.toDouble()
                                    ?: 0.0)
                            ) {
                                coinValueTextView.textColors(R.color.coinValueDrop)
                            } else {
                                coinValueTextView.textColors(R.color.appGray)
                            }

                            changeValueController = tickerData?.lastPrice?.toDouble() ?: 0.0
                            if (percent != null) {
                                if (percent > 0) {
                                    textViewPercent.textColors(R.color.coinValueRise)
                                } else if (percent < 0) {
                                    textViewPercent.textColors(R.color.coinValueDrop)
                                } else {
                                    textViewPercent.textColors(R.color.appGray)
                                }
                            }
                            textViewPercent.text = getString(
                                R.string.coin_exchange_parcent_text,
                                tickerData?.priceChangePercent?.let {
                                    NumberDecimalFormat.numberDecimalFormat(
                                        it,
                                        "0.##"
                                    )
                                },
                                "%"
                            )
                            coinValueTextView.text =
                                tickerData?.lastPrice?.let {
                                    NumberDecimalFormat.numberDecimalFormat(
                                        it,
                                        "###,###,###,###.########"
                                    )
                                }
                            textViewVolume.text = getString(
                                R.string.volume_base_text,
                                base.uppercase(),
                                tickerData?.baseVolume?.toDouble()?.let {
                                    MoneyCalculateUtil.volumeShortConverter(
                                        it,
                                        this@ChartActivity
                                    )
                                }
                            )
                            textViewVolumeQuote.text = getString(
                                R.string.volume_base_text,
                                quote.uppercase(),
                                tickerData?.quoteVolume?.toDouble()?.let {
                                    MoneyCalculateUtil.volumeShortConverter(
                                        it,
                                        this@ChartActivity
                                    )
                                }
                            )
                            textViewHighest.text = getString(
                                R.string.high_price_text,
                                tickerData?.highPrice?.let {
                                    NumberDecimalFormat.numberDecimalFormat(
                                        it,
                                        "###,###,###,###.########"
                                    )
                                }
                            )
                            textViewLowestPrice.text = getString(
                                R.string.low_price_text,
                                tickerData?.lowPrice?.let {
                                    NumberDecimalFormat.numberDecimalFormat(
                                        it,
                                        "###,###,###,###.########"
                                    )
                                }
                            )
                        }
                }

            }
        }
        //Coin Buy Click
        btnBuy.setOnClickListener {
            if (!edittext_unit.text.isNullOrEmpty()) {
                val coinUnit: Double
                coinUnit = MoneyCalculateUtil.doubleConverter(edittext_unit.text.toString())
                dataMarket.apply {
                    val coinBuyItem = CoinBuyItem(
                        baseSymbol,
                        quoteSymbol,
                        baseId,
                        coinUnit,
                        priceQuote?.toDouble(),
                        System.currentTimeMillis()
                    )
                    coinPortfolioViewModel.upsertCoinBuyItem(coinBuyItem)
                    CustomDialogUtil(
                        this@ChartActivity,
                        container = binding.root,
                        isSuccessDialog = true,
                        forForcedUpdate = false
                    ).showDialog()
                }
            } else {
                showToastShort(getString(R.string.check_inputs))
            }
        }

        //Önceki ekrana dönme
        char_back_button.setOnClickListener {
            onBackPressed()
        }
    }

    private fun fillCandleData() {
        binding.apply {
            viewModel.chartBinanceLiveData.observe(this@ChartActivity) {
                it?.let { it1 -> setCandelStickChart(it1) }
            }
            viewModel.getBinanceSocketChartListener()?.observe(this@ChartActivity) {
                if (it != null) {
                    setCandelStickChart(it)
                }
            }
        }
    }

    var _dialog: Dialog? = null
    var bindingSearchDialog: CustomSpinnerLayoutBinding? = null
    private fun searchDialog(textView: TabLayout, currencyList: ArrayList<String>) {
        val dialog = Dialog(this)
        _dialog = dialog
        bindingSearchDialog =
            CustomSpinnerLayoutBinding.inflate(layoutInflater/*, binding.root, false*/)
        dialog.setContentView(bindingSearchDialog!!.root)
        val layoutParams = WindowManager.LayoutParams()
        val dialogVindow = dialog.window
        layoutParams.gravity = Gravity.TOP or Gravity.END
        layoutParams.x = (textView.x + 6).toInt()
        layoutParams.y = (textView.height + 320)
        dialogVindow?.attributes = layoutParams
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, currencyList)
        bindingSearchDialog.apply {
            this?.listview?.adapter = adapter
        }
        dialogVindow?.setLayout(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
        initSpinner()
    }

    private fun initSpinner() {
        bindingSearchDialog?.listview?.setOnItemClickListener { parent, view, position, id ->
            binding.tabLayout.getTabAt(4)?.text = moreTimeList.get(position)
            spinner?.prompt = moreTimeList.get(position)
            val intervalList = arrayListOf("1m", "5m", "30m", "2h", "8h", "12h", "1w", "1M")
            interval = moreTimeList[position]
            if (dataMarket.baseSymbol != null && dataMarket.quoteSymbol != null) {
                viewModel.getChartFromBinanceData(
                    dataMarket.baseSymbol!!,
                    dataMarket.quoteSymbol!!,
                    intervalList[position]
                )
            }
            bindingSearchDialog?.listview?.onItemClickListener = null
            _dialog?.hide()
        }
    }

    var spinner: Spinner? = null
    var interval = "15m"
    private fun initTabLayout() {
        val tab = binding.tabLayout.newTab()
        binding.tabLayout.addTab(tab.setText(moreTimeList[0]+"+"))
        val layout =
            (binding.tabLayout.getChildAt(0) as LinearLayout).getChildAt(4) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 1.5f
        layout.layoutParams = layoutParams
        binding.apply {
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    selectedTab(tab)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }
    }

    private fun selectedTab(tab: TabLayout.Tab?) {
        when (tab?.position) {
            0 -> {
                interval = "15m"
            }
            1 -> {
                interval = "1h"
            }
            2 -> {
                interval = "4h"
            }
            3 -> {
                interval = "1d"
            }
            4 -> {
                interval = "1m"
                searchDialog(binding.tabLayout, moreTimeList)
            }
        }
        if (tab?.position != 4) {
            bindingSearchDialog?.listview?.onItemClickListener = null
        }
        progressBar.show()
        dataMarket.baseSymbol?.let { base ->
            dataMarket.quoteSymbol?.let { quote ->
                viewModel.getChartFromBinanceData(
                    base, quote, interval
                )
//                            viewModel.getBinanceChartWebSocket(
//                                base.lowercase(), quote.lowercase(), interval
//                            )
            }
        }
    }

    private fun setCandelStickChart(binanceRoot: Any) {
        var sayac = 0f
        val candlestickentry = ArrayList<CandleEntry>()
        val areaCount = ArrayList<String>()
        progressBar.hide()
        if (binanceRoot is BinanceRoot) {
            binanceRoot.forEach { candleData ->
                areaCount.add(getDate((candleData[0] as Double).toLong()) ?: "")
                candlestickentry.add(
                    CandleEntry(
                        sayac,
                        candleData.get(2).toString().toFloat(),
                        candleData.get(3).toString().toFloat(),
                        candleData.get(1).toString().toFloat(),
                        candleData.get(4).toString().toFloat()
                    )
                )
                sayac++
            }
        } /*else if (binanceRoot is BinanceWebSocketCandleRoot) {
            binanceRoot.k?.let { binanceResponse ->
                candlestickentry.last().apply {
                    this.high = binanceResponse.highPrice.toString().toFloat()
                    this.low = binanceResponse.lowPrice.toString().toFloat()
                    this.open = binanceResponse.openPrice.toString().toFloat()
                    this.close = binanceResponse.closePrice.toString().toFloat()
                }
            }
        }*/
        val candledataset = CandleDataSet(candlestickentry, "Coin")
        candledataset.apply {
            shadowColor = getColorful(this@ChartActivity, R.color.green)
            shadowWidth = 1f
            setDrawValues(false)
            decreasingColor = getColorful(this@ChartActivity, R.color.coinValueDrop)
            decreasingPaintStyle = Paint.Style.FILL
            increasingColor = getColorful(this@ChartActivity, R.color.coinValueRise)
            increasingPaintStyle = Paint.Style.FILL
        }

        val candledata = CandleData(candledataset)

        coinDataChart.apply {
            this.clear()
            data = candledata
            val xval = this.xAxis
            val typedValue = TypedValue()
            val theme: Resources.Theme = context.theme
            theme.resolveAttribute(
                androidx.constraintlayout.widget.R.attr.textFillColor,
                typedValue,
                true
            )
            @ColorInt val color = typedValue.data
            axisLeft.textColor = color
            this.isAutoScaleMinMaxEnabled = true
            xval.textColor = color
            xval.position = XAxis.XAxisPosition.BOTTOM
            xval.setDrawGridLines(true)
            xval.valueFormatter = IndexAxisValueFormatter(areaCount)
            bacgroundColour(R.color.transparent)
            axisLeft.setDrawAxisLine(false)
            axisRight.isEnabled = false // right line and value remove
            xAxis.setDrawAxisLine(false)  // x line removed
            animateXY(1000, 1000)
            setVisibleXRangeMaximum(65F) //max item range
            moveViewToX(candlestickentry.size.toFloat()) //last item than view
        }
    }

    private fun calculateCoin() {
        binding.apply {
            dataMarket.priceQuote?.let {
                MoneyCalculateUtil.coinConverter(
                    edittextUnit, edittextTotal,
                    it
                )
            }
        }
    }

    private fun getDate(milliSeconds: Long): String? {
        val formatter: DateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onPause() {
        viewModel.clearChartBinanceData()
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.clearChartBinanceData()
        viewModel.clearBinanceSocketChartLiveData()
        viewModel.clearBinanceSocketTickerLiveData()
        viewModel.clearGetTickerFromBinanceLiveData()
        binanceSocket.cancel()
        super.onDestroy()
        coinPortfolioViewModel.compositeDisposable.clear()
    }

}