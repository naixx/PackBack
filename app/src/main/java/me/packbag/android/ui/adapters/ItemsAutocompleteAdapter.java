package me.packbag.android.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.github.naixx.BaseAdapter;
import com.github.naixx.BaseViewHolder;
import com.github.naixx.Bus;

import butterknife.Bind;
import me.packbag.android.R;
import me.packbag.android.db.model.Item;
import me.packbag.android.ui.events.UselessEvent;

/**
 * Created by astra on 17.07.2015.
 */
public class ItemsAutocompleteAdapter extends BaseAdapter<Item, ItemsAutocompleteAdapter.ViewHolder> {

    class ViewHolder extends BaseViewHolder<Item> {

        @Bind(R.id.name) TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(Item item, int position) {
            name.setText(item.getName());
            itemView.setOnClickListener(v -> listener.onClick(item));
        }

        private void showPopup(View v, Item item) {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.inflate(R.menu.item_more_actions);
            popup.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.action_useless:
                        Bus.post(new UselessEvent(item));
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        }
    }

    final InteractionListener<Item> listener;

    public ItemsAutocompleteAdapter(InteractionListener<Item> listener) {this.listener = listener;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_autocomplete, parent, false));
    }
}
