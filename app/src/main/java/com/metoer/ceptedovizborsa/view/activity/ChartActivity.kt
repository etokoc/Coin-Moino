package com.metoer.ceptedovizborsa.view.activity

import android.app.ActionBar
import android.app.Dialog
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
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
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.adapter.DepthViewPagerAdapter
import com.metoer.ceptedovizborsa.data.db.CoinBuyItem
import com.metoer.ceptedovizborsa.data.response.coin.candles.BinanceRoot
import com.metoer.ceptedovizborsa.data.response.coin.tickers.CoinPageTickerItem
import com.metoer.ceptedovizborsa.databinding.ActivityChartBinding
import com.metoer.ceptedovizborsa.databinding.CustomSpinnerLayoutBinding
import com.metoer.ceptedovizborsa.util.*
import com.metoer.ceptedovizborsa.util.Constants.WEBSOCKET_CLOSE_NORMAL
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
    private lateinit var dataMarket: CoinPageTickerItem

    private val viewModel: ChartViewModel by viewModels()
    private val coinPortfolioViewModel: CoinPortfolioViewModel by viewModels()
    private var moreTimeList = arrayListOf<String>()
    lateinit var binanceSocket: WebSocket
    private var isLineChart = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChartBinding.inflate(layoutInflater)
        dataMarket = intent.getSerializableExtra("send") as CoinPageTickerItem
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
        setContentView(binding.root)
    }

    private fun baseSymbol(symbol: String): String {
        return when {
            symbol.endsWith("USDT") -> symbol.substring(
                0,
                symbol.length - 4
            )

            else -> dataMarket.symbol!!.substring(0, symbol.length - 3)
        }
    }

    private fun quoteSymbol(symbol: String): String {
        return dataMarket.symbol!!.substring(baseSymbol(symbol).length, dataMarket.symbol!!.length)
    }

    override fun onResume() {
        loadLocale()
        super.onResume()
        supportLocale()
        binding.apply {
            edittextTotal.filters = editTextFilter()
            edittextUnit.filters = editTextFilter()
            dataMarket.symbol?.let { symbol ->
                edittextUnit.hint = getString(R.string.miktar, baseSymbol(symbol))
                edittextTotal.hint = getString(
                    R.string.toplam, quoteSymbol(symbol)
                )
            }
        }
        initListeners()
        calculateCoin()
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)

        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(
            configuration, baseContext.resources.displayMetrics
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
            dataMarket.symbol?.let { symbol ->
                viewModel.getChartFromBinanceData(symbol, interval)
                viewModel.getTickerFromBinanceData(symbol, "1d")
                    .observe(this@ChartActivity) { tickerData ->
                        val percent = tickerData?.priceChangePercent?.toDouble()
                        if (percent != null) {
                            if (percent > 0) {
                                textViewPercent.appliedTheme(
                                    GlobalThemeUtil.getTheme(
                                        applicationContext
                                    ).first
                                )
                            } else if (percent < 0) {
                                textViewPercent.appliedTheme(
                                    GlobalThemeUtil.getTheme(
                                        applicationContext
                                    ).second
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
                        coinValueTextView.patternText(
                            tickerData?.lastPrice,
                            "###,###,###,###.########"
                        )

                        textViewVolume.text = getString(R.string.volume_base_text,
                            baseSymbol(symbol),
                            tickerData?.volume?.toDouble()?.let {
                                MoneyCalculateUtil.volumeShortConverter(
                                    it, this@ChartActivity
                                )
                            })
                        textViewVolumeQuote.text = getString(R.string.volume_base_text,
                            quoteSymbol(symbol),
                            tickerData?.quoteVolume?.toDouble()?.let {
                                MoneyCalculateUtil.volumeShortConverter(
                                    it, this@ChartActivity
                                )
                            })
                        textViewHighest.text =
                            getString(R.string.high_price_text, tickerData?.highPrice?.let {
                                NumberDecimalFormat.numberDecimalFormat(
                                    it, "###,###,###,###.########"
                                )
                            })
                        textViewLowestPrice.text =
                            getString(R.string.low_price_text, tickerData?.lowPrice?.let {
                                NumberDecimalFormat.numberDecimalFormat(
                                    it, "###,###,###,###.########"
                                )
                            })
                    }
                binanceSocket = viewModel.getBinanceTickerWebSocket(symbol)
                viewModel.getBinanceSocketTickerListener()
                    ?.observe(this@ChartActivity) { tickerData ->
                        val percent = tickerData?.priceChangePercent?.toDouble()
                        if (changeValueController < (tickerData?.lastPrice?.toDouble()
                                ?: 0.0)
                        ) {
                            coinValueTextView.appliedTheme(
                                GlobalThemeUtil.getTheme(
                                    applicationContext
                                ).first
                            )
                        } else if (changeValueController > (tickerData?.lastPrice?.toDouble()
                                ?: 0.0)
                        ) {
                            coinValueTextView.appliedTheme(
                                GlobalThemeUtil.getTheme(
                                    applicationContext
                                ).second
                            )
                        } else {
                            coinValueTextView.textColors(R.color.appGray)
                        }

                        changeValueController = tickerData?.lastPrice?.toDouble() ?: 0.0
                        if (percent != null) {
                            if (percent > 0) {
                                textViewPercent.appliedTheme(
                                    GlobalThemeUtil.getTheme(
                                        applicationContext
                                    ).first
                                )
                            } else if (percent < 0) {
                                textViewPercent.appliedTheme(
                                    GlobalThemeUtil.getTheme(
                                        applicationContext
                                    ).second
                                )
                            } else {
                                textViewPercent.textColors(R.color.appGray)
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
                        coinValueTextView.patternText(
                            tickerData?.lastPrice,
                            "###,###,###,###.########"
                        )
                        textViewVolume.text = getString(R.string.volume_base_text,
                            baseSymbol(symbol),
                            tickerData?.baseVolume?.toDouble()?.let {
                                MoneyCalculateUtil.volumeShortConverter(
                                    it, this@ChartActivity
                                )
                            })
                        textViewVolumeQuote.text = getString(R.string.volume_base_text,
                            quoteSymbol(symbol),
                            tickerData?.quoteVolume?.toDouble()?.let {
                                MoneyCalculateUtil.volumeShortConverter(
                                    it, this@ChartActivity
                                )
                            })
                        textViewHighest.text =
                            getString(R.string.high_price_text, tickerData?.highPrice?.let {
                                NumberDecimalFormat.numberDecimalFormat(
                                    it, "###,###,###,###.########"
                                )
                            })
                        textViewLowestPrice.text =
                            getString(R.string.low_price_text, tickerData?.lowPrice?.let {
                                NumberDecimalFormat.numberDecimalFormat(
                                    it, "###,###,###,###.########"
                                )
                            })
                    }
            }
        }
        //Coin Buy Click
        btnBuy.setOnClickListener {
            if (!edittext_unit.text.isNullOrEmpty()) {
                val coinUnit: Double =
                    MoneyCalculateUtil.doubleConverter(edittext_unit.text.toString())
                dataMarket.apply {
                    dataMarket.symbol?.let { symbol ->
                        val coinBuyItem = CoinBuyItem(
                            baseSymbol(symbol),
                            quoteSymbol(symbol),
                            dataMarket.symbol,
                            coinUnit,
                            lastPrice?.toDouble(),
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
                it?.let { it1 ->
                    if (isLineChart)
                        setLineChart(it1)
                    else
                        setCandelStickChart(it1)
                }
            }
        }
    }

    private var _dialog: Dialog? = null
    private var bindingSearchDialog: CustomSpinnerLayoutBinding? = null
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
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, currencyList)
        bindingSearchDialog.apply {
            this?.listview?.adapter = adapter
        }
        dialogVindow?.setLayout(
            ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
        initSpinner()
    }

    private fun initSpinner() {
        bindingSearchDialog?.listview?.setOnItemClickListener { _, _, position, _ ->
            isLineChart = false
            binding.tabLayout.getTabAt(5)?.text = moreTimeList.get(position)
            spinner?.prompt = moreTimeList.get(position)
            val intervalList = arrayListOf("1m", "5m", "30m", "2h", "8h", "12h", "1w", "1M")
            interval = moreTimeList[position]

            dataMarket.symbol?.let {
                viewModel.getChartFromBinanceData(
                    it, intervalList[position]
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
        val tab1 = binding.tabLayout.newTab()
        val tab2 = binding.tabLayout.newTab()
        val tab3 = binding.tabLayout.newTab()
        val tab4 = binding.tabLayout.newTab()
        val tab5 = binding.tabLayout.newTab()

        binding.tabLayout.addTab(tab1.setText(getString(R.string.line)))
        binding.tabLayout.addTab(tab2.setText(getString(R.string.coin_time_15_minute)))
        binding.tabLayout.addTab(tab3.setText(getString(R.string.coin_time_1_hour)))
        binding.tabLayout.addTab(tab4.setText(getString(R.string.coin_time_4_hour)))
        binding.tabLayout.addTab(tab5.setText(getString(R.string.coin_time_1_day)))
        binding.tabLayout.addTab(tab.setText(moreTimeList[0] + "+"))

        setTabWidth(binding.tabLayout, 0, 1.5f)
        setTabWidth(binding.tabLayout, 1, 1.5f)
        setTabWidth(binding.tabLayout, 5, 1.5f)

        /*
        İlk olarak 2. index seçili gelsin diye yaptık.
        * */
        tab2.select()
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

        val depthPagerAdapter = dataMarket.symbol?.let { DepthViewPagerAdapter(this, it) }
        binding.depthViewPager.offscreenPageLimit =
            1 // yalnızca mevcut ve bir önceki sayfayı saklar
        binding.depthViewPager.adapter = depthPagerAdapter
        TabLayoutMediator(
            binding.tabLayoutDepth,
            binding.depthViewPager
        ) { tabItem, position ->
            when (position) {
                0 -> tabItem.text = getString(R.string.orders)
                1 -> tabItem.text = getString(R.string.history)
            }
        }.attach()

    }

    private fun selectedTab(tab: TabLayout.Tab?) {
        when (tab?.position) {
            0 -> {
                isLineChart = true
                interval = "4h"
            }

            1 -> {
                isLineChart = false
                interval = "15m"
            }

            2 -> {
                isLineChart = false
                interval = "1h"
            }

            3 -> {
                isLineChart = false
                interval = "4h"
            }

            4 -> {
                isLineChart = false
                interval = "1d"
            }

            5 -> {
                isLineChart = false
                interval = "1m"
                searchDialog(binding.tabLayout, moreTimeList)
            }
        }
        if (tab?.position != 5) {
            bindingSearchDialog?.listview?.onItemClickListener = null
        }
        progressBar.show()

        dataMarket.symbol?.let {
            viewModel.getChartFromBinanceData(
                it, interval
            )
        }
    }

    private fun setTabWidth(tabLayout: TabLayout, position: Int, weight: Float) {
        val layout = (tabLayout.getChildAt(0) as LinearLayout).getChildAt(position) as LinearLayout
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = weight
        layout.layoutParams = layoutParams
    }

    private fun setCandelStickChart(binanceRoot: Any) {
        binding.lineChart.hide()
        binding.coinDataChart.show()
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
        }
        val candledataset = CandleDataSet(candlestickentry, "Coin")
        candledataset.apply {
            shadowColor = getColorful(this@ChartActivity, R.color.green)
            shadowWidth = 1f
            setDrawValues(false)
            decreasingColor = getColorful(
                this@ChartActivity,
                GlobalThemeUtil.getThemeColor(applicationContext, true)
            )
            decreasingPaintStyle = Paint.Style.FILL
            increasingColor = getColorful(this@ChartActivity,GlobalThemeUtil.getThemeColor(applicationContext, false))
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
                androidx.constraintlayout.widget.R.attr.textFillColor, typedValue, true
            )
            @ColorInt val color = typedValue.data
            axisLeft.textColor = Color.TRANSPARENT
            axisLeft.setDrawAxisLine(false)
            axisLeft.isEnabled = true
            axisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)

            axisRight.isEnabled = true // right line and value remove
            axisRight.textColor = color
            axisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)

            this.isAutoScaleMinMaxEnabled = true
            xval.textColor = color
            xval.position = XAxis.XAxisPosition.BOTTOM
            xval.setDrawGridLines(true)
            xval.valueFormatter = IndexAxisValueFormatter(areaCount)
            xval.setDrawAxisLine(false)  // x line removed

            bacgroundColour(R.color.transparent)
            animateXY(1000, 1000)
            setVisibleXRangeMaximum(65F) //max item range
            moveViewToX(candlestickentry.size.toFloat()) //last item than view
        }
    }

    private fun setLineChart(binanceRoot: Any) {
        binding.lineChart.show()
        binding.coinDataChart.hide()
        progressBar.hide()
        val typedValue = TypedValue()
        val theme: Resources.Theme = this.theme
        theme.resolveAttribute(
            androidx.constraintlayout.widget.R.attr.textFillColor, typedValue, true
        )
        @ColorInt val color = typedValue.data
        val linekentry = ArrayList<Entry>()
        var sayac2 = 0f
        if (binanceRoot is BinanceRoot) {
            binanceRoot.forEach { candleData ->
                linekentry.add(
                    Entry(
                        sayac2,
                        candleData.get(2).toString().toFloat(),
                    )
                )
                sayac2++
            }
        }
        // veri seti oluşturma
        val dataSet = LineDataSet(linekentry, "Veri Seti Adı")

        // çizgi özelliklerini ayarlama
        dataSet.apply {
            val startColor = ContextCompat.getColor(this@ChartActivity, R.color.light_blue2)
            val endColor = ContextCompat.getColor(this@ChartActivity, R.color.transparent)
            val gradient = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(startColor, endColor)
            )
            gradient.alpha = 234
            gradient.cornerRadius = 0f
            setDrawIcons(false)
            this.color = Color.BLUE
            lineWidth = 1f
            setDrawCircles(false)
            setDrawCircleHole(false)
            valueTextSize = 9f
            setDrawFilled(true)
            fillDrawable = gradient
            mode = LineDataSet.Mode.LINEAR
        }
        // grafiğin özelliklerini ayarlama
        val data = LineData(dataSet)
        binding.lineChart.apply {
            clear()
            this.data = data
            data.setDrawValues(false)
            legend.isEnabled = false
            description.isEnabled = false
            isAutoScaleMinMaxEnabled = true
            setGridBackgroundColor(Color.TRANSPARENT)
            setViewPortOffsets(0f, 0f, 50f, 0f)
            animateXY(1000, 1000)
            setVisibleXRangeMaximum(60F)
            moveViewToX(linekentry.size.toFloat())

            xAxis.setDrawGridLines(false)
            xAxis.isEnabled = false

            val lefT = axisLeft
            lefT.setDrawGridLines(false)
            lefT.isEnabled = true
            lefT.textColor = Color.TRANSPARENT
            lefT.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)

            val rightAxis = axisRight
            rightAxis.setDrawGridLines(false)
            rightAxis.isEnabled = true
            rightAxis.textColor = color
            rightAxis.setDrawLabels(true)
            rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)

            invalidate()
        }
    }

    private fun calculateCoin() {
        binding.apply {
            dataMarket.lastPrice?.let {
                MoneyCalculateUtil.coinConverter(
                    edittextUnit, edittextTotal, it
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
        binanceSocket.close(WEBSOCKET_CLOSE_NORMAL, "Ticker Data Websocket closed")
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.clearChartBinanceData()
        viewModel.clearBinanceSocketChartLiveData()
        viewModel.clearBinanceSocketTickerLiveData()
        viewModel.clearGetTickerFromBinanceLiveData()
        super.onDestroy()
        coinPortfolioViewModel.compositeDisposable.clear()
    }

}