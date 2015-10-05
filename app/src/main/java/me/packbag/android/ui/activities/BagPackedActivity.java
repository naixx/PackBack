package me.packbag.android.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.share.widget.ShareButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import me.packbag.android.R;
import me.packbag.android.db.model.ItemSet;
import me.packbag.android.ui.utils.ShareHelper;

@EActivity(R.layout.activity_bag_packed)
public class BagPackedActivity extends AppCompatActivity {

    @Extra ItemSet itemSet;

    @ViewById               TextView    text;
    @ViewById(R.id.shareFb) ShareButton shareButton;

    @AfterViews
    void afterViews() {
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String text = String.format("Рюкзак для похода '%s' собран.", itemSet.getName());
        this.text.setText(text);

        shareButton.setShareContent(ShareHelper.prepareFbShareContent(this));
    }

    @Click(R.id.share)
    void onClick() {
        ShareHelper.shareCommon(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
