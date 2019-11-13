package com.example.beerrecruitmentexercise.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beerrecruitmentexercise.R;
import com.example.beerrecruitmentexercise.dto.BeerDTO;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class BeersAdapter extends RecyclerView.Adapter<BeersAdapter.BeersViewHolder> {

    private final ArrayList<BeerDTO> beersList;

    /**
     * Beers viewholder, view to show item in recyclerview
     */
    static class BeersViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvTagline, tvDescription, tvABV;
        SimpleDraweeView imageView;

        BeersViewHolder(final View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvTagline = view.findViewById(R.id.tvTagline);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvABV = view.findViewById(R.id.tvABV);
            imageView = view.findViewById(R.id.imageView);
        }
    }

    /**
     * Constructor for beers adapter
     *
     * @param beers the list of beers to display
     */
    public BeersAdapter(final ArrayList<BeerDTO> beers) {
        this.beersList = beers;
    }

    @NonNull
    @Override
    public BeersViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beer_item, parent, false);
        return new BeersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeersViewHolder holder, int position) {

        holder.tvName.setText(beersList.get(position).getName());
        holder.tvTagline.setText(beersList.get(position).getTagline());
        holder.tvDescription.setText(beersList.get(position).getDescription());
        holder.tvABV.setText(new StringBuilder().append("ABV: ").append(beersList.get(position).getAbv()).append(" %").toString());

        holder.imageView.setImageURI(beersList.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return beersList.size();
    }
}
