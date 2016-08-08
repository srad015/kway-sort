/**
 * Srdjan (Serge) Radinovich 
 * 1298923
 * COMP317 
 * Assignment 1 - K-way Sort
 */

import java.util.Arrays;

public class MinHeap {
	/**
	 * DATA
	 */
    private String[] array;
    private int size;
    
    /**
     * Constructor
     */
	public MinHeap (int capacity) {

        array = new String[capacity];  
        size = 0;
    }
    
	/**
	 * Input item into heap and grow 
	 * array if required
	 * @param in		Item to be added to heap
	 */
    public void add(String in) {
       
        if (size >= array.length - 1) {
            array = this.resize();
        }        
        
        // Place item into heap at bottom
        size++;
        array[size] = in;
        
        upHeap();
    }
    
    /**
     * Whether nothing has been added to heap
     * @return		Truth value re above
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Get top item of heap without removing it
     * @return		Top item of heap
     */
    public String peek() {
        if (this.isEmpty()) {
            throw new IllegalStateException();
        }
        
        return array[1];
    }

    /**
     * Get top item of heap and remove it from the heap
     * @return		Top item of the heap
     */
    public String remove() {

    	String topItem = peek();
    	
    	// Swap current top with bottom item
    	// and remove new bottom item
    	array[1] = array[size];
    	array[size] = null;
    	size--;
    	
    	downHeap();
    	
    	return topItem;
    }
    
    /**
     * INTERNALS
     */
    
    /**
     * Sweep a value that is not the smallest down the heap
     * Executed when removing item
     */
    private void downHeap() {
        int index = 1;
        
        // Go downwards
        while (hasLeftChild(index)) {
            // Find smaller child
            int smallChild = leftIndex(index);
            
            // Go downwards with smaller child if possible
            if (hasRightChild(index)
                && array[leftIndex(index)].compareTo(array[rightIndex(index)]) > 0) {
            	smallChild = rightIndex(index);
            } 
            
            if (array[index].compareTo(array[smallChild]) > 0) {
                swap(index, smallChild);
            } else {                
                break;
            }
             
            index = smallChild;
        }        
    }
    
    /**
     * Sweep newly added item up the heap
     * based on its value
     */
    private void upHeap() {
        int idx = this.size;
        
        while (hasParent(idx) && (parent(idx).compareTo(array[idx]) > 0)) {
            // Swap if parent and child out of order
            swap(idx, parentIndex(idx));
            idx = parentIndex(idx);
        }        
    }
    
    
    private boolean hasParent(int i) {
        return i > 1;
    }
    
    
    private int leftIndex(int i) {
        return i * 2;
    }
    
    
    private int rightIndex(int i) {
        return i * 2 + 1;
    }
    
    
    private boolean hasLeftChild(int i) {
        return leftIndex(i) <= size;
    }
    
    
    private boolean hasRightChild(int i) {
        return rightIndex(i) <= size;
    }
    
    
    private String parent(int i) {
        return array[parentIndex(i)];
    }
    
    
    private int parentIndex(int i) {
        return i / 2;
    }
    
    
    private String[] resize() {
        return Arrays.copyOf(array, array.length * 2);
    }
    
    
    private void swap(int index1, int index2) {
    	String temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;        
    }
}
