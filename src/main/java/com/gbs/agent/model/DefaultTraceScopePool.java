package com.gbs.agent.model;

import java.util.Map;
import java.util.TreeMap;

public class DefaultTraceScopePool {

    private final Map<String,TraceScope> map = new TreeMap<String,TraceScope>();

    public TraceScope get(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }

        return map.get(name);
    }

    public TraceScope add(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        TraceScope newScope = new TraceScope(name);
        boolean containsKey = map.containsKey(name);
        if(containsKey){
        	TraceScope old = map.get(name);
        	map.put(name, newScope);
        	return old;
        }
        map.put(name, newScope);
        return null;
    }

    public void clear() {
    	map.clear();
    }
}