package me.packbag.android.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import me.packbag.android.R;
import me.packbag.android.db.model.Item;
import me.packbag.android.ui.utils.BaseAdapter;
import me.packbag.android.ui.utils.BaseViewHolder;

/**
 * Created by astra on 17.07.2015.
 */
public class ItemListAdapter extends BaseAdapter<Item, ItemListAdapter.ViewHolder> {

    class ViewHolder extends BaseViewHolder<Item> {

        @Bind(R.id.name)  TextView  name;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(Item item) {
            name.setText(item.getName());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }
}
