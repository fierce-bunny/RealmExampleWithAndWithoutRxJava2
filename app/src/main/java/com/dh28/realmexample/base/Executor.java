package com.dh28.realmexample.base;

import io.realm.Realm;

public interface Executor<T> {

	T execute(Realm realm);

}
