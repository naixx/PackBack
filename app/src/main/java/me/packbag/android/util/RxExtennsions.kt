package me.packbag.android.util

import rx.Observable
import rx.Single

public fun<T> Observable<List<T>>.flatten(): Observable<T> = this.flatMap { Observable.from(it) }
public fun<T> Single<List<T>>.flatten(): Observable<T> = this.flatMapObservable { Observable.from(it) }
public fun<T> T.just(): Observable<T> = Observable.just(this)
