package me.packbag.android.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Collections;
import java.util.List;

import me.packbag.android.db.api.Db;

import static me.packbag.android.util.Empties.emptyIfNull;

/**
 * Created by astra on 13.07.2015.
 */
@Table(databaseName = Db.NAME)
public class ItemSet extends BaseModel implements WithId{

    @Column
    @PrimaryKey
    @JsonProperty("id")
    long id;

    @JsonProperty("name")
    @Column
    String name;

    @Column String item_ids;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("item_ids")
    public void setItemIds(List<Integer> ids) {

        item_ids = Joiner.on(",").join(emptyIfNull(ids)).trim();
    }

    public List<Item> getItems() {
        Integer[] ids = FluentIterable.from(Splitter.on(",").split(item_ids)).transform(Integer::valueOf).toArray(Integer.class);
        if (ids.length > 0) {
            Integer first = ids[0];
            Object[] o = new Object[ids.length];
            System.arraycopy(ids, 1, o, 0, ids.length - 1);
            return new Select().from(Item.class).where(Condition.column(Item_Table.ID).in(first, o)).queryList();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("name", name).add("item_ids", item_ids).toString();
    }
}
