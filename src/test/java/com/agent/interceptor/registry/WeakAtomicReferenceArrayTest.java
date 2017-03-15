package com.agent.interceptor.registry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.gbs.agent.interceptor.registry.WeakAtomicReferenceArray;

public class WeakAtomicReferenceArrayTest {

	WeakAtomicReferenceArray<String> referenceArray;
	
	@Before
	public void bf(){
		referenceArray = new WeakAtomicReferenceArray<String>(20, String.class);
	}
	
	@After
	public void af(){
		referenceArray = null;
	}
	
	@Test
	public void testSet(){
		referenceArray.set(0, "a");
		String a = referenceArray.get(0);
		System.out.println(a);
		referenceArray.set(0, "b");
		String b = referenceArray.get(0);
		System.out.println(b);
	}
}
