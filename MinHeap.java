/**
 * Srdjan (Serge) Radinovich 
 * 1298923
 * COMP317 
 * Assignment 1 - K-way Sort
 */
//Dylan Byrne
//1234065

import java.util.Arrays;

public class MinHeap {
	/**
	 * DATA
	 */
    private HeapEntry[] array;
    private int size;
		private int maxSize;
		private int backIndex;
		private boolean backHasItems = false;

    /**
     * Constructor
     */
	public MinHeap (int capacity) {

        array = new HeapEntry[capacity];
        size = 0;
				maxSize = capacity-1;
				backIndex = capacity-1;
    }

	/**
	 * Input item into heap
	 * @param in		Item to be added to heap
	 */
    public boolean add(String in, int index) {

				//array full so can't add
				if (size >= maxSize) {
						return true;
        }

        // Place item into heap at bottom
        size++;
        array[size] = new HeapEntry(in, index);

        upHeap();
        
        return size >= maxSize;
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
    public HeapEntry peek() {
        if (this.isEmpty()) {
            throw new IllegalStateException();
        }

        return array[1];
    }

    /**
     * Get top item of heap and remove it from the heap
     * @return		Top item of the heap
     */
    public HeapEntry remove() {

    	HeapEntry topItem = peek();

    	// Swap current top with bottom item
    	// and remove new bottom item
    	array[1] = array[size];
    	array[size] = null;
    	size--;

    	downHeap();

    	return topItem;
    }

		/**
		 * Input item to back of heap, not growing
		 * the heap
		 * @param in		Item to be added to back of heap
		 */
		public void toEnd(String in, int index)
		{
			//put in next free back positon
			array[backIndex] = new HeapEntry(in, index);
			//decrement next free back position
			backIndex--;

			if(backHasItems == false)
				backHasItems = true; //say there are now back items
		}

		/**
		 * Whether heap has unincluded back portion
		 * @return		bool of statement
		 */
		public boolean hasBack()
		{
			return backHasItems;
		}

		/**
     * Whether their is any space left on the heap
     * @return		bool of statment
     */
		public boolean full()
		{
			if(size >= array.length - 1)
				return true;

			//if there are no back items
			//or
			//size does not reach back items
			if(backHasItems == false  &&  size == maxSize || size < backIndex)//
				return false;

			//else size reaches back items
			return true;
		}

		/**
     * fill the heap from a the unincluded back portion
		 * if possible
     */
		public void makeMax()
		{
			if(backHasItems == false) //if no back portion
				return;

			//if array only has size of 1
			if(maxSize == 1)
			{
				size = 1;
				backIndex = 1;
				backHasItems = false;
				return;
			}

			//if back portion doesn't take up entire array
			if(backIndex != 0)
			{
				fillFromPartial();
			}
			//else if back portion takes up entire array
			else
			{
				//make the the first item be the start of a new heap
				size = 1;
				backIndex++;
				fillFromPartial();
			}
		}

    /**
     * INTERNALS
     */

		 /**
      * fill the heap from a partially filled unincluded
 		  * back portion
      */
 		private void fillFromPartial()
 		{
 			int currentBackIndex = backIndex + 1;

			//if only 1 item in back portion
			if(currentBackIndex == maxSize)
 			{
 				add(array[currentBackIndex].getLine(), array[currentBackIndex].getIndex());
 				backHasItems = false;
 				backIndex = maxSize;
 				return;
 			}

 			//while there are items in the back portion
 			while(currentBackIndex != maxSize)
 			{
 				currentBackIndex = backIndex + 1;
 				add(array[currentBackIndex].getLine(), array[currentBackIndex].getIndex());
 				backIndex++;
 			}

 			//there are now no more back items
 			backHasItems = false;
 		}

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
                && array[leftIndex(index)].getLine().compareTo(array[rightIndex(index)].getLine()) > 0) {
            	smallChild = rightIndex(index);
            }

            if (array[index].getLine().compareTo(array[smallChild].getLine()) > 0) {
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

        while (hasParent(idx) && (parent(idx).getLine().compareTo(array[idx].getLine()) > 0)) {
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


    private HeapEntry parent(int i) {
        return array[parentIndex(i)];
    }


    private int parentIndex(int i) {
        return i / 2;
    }

    private void swap(int index1, int index2) {
    	HeapEntry temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }
}