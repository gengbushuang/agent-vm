package com.gbs.vm;

import java.io.IOException;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

public class TargetVM {

	public void test() {
		System.out.println("test");
		try{
		test1();
		}catch(Exception e){
			
		}
		test2();
	}

	public void test1() {
		System.out.println("test1");
		int test3 = test3();
		System.out.println(test3);
	}

	public void test2() {
		System.out.println("test2");
	}

	public int test3() {
		System.out.println("test3");
		int t = 0;
			t = 4 / 0;
		return t;
	}

	public static void main(String[] args) throws AttachNotSupportedException, IOException, AgentLoadException, AgentInitializationException {
		VirtualMachine vm = VirtualMachine.attach(args[0]);
		vm.loadAgent(args[1]);
	}
}