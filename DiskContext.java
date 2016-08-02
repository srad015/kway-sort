package kway;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DiskContext {
	
	private int _k;
	private int outIdx;
	private int inIdx;
	
	private FileContext[] fileCtxs = null;
	
	public DiskContext(int k) throws IOException {

		inIdx = 0;
		outIdx = 1;
		
		_k = k;
		
		fileCtxs = new FileContext[k*2];
		
		for(int i = 0; i < k*2; ++i) {
			fileCtxs[i] = new FileContext("k-" + i + ".txt");					
		}
	}
	
	public void write(int fileIdx, String in) throws IOException {
		int buffOffset = outIdx * _k;
		fileCtxs[buffOffset + fileIdx].write(in);

	}
	
	public void finishRun(int fileIdx) throws IOException {
		int buffOffset = outIdx * _k;
		fileCtxs[buffOffset + fileIdx].finishRun();
	}
	
	public String getHeadElement(int fileIdx, int runIter) throws IOException {
		int buffOffset = inIdx * _k;		
		
		return fileCtxs[buffOffset + fileIdx].popTopItem(runIter);

	}
	
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

	
	public boolean hasInputRuns() {	
		for(int i = 0; i < _k; i++) {
			int bufferIdx = inIdx * _k + i;
			if(fileCtxs[bufferIdx].hasRuns()) {
				return true;
			}			
		}
		return false;
	}
	
	public boolean runReadComplete(int runIter) {
		boolean runsDone = true;
		for(int i = 0; i < _k; i++) { 
			int bufferIdx = inIdx * _k + i;
			runsDone &= fileCtxs[bufferIdx].runIsDone(runIter);
		}
		return runsDone;
	}
	
	public void close() throws IOException {
		for(int i = 0; i < _k; ++i) {
			fileCtxs[i].close();
		}
	}
	

}