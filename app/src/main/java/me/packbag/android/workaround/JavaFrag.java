package me.packbag.android.workaround;

import android.app.Fragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

import me.packbag.android.R;

@EFragment
@OptionsMenu(R.menu.activity_item_list)
public class JavaFrag extends Fragment {
    @FragmentArg String arg;

    @OptionsItem(R.id.action_clear_items)
    void onItem() {
    }

    @OnActivityResult(0)
    void onResult() {
    }
}
