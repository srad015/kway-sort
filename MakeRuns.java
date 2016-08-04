//Dylan Byrne
//1234065
import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MakeRuns
{
  public static void main(String [] args)
  {
    //check command line arguments are correct
    if(args.length != 1)
    {
      System.out.println("[-1] Wrong args");
      System.out.println("[-1] Usage:");
      System.out.println("[-1] java makeRuns <heap size int>");
      return;
    }

    int heapMax = 0;
    //try parse the heap size, and check if it is positive
    try
    {
      //get the inputted heap size
      heapMax = Integer.parseInt(args[0]);
      if(heapMax <= 0)
      {
        System.out.println("[-1] 0 or negative heap size" );
        return;
      }
    }
    catch(Exception e)
    {
      System.out.println("[-1] Error: " + e);
      System.out.println("[-1] Please check you are using an int");
      return;
    }

    //create the heap
    Comparator<String> comparator = new lexiCompareStrings();
    PriorityQueue<String> lineHeap = new PriorityQueue<String>(heapMax, comparator);

    //create the list
    ArrayList<String> lineList = new ArrayList<String>();

    //create variables for keeping track of heap length
    int heapLength = 0;
    int currentHeapMax = heapMax;
    //create variables for string storage
    String lastOutput = null;
    String input = null;
    String NEW_RUN = "NEW RUN NEW RUN";

    //read in from standard input
    BufferedReader br = null;

    try
    {
      br = new BufferedReader(new InputStreamReader(System.in));

      //keep reading in while memory has space, start outputting after this
        while(br.ready() == true)
        {
          input = br.readLine();

          //if this input would fill the heap
          if(heapLength == (currentHeapMax-1) && lastOutput != null)
          {
            //if the input is smaller than the lastOutput
            if(input.compareTo(lastOutput) < 0)
            {
              //put it in the list and shrink the heap by one
              //System.out.println("List: " + input);
              lineList.add(input);
              currentHeapMax--;
              input = lineHeap.peek();
            }
            else
            {
              //add the input to the heap and inc heapLength
              //System.out.println("Read: " + input);
              lineHeap.add(input);
              heapLength++;
            }
          }
          else
          {
            //add the input to the heap and inc heapLength
            //System.out.println("Read: " + input);
            lineHeap.add(input);
            heapLength++;
          }

          //remake heap from list if list is full
          if(currentHeapMax == 0)
          {
            //System.out.println("LIST FULL");
            currentHeapMax = heapMax;
            while(lineList.isEmpty() == false)
            {
              //input is used as string storage to minimise variables
              input = lineList.get(0);
              lineList.remove(input);
              lineList.trimToSize();
              lineHeap.add(input);
            }
            System.out.println(NEW_RUN);
            lastOutput = null;
            heapLength = lineHeap.size();
          }

          //if heap is full
          if(heapLength >= currentHeapMax)
          {
            //lastOutput is null if at start of new run
            if(lastOutput == null)
            {
              lastOutput = lineHeap.peek();
              System.out.println(lineHeap.poll());
              heapLength--;
            }
            //this is seperate from the null check as you cannot .compareTo(null)
            else if(input.compareTo(lastOutput) >= 0)
            {
              lastOutput = lineHeap.peek();
              System.out.println(lineHeap.poll());
              heapLength--;
            }
            else //if here then at end of run
            {
              System.out.println(NEW_RUN);
              lastOutput = null;
            }

          }
        }

        //output the rest of memory while there is data to output

        while(heapLength != 0)
        {
          if(lastOutput == null)
            {
              lastOutput = lineHeap.peek();
              System.out.println(lineHeap.poll());
              heapLength--;
            }
            else if(lineHeap.peek().compareTo(lastOutput) >= 0)
            {
                  lastOutput = lineHeap.peek();
                  System.out.println(lineHeap.poll());
                  heapLength--;
            }
            else
            {
              System.out.println(NEW_RUN);
              lastOutput = null;
            }
        }

        //if there is data in the list move it the heap and output it
        if(lineList.size() != 0)
        {
            //System.out.println("LIST FULL");
            currentHeapMax = heapMax;
            while(lineList.isEmpty() == false)
            {
              //input is used as string storage to minimise variables
              input = lineList.get(0);
              lineList.remove(input);
              lineList.trimToSize();
              lineHeap.add(input);
            }
            System.out.println(NEW_RUN);
            //numRuns++;
            lastOutput = null;
            heapLength = lineHeap.size();

            while(heapLength != 0)
            {
              if(lastOutput == null)
                {
                  lastOutput = lineHeap.peek();
                  System.out.println(lineHeap.poll());
                  heapLength--;
                }
                else if(lineHeap.peek().compareTo(lastOutput) >= 0)
                {
                      lastOutput = lineHeap.peek();
                      System.out.println(lineHeap.poll());
                      heapLength--;
                }
                else
                {
                  System.out.println(NEW_RUN);
                  lastOutput = null;
                }
            }
        }
      } catch (IOException e) { //catch IO exceptions
            e.printStackTrace();
        } finally {
            if (br != null) { //close the reader
                try {
                    br.close();
                } catch (IOException e) { //catch IO exception
                    e.printStackTrace();
                }
            }
        }
  }
}

class lexiCompareStrings implements Comparator<String>
{
    //compare the strings based on lexi order
    @Override
    public int compare(String x, String y)
    {
        if (x.compareTo(y) < 0)
        {
            return -1;
        }
        if (x.compareTo(y) > 0)
        {
            return 1;
        }
        //if here strings are equal
        return 0;
    }
}
