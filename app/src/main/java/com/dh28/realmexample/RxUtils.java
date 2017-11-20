package com.dh28.realmexample;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class RxUtils {

	private CompositeDisposable compositeSubscription = new CompositeDisposable();

	void addDisposable(Disposable disposable) {
		compositeSubscription.add(disposable);
	}

	void dispose() {
		compositeSubscription.clear();
	}

	Completable inBackground(Completable dest) {
		return dest.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	<T> Maybe<T> inBackground(Maybe<T> dest) {
		return dest.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

}
