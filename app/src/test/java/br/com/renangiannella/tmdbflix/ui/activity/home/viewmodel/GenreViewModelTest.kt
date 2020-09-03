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
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.exceptions.base.MockitoException
import java.io.IOException

@ExperimentalCoroutinesApi
class GenreViewModelTest {

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
    fun `when actionMovies get success then set actionMovieResponse` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = GenreViewModel(repository, testCoroutineDispatcher)
        val movieResponse = returnMovie()
        testCoroutineDispatcher.pauseDispatcher()

        Mockito.doReturn(movieResponse)
            .`when`(repository).getMovieGenre(BuildConfig.API_KEY, "pt-BR", 28)
        viewModel.getActionMovie(BuildConfig.API_KEY, "pt-BR", 28)

        testCoroutineDispatcher.resumeDispatcher()
        Truth.assertThat(viewModel.actionMovieResponse.value).isEqualTo(State.success(movieResponse))

    }

    @Test
    fun `when comedyMovies get success then set comedyMovieResponse` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = GenreViewModel(repository, testCoroutineDispatcher)
        val movieResponse = returnMovie()
        testCoroutineDispatcher.pauseDispatcher()

        Mockito.doReturn(movieResponse)
            .`when`(repository).getMovieGenre(BuildConfig.API_KEY, "pt-BR", 35)
        viewModel.getComedyMovie(BuildConfig.API_KEY, "pt-BR", 35)

        testCoroutineDispatcher.resumeDispatcher()
        Truth.assertThat(viewModel.comedyMovieResponse.value).isEqualTo(State.success(movieResponse))

    }

    @Test
    fun `when adventureMovies get success then set adventureMovieResponse` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = GenreViewModel(repository, testCoroutineDispatcher)
        val movieResponse = returnMovie()
        testCoroutineDispatcher.pauseDispatcher()

        Mockito.doReturn(movieResponse)
            .`when`(repository).getMovieGenre(BuildConfig.API_KEY, "pt-BR", 12)
        viewModel.getAdventureMovie(BuildConfig.API_KEY, "pt-BR", 12)

        testCoroutineDispatcher.resumeDispatcher()
        Truth.assertThat(viewModel.adventureMovieResponse.value).isEqualTo(State.success(movieResponse))

    }

    @Test(expected = MockitoException::class)
    fun `when actionMovies failure then set error state` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = GenreViewModel(repository, testCoroutineDispatcher)
        testCoroutineDispatcher.pauseDispatcher()

        Mockito.doThrow(IOException::class.java)
            .`when`(repository).getMovieGenre(BuildConfig.API_KEY, "pt-BR", 28)
        viewModel.getActionMovie(BuildConfig.API_KEY, "pt-BR", 28)

        testCoroutineDispatcher.resumeDispatcher()
        Truth.assertThat(viewModel.actionMovieResponse.value)
            .isEqualTo(State.error<MovieResponse>(IOException()))

    }

    @Test(expected = MockitoException::class)
    fun `when comedyMovies failure then set error state` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = GenreViewModel(repository, testCoroutineDispatcher)
        testCoroutineDispatcher.pauseDispatcher()

        Mockito.doThrow(IOException::class.java)
            .`when`(repository).getMovieGenre(BuildConfig.API_KEY, "pt-BR", 35)
        viewModel.getComedyMovie(BuildConfig.API_KEY, "pt-BR", 35)

        testCoroutineDispatcher.resumeDispatcher()
        Truth.assertThat(viewModel.comedyMovieResponse.value)
            .isEqualTo(State.error<MovieResponse>(IOException()))

    }

    @Test(expected = MockitoException::class)
    fun `when adventureMovies failure then set error state` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = GenreViewModel(repository, testCoroutineDispatcher)
        testCoroutineDispatcher.pauseDispatcher()

        Mockito.doThrow(IOException::class.java)
            .`when`(repository).getMovieGenre(BuildConfig.API_KEY, "pt-BR", 12)
        viewModel.getAdventureMovie(BuildConfig.API_KEY, "pt-BR", 12)

        testCoroutineDispatcher.resumeDispatcher()
        Truth.assertThat(viewModel.adventureMovieResponse.value)
            .isEqualTo(State.error<MovieResponse>(IOException()))

    }

    @Test
    fun `when actionMovies verify loading state` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = GenreViewModel(repository, testCoroutineDispatcher)
        val movieResponse = returnMovie()
        testCoroutineDispatcher.pauseDispatcher()

        Mockito.doReturn(movieResponse)
            .`when`(repository).getMovieGenre(BuildConfig.API_KEY, "pt-BR", 28)
        viewModel.getActionMovie(BuildConfig.API_KEY, "pt-BR", 28)

        Truth.assertThat(viewModel.actionMovieResponse.value).isEqualTo(State.loading<MovieResponse>(true))

    }

    @Test
    fun `when comedyMovies verify loading state` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = GenreViewModel(repository, testCoroutineDispatcher)
        val movieResponse = returnMovie()
        testCoroutineDispatcher.pauseDispatcher()

        Mockito.doReturn(movieResponse)
            .`when`(repository).getMovieGenre(BuildConfig.API_KEY, "pt-BR", 35)
        viewModel.getComedyMovie(BuildConfig.API_KEY, "pt-BR", 35)

        Truth.assertThat(viewModel.comedyMovieResponse.value).isEqualTo(State.loading<MovieResponse>(true))

    }

    @Test
    fun `when adventureMovies verify loading state` () = testCoroutineDispatcher.runBlockingTest {
        val viewModel = GenreViewModel(repository, testCoroutineDispatcher)
        val movieResponse = returnMovie()
        testCoroutineDispatcher.pauseDispatcher()

        Mockito.doReturn(movieResponse)
            .`when`(repository).getMovieGenre(BuildConfig.API_KEY, "pt-BR", 12)
        viewModel.getAdventureMovie(BuildConfig.API_KEY, "pt-BR", 12)

        Truth.assertThat(viewModel.adventureMovieResponse.value).isEqualTo(State.loading<MovieResponse>(true))

    }

    private fun returnMovie(): MovieResponse = MovieResponse(listOf(
        MovieResult(
        1, "renan",
        "vai", "20-02", listOf(1),
        "lieberson", "bruna")
    ))
}
