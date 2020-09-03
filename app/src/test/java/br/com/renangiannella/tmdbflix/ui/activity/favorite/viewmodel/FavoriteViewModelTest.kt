package br.com.renangiannella.tmdbflix.ui.activity.favorite.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import br.com.renangiannella.tmdbflix.BuildConfig
import br.com.renangiannella.tmdbflix.core.State
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.model.response.MovieResponse
import br.com.renangiannella.tmdbflix.data.model.result.MovieResult
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel.GenreViewModel
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class FavoriteViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private var repository = Mockito.mock(MovieRepository::class.java)

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        testCoroutineDispatcher.cleanupTestCoroutines()
        Dispatchers.resetMain()
    }

    @Test
    fun `when insert Favorite movie get success` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = FavoriteViewModel(repository, testCoroutineDispatcher)
        val favoriteMovie  = favoriteMock()
        testCoroutineDispatcher.pauseDispatcher()

        Mockito.doReturn(favoriteMovie)
            .`when`(repository).insertFavoriteMovie(favoriteMovie)
        viewModel.insertMovie(favoriteMovie)

        testCoroutineDispatcher.resumeDispatcher()
        Truth.assertThat(viewModel.insertMovie(favoriteMovie))

    }

    @Test
    fun `when delete favorite movie get success` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = FavoriteViewModel(repository, testCoroutineDispatcher)
        val favoriteMovie  = favoriteMock()
        testCoroutineDispatcher.pauseDispatcher()

        Mockito.doReturn(favoriteMovie)
            .`when`(repository).deleteFavoriteMovie(favoriteMovie)
        viewModel.deleteMovie(favoriteMovie)

        testCoroutineDispatcher.resumeDispatcher()
        Truth.assertThat(viewModel.deleteMovie(favoriteMovie))

    }

    private fun favoriteMock() : FavoriteMovie = FavoriteMovie(
        1,
        "renan",
        "vai", "20-02","2020-02-02", listOf(1),
        "lieberson","6.0")
}