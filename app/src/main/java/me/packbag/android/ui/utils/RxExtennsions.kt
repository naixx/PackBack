package me.packbag.android.ui.utils

import rx.Observable
import rx.Single

public fun<T> Observable<List<T>>.flatten(): Observable<T> = this.flatMap { Observable.from(it) }
public fun<T> Single<List<T>>.flatten(): Observable<T> = this.flatMapObservable { Observable.from(it) }
