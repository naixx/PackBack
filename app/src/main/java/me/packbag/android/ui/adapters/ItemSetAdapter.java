package me.packbag.android.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.naixx.BindingBaseAdapter;
import com.github.naixx.BindingBaseViewHolder;

import me.packbag.android.R;
import me.packbag.android.databinding.ListItemSetBinding;
import me.packbag.android.db.model.ItemSet;

/**
 * Created by astra on 17.07.2015.
 */
public class ItemSetAdapter extends BindingBaseAdapter<ItemSet, ListItemSetBinding, ItemSetAdapter.ViewHolder> {

    class ViewHolder extends BindingBaseViewHolder<ItemSet, ListItemSetBinding> {
        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(ListItemSetBinding binding, ItemSet item, int position) {
            binding.setItemSet(item);
            itemView.setOnClickListener(v -> listener.onClick(item));
        }
    }

    private final InteractionListener<ItemSet> listener;

    public ItemSetAdapter(InteractionListener<ItemSet> listener) {
        super();
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_set, parent, false));
    }
}
