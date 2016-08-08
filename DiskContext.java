/**
 * Srdjan (Serge) Radinovich 
 * 1298923
 * COMP317 
 * Assignment 1 - K-way Sort
 */

import java.io.IOException;

/**
 * Abstraction of I/O interface for k-way merge algorithm
 * Contains list of files to be used for recording and
 * reading runs
 * @author Serge Radinovich
 *
 */
public class DiskContext {
	
	/**
	 * DATA
	 */
	
	private int _k;
	private int outIdx;
	private int inIdx;
	
	private FileContext[] fileCtxs = null;
	
	/**
	 * INTERFACE
	 */
	
	/**
	 * Constructor
	 * @param k Number of files used for simultaneous merge 
	 * 			(require 2*k files for double buffering)
	 * @throws IOException
	 */
	public DiskContext(int k) throws IOException {

		inIdx = 0;
		outIdx = 1;
		
		_k = k;
		
		fileCtxs = new FileContext[k*2];
		
		for(int i = 0; i < k*2; ++i) {
			fileCtxs[i] = new FileContext("k-" + i + ".txt");					
		}
	}
	
	/**
	 * Write inbound String to disk 
	 * @param fileIdx 	Index into output file to be written to
	 * @param in 		String to be written to disk
	 * @throws IOException
	 */
	public void write(int fileIdx, String in) throws IOException {
		int buffOffset = outIdx * _k;
		fileCtxs[buffOffset + fileIdx].write(in);

	}
	
	/**
	 * Finalize run recording to output file
	 * @param fileIdx		Index into output file 
	 * @throws IOException
	 */
	public void finishRun(int fileIdx) throws IOException {
		int buffOffset = outIdx * _k;
		fileCtxs[buffOffset + fileIdx].finishRun();
	}
	
	/**
	 * Read top String for run in requested input file
	 * @param fileIdx		Index to requested input file
	 * @param runIter		Index to run currently being read
	 * @return	String value to sort
	 * @throws IOException
	 */
	public String getHeadElement(int fileIdx, int runIter) throws IOException {
		int buffOffset = inIdx * _k;		
		
		return fileCtxs[buffOffset + fileIdx].popTopItem(runIter);
	}
	
	/**
	 * Swap input and output indices for double buffering purposes
	 * Input and output files change during k-way merge algorithm
	 * @throws IOException
	 */
	public void swapBuffers() throws IOException {
		
		// All buffers used for input must now be cleared
		// Previous output becomes next input
		int buffOffset = inIdx * _k;
		for(int i = 0; i < _k; ++i) {
			fileCtxs[buffOffset + i].clear();
		}
		
		// Swap buffers
		inIdx = 1 - inIdx;
		outIdx = 1 - outIdx;
		
	}

	/**
	 * Indicate whether current input files have any runs
	 * remaining to be read
	 * @return 		Truth value as to whether any runs remain to be read
	 */
	public boolean hasInputRuns() {	
		for(int i = 0; i < _k; i++) {
			int bufferIdx = inIdx * _k + i;
			if(fileCtxs[bufferIdx].hasRuns()) {
				return true;
			}			
		}
		return false;
	}
	
	/**
	 * Indicate whether current run being read has been completed
	 * @param runIter	Index into current run being read from files
	 * @return			Truth value as to above
	 */
	public boolean runReadComplete(int runIter) {
		boolean runsDone = true;
		for(int i = 0; i < _k; i++) { 
			int bufferIdx = inIdx * _k + i;
			runsDone &= fileCtxs[bufferIdx].runIsDone(runIter);
		}
		return runsDone;
	}
	
	/**
	 * Deconstruct and close files
	 * @throws IOException
	 */
	public void close() throws IOException {
		for(int i = 0; i < _k; ++i) {
			fileCtxs[i].close();
		}
	}
	

}