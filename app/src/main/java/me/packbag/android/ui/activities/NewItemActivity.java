package me.packbag.android.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.github.naixx.L;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import me.packbag.android.R;
import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemCategory;
import me.packbag.android.db.model.ItemInSet;
import me.packbag.android.db.model.ItemSet;

@EActivity(R.layout.activity_new_item)
@OptionsMenu(R.menu.menu_new_item)
public class NewItemActivity extends AppCompatActivity {

    @Extra               ItemSet  itemSet;
    @ViewById(R.id.name) EditText name;

    @OptionsItem(R.id.action_add)
    void onAdd() {
        Item item = new Item();
        item.setName(String.valueOf(name.getText()));
        item.setCategory(ItemCategory.USER);

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
