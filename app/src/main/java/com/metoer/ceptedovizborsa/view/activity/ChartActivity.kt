package com.metoer.ceptedovizborsa.view.activity

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.google.android.material.tabs.TabLayout
import com.metoer.ceptedovizborsa.R
import com.metoer.ceptedovizborsa.data.db.CoinBuyItem
import com.metoer.ceptedovizborsa.data.response.coin.markets.MarketData
import com.metoer.ceptedovizborsa.databinding.ActivityChartBinding
import com.metoer.ceptedovizborsa.util.*
import com.metoer.ceptedovizborsa.util.EditTextUtil.editTextFilter
import com.metoer.ceptedovizborsa.viewmodel.activity.ChartViewModel
import com.metoer.ceptedovizborsa.viewmodel.fragment.CoinPortfolioViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_chart.*
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class ChartActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var _binding: ActivityChartBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var dataMarket: MarketData

    private val viewModel: ChartViewModel by viewModels()
    private val coinPortfolioViewModel: CoinPortfolioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {

        }
    }

    override fun onResume() {
        super.onResume()
        dataMarket = intent.getSerializableExtra("send") as MarketData
        binding.apply {
            edittextTotal.filters = editTextFilter()
            edittextUnit.filters = editTextFilter()
            edittextUnit.hint = getString(R.string.miktar, dataMarket.baseSymbol)
            edittextTotal.hint = getString(R.string.toplam, dataMarket.quoteSymbol)
        }
        initTabLayout()
        initSpinner()
        initListeners()
        calculateCoin()
    }

    private fun initListeners() {
        binding.apply { }
        setCandelStickChart()
        viewModel.getAllCandlesData(interval, dataMarket.baseId, dataMarket.quoteId)

        //Coin Buy Click
        btnBuy.setOnClickListener {
            var coinUnit = 0
            edittext_unit?.let {
                coinUnit = it.text.toString().toInt()
            }
            dataMarket.apply {
                val coinBuyItem = CoinBuyItem(
                    baseSymbol,
                    quoteSymbol,
                    baseId,
                    coinUnit,
                    priceQuote.toDouble(),
                    System.currentTimeMillis()
                )
                coinPortfolioViewModel.upsertCoinBuyItem(coinBuyItem)
            }
        }

    }

    private fun initSpinner() {
        val moreTimeList = arrayListOf(
            getString(R.string.coin_time_1_minute),
            getString(R.string.coin_time_5_minute),
            getString(
                R.string.coin_time_30_minute,
            ),
            getString(R.string.coin_time_2_hour),
            getString(R.string.coin_time_8_hour),
            getString(R.string.coin_time_12_hour),
            getString(R.string.coin_time_1_week),
        )

        binding.spinnerCoinMoreItems.apply {
            this.adapter =
                ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, moreTimeList)
        }
    }

    var interval = "m15"
    private fun initTabLayout() {
        binding.apply {
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            interval = "m15"
                        }
                        1 -> {
                            interval = "h1"
                        }
                        2 -> {
                            interval = "h4"
                        }
                        3 -> {
                            interval = "d1"
                        }
                    }
                    progressBar.show()
                    viewModel.getAllCandlesData(interval, dataMarket.baseId, dataMarket.quoteId)
                    textViewVolume.text = getString(R.string.volume_text, tab?.text)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }
    }


    private fun setCandelStickChart() {
        binding.apply {
            val candlestickentry = ArrayList<CandleEntry>()
            candlestickentry.clear()
            val areaCount = ArrayList<String>()
            viewModel.coinCanslesData.observe(this@ChartActivity) {
                progressBar.hide()
                candlestickentry.clear()
                areaCount.clear()
                var sayac = 0f
                Log.i("MYLOG", "1: ")
                it.forEach { candleData ->
                    areaCount.add(getDate(candleData.period)!!)
                    Log.i("valueobserve", "" + candleData.close)
                    candlestickentry.add(
                        CandleEntry(
                            sayac,
                            candleData.high.toFloat(),
                            candleData.low.toFloat(),
                            candleData.open.toFloat(),
                            candleData.close.toFloat()
                        )
                    )
                    sayac++
                }
                val candledataset = CandleDataSet(candlestickentry, "Coin")
                coinValueTextView.text =
                    NumberDecimalFormat.numberDecimalFormat(
                        it.last().close,
                        "###,###,###,###.######"
                    )
                volumeTextView.text =
                    NumberDecimalFormat.numberDecimalFormat(
                        it.last().volume,
                        "###,###,###,###.##"
                    )
                highestPriceTextView.text =
                    NumberDecimalFormat.numberDecimalFormat(
                        it.last().high,
                        "###,###,###,###.######"
                    )

                lowestPriceTextView.text =
                    NumberDecimalFormat.numberDecimalFormat(
                        it.last().low,
                        "###,###,###,###.######"
                    )

                candledataset.apply {
                    color = Color.rgb(80, 80, 80)
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
                    onChartGestureListener = object : OnChartGestureListener {
                        override fun onChartGestureStart(
                            me: MotionEvent?,
                            lastPerformedGesture: ChartTouchListener.ChartGesture?
                        ) {

                        }

                        override fun onChartGestureEnd(
                            me: MotionEvent?,
                            lastPerformedGesture: ChartTouchListener.ChartGesture?
                        ) {

                        }

                        override fun onChartLongPressed(me: MotionEvent?) {

                        }

                        override fun onChartDoubleTapped(me: MotionEvent?) {

                        }

                        override fun onChartSingleTapped(me: MotionEvent?) {

                        }

                        override fun onChartFling(
                            me1: MotionEvent?,
                            me2: MotionEvent?,
                            velocityX: Float,
                            velocityY: Float
                        ) {


                        }

                        override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {


                        }

                        override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
                            autoScale(coinDataChart)

                        }
                    }
                    val xval = coinDataChart.xAxis
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
        }
    }

    private fun calculateCoin() {
        var money: Double
        binding.apply {
            var editControl = false
            var editControl2 = false
            edittextUnit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int
                ) {
                    editControl = true
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0 != null && p0.isNotEmpty() && !edittext_unit.text.toString()
                            .startsWith(',')
                    ) {
                        if (editControl && !editControl2) {
                            money = MoneyCalculateUtil.doubleConverter(p0)
                            val coinCalculate = dataMarket.priceQuote.toDouble() * money
                            edittextTotal.setText(
                                DecimalFormat("##.######").format(coinCalculate).toString()
                            )
                        }
                    } else {
                        edittext_unit.text?.clear()
                        edittext_total.text?.clear()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    edittextUnit.setDefaultKeyListener(EditTextUtil.editTextSelectDigits(p0))
                    editControl = false
                }

            })

            edittextTotal.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    p0: CharSequence?,
                    p1: Int,
                    p2: Int,
                    p3: Int
                ) {
                    editControl2 = true
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0 != null && p0.isNotEmpty() && !edittext_total.text.toString()
                            .startsWith(',')
                    ) {
                        if (editControl2 && !editControl) {
                            money = MoneyCalculateUtil.doubleConverter(p0)
                            val coinCalculate = money / dataMarket.priceQuote.toDouble()
                            edittextUnit.setText(
                                DecimalFormat("##.######").format(coinCalculate).toString()
                            )
                        }
                    } else {
                        edittext_total.text?.clear()
                        edittext_unit.text?.clear()
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    edittext_total.setDefaultKeyListener(EditTextUtil.editTextSelectDigits(p0))
                    editControl2 = false
                }

            })

        }
    }

    private fun getDate(milliSeconds: Long): String? {
        val formatter: DateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    private fun autoScale(candleChart: CandleStickChart) {
        val lowestVisibleX: Float = candleChart.lowestVisibleX
        val highestVisibleX: Float = candleChart.highestVisibleX
        val chartData: CandleData = candleChart.candleData
        chartData.calcMinMaxY(lowestVisibleX, highestVisibleX)
        candleChart.xAxis.calculate(chartData.xMin, chartData.xMax)
        calculateMinMaxForYAxis(candleChart, AxisDependency.LEFT)
        calculateMinMaxForYAxis(candleChart, AxisDependency.RIGHT)
        candleChart.calculateOffsets()
    }

    private fun calculateMinMaxForYAxis(
        candleChart: CandleStickChart,
        axisDependency: AxisDependency
    ) {
        val chartData = candleChart.candleData
        val yAxis = candleChart.getAxis(axisDependency)
        if (yAxis.isEnabled) {
            val yMin = chartData.getYMin(axisDependency)
            val yMax = chartData.getYMax(axisDependency)
            yAxis.calculate(yMin, yMax)
        }
    }

    //Listen to spinner
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onDestroy() {
        super.onDestroy()
        coinPortfolioViewModel.compositeDisposable.clear()
    }

}