package com.example.beerrecruitmentexercise.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private final int visibleThreshold = 10;
    private int currentPage = 1;
    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private final int startingPageIndex = 1;
    private final RecyclerView.LayoutManager mLayoutManager;

    protected EndlessRecyclerViewScrollListener(final LinearLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull final RecyclerView view, final int dx, final int dy) {
        final int lastVisibleItemPosition;
        final int totalItemCount = mLayoutManager.getItemCount();

        lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager)
            .findLastVisibleItemPosition();

        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex;
            previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                loading = true;
            }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, view);
            loading = true;
        }
    }

    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView recyclerView);
}