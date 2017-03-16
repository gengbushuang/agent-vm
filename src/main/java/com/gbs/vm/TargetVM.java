package com.gbs.vm;

import java.io.IOException;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

public class TargetVM {

	public void test() {
		System.out.println("test");
		test1();
		test2();
	}

	public void test1() {
		System.out.println("test1");
		test3();
	}

	public void test2() {
		System.out.println("test2");
	}
	
	public void test3() {
		System.out.println("test3");
	}

	public static void main(String[] args) throws AttachNotSupportedException, IOException, AgentLoadException, AgentInitializationException {
		VirtualMachine vm = VirtualMachine.attach(args[0]);
		vm.loadAgent(args[1]);
	}
}