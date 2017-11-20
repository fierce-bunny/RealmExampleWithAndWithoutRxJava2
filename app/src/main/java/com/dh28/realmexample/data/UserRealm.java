package com.dh28.realmexample.data;

import com.dh28.realmexample.base.RealmBase;
import com.dh28.realmexample.model.User;

public class UserRealm extends RealmBase {

	public User getUser(final String name) {
		return execute(realm -> {
			User result = realm
					.where(User.class)
					.equalTo("name", name)
					.findFirst();
			if (result == null) {
				return null;
			}
			return realm.copyFromRealm(result);
		});
	}

	public void saveOrUpdateUser(User user) {
		execute(realm -> {
			realm.copyToRealmOrUpdate(user);
		});
	}

}
