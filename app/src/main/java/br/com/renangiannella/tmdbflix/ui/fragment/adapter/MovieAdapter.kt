package br.com.renangiannella.tmdbflix.ui.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.renangiannella.tmdbflix.BuildConfig
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.model.result.MovieResult
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter(val movies: List<MovieResult>,
                   val favorites: List<FavoriteMovie>,
                   val clickMovie: ((movie: MovieResult)-> Unit),
                   val clickLike: ((movie: MovieResult)-> Unit),
                   val clickDislike:((movie: MovieResult)-> Unit)):
    RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.MovieAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieAdapterViewHolder(itemView, clickMovie, clickLike, clickDislike)
    }

    override fun getItemCount() = movies.count()

    override fun onBindViewHolder(holder: MovieAdapter.MovieAdapterViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(movies[position], favorites)
    }

    class MovieAdapterViewHolder(itemView: View,
                                 val clickMovie: ((movie: MovieResult)-> Unit),
                                 val clickLike: ((movie: MovieResult)-> Unit),
                                 val clickDislike:((movie: MovieResult)-> Unit)): RecyclerView.ViewHolder(itemView) {

        private val title = itemView.textNameMovie
        private val vote = itemView.textVoteAverage
        private val releaseDate = itemView.textReleaseDate
        private val posterMovie = itemView.imageViewMovie
        private val imageLike = itemView.img_favorite_red
        private val imageDislike = itemView.img_favorite_movie

        private val picasso = Picasso.get()

        fun bind(movie: MovieResult, favorite: List<FavoriteMovie>){
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