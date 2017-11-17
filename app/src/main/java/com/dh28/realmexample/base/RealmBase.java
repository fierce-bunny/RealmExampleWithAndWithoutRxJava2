package com.dh28.realmexample.base;

import io.realm.Realm;

public class RealmBase {

	private Realm getRealmInstance() {
		return Realm.getDefaultInstance();
	}

	/**
	 * Will be executed on the current thread.
	 */
	protected <T> T execute(Executor<T> executor) {
		Realm realm = null;
		try {
			realm = getRealmInstance();
			return executor.execute(realm);
		} finally {
			if (realm != null && !realm.isClosed()) {
				realm.close();
			}
		}
	}

	/**
	 * Will be executed on a new thread.
	 * Use async transaction to <b>write</b> to Realm.
	 */
	protected void execute(Realm.Transaction transaction) {
		Realm realm = null;
		try {
			realm = getRealmInstance();
			realm.executeTransactionAsync(transaction);
		} finally {
			if (realm != null && !realm.isClosed()) {
				realm.close();
			}
		}
	}

}
