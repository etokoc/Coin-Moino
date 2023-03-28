package com.metoer.ceptedovizborsa.data.paging

import androidx.paging.PagingSource
import com.metoer.ceptedovizborsa.data.AppApi
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.logging.Handler
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class AllCoinDataSoruce @Inject constructor(val appApi: AppApi) :
    PagingSource<Int, CoinData>() {
    //    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinData> {
//        return try {
//            val page = params.key ?: 1
//            val response = withContext(Dispatchers.IO) {
//                appApi.getAllCoinData(page)
//            }
//            LoadResult.Page(
//                data = response.blockingNext().iterator().next().data,
//                prevKey = if (page > 0) page - 1 else null,
//                nextKey = if (page < 100) page + 1 else null
//            )
//
//        } catch (e: Exception) {
//            return LoadResult.Error(e)
//        }
//    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinData> {
        return try {
            val currentPage = params.key ?: 1
            val response = appApi.getAllCoinData(currentPage)
//            val data = response.body()?.data ?: emptyList()
            var data = listOf<CoinData>()
            response.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    data = it.data
                }, {

                }).let {

                }
            val responseData = mutableListOf<CoinData>()
            responseData.addAll(data)

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(100)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}