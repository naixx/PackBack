package me.packbag.android.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import me.packbag.android.R;
import me.packbag.android.db.model.ItemSet;

@EActivity(R.layout.activity_bag_packed)
public class BagPackedActivity extends AppCompatActivity {

    @Extra    ItemSet  itemSet;
    @ViewById TextView text;

    @AfterViews
    void afterViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String text = String.format("Рюкзак для похода '%s' собран.", itemSet.getName());
        this.text.setText(text);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
