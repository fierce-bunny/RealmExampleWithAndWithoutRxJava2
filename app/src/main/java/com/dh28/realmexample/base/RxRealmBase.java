package com.dh28.realmexample.base;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.realm.Realm;

public class RxRealmBase {

    private Realm getRealmInstance() {
        return Realm.getDefaultInstance();
    }

    protected <T> Maybe<T> execute(Executor<T> executor) {
        Realm realm = null;
        try {
            realm = getRealmInstance();
            T instance = executor.execute(realm);
            return instance != null
                    ? Maybe.just(executor.execute(realm))
                    : Maybe.error(new RuntimeException("Value is null"));
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
