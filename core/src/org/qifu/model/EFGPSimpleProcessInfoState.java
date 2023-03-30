package org.qifu.model;

public class EFGPSimpleProcessInfoState {
	/*
	 * 1 = open.running 		開單執行中
	 * 3 = closed.completed  	流程完成結束
	 * 4 = closed.aborted 		取消流程
	 * 5 = closed.terminated 	中止流程
	 */
	public static final String OPEN_RUNNING = "open.running";
	
	public static final String CLOSED_COMPLETED = "closed.completed";
	
	public static final String CLOSED_ABORTED = "closed.aborted";
	
	public static final String CLOSED_TERMINATED = "closed.terminated";
	
	public static boolean isState(String state) {
		return ( OPEN_RUNNING.equals(state) || CLOSED_COMPLETED.equals(state) || CLOSED_ABORTED.equals(state) || CLOSED_TERMINATED.equals(state) );
	}
	
	public static String getValue(String state) {
		if (OPEN_RUNNING.equals(state)) {
			return "1";
		}
		if (CLOSED_COMPLETED.equals(state)) {
			return "3";
		}
		if (CLOSED_ABORTED.equals(state)) {
			return "4";
		}
		return "5";
	}
	
}
