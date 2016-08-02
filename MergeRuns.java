package kway;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class MergeRuns {
	
	static MinHeap dataProc = new MinHeap();
		
	public static void main(String args[]) throws IOException {
			
		//int k = args.length > 0 ? Integer.parseInt(args[0]) : 5;
		int k = 4;
		DiskContext dc = new DiskContext(k);		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    String s;
	    int writeIdx = 0;

	    String[] test = {"n", "o", "p", "*",
	    				 "g","*",
	    				 "h", "i", "j", "k", "*", 
	    				 "d", "e", "f", "*",
	    				 "l", "m", "*",
	    				 "a", "b", "c", "*"};
	    
	    // Read stdin and record runs into iterative files
	    //while ((s = in.readLine()) != null && s.length() != 0) {
	    // Testing loop
	    int totalRuns = 0;
	    for(int i = 0; i < test.length; ++i) {
	        s = test[i]; 	
	    	if(s.compareTo("*") == 0) {
	    		dc.finishRun(writeIdx);
	    		writeIdx = ( writeIdx + 1 ) % k;
	    		totalRuns++;
	    	} else {
	    		dc.write(writeIdx, s);
	    	}
	    }
	    
	    
	    LinkedList<String> result = new LinkedList<String>();
	    // Get k-tuples of runs from disk, merge them, save to disk again as one run
    	// Add every file's top element to heap    			 
	    //TODO: Outer loop does not work for k == 2 or numbers > totalRuns
		for(int mergeRun = totalRuns; mergeRun >= 1; mergeRun = (mergeRun + k - 1) / k){
			int runIter = 0;
			writeIdx = 0;
			dc.swapBuffers();
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
			    	dc.finishRun(writeIdx);
			    	writeIdx = ( writeIdx + 1 ) % k;	
			    	runIter++;
		    	}
		    }
		    if(mergeRun == 1)break;
		}
		
		// Read final file to stdout
		while(!result.isEmpty()){
			System.out.println(result.pop());
		}
	}
}

