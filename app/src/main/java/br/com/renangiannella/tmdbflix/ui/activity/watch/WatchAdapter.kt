package br.com.renangiannella.tmdbflix.ui.activity.watch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.renangiannella.tmdbflix.BuildConfig
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.db.modeldb.WatchMovie
import br.com.renangiannella.tmdbflix.data.model.result.MovieResult
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*

class WatchAdapter(val watch: List<WatchMovie>,
                   val favorites: List<FavoriteMovie>,
                   val clickLike: ((movie: WatchMovie) -> Unit),
                   val clickDislike: ((movie: WatchMovie) -> Unit),
                   val clickMovie: ((movie: WatchMovie) -> Unit),
                   val clickDisWatchMovie: ((movie: WatchMovie) -> Unit)):
    RecyclerView.Adapter<WatchAdapter.MovieAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchAdapter.MovieAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieAdapterViewHolder(itemView, clickLike, clickDislike, clickMovie, clickDisWatchMovie)
    }

    override fun getItemCount() = watch.count()

    override fun onBindViewHolder(holder: WatchAdapter.MovieAdapterViewHolder, position: Int) {
        holder.bind(watch[position], favorites)
    }

    fun getCurrentMovie(position: Int) = watch[position]

    class MovieAdapterViewHolder(itemView: View,
                                 val clickLike: ((movie: WatchMovie) -> Unit),
                                 val clickDislike: ((movie: WatchMovie) -> Unit),
                                 val clickMovie: ((movie: WatchMovie)-> Unit),
                                 val clickDisWatchMovie:((movie: WatchMovie)-> Unit)): RecyclerView.ViewHolder(itemView) {

        private val title = itemView.textNameMovie
        private val vote = itemView.textVoteAverage
        private val releaseDate = itemView.textReleaseDate
        private val posterMovie = itemView.imageViewMovie
        private val imageLike = itemView.img_favorite_red
        private val imageDislike = itemView.img_favorite_movie
        private val imageWatch = itemView.img_towatch
        private val imageDiswatch = itemView.img_diswatch

        private val picasso = Picasso.get()

        fun bind(movie: WatchMovie, favorite: List<FavoriteMovie>){
            title.text = movie.original_title
            vote.text = movie.vote_average
            releaseDate.text = movie.release_date

            for (i in favorite) {
                when {
                    movie.id.equals(i.id) -> imageLike.visibility = View.VISIBLE
                }
            }
            movie.poster_path.let {
                picasso.load("""${BuildConfig.BASE_URL_IMAGE}${movie.poster_path}""").into(posterMovie)
            }

            imageWatch.visibility = View.VISIBLE
            imageDiswatch.visibility = View.GONE

            imageWatch.setOnClickListener{
                clickDisWatchMovie.invoke(movie)
                imageDiswatch.visibility = View.GONE
                imageWatch.visibility = View.VISIBLE
            }

            imageLike.setOnClickListener{
                clickDislike.invoke(movie)
                imageDislike.visibility = View.VISIBLE
                imageLike.visibility = View.GONE
            }

            imageDislike.setOnClickListener {
                clickLike.invoke(movie)
                imageLike.visibility = View.VISIBLE
                imageDislike.visibility = View.GONE
            }
        }

    }

}