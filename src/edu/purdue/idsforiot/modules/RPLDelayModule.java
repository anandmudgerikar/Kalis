package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.RplPacket;
import java.util.HashMap;
import java.util.Queue;

public class RPLDelayModule extends DetectionModule {

	private HashMap<String, Long> lastDAOts;

	public RPLDelayModule(ModuleManager mgr) {
		super(mgr);
		this.lastDAOts = new HashMap<String, Long>();
		// TODO Auto-generated constructor stub
	}

	public void start() {
		// initializing the queues		
		super.start();
	}



	public static boolean shouldBeActive(KnowledgeBase kb) {
		if(kb.getKnowledgeBooleanOrFalse("rplauth") || kb.getKnowledgeBooleanOrFalse("timesynch"))
		{
			return false;
		}	
		else
			return true;
	}

	public static String[] subscribedKnowggets() {
		return new String[] { "rplauth", "timesynch" };
	}


	@Override
	public void onNewPacket(Packet p) {
		// TODO Auto-generated method stub
		if(p instanceof RplPacket)
		{
			if(((RplPacket) p).getmessage_type().equals("DAO"))
			{
				if(this.lastDAOts.containsKey(p.getSrc()))
				{
					if((p.getTimestamp() - this.lastDAOts.get(p.getSrc())) > 40)
					{
						this.getManager().onDetection(this, "RPL Delay Attack", p.getSrc(),p);
					}
					else
					{
						this.lastDAOts.put(p.getSrc(),p.getTimestamp());
					}
				}

				this.lastDAOts.put(p.getSrc(),p.getTimestamp());
			}
		}
		else 
			return;

	}


}
