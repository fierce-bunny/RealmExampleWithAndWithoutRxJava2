package com.dh28.realmexample.base;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.realm.Realm;

public class RxRealmBase {

	private Realm getRealmInstance() {
		return Realm.getDefaultInstance();
	}

	protected <T> Single<T> execute(Executor<T> executor) {
		Realm realm = null;
		try {
			realm = getRealmInstance();
			return Single.just(executor.execute(realm));
		} finally {
			if (realm != null && !realm.isClosed()) {
				realm.close();
			}
		}
	}

	protected Completable execute(Realm.Transaction transaction) {
		return Completable.fromAction(() -> {
			Realm realm = null;
			try {
				realm = getRealmInstance();
				realm.executeTransaction(transaction);
			} finally {
				if (realm != null && !realm.isClosed()) {
					realm.close();
				}
			}
		});
	}

}
