package com.github.naixx;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T extends WithId, VH extends com.github.naixx.BaseViewHolder<T>> extends RecyclerView.Adapter<VH> {

    public interface InteractionListener<T> {

        void onClick(T item);
    }

    protected List<T> items = new ArrayList<>();

    public BaseAdapter() {
        setHasStableIds(true);
    }

    public void swapItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.bind(items.get(position), position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
