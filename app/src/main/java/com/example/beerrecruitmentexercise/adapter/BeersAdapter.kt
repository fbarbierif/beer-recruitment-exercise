package com.example.beerrecruitmentexercise.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.beerrecruitmentexercise.R
import com.example.beerrecruitmentexercise.dto.BeerDTO
import com.facebook.drawee.view.SimpleDraweeView

import java.util.ArrayList

class BeersAdapter
/**
 * Constructor for beers adapter
 *
 * @param beers the list of beers to display
 */
(private val beersList: ArrayList<BeerDTO>) : RecyclerView.Adapter<BeersAdapter.BeersViewHolder>() {

    /**
     * Beers viewholder, view to show item in recyclerview
     */
    class BeersViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvName: TextView
        var tvTagline: TextView
        var tvDescription: TextView
        var tvABV: TextView
        var imageView: SimpleDraweeView

        init {
            tvName = view.findViewById(R.id.tvName)
            tvTagline = view.findViewById(R.id.tvTagline)
            tvDescription = view.findViewById(R.id.tvDescription)
            tvABV = view.findViewById(R.id.tvABV)
            imageView = view.findViewById(R.id.imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeersViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.beer_item, parent, false)
        return BeersViewHolder(view)
    }

    override fun onBindViewHolder(holder: BeersViewHolder, position: Int) {

        holder.tvName.text = beersList[position].name
        holder.tvTagline.text = beersList[position].tagline
        holder.tvDescription.text = beersList[position].description
        holder.tvABV.text = StringBuilder().append("ABV: ").append(beersList[position].abv).append(" %").toString()

        holder.imageView.setImageURI(beersList[position].imageUrl)
    }

    override fun getItemCount(): Int {
        return beersList.size
    }
}
