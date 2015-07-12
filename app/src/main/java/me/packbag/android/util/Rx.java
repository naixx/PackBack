package me.packbag.android.util;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by astra on 22.05.2015.
 */
public class Rx {

    public static <T> Observable.Transformer<T, T> async2ui() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
