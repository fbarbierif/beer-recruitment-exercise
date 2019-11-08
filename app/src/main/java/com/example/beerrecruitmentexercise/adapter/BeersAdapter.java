package com.example.beerrecruitmentexercise.adapter;

import android.content.Context;
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
    private Context context;

    static class BeersViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        SimpleDraweeView imageView;

        BeersViewHolder(final View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            imageView = view.findViewById(R.id.imageView);
        }
    }

    public BeersAdapter(final ArrayList<BeerDTO> beers, final Context context) {
        this.beersList = beers;
        this.context = context;
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
        holder.imageView.setImageURI(beersList.get(position).getImageUrl());
    }

    @Override
    public int getItemCount() {
        return beersList.size();
    }
}
