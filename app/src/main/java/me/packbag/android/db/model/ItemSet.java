package me.packbag.android.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.naixx.WithId;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import net.tribe7.common.base.Objects;

import me.packbag.android.db.api.Db;

/**
 * Created by astra on 13.07.2015.
 */
@Table(databaseName = Db.NAME)
@ModelContainer
public class ItemSet extends BaseModel implements WithId, Parcelable {

    @Column
    @PrimaryKey
    @JsonProperty("id")
    long id;

    @JsonProperty("name")
    @Column
    String name;

    @Column String item_ids;

    Long[] ids;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @JsonProperty("item_ids")
    public void setItemIds(Long[] ids) {
        this.ids = ids;
    }

    public Long[] getServerIds() {
        return ids;
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
        return Objects.toStringHelper(this).add("id", id).add("name", name).add("item_ids", item_ids).toString();
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
