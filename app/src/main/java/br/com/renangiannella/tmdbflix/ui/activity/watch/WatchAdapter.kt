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
                   val clickWatchMovie: ((movie: WatchMovie) -> Unit),
                   val clickDisWatchMovie: ((movie: WatchMovie) -> Unit)):
    RecyclerView.Adapter<WatchAdapter.MovieAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchAdapter.MovieAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return WatchAdapter.MovieAdapterViewHolder(itemView, clickWatchMovie, clickDisWatchMovie)
    }

    override fun getItemCount() = watch.count()

    override fun onBindViewHolder(holder: WatchAdapter.MovieAdapterViewHolder, position: Int) {
        holder.bind(watch[position])
    }

    fun getCurrentMovie(position: Int) = watch[position]

    class MovieAdapterViewHolder(itemView: View,
                                 val clickWatchMovie: ((movie: WatchMovie)-> Unit),
                                 val clickDisWatchMovie:((movie: WatchMovie)-> Unit)): RecyclerView.ViewHolder(itemView) {

        private val title = itemView.textNameMovie
        private val vote = itemView.textVoteAverage
        private val releaseDate = itemView.textReleaseDate
        private val posterMovie = itemView.imageViewMovie
        private val imageWatch = itemView.img_towatch
        private val imageDiswatch = itemView.img_diswatch

        private val picasso = Picasso.get()

        fun bind(movie: WatchMovie){
            title.text = movie.original_title
            vote.text = movie.vote_average
            releaseDate.text = movie.release_date

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
        }

    }

}