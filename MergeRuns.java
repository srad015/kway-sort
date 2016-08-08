/**
 * Srdjan (Serge) Radinovich 
 * 1298923
 * COMP317 
 * Assignment 1 - K-way Sort
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class MergeRuns {
	
	public static void main(String args[]) throws IOException {

		// Read in k value if one is provided, otherwise default value
		int k = args.length > 0 ? Integer.parseInt(args[0]) : 4;
		
		// Disallow more than 2000 files to be requested (files = 2*k)
		if(k > 1000){
			throw new IOException("Too many merge files specified (k = 1000 is maximum)");
		}
		
		// Instantiate our helpful heap
		MinHeap dataProc = new MinHeap(k);
		
		// Instantiate our interface to the file system and 
		// the runs on disk
		DiskContext dc = new DiskContext(k);	
		
		// Read from stdin to gather runs from MakeRuns exe
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    int writeIdx = 0;
	    int totalRuns = 0;		    
	    String s;
	    while ((s = in.readLine()) != null) {
	    	// Ignore empty runs
	    	if(s.length() == 0) continue;
	    	
	    	// Catch our end-of-run delimiter or continue writing run to file
	    	if(s.compareTo("NEW RUN NEW RUN") == 0) {
	    		// Finish off our current run and iterate to next one
	    		dc.finishRun(writeIdx);
	    		writeIdx = ( writeIdx + 1 ) % k;
	    		totalRuns++;
	    	} else {
	    		// Our device context abstracts away the file writes
	    		dc.write(writeIdx, s);
	    	}
	    }
	    
	    // Instantiate list to gather final output as we go
	    LinkedList<String> result = new LinkedList<String>();
	    
	    // Get k-tuples of runs from disk, merge them, save to disk again as one run
    	// Add every file's top element to heap    			 
		for(int mergeRun = totalRuns; mergeRun >= 1; mergeRun = (mergeRun + k - 1) / k){
			
			// Our device context needs to keep track of which run we are processing
			int runIter = 0;
			
			// Restart the write index for our double-buffered read/write
			writeIdx = 0;
			
			// Swap our buffers after each merge for our double-buffered read/write
			dc.swapBuffers();
			
			// End inner loop when all runs for this merge have been processed
		    while(dc.hasInputRuns()) {
		    
		    	// k-way read (top of each file's top run)
		    	for(int fileIdx = 0; fileIdx < k; fileIdx++) {
		    		
		    		String headItem = dc.getHeadElement(fileIdx, runIter);
		    		
		    		if(headItem != null) {
		    			dataProc.add(headItem);
		    		}
		    	}  	
		    	
		    	//Write heap as run
		    	if(dc.runReadComplete(runIter)) {
		    		while(!dataProc.isEmpty()) {
			    		String heapItem = dataProc.remove();
			    		dc.write(writeIdx, heapItem);
			    		// Record final result for stdout
			    		if(mergeRun <= 2) {
			    			result.add(heapItem);
			    		}
			    	}
		    		// We must always indicate when we have no more 
		    		// data for our current output buffer
			    	dc.finishRun(writeIdx);
			    	writeIdx = ( writeIdx + 1 ) % k;	
			    	runIter++;
		    	}
		    }
		    // This is necessay because we cannot ensure that
		    // k is a power of 2 (cannot use >>= k instead of /= k in outer loop)
		    if(mergeRun == 1)break;
		}
		
		// Write final file to stdout
		while(!result.isEmpty()){
			System.out.println(result.pop());
		}
	}
}
