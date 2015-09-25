package me.packbag.android.ui.adapters;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.naixx.BaseAdapter;
import com.github.naixx.BaseViewHolder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import me.packbag.android.R;
import me.packbag.android.db.model.Item;

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
            name.setText(hightlight(item.getName()));
            itemView.setOnClickListener(v -> listener.onClick(item));
        }

        private CharSequence hightlight(String text) {
            SpannableStringBuilder highlightedText = new SpannableStringBuilder(text);
            Pattern pattern = Pattern.compile(Pattern.quote(highlightQuery), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                BackgroundColorSpan span = new BackgroundColorSpan(Color.YELLOW);
                highlightedText.setSpan(span, start, end, 0);
            }
            return highlightedText;
        }
    }

    final InteractionListener<Item> listener;

    private String highlightQuery;

    public ItemsAutocompleteAdapter(InteractionListener<Item> listener) {this.listener = listener;}

    public void setHighlightQuery(String highlightQuery) {
        this.highlightQuery = highlightQuery;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_autocomplete, parent, false));
    }
}
