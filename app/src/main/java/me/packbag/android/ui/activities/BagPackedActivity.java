package me.packbag.android.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareButton;
import com.github.naixx.L;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import me.packbag.android.App;
import me.packbag.android.R;
import me.packbag.android.analytics.Analytics;
import me.packbag.android.db.model.ItemSet;
import me.packbag.android.ui.utils.ShareHelper;

@EActivity(R.layout.activity_bag_packed)
public class BagPackedActivity extends AppCompatActivity {

    @Extra ItemSet itemSet;

    @ViewById               TextView    text;
    @ViewById(R.id.shareFb) ShareButton shareButton;

    @Inject Analytics analytics;
    private CallbackManager callbackManager;

    @AfterViews
    void afterViews() {
        App.get(this).component().inject(this);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String text = String.format("Рюкзак для похода '%s' собран.", itemSet.getName());
        this.text.setText(text);

        shareButton.setShareContent(ShareHelper.prepareFbShareContent(this));
        callbackManager = CallbackManager.Factory.create();
        shareButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                analytics.logShare(itemSet, "facebook");
            }

            @Override
            public void onCancel() {
                analytics.logShareCancelled(itemSet, "facebook");
            }

            @Override
            public void onError(FacebookException error) {
                L.e(error);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        ShareHelper.onActivityResultForCommon(this, requestCode, resultCode, data, itemSet);
    }
}
