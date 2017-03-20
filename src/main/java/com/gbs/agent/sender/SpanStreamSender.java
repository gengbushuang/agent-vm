package com.gbs.agent.sender;

import java.util.List;

import com.gbs.agent.model.Span;
import com.gbs.agent.model.SpanEvent;

public class SpanStreamSender implements DataSender {

	@Override
	public boolean send(Span data) {
		System.out.println(data);
		List<SpanEvent> spanEventList = data.getSpanEventList();
		for(SpanEvent event:spanEventList){
			System.out.println(event);
		}
		return false;
	}

}
