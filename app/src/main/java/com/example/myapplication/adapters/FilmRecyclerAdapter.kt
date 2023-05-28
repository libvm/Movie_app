package com.example.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFilmlistItemBinding
import com.example.myapplication.interfaces.RecycleViewOnClickListener
import com.example.myapplication.models.FilmModel
import com.squareup.picasso.Picasso

class FilmRecyclerAdapter(private val clickListener: RecycleViewOnClickListener) : ListAdapter<FilmModel, FilmRecyclerAdapter.ViewHolder>(Comparator()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = FragmentFilmlistItemBinding.bind(view)
        fun bind(item: FilmModel) = with(binding){
            filmName.text = item.name
            val genreAndYear = item.genres[0] + " " + "(" + item.year + ")"
            filmGenreYear.text = genreAndYear
            filmLogo.visibility = View.VISIBLE
            Picasso.get().load(item.posterUrlPreview).into(filmLogo)
            vector.setImageResource(item.favourite)
        }
    }

    class Comparator : DiffUtil.ItemCallback<FilmModel>(){
        override fun areItemsTheSame(oldItem: FilmModel, newItem: FilmModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FilmModel, newItem: FilmModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_filmlist_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            clickListener.onItemClick(holder.bindingAdapterPosition)
        }
        holder.itemView.setOnLongClickListener {
            clickListener.onItemLongClick(holder.bindingAdapterPosition)
            true
        }
        holder.bind(getItem(position))
    }
}

