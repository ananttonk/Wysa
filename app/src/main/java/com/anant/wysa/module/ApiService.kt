package com.anant.wysa.module

import com.anant.wysa.database.MovieDAO
import com.anant.wysa.repository.MovieAPIService
import com.anant.wysa.repository.MovieRepo
import com.anant.wysa.repository.MovieRepoImpl
import com.anant.wysa.util.Constant

interface ApiService {
    val movieAPIService: MovieAPIService
    val movieRepoImpl: MovieRepo
}

class ModuleImpl(dataBase: MovieDAO) : ApiService {
    override val movieAPIService: MovieAPIService by lazy {
        RetrofitClient().getRetrofit(Constant.BASE_URL)
            .create(MovieAPIService::class.java)
    }
    override val movieRepoImpl: MovieRepo by lazy {
        MovieRepoImpl(movieAPIService,dataBase)
    }


}