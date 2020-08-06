package br.com.renangiannella.tmdbflix.ui.activity.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ViewUtils
import androidx.recyclerview.widget.RecyclerView
import br.com.renangiannella.tmdbflix.BuildConfig
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*

class FavoriteAdapter(val favorites: List<FavoriteMovie>, val clickMovie: ((movie: FavoriteMovie)-> Unit), val clickDislike:((movie: FavoriteMovie)-> Unit)):
    RecyclerView.Adapter<FavoriteAdapter.MovieAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.MovieAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieAdapterViewHolder(itemView, clickMovie, clickDislike)
    }

    override fun getItemCount() = favorites.count()

    override fun onBindViewHolder(holder: FavoriteAdapter.MovieAdapterViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    fun getCurrentMovie(position: Int) = favorites[position]

    class MovieAdapterViewHolder(itemView: View,
                                 val clickMovie: ((movie: FavoriteMovie)-> Unit),
                                 val clickDislike:((movie: FavoriteMovie)-> Unit)): RecyclerView.ViewHolder(itemView) {

        private val title = itemView.textNameMovie
        private val vote = itemView.textVoteAverage
        private val releaseDate = itemView.textReleaseDate
        private val posterMovie = itemView.imageViewMovie
        private val imageLike = itemView.img_favorite_red
        private val imageDislike = itemView.img_favorite_movie

        private val picasso = Picasso.get()

        fun bind(movie: FavoriteMovie){
            title.text = movie.original_title
            vote.text = movie.vote_average
            releaseDate.text = movie.release_date

            movie.poster_path.let {
                picasso.load("""${BuildConfig.BASE_URL_IMAGE}${movie.poster_path}""").into(posterMovie)
            }

            imageLike.visibility = View.VISIBLE
            imageDislike.visibility = View.GONE

            imageLike.setOnClickListener{
                clickDislike.invoke(movie)
                imageDislike.visibility = View.GONE
                imageLike.visibility = View.VISIBLE
            }
        }

    }


}