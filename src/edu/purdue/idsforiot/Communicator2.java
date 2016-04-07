package edu.purdue.idsforiot;

import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.tools.PrintfMsg;
import net.tinyos.util.*;

public class Communicator2 implements MessageListener {

  private MoteIF moteIF;

  public Communicator2(MoteIF moteIF) {
    this.moteIF = moteIF;
    this.moteIF.registerListener(new PrintfMsg(), this);
  }
  
  public Communicator2(String source){
	  	PhoenixSource phoenix;
	    
	    if (source == null) {
	      phoenix = BuildSource.makePhoenix(PrintStreamMessenger.err);
	    }
	    else {
	      phoenix = BuildSource.makePhoenix(source, PrintStreamMessenger.err);
	    }
	    System.out.print(phoenix);
	    MoteIF mif = new MoteIF(phoenix);
	    Communicator2 client = new Communicator2(mif);
  }

  public void messageReceived(int to, Message message) {
    PrintfMsg msg = (PrintfMsg)message;
    for(int i=0; i<PrintfMsg.totalSize_buffer(); i++) {
      char nextChar = (char)(msg.getElement_buffer(i));
      if(nextChar != 0)
        System.out.print(nextChar);
    }
  }

}
