package me.packbag.android.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.github.naixx.BaseAdapter;
import com.github.naixx.BaseViewHolder;
import com.github.naixx.Bus;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import butterknife.Bind;
import me.packbag.android.R;
import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemCategory;
import me.packbag.android.ui.events.TakenEvent;
import me.packbag.android.ui.events.UselessEvent;

/**
 * Created by astra on 17.07.2015.
 */
public class ItemsAdapter extends BaseAdapter<Item, ItemsAdapter.ViewHolder> implements StickyRecyclerHeadersAdapter<ItemsAdapter.HeaderViewHolder> {

    class ViewHolder extends BaseViewHolder<Item> {

        @Bind(R.id.name)    TextView name;
        @Bind(R.id.takeBtn) View     takeBtn;
        @Bind(R.id.moreBtn) View     moreBtn;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(Item item) {
            name.setText(item.getName());
            takeBtn.setOnClickListener(v -> Bus.post(new TakenEvent(item)));
            moreBtn.setOnClickListener(v -> {
                showPopup(v, item);
            });
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

    class HeaderViewHolder extends com.github.naixx.BaseViewHolder<String> {

        @Bind(android.R.id.text1) TextView name;

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(String item) {
            name.setText(item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public long getHeaderId(int i) {
        return items.get(i).getCategory().getId() + 2; //we add 2 because we have default category with id -1
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder headerViewHolder, int i) {
        ItemCategory cat = items.get(i).getCategory();
        if (cat.getId() == -1) {
            headerViewHolder.bind(headerViewHolder.itemView.getContext().getString(R.string.list_item_category_user));
        } else {
            headerViewHolder.bind(cat.getName());
        }
    }
}