package com.metoer.ceptedovizborsa.data.paging

import androidx.paging.PagingSource
import com.metoer.ceptedovizborsa.data.AppApi
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinData
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import java.util.logging.Handler
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class AllCoinDataSoruce @Inject constructor(val appApi: AppApi) :
    PagingSource<Int, CoinData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinData> {
        return try {
            val currentPage = params.key ?: 0
            val response = appApi.getAllCoinData(currentPage)

            LoadResult.Page(
                data = response.data,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = if (response.data.isNullOrEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
