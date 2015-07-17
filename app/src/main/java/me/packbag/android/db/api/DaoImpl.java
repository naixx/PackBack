package me.packbag.android.db.api;

import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

import me.packbag.android.db.model.Item;
import me.packbag.android.db.model.ItemSet;
import rx.Observable;

/**
 * Created by astra on 22.05.2015.
 */
public class DaoImpl implements Dao {

    @Override
    public Observable<Item> findAllProducts() {
        return null;
    }

    @Override
    public Observable<List<ItemSet>> itemSets() {
        return Observable.defer(() -> Observable.just(new Select().from(ItemSet.class).queryList()));
    }

//	private final DatabaseDao dao;
//
//	public DaoImpl(DatabaseDao dao) {
//		this.dao = dao;
//	}
//
//	@Override
//	public Observable<Item> findAllProducts() {
//		return Observable.create(new Observable.OnSubscribe<Item>() {
//			@Override
//			public void call(Subscriber<? super Item> subscriber) {
//				SquidCursor<Item> cursor = dao.query(Item.class, Query.select());
//				try {
//					while (cursor.moveToNext()) {
//						if (subscriber.isUnsubscribed()) {
//							break;
//						}
//						subscriber.onNext(new Item(cursor));
//					}
//					subscriber.onCompleted();
//				} catch (Exception e) {
//					subscriber.onError(e);
//				} finally {
//					cursor.close();
//				}
//			}
//		});
//	}
//
//	@Override
//	public Observable<ItemWithCategory> findAllProductsWithCategory() {
//		return Observable.create(new Observable.OnSubscribe<ItemWithCategory>() {
//			@Override
//			public void call(Subscriber<? super ItemWithCategory> subscriber) {
//				SquidCursor<ItemWithCategory> cursor = dao.query(ItemWithCategory.class, Query.select());
//				try {
//					while (cursor.moveToNext()) {
//						if (subscriber.isUnsubscribed()) {
//							break;
//						}
//						subscriber.onNext(new ItemWithCategory(cursor));
//					}
//					subscriber.onCompleted();
//				} catch (Exception e) {
//					subscriber.onError(e);
//				} finally {
//					cursor.close();
//				}
//			}
//		});
//	}
}
