package edu.purdue.idsforiot.modules;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.RplPacket;

public class RPLSelectiveForwardingModule extends DetectionModule {

	private HashMap<String, Long> lastDAOsent;

	public RPLSelectiveForwardingModule(ModuleManager mgr) {
		super(mgr);
		// TODO Auto-generated constructor stub
	}

	public void start() {
		// initializing the queues
		this.lastDAOsent = new HashMap<String, Long>();
		super.start();
	}

	@Override
	public void onNewPacket(Packet p) {
		// TODO Auto-generated method stub
		if(p instanceof RplPacket)
		{
			if(((RplPacket) p).getmessage_type().equals("DIS") || ((RplPacket) p).getmessage_type().equals("DAO"))
			{
				this.lastDAOsent.put(p.getSrc(),p.getTimestamp());
			}
			
			
			for (Entry<String, Long> entry : this.lastDAOsent.entrySet()) {
			    String srcip = entry.getKey();
			    Long ts = entry.getValue();
			   
			    //System.out.println(srcip+","+ts);
			    if((p.getTimestamp() - ts) > 100)
			    {
			    	this.getManager().onDetection(this, "RPL Selective Forwarding Attack", srcip,p);
			    }
			}
		}
			else 
				return;

		}
}
