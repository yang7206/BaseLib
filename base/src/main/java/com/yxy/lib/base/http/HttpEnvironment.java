package com.yxy.lib.base.http;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HttpEnvironment {
	private static HttpEnvironment mInstance;
	private ExecutorService mThreadPool;
	
	private HttpEnvironment() {
		mThreadPool = Executors.newCachedThreadPool();
	}

	public static HttpEnvironment getInstance() {
		if (mInstance == null) {
			mInstance = new HttpEnvironment();
		}
		return mInstance;
	}

	public WeakReference<Future<?>> submit(Runnable runable) {
		return new WeakReference<Future<?>>(mThreadPool.submit(runable));
	}

	public void shundown() {
		if (mThreadPool != null) {
			mThreadPool.shutdown();
			mInstance = null;
		}
	}

}
