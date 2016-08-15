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
      if(heapMax <= 1)
      {
        System.out.println("[-1] Heap size must be greater than 1" );
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
    MinHeap lineHeap = new MinHeap(heapMax);
    
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
          if(lineHeap.full() == false && lastOutput != null)
          {
            //if the input is smaller than the lastOutput
            if(input.compareTo(lastOutput) < 0)
            {
              //put it in the back portion
              lineHeap.toEnd(input, 0); //System.out.println("End: " + input);
            }
            else
            {
              //add the input to the heap
              lineHeap.add(input, 0); //System.out.println("Read: " + input);
            }
          }
          else if(lineHeap.full() == false)
          {
            //add the input to the heap
            lineHeap.add(input, 0); //System.out.println("Read: " + input);
          }

          //remake heap from backportion if it reaches the end
          if(lineHeap.isEmpty())
          {
            lineHeap.makeMax();
            System.out.println(NEW_RUN);
            lastOutput = null;
          }

          //if heap is full
          if(lineHeap.full())
          {
            //lastOutput is null if at start of new run
            if(lastOutput == null)
            {
              lastOutput = lineHeap.peek().getLine();
              System.out.println(lineHeap.remove().getLine());
            }
            //this is seperate from the null check as you cannot .compareTo(null)
            else if(lineHeap.peek().getLine().compareTo(lastOutput) >= 0)
            {
              lastOutput = lineHeap.peek().getLine();
              System.out.println(lineHeap.remove().getLine());
            }
            else //if here then at end of run
            {
              System.out.println(NEW_RUN);
              lastOutput = null;
            }

          }
        }

        //output the rest of memory while there is data to output
        while(lineHeap.isEmpty() == false)
        {
          if(lastOutput == null)
            {
              lastOutput = lineHeap.peek().getLine();
              System.out.println(lineHeap.remove().getLine());
            }
            else if(lineHeap.peek().getLine().compareTo(lastOutput) >= 0)
            {
                  lastOutput = lineHeap.peek().getLine();
                  System.out.println(lineHeap.remove().getLine());
            }
            else
            {
              System.out.println(NEW_RUN);
              lastOutput = null;
            }
        }

        //if there is data left in the back move it to the heap and output it
        if(lineHeap.hasBack())
        {
            System.out.println(NEW_RUN);
            lineHeap.makeMax();
            lastOutput = null;

            while(lineHeap.isEmpty() == false)
            {
              if(lastOutput == null)
                {
                  lastOutput = lineHeap.peek().getLine();
                  System.out.println(lineHeap.remove().getLine());
                }
                else if(lineHeap.peek().getLine().compareTo(lastOutput) >= 0)
                {
                      lastOutput = lineHeap.peek().getLine();
                      System.out.println(lineHeap.remove().getLine());
                }
                else
                {
                  lastOutput = null;
                  System.out.println(NEW_RUN);
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
