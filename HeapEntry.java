//package kway;
/**
 * Srdjan (Serge) Radinovich 
 * 1298923
 * COMP317 
 * Assignment 1 - K-way Sort
 */

public class HeapEntry {
	private String line;
	private int index;
	
	public HeapEntry(String s, int i) {
		line = s;
		index = i;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getLine() {
		return line;
	}
}
