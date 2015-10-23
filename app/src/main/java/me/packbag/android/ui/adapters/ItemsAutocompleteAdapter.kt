package me.packbag.android.ui.adapters

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.Bind
import com.github.naixx.BaseAdapter
import com.github.naixx.BaseViewHolder
import me.packbag.android.R
import me.packbag.android.db.model.Item
import java.util.regex.Pattern

/**
 * Created by astra on 17.07.2015.
 */
class ItemsAutocompleteAdapter(private val listener: BaseAdapter.InteractionListener<Item>) :
        BaseAdapter<Item, ItemsAutocompleteAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : BaseViewHolder<Item>(itemView) {

        @Bind(R.id.name) lateinit var name: TextView

        override fun bind(item: Item, position: Int) {
            name.text = highlight(item.name)
            itemView.setOnClickListener { v -> listener.onClick(item) }
        }

        private fun highlight(text: String): CharSequence {
            val highlightedText = SpannableStringBuilder(text)
            val pattern = Pattern.compile(Pattern.quote(highlightQuery), Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(text)
            while (matcher.find()) {
                val start = matcher.start()
                val end = matcher.end()
                val span = BackgroundColorSpan(Color.YELLOW)
                highlightedText.setSpan(span, start, end, 0)
            }
            return highlightedText
        }
    }

    var highlightQuery: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_autocomplete, parent, false))
    }
}
