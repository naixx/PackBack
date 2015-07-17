package me.packbag.android.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import me.packbag.android.R;
import me.packbag.android.db.model.ItemSet;
import me.packbag.android.ui.utils.BaseAdapter;
import me.packbag.android.ui.utils.BaseViewHolder;

/**
 * Created by astra on 17.07.2015.
 */
public class ItemSetAdapter extends BaseAdapter<ItemSet, ItemSetAdapter.ViewHolder> {

    class ViewHolder extends BaseViewHolder<ItemSet> {

        @Bind(R.id.name) TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(ItemSet item) {
            name.setText(item.getName());
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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_item_set, null));
    }
}
