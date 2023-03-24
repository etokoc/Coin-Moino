package com.metoer.ceptedovizborsa.data.paging

import androidx.paging.PagingSource
import androidx.paging.rxjava2.RxPagingSource
import com.metoer.ceptedovizborsa.data.AppApi
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinData
import com.metoer.ceptedovizborsa.data.response.coin.assets.CoinResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AllCoinDataSoruce @Inject constructor(val appApi: AppApi) :
    PagingSource<Int, CoinData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinData> {
        try {
            val page = params.key ?: 1
            val response = appApi.getAllCoinData(page)
            val data = response.body()?.data?: emptyList()
            val prevKey = if (page == 1) null else page - 1
            val nextKey = if (data.isEmpty()) null else page + 1
            return LoadResult.Page(data, prevKey, nextKey)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}