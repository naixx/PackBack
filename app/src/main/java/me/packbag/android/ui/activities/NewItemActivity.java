package me.packbag.android.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.github.naixx.BaseAdapter;
import com.github.naixx.L;
import com.jakewharton.rxbinding.widget.RxTextView;

import net.tribe7.common.collect.Iterables;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import me.packbag.android.App;
import me.packbag.android.R;
import me.packbag.android.db.api.Dao;
import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemCategory;
import me.packbag.android.db.model.ItemInSet;
import me.packbag.android.db.model.ItemSet;
import me.packbag.android.ui.adapters.ItemsAutocompleteAdapter;
import rx.Observable;

@EActivity(R.layout.activity_new_item)
public class NewItemActivity extends AppCompatActivity implements BaseAdapter.InteractionListener<Item> {

    private static class ItemCategoryView {

        final ItemCategory category;

        ItemCategoryView(ItemCategory category) {this.category = category;}

        @Override
        public String toString() {
            return category.getName();
        }
    }

    @Extra                       ItemSet          itemSet;
    @ViewById(R.id.name)         EditText         name;
    @ViewById(R.id.spinner)      AppCompatSpinner spinner;
    @ViewById(R.id.recyclerView) RecyclerView     recyclerView;
    @ViewById(R.id.addBtn)       View             addButton;

    private ArrayAdapter<ItemCategoryView> spinnerAdapter;

    @AfterViews
    void afterViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Dao dao = App.get(this).component().dao();
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        dao.categories(itemSet).flatMapObservable(Observable::from).doOnNext(itemCategory -> {
            if (itemCategory.getId() == ItemCategory.USER) {
                itemCategory.setName(getString(R.string.list_item_category_user));
            }
        }).map(ItemCategoryView::new).subscribe(spinnerAdapter::add);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemsAutocompleteAdapter autocompleteAdapter = new ItemsAutocompleteAdapter(this);
        recyclerView.setAdapter(autocompleteAdapter);

        Observable<Item> items = dao.itemsInSets(itemSet)
                .flatMapObservable(Observable::from)
                .map(ItemInSet::getItem)
                .toList()
                .flatMap(itemInSets -> {
                    return dao.itemsAll()
                            .flatMapObservable(Observable::from)
                            .filter(item -> notContains(itemInSets, item));
                })
                .cache();
        Observable<CharSequence> textEvents = RxTextView.textChanges(name)
                .map(it -> it.toString().toLowerCase().trim());
        textEvents.flatMap(text -> items.filter(item -> item.getName().toLowerCase().contains(text)).toList())
                .subscribe(autocompleteAdapter::swapItems);
        textEvents.filter(it -> it.length() > 0).subscribe(it -> name.setError(null));
        textEvents.map(CharSequence::toString).subscribe(autocompleteAdapter::setHighlightQuery);
    }

    private static boolean notContains(List<Item> items, Item item) {
        return !Iterables.tryFind(items, input -> input.getId() == item.getId()).isPresent();
    }

    @Click(R.id.addBtn)
    void onAdd() {
        if (TextUtils.isEmpty(name.getText())) {
            name.setError(getString(R.string.new_item_name_too_short));
            return;
        }
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

    @Override
    public void onClick(Item item) {
        ItemInSet itemInSet = new ItemInSet();
        itemInSet.setItemSet(itemSet.getId());
        itemInSet.setItem(item.getId());
        itemInSet.insert();
        L.v(itemInSet);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
