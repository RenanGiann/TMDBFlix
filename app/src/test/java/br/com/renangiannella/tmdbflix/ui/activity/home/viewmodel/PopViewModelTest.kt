package br.com.renangiannella.tmdbflix.ui.activity.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.renangiannella.tmdbflix.BuildConfig
import br.com.renangiannella.tmdbflix.core.State
import br.com.renangiannella.tmdbflix.data.model.response.MovieResponse
import br.com.renangiannella.tmdbflix.data.model.result.MovieResult
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import br.com.renangiannella.tmdbflix.ui.activity.search.SearchViewModel
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.exceptions.base.MockitoException
import java.io.IOException

@ExperimentalCoroutinesApi
class PopViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val repository = Mockito.mock(MovieRepository::class.java)

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
    fun `when popMovies get success then set searchResponse` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = PopViewModel(repository, testCoroutineDispatcher)
        val movieResponse = returnMovie()
        testCoroutineDispatcher.pauseDispatcher()

        Mockito.doReturn(movieResponse)
            .`when`(repository).getMoviePopular(BuildConfig.API_KEY, "pt-BR")
        viewModel.getPopularMovie(BuildConfig.API_KEY, "pt-BR")

        testCoroutineDispatcher.resumeDispatcher()
        Truth.assertThat(viewModel.popMovieResponse.value).isEqualTo(State.success(movieResponse))

    }

    @Test(expected = MockitoException::class)
    fun `when popMovies failure then set error state` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = PopViewModel(repository, testCoroutineDispatcher)
        testCoroutineDispatcher.pauseDispatcher()

        Mockito.doThrow(IOException::class.java)
            .`when`(repository).getMoviePopular(BuildConfig.API_KEY, "pt-BR")
        viewModel.getPopularMovie(BuildConfig.API_KEY, "pt-BR")

        testCoroutineDispatcher.resumeDispatcher()
        Truth.assertThat(viewModel.popMovieResponse.value)
            .isEqualTo(State.error<MovieResponse>(IOException()))

    }

    @Test
    fun `when popMovies verify loading state` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = PopViewModel(repository, testCoroutineDispatcher)
        val movieResponse = returnMovie()
        testCoroutineDispatcher.pauseDispatcher()

        Mockito.doReturn(movieResponse)
            .`when`(repository).getMoviePopular(BuildConfig.API_KEY, "pt-BR")
        viewModel.getPopularMovie(BuildConfig.API_KEY, "pt-BR")

        Truth.assertThat(viewModel.popMovieResponse.value).isEqualTo(State.loading<MovieResponse>(true))

    }

    private fun returnMovie(): MovieResponse = MovieResponse(listOf(
        MovieResult(
        1, "renan",
        "vai", "20-02", listOf(1),
        "lieberson", "bruna")
    ))
}