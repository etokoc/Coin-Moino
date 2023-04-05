package com.metoer.ceptedovizborsa.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
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

/**
 * key 100 olarak artırıp azaltınca çekme işlemi yani offset yapılıyor
 */
class AllCoinDataSoruce @Inject constructor(val appApi: AppApi) :
    PagingSource<Int, CoinData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinData> {
        return try {
            val currentPage = params.key ?: 0
            val response = appApi.getAllCoinData(currentPage)

            LoadResult.Page(
                data = response.data,
                prevKey = if (currentPage == 0) null else currentPage - 100,
                nextKey = if (response.data.isEmpty()) null else currentPage + 100
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CoinData>): Int? {
        return null
    }
}
