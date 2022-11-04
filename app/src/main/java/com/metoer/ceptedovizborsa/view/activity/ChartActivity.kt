package com.metoer.ceptedovizborsa.view.activity

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
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
import com.metoer.ceptedovizborsa.util.bacgroundColour
import com.metoer.ceptedovizborsa.viewmodel.activity.ChartViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class ChartActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var _binding: ActivityChartBinding? = null
    private val binding
        get() = _binding
    private val viewModel: ChartViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }

    override fun onResume() {
        super.onResume()
        val dataMarket = intent.getSerializableExtra("send") as MarketData

        binding?.apply {
            setCandelStickChart(dataMarket.baseId, dataMarket.quoteId)
        }
        initTabLayout()
        initSpinner()
    }

    private fun initSpinner() {
        val moreTimeList = arrayListOf<String>(
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

    private fun initTabLayout() {
        binding?.tabLayout.apply {
            this?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    Toast.makeText(context, "${tab?.text}", Toast.LENGTH_SHORT).show()
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }

            })
        }
    }


    private fun setCandelStickChart(baseId: String, qutoId: String) {
        binding!!.apply {
            val candlestickentry = ArrayList<CandleEntry>()
            val areaCount = ArrayList<String>()
//        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            viewModel.getAllCandlesData("h1", baseId, qutoId).observe(this@ChartActivity) {
                var sayac = 0f
                it.forEach { candleData ->
                    areaCount.add(getDate(candleData.period)!!)
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
                coinValueTextView.text = DecimalFormat(
                    "###,###,###,###.######",
                    DecimalFormatSymbols.getInstance()
                ).format(it.get(candlestickentry.size - 1).close.toDouble())
                volumeTextView.text =
                    DecimalFormat("###,###,###,###.##", DecimalFormatSymbols.getInstance()).format(
                        it.get(candlestickentry.size - 1).volume.toDouble()
                    )
                highestPriceTextView.text = DecimalFormat(
                    "###,###,###,###.######",
                    DecimalFormatSymbols.getInstance()
                ).format(it.get(candlestickentry.size - 1).high.toDouble())
                lowestPriceTextView.text = DecimalFormat(
                    "###,###,###,###.######",
                    DecimalFormatSymbols.getInstance()
                ).format(it.get(candlestickentry.size - 1).low.toDouble())
                candledataset.color = Color.rgb(80, 80, 80)
                candledataset.shadowColor = ContextCompat.getColor(
                    this@ChartActivity,
                    R.color.green
                )//çizgi rengi düşüşe veya yükselişe göre değişecek
                candledataset.shadowWidth = 1f
                candledataset.setDrawValues(false)
                candledataset.decreasingColor =
                    ContextCompat.getColor(this@ChartActivity, R.color.coinValueDrop)
                candledataset.decreasingPaintStyle = Paint.Style.FILL
                candledataset.increasingColor =
                    ContextCompat.getColor(this@ChartActivity, R.color.coinValueRise)
                candledataset.increasingPaintStyle = Paint.Style.FILL
                val candledata = CandleData(candledataset)
                coinDataChart.data = candledata

                coinDataChart.onChartGestureListener = object : OnChartGestureListener {
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
                xval.setValueFormatter(IndexAxisValueFormatter(areaCount))
                coinDataChart.bacgroundColour(R.color.transparent)
                coinDataChart.axisLeft.setDrawAxisLine(false)
                coinDataChart.axisRight.isEnabled = false // right line and value remove
                coinDataChart.xAxis.setDrawAxisLine(false)  // x line removed
                coinDataChart.animateXY(1000, 1000)
                coinDataChart.setVisibleXRangeMaximum(65F) //max item range
                coinDataChart.moveViewToX(candlestickentry.size.toFloat()) //last item than view

            }
        }
    }

    private fun getDate(milliSeconds: Long): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter: DateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds)
        return formatter.format(calendar.getTime())
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