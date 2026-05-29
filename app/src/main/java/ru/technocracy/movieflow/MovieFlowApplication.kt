package ru.technocracy.movieflow

import android.app.Application
import com.google.firebase.FirebaseApp
import ru.technocracy.feature.feed.di.CatalogViewModelFactory
import ru.technocracy.movieflow.core.domain.repository.AuthRepository
import ru.technocracy.movieflow.core.domain.usecase.auth.IsLoggedInUseCase
import ru.technocracy.movieflow.di.AppComponent
import ru.technocracy.movieflow.di.DaggerAppComponent
import ru.technocracy.movieflow.feature.auth.di.AuthViewModelFactory
import ru.technocracy.movieflow.feature.collections.di.CollectionsViewModelFactory
import ru.technocracy.movieflow.feature.details.di.DetailsViewModelFactory
import ru.technocracy.movieflow.feature.profile.di.ProfileViewModelFactory
import ru.technocracy.movieflow.feature.profile.di.UserMovieListViewModelFactory
import ru.technocracy.movieflow.feature.search.di.SearchViewModelFactory
import javax.inject.Inject

class MovieFlowApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory
    @Inject
    lateinit var catalogViewModelFactory: CatalogViewModelFactory
    @Inject
    lateinit var detailsViewModelFactory: DetailsViewModelFactory
    @Inject
    lateinit var searchViewModelFactory: SearchViewModelFactory
    @Inject
    lateinit var collectionsViewModelFactory: CollectionsViewModelFactory
    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory
    @Inject
    lateinit var isLoggedInUseCase: IsLoggedInUseCase
    @Inject
    lateinit var authRepository: AuthRepository
    @Inject
    lateinit var userMovieListViewModelFactory: UserMovieListViewModelFactory

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        appComponent.inject(this)
    }
}