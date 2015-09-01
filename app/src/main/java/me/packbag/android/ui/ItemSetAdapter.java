package me.packbag.android.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.naixx.BaseAdapter;
import com.github.naixx.BaseViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.Bind;
import me.packbag.android.App;
import me.packbag.android.R;
import me.packbag.android.db.model.ItemSet;
import me.packbag.android.network.Splashable;
import me.packbag.android.network.model.SplashableImage;
import me.packbag.android.util.Utils;
import me.packbag.android.util.timber.L;
import rx.Observable;

import static com.github.naixx.Rx.async2ui;

/**
 * Created by astra on 17.07.2015.
 */
public class ItemSetAdapter extends BaseAdapter<ItemSet, ItemSetAdapter.ViewHolder> {

    class ViewHolder extends BaseViewHolder<ItemSet> {

        @Bind(R.id.name)  TextView  name;
        @Bind(R.id.image) ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(ItemSet item) {
            name.setText(item.getName());
            itemView.setOnClickListener(v -> listener.onClick(item));
            String tag = "";
            String name = item.getName().toLowerCase();
            if (name.contains("пеший") || name.contains("hik") || name.contains("mount") || name.contains("горн")) {
                tag = "mountains";
            }
            if (name.contains("водный") || name.contains("water") || name.contains("raft")) {
                tag = "water";
            }
            if (name.contains("аптеч")) {
                tag = "medical";
            }
            Context context = itemView.getContext();

            if (meta.get(item.getId()) != null && meta.get(item.getId()).url != null) {
                loadImage(context, meta.get(item.getId()));
            } else {
                Splashable splashable = App.get(context).component().splashable();
                splashable.search(tag).map(imageList -> imageList.images).flatMap(splashableImages -> {
                    int size = splashableImages.size();
                    if (size == 0) {
                        return Observable.<String>empty();
                    }
                    return Observable.from(splashableImages)
                            .elementAt(new Random().nextInt(size))
                            .onErrorResumeNext(Observable.<SplashableImage>empty())
                            .map((SplashableImage o) -> o.url);
                }).compose(async2ui()).doOnNext(L::i).subscribe(url -> {
                    MetaHolder value = new MetaHolder(url);
                    meta.put(item.getId(), value);
                    loadImage(context, value);
                });
            }
        }

        private void loadImage(Context context, MetaHolder meta) {
            Glide.with(context).load(meta.url).asBitmap().centerCrop().into(new BitmapImageViewTarget(image) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    super.onResourceReady(resource, glideAnimation);
                    if (meta.swatch != null) {
                        setSwatch(meta.swatch);
                    }
                    Palette.from(resource).generate(palette -> {

                        Palette.Swatch s = palette.getVibrantSwatch();
                        if (s == null) {
                            s = palette.getDarkVibrantSwatch();
                        }
                        if (s == null) {
                            s = palette.getLightVibrantSwatch();
                        }
                        if (s == null) {
                            s = palette.getMutedSwatch();
                        }

                        if (s != null) {
                            meta.swatch = s;
                            setSwatch(s);
                        }
                    });
                }
            });
        }

        private void setSwatch(Palette.Swatch s) {
            name.setTextColor(s.getBodyTextColor());
            Utils.animateViewTextColor(name, name.getCurrentTextColor(), s.getBodyTextColor());
        }
    }

    private final InteractionListener<ItemSet> listener;

    private class MetaHolder {

        public String         url;
        public Palette.Swatch swatch;

        public MetaHolder(String url) {
            this.url = url;
        }
    }

    private Map<Long, MetaHolder> meta = new HashMap<>();

    public ItemSetAdapter(InteractionListener<ItemSet> listener) {
        super();
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_set, parent, false));
    }
}
