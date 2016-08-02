package kway;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class FileContext {
	private LinkedList<Integer> runs = new LinkedList<Integer>(); 
	private int currentRun = 0;
	
	private File file = null;
	private BufferedWriter bw = null;
	private BufferedReader br = null;
	
	
	public FileContext(String name) throws IOException {
		file = new File(name);
		if(!file.exists()) {
			file.createNewFile();
		}
		bw = new BufferedWriter(new FileWriter(file));		
		br = new BufferedReader(new FileReader(file));
	}
	
	private void addValue() {
		if(runs.isEmpty() || currentRun == runs.size()){
			runs.add(1);			
		} else {
			int curr = runs.getLast();
			runs.set(currentRun, curr + 1);
		}
	}
	
	public void write(String in) throws IOException {
		bw.write(in);
		if(in.charAt(in.length()-1) != '\n') {
			bw.write("\n");
		}
		addValue();
	}
	
	public void finishRun() throws IOException {
		currentRun++;
		bw.flush();
	}
	
	public int getRunCount() {
		return runs.size();
	}
	
	public void clear() throws IOException {
		currentRun = 0;
		runs.clear();
		close();
		bw = new BufferedWriter(new FileWriter(file));		
		br = new BufferedReader(new FileReader(file));
	}
	
	public void close() throws IOException {
		bw.close();
		br.close();
	} 
	
	// runIter starts at 0
	public String popTopItem(int runIter) throws IOException {
		if(runs.size() < runIter + 1 || runs.get(runIter) == 0) {
			return null;
		}
		
		String topItem = br.readLine();
		
		int runSize = runs.get(runIter);
		runs.set(runIter, runSize - 1);
		
		return topItem;
	}
	
	public boolean runIsDone(int runIter) {
		return runs.size() < runIter + 1 || runs.get(runIter) == 0;
	}
	
	public boolean hasRuns() {
		int runCount = 0;
		for(int i = 0; i < runs.size(); i++) {
			runCount += runs.get(i);
		}
		
		return runCount != 0;
	}
}
