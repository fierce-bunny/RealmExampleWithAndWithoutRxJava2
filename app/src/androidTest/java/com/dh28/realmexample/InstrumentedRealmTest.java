package com.dh28.realmexample;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.dh28.realmexample.datasource.RxUserRealm;
import com.dh28.realmexample.datasource.UserRealm;
import com.dh28.realmexample.model.User;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import io.realm.Realm;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class InstrumentedRealmTest extends RxUtils {

	private UserRealm userRealm;
	private RxUserRealm rxUserRealm;

	private User userJohn;
	private User userBill;

	@Before
	public void initValuesAndSaveUsersToRealm() {
		Realm.init(InstrumentationRegistry.getTargetContext());

		userRealm = new UserRealm();
		rxUserRealm = new RxUserRealm();

		userJohn = new User("John", 22);
		userBill = new User("Bill", 30);

		userRealm.saveOrUpdateUser(userJohn);

		//--RX--//
		final CountDownLatch signal = new CountDownLatch(1);
		addDisposable(
				inBackground(rxUserRealm.saveOrUpdateUser(userBill))
						.subscribe(signal::countDown));
		try {
			signal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void saveAndGetUsersTest() {
		User userFromDb = userRealm.getUser(userJohn.getName());
		System.out.println(userJohn.toString());
		System.out.println(userFromDb.toString());
		assertEquals(userJohn.toString(), userFromDb.toString());

		//--RX--//
		final CountDownLatch signal = new CountDownLatch(1);
		addDisposable(
				inBackground(rxUserRealm.getUser(userBill.getName()))
						.subscribe(user -> {
							System.out.println("--RX--");
							System.out.println(userBill.toString());
							System.out.println(user.toString());
							assertEquals(userBill.toString(), user.toString());
							signal.countDown();
						}, throwable -> {
							throwable.printStackTrace();
							signal.countDown();
							Assert.fail("rx onError");
						})
		);
		try {
			signal.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@After
	public void clearSubscriptions() {
		dispose();
	}

}
