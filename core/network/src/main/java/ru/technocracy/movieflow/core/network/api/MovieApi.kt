package ru.technocracy.movieflow.core.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.technocracy.movieflow.core.network.dto.FilmDetailsDto
import ru.technocracy.movieflow.core.network.dto.FilmsCollectionResponse
import ru.technocracy.movieflow.core.network.dto.SearchResponseDto

interface MovieApi {

    companion object {
        const val COLLECTION_TYPE_POPULAR = "TOP_POPULAR_MOVIES"
        const val FIRST_PAGE = 1
    }

    @GET("v2.2/films/collections")
    suspend fun getPopularMovies(
        @Query("type") type: String = COLLECTION_TYPE_POPULAR,
        @Query("page") page: Int = FIRST_PAGE
    ): FilmsCollectionResponse

    // получить детальную инфу (для экрана фильма)
    @GET("v2.2/films/{id}")
    suspend fun getMovieDetails(@Path("id") id: Int): FilmDetailsDto

    // поиск
    @GET("v2.1/films/search-by-keyword")
    suspend fun searchMovies(
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 1
    ): SearchResponseDto
}