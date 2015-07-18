package me.packbag.android.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
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
public class ItemSet extends BaseModel implements WithId, Parcelable {

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemSet)) {
            return false;
        }
        ItemSet itemSet = (ItemSet) o;
        return Objects.equal(id, itemSet.id) && Objects.equal(name, itemSet.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("name", name).add("item_ids", item_ids).toString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.item_ids);
    }

    public ItemSet() {}

    protected ItemSet(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.item_ids = in.readString();
    }

    public static final Parcelable.Creator<ItemSet> CREATOR = new Parcelable.Creator<ItemSet>() {
        public ItemSet createFromParcel(Parcel source) {return new ItemSet(source);}

        public ItemSet[] newArray(int size) {return new ItemSet[size];}
    };
}
