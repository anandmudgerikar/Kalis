
package edu.purdue.idsforiot;

import edu.purdue.idsforiot.packets.CTPPacket;
import edu.purdue.idsforiot.packets.PacketFactory;
import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.tools.PrintfMsg;
import net.tinyos.util.*;

public class Communicator2 implements MessageListener {

  private MoteIF moteIF;
  private String source;
  
  public Communicator2(MoteIF moteIF) {
    this.moteIF = moteIF;
    this.moteIF.registerListener(new PrintfMsg(), this);
  }

  public Communicator2(String source) {
	  this.source = source;
  }
  
  private String nextline="";
  
  public void messageReceived(int to, Message message) {
    PrintfMsg msg = (PrintfMsg)message;
    
    for(int i=0; i<PrintfMsg.totalSize_buffer(); i++) {
    char nextChar = (char)(msg.getElement_buffer(i));
      if(nextChar != 0)
      { 
    	  	nextline += nextChar;
    	  	System.out.print(nextChar);
      }
      if(nextChar == '\n')
      {
    	  System.out.println("The full message is "+nextline);
    	  CTPPacket p = PacketFactory.getPacket(nextline.split(","));
			if (p == null) continue;
							
			// notify the DataStore
			DataStore.getInstance().onNewPacket(p);
    	  nextline = "";
      }
    }
    
  }
  
  public void listen() throws Exception {
    // source = "serial@/dev/ttyUSB0:telosb"; //hard-coded for now
    
    
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
}
