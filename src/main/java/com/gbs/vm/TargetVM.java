package com.gbs.vm;

import java.io.IOException;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;


public class TargetVM {
	
    public static void main(String[] args) throws AttachNotSupportedException, IOException, AgentLoadException, AgentInitializationException {
    	VirtualMachine vm = VirtualMachine.attach(args[0]);
        vm.loadAgent(args[1]);
	}
}