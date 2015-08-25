package me.packbag.android.ui;

import java.util.List;

import me.packbag.android.db.model.Item;

/**
 * Created by astra on 25.08.2015.
 */
interface ItemProvider {

    enum Type {CURRENT, TAKEN, USELESS}

    List<Item> getItems(Type type);
}
