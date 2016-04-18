//import net.tinyos.message.*;
//
//import net.tinyos.util.*;
//
//import java.io.*;
//
// 
//
//public class TestNetwork implements MessageListener
//
//{
//
//    MoteIF mote;
//
//    
//
//    // Main entry point
//
//    void run() {
//
//                
//
//                                mote = new MoteIF(PrintStreamMessenger.err);
//
//                                mote.registerListener(new TestNetworkMsg(), this);
//
//    }
//
// 
//
//  synchronized public void messageReceived(int dest_addr, Message message) {
//
//                 if (message instanceof TestNetworkMsg) {
//
//                    TestNetworkMsg msg = (TestNetworkMsg)message;
//
//                                
//                                System.out.println("Event is happening at node number      =  " + msg.get_source());
//				System.out.println("The message sequence for source node   =  " + msg.get_seqno());
//				System.out.println("PARENT for the event generating node   =  " + msg.get_parent());
//
//                
//
//                                System.out.println("Link quality with the parent is        =  " + msg.get_metric());
//
//                                
//
//                                System.out.println("Number of hops to get to the base      =  " + msg.get_data());
//
// 
//
//                                System.out.println("Number of hops to get to the base      =  " + msg.get_hopcount());
//
//                                
//
//                                System.out.println("Number of hops to get to the base      =  " + msg.get_sendCount ());
//
// 
//
//                                System.out.println("Number of hops to get to the base      =  " + msg.get_sendSuccessCount ());
//
//                
//
//                                System.out.println(" " );
//
//                 }
//		System.out.println("Wrong packet format");
//
//    }
//
// 
//
//    
//
//    public static void main(String[] args) {
//
//                                TestNetwork me = new TestNetwork();
//
//                                me.run();
//
//    }
//
//}
//
