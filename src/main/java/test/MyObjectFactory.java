package test;

import test.MyObject;

import java.util.concurrent.atomic.AtomicInteger;

public class MyObjectFactory {

	private static final AtomicInteger index = new AtomicInteger(0);
	
	public static MyObject getMyObject() {
		int tempIndex = index.getAndIncrement();
		return new MyObject(tempIndex++, tempIndex++, tempIndex++, tempIndex++, tempIndex++, tempIndex++,
				tempIndex++, tempIndex++, tempIndex++, tempIndex++, tempIndex++, tempIndex++, tempIndex++, tempIndex++, tempIndex++, tempIndex++,
				tempIndex++, tempIndex++, tempIndex++, tempIndex++,
				tempIndex++, tempIndex++, tempIndex++, tempIndex++, tempIndex++, tempIndex++,
				tempIndex++, tempIndex++, tempIndex++, tempIndex++
				);
	}
}
