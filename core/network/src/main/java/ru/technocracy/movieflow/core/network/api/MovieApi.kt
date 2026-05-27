package ru.technocracy.movieflow.core.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.technocracy.movieflow.core.network.dto.FilmDetailsDto
import ru.technocracy.movieflow.core.network.dto.FilmsCollectionResponse

interface MovieApi {

    // получить коллекцию (для ленты)
    @GET("v2.2/films/collections")
    suspend fun getPopularMovies(
        @Query("type") type: String = "TOP_POPULAR_MOVIES", //todo хардкод
        @Query("page") page: Int = 1
    ): FilmsCollectionResponse

    // получить детальную инфу (для экрана фильма)
    @GET("v2.2/films/{id}")
    suspend fun getMovieDetails(@Path("id") id: Int): FilmDetailsDto
}