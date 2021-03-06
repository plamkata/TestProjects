package com.startup.solynchron.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * TODO: migrate logic instantiation to OSGI style instantiation
 * 
 * @author plamKaTa
 *
 */
public class LogicManager {
	
	public static final String RECORD_LOGIC = "record";
	
	public static final String PROBLEM_LOGIC = "problem";
	
	private static Map<String, ILogic> logicRegistry = 
		new HashMap<String, ILogic>();

	/**
	 * Initialize the logic manager's registry. 
	 */
	public static void init() {
		register(RECORD_LOGIC, new RecordLogic());
		register(PROBLEM_LOGIC, new ProblemLogic());
	}
	
	/**
	 * Dispose the logic manager's registry.
	 */
	public static void dispose() {
		Iterator<Map.Entry<String, ILogic>> it = 
			logicRegistry.entrySet().iterator();
		while (it.hasNext()) {
			ILogic logic = it.next().getValue();
			logic.dispose();
		}
		logicRegistry.clear();
		logicRegistry = null;
	}
	
	public static void register(String key, ILogic logic) {
		logicRegistry.put(key, logic);
	}
	
	public static void deregister(String key) {
		logicRegistry.remove(key);
	}
	
	public static ILogic get(String key) {
		return logicRegistry.get(key);
	}

}
