# kway-sort

# build

	$ javac *.java
	
# execute

	cat input.txt | java MakeRuns 10 | java MergeRuns 7 > output.srtd

# notes

	We do NOT output newlines for sorting!	
	MergeRuns argument specifies the k value in the k-way sort. A maximum of 1000 is permitted. 
