package me.packbag.android.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.naixx.BaseAdapter;
import com.github.naixx.Bus;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import butterknife.Bind;
import me.packbag.android.R;
import me.packbag.android.db.model.Item;
import me.packbag.android.ui.events.TakenEvent;
import me.packbag.android.ui.events.UselessEvent;

/**
 * Created by astra on 17.07.2015.
 */
public class ItemListAdapter extends BaseAdapter<Item, ItemListAdapter.ViewHolder> implements StickyRecyclerHeadersAdapter<ItemListAdapter.HeaderViewHolder> {

    class ViewHolder extends com.github.naixx.BaseViewHolder<Item> {

        @Bind(R.id.name)       TextView name;
        @Bind(R.id.takeBtn)    View     takeBtn;
        @Bind(R.id.uselessBtn) View     uselessBtn;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(Item item) {
            name.setText(item.getName());
            takeBtn.setOnClickListener(v -> Bus.post(new TakenEvent(item)));
            uselessBtn.setOnClickListener(v -> Bus.post(new UselessEvent(item)));
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
        return items.get(i).getCategory().getId();
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_header, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder headerViewHolder, int i) {
        headerViewHolder.bind(items.get(i).getCategory().getName());
    }
}
