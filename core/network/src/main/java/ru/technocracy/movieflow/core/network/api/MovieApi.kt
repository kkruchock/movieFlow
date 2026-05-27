package ru.technocracy.movieflow.core.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.technocracy.movieflow.core.network.dto.FilmsCollectionResponse

interface MovieApi {

    @GET("v2.2/films/collections")
    suspend fun getPopularMovies(
        @Query("type") type: String = "TOP_POPULAR_MOVIES", //todo хардкод
        @Query("page") page: Int = 1
    ): FilmsCollectionResponse
}