package com.gbs.agent.sender;

import com.gbs.agent.model.Span;

public interface DataSender {
	
	boolean send(Span data);
}
