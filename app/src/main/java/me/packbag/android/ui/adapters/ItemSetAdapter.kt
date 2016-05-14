package me.packbag.android.ui.adapters

import android.view.View
import android.view.ViewGroup
import com.github.naixx.BindingBaseAdapter
import com.github.naixx.BindingBaseViewHolder
import me.packbag.android.R
import me.packbag.android.databinding.ListItemSetBinding
import me.packbag.android.db.model.ItemSet
import me.packbag.android.util.layoutInflater

/**
 * Created by astra on 17.07.2015.
 */
class ItemSetAdapter(private val listener: BindingBaseAdapter.InteractionListener<ItemSet>) :
        BindingBaseAdapter<ItemSet, ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.context.layoutInflater.inflate(R.layout.list_item_set, parent, false), listener)
    }
}

class ViewHolder(itemView: View, private val listener: BindingBaseAdapter.InteractionListener<ItemSet>) : BindingBaseViewHolder<ItemSet, ListItemSetBinding>(itemView) {

    override fun bind(binding: ListItemSetBinding, item: ItemSet, position: Int) {
        binding.setItemSet(item)
        itemView.setOnClickListener { listener.onClick(item) }
    }
}
