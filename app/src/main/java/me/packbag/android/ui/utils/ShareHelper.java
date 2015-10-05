package me.packbag.android.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import java.util.List;

/**
 * Created by astra on 03.10.2015.
 */
public class ShareHelper {

    private void initShareIntent(final Context context, String targetPackage, Uri imageUri) {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/jpeg");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(targetPackage) ||
                    info.activityInfo.name.toLowerCase().contains(targetPackage)) {
                    if (info.activityInfo.name.toLowerCase().contains("messenger")) {
                        continue;
                    }
                    Log.i("tag", "_info_name_" + info.activityInfo.name);
                    share.putExtra(Intent.EXTRA_SUBJECT, "subject");
                    share.putExtra(Intent.EXTRA_TEXT, "your text");
                    share.putExtra(Intent.EXTRA_STREAM, imageUri);
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found) {
                return;
            }

            context.startActivity(Intent.createChooser(share, "Select"));
        }
    }
}
