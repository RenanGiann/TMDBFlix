package br.com.renangiannella.tmdbflix.ui.activity.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.renangiannella.tmdbflix.BuildConfig
import br.com.renangiannella.tmdbflix.R
import br.com.renangiannella.tmdbflix.data.db.modeldb.FavoriteMovie
import br.com.renangiannella.tmdbflix.data.model.result.MovieResult
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_profile.view.*

class ProfileAdapter(val images: List<Int>,
                     val click: ((image: Int) -> Unit)):
    RecyclerView.Adapter<ProfileAdapter.ProfileAdapterViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_profile, parent, false)
        return ProfileAdapterViewHolder(itemView, click)
    }

    override fun getItemCount() = images.count()

    override fun onBindViewHolder(holder: ProfileAdapterViewHolder, position: Int) {
        holder.bind(images[position])
    }


    class ProfileAdapterViewHolder(itemView: View, val click: ((image: Int) -> Unit)):
        RecyclerView.ViewHolder(itemView) {

        private val imageProfile = itemView.imageProfile
        private val picasso = Picasso.get()

        fun bind(image: Int){

            picasso.load(image).into(imageProfile)

            itemView.setOnClickListener {
                click.invoke(image)
            }
        }

    }

}