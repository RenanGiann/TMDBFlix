package br.com.renangiannella.tmdbflix.ui.activity.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.renangiannella.tmdbflix.BuildConfig
import br.com.renangiannella.tmdbflix.core.State
import br.com.renangiannella.tmdbflix.data.model.response.MovieResponse
import br.com.renangiannella.tmdbflix.data.model.result.MovieResult
import br.com.renangiannella.tmdbflix.data.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.Dispatcher
import org.junit.After
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.exceptions.base.MockitoException
import java.io.IOException

@ExperimentalCoroutinesApi
class SearchViewModelTest {

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
    fun `when searchMovies get success then set searchResponse` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = SearchViewModel(repository, testCoroutineDispatcher)
        val movieResponse = returnMovie()
        testCoroutineDispatcher.pauseDispatcher()

        doReturn(movieResponse).`when`(repository).searchMovies("batman", BuildConfig.API_KEY, "pt-BR")
        viewModel.searchMovies("batman", BuildConfig.API_KEY, "pt-BR")

        testCoroutineDispatcher.resumeDispatcher()
        assertThat(viewModel.searchResponse.value).isEqualTo(State.success(movieResponse))

    }

    @Test(expected = MockitoException::class)
    fun `when searchMovies failure then set error state` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = SearchViewModel(repository, testCoroutineDispatcher)
        testCoroutineDispatcher.pauseDispatcher()

        doThrow(IOException::class.java).`when`(repository).searchMovies("batman", BuildConfig.API_KEY, "pt-BR")
        viewModel.searchMovies("batman", BuildConfig.API_KEY, "pt-BR")

        testCoroutineDispatcher.resumeDispatcher()
        assertThat(viewModel.searchResponse.value).isEqualTo(State.error<MovieResponse>(IOException()))

    }

    @Test
    fun `when searchMovies verify loading state` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = SearchViewModel(repository, testCoroutineDispatcher)
        val movieResponse = returnMovie()
        testCoroutineDispatcher.pauseDispatcher()

        doReturn(movieResponse).`when`(repository).searchMovies("batman", BuildConfig.API_KEY, "pt-BR")
        viewModel.searchMovies("batman", BuildConfig.API_KEY, "pt-BR")

        assertThat(viewModel.searchResponse.value).isEqualTo(State.loading<MovieResponse>(true))

    }

    private fun returnMovie(): MovieResponse = MovieResponse(listOf(MovieResult(
        1, "renan",
        "vai", "20-02", listOf(1),
        "lieberson", "bruna")))
}