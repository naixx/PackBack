package me.packbag.android.ui.events;

import me.packbag.android.db.model.Item;

/**
 * Created by astra on 25.08.2015.
 */
public class TakenEvent {

    public final Item item;

    public TakenEvent(Item item) {this.item = item;}
}
