package com.metoer.ceptedovizborsa.view.activity

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import com.metoer.ceptedovizborsa.data.response.coin.markets.MarketData
import com.metoer.ceptedovizborsa.databinding.ActivityChartBinding
import com.metoer.ceptedovizborsa.util.*
import com.metoer.ceptedovizborsa.viewmodel.activity.ChartViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class ChartActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var _binding: ActivityChartBinding? = null
    private lateinit var dataMarket: MarketData
    private val binding
        get() = _binding
    private val viewModel: ChartViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding?.apply {
            btnBuy.setBackgroundResource(R.drawable.buy_button_design)
        }
    }

    override fun onResume() {
        super.onResume()
        dataMarket = intent.getSerializableExtra("send") as MarketData
        initTabLayout()
        initSpinner()
        initListeners()
    }

    private fun initListeners() {
        binding.apply { }
        setCandelStickChart()
        viewModel.getAllCandlesData(interval, dataMarket.baseId, dataMarket.quoteId)

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

        binding?.spinnerCoinMoreItems.apply {
            this?.adapter =
                ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, moreTimeList)
        }
    }

    var interval = "m15"
    private fun initTabLayout() {
        binding?.apply {
//            setCandelStickChart("m15", dataMarket.baseId, dataMarket.quoteId)
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
                    textViewVolume.text = "${tab?.text} Hacim"
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }
    }


    private fun setCandelStickChart() {
        binding!!.apply {
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
                    Log.i("valueobserve", ""+candleData.close)
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
                Log.i("MYLOG", "2: ")
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
                Log.i("MYLOG", "3: ")

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

}