package me.packbag.android.ui.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import me.packbag.android.R;
import me.packbag.android.db.model.ItemSet;

@EActivity(R.layout.activity_bag_packed)
public class BagPackedActivity extends AppCompatActivity {

    @Extra    ItemSet     itemSet;
    @ViewById TextView    text;
    @ViewById ShareButton shareButton;

    @AfterViews
    void afterViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String text = String.format("Рюкзак для похода '%s' собран.", itemSet.getName());
        this.text.setText(text);

        ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse(
                "https://www.facebook.com/hike.and.me")).setContentTitle("Я только что собрал рюкзак!").build();
        shareButton.setShareContent(content);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
