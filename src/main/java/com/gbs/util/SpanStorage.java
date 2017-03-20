package com.gbs.util;

import java.util.ArrayList;
import java.util.List;

import com.gbs.agent.model.Span;
import com.gbs.agent.model.SpanEvent;
import com.gbs.agent.sender.DataSender;

public class SpanStorage {
	protected List<SpanEvent> spanEventList = new ArrayList<SpanEvent>(10);

	private final DataSender dataSender;

	public SpanStorage(DataSender dataSender) {
		if (dataSender == null) {
			throw new NullPointerException("dataSender must not be null");
		}
		this.dataSender = dataSender;
	}

	public void store(SpanEvent spanEvent) {
		if (spanEvent == null) {
			throw new NullPointerException("spanEvent must not be null");
		}
		final List<SpanEvent> spanEventList = this.spanEventList;
		if (spanEventList != null) {
			spanEventList.add(spanEvent);
		} else {
			throw new IllegalStateException("spanEventList is null");
		}
	}

	public void store(Span span) {
		if (span == null) {
			throw new NullPointerException("span must not be null");
		}
		span.setSpanEventList(spanEventList);
		spanEventList = null;
		this.dataSender.send(span);
	}
	
	public void close() {
		
	}
}
