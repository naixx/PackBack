package me.packbag.android.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.github.naixx.L;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import me.packbag.android.App;
import me.packbag.android.R;
import me.packbag.android.db.api.Dao;
import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemCategory;
import me.packbag.android.db.model.ItemInSet;
import me.packbag.android.db.model.ItemSet;
import rx.Observable;

@EActivity(R.layout.activity_new_item)
@OptionsMenu(R.menu.menu_new_item)
public class NewItemActivity extends AppCompatActivity {

    private ArrayAdapter<ItemCategoryView> spinnerAdapter;

    private static class ItemCategoryView {

        final ItemCategory category;

        ItemCategoryView(ItemCategory category) {this.category = category;}

        @Override
        public String toString() {
            return category.getName();
        }
    }

    @Extra                  ItemSet          itemSet;
    @ViewById(R.id.name)    EditText         name;
    @ViewById(R.id.spinner) AppCompatSpinner spinner;

    @AfterViews
    void afterViews() {
        Dao dao = App.get(this).component().dao();
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        dao.categories(itemSet).flatMapObservable(Observable::from).map(ItemCategoryView::new).subscribe(spinnerAdapter::add);
    }

    @OptionsItem(R.id.action_add)
    void onAdd() {
        Item item = new Item();
        item.setName(String.valueOf(name.getText()));
        ItemCategoryView selectedItem = (ItemCategoryView) spinner.getSelectedItem();
        item.setCategory(selectedItem.category.getId());

        item.insert();
        ItemInSet itemInSet = new ItemInSet();
        itemInSet.setItemSet(itemSet.getId());
        itemInSet.setItem(item.getId());
        itemInSet.insert();
        L.v(itemInSet);
        setResult(RESULT_OK);
        finish();
    }
}
