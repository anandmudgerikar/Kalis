
import java.io.*;

public class Mod_Manager extends Thread {

   public static void main(String args[]) throws NumberFormatException, IOException{
      
      char option = 'A';
      
      FileInputStream configstream = new FileInputStream("/home/odroid/tinyos-main/project/ids/data/config.txt");
		 BufferedReader cr = new BufferedReader(new InputStreamReader(configstream));
		 int n; 
			
		 n = Integer.parseInt((cr.readLine())); //get other parameters from config file
		 cr.close();

      switch(option) //currently the implementation supports static switch cases, but each of the modules will be 
      				 //implemented as a new runnable thread with a run method. It is up to the module manager to 
      				// decide when and which module has to run. 
      {
         case 'A' : //sample print module which pops out everything from the queues and prints it
            
        	 //each module runs in its own separate thread
        	 
        	 PrintModule p1 = new PrintModule("Thread 1");
        	 p1.start();
        	 
//        	System.out.println("\n Print Module working \n");
//            Data_Store ds = new Data_Store();
//            try
//            {
//            	ds.update_queues();
//            }
//            catch (IOException e)
//            {
//            	System.out.println("Error in input from data store");
//            }
//            
//            //printing out all the packet captures for each queue
//            for (int i=0;i<n;i++)
//            {
//            	if(ds.queues[i].isEmpty())
//            	{
//            		System.out.println("Queue for node "+i+"is empty");
//            	}
//            	else
//            	{
//            		while(!ds.queues[i].isEmpty())
//            		System.out.println(ds.queues[i].remove());
//            	}
//            		
//            }
            break;
         case 'B' :
        	 System.out.println("\n Module B working \n");
        	 
        	 break;
         case 'C' :
        	 System.out.println("\n Module C working \n");
            break;
         case 'D' :
        	 System.out.println("\n Module D working \n");
         case 'E' :
        	 System.out.println("\n Module E working \n");
            break;
         default :
            System.out.println("\nInvalid option provided \n");
      }
   }
}
