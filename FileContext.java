/**
 * Srdjan (Serge) Radinovich 
 * 1298923
 * COMP317 
 * Assignment 1 - K-way Sort
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Abstraction of a file with respect to k-way merge algorithm
 * Contains information regarding runs being written to and
 * read from disk
 * @author Serge Radinovich
 *
 */
public class FileContext {
	
	/**
	 * DATA
	 */
	private LinkedList<Integer> runs = new LinkedList<Integer>(); 
	private int currentRun = 0;
	
	private File file = null;
	private BufferedWriter bw = null;
	private BufferedReader br = null;
	
	/**
	 * INTERFACE
	 */
	
	/**
	 * Constructor
	 * @param 		name name of file on disk
	 * @throws IOException
	 */
	public FileContext(String name) throws IOException {
		file = new File(name);
		if(!file.exists()) {
			file.createNewFile();
		}
		bw = new BufferedWriter(new FileWriter(file));		
		br = new BufferedReader(new FileReader(file));
	}
		
	/**
	 * Write a value to file on disk (buffered)
	 * @param 		in String value to be recorded on disk
	 * @throws IOException
	 */
	public void write(String in) throws IOException {
		bw.write(in);
		if(in.charAt(in.length()-1) != '\n') {
			bw.write("\n");
		}
		addValue();
	}
	
	/**
	 * Iterate run iter because current one is complete
	 * Output to file by flushing buffer
	 * @throws IOException
	 */
	public void finishRun() throws IOException {
		currentRun++;
		bw.flush();
	}
	
	/**
	 * Return the number of runs this file has recorded
	 * @return number of runs currently (partially) recorded in this file
	 */
	public int getRunCount() {
		return runs.size();
	}
	
	/**
	 * Flush the object's values to allow for restart
	 * File is closed and opened
	 * @throws IOException
	 */
	public void clear() throws IOException {
		currentRun = 0;
		runs.clear();
		close();
		bw = new BufferedWriter(new FileWriter(file));		
		br = new BufferedReader(new FileReader(file));
	}
	
	/**
	 * Deconstruct (close files)
	 * @throws IOException
	 */
	public void close() throws IOException {
		bw.close();
		br.close();
	} 
	
	/**
	 * Read top value from current run being read from file
	 * @param 		runIter Iterator indicating which run is being currently read
	 * @return 		String value of top item recorded in file
	 * @throws IOException
	 */
	public String popTopItem(int runIter) throws IOException {
		if(runs.size() < runIter + 1 || runs.get(runIter) == 0) {
			return null;
		}
		
		String topItem = br.readLine();
		
		int runSize = runs.get(runIter);
		runs.set(runIter, runSize - 1);
		
		return topItem;
	}
	
	/**
	 * Indicate whether there are no further values
	 * to read from the currently processed run
	 * @param runIter 		Iterator indicating which run is being currently read
	 * @return 				Truth value as to completeness of this run's read
	 */
	public boolean runIsDone(int runIter) {
		return runs.size() < runIter + 1 || runs.get(runIter) == 0;
	}
	
	/**
	 * Final check as to whether all data has been 
	 * read from all runs on this file
	 * @return 				Truth value as to above
	 */
	public boolean hasRuns() {
		int runCount = 0;
		for(int i = 0; i < runs.size(); i++) {
			runCount += runs.get(i);
		}
		
		return runCount != 0;
	}
	
	/**
	 *	INTERNALS 
	 */
	
	/**
	 * Update information regarding runs in this file
	 * when a value is added to the current run
	 */
	private void addValue() {
		if(runs.isEmpty() || currentRun == runs.size()){
			runs.add(1);			
		} else {
			int curr = runs.getLast();
			runs.set(currentRun, curr + 1);
		}
	}
}
