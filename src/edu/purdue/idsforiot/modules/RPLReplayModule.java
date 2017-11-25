package edu.purdue.idsforiot.modules;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.RplPacket;

public class RPLReplayModule extends DetectionModule {

	private HashMap<String, Queue<Packet>> previouspackets;

	public RPLReplayModule(ModuleManager mgr) {
		super(mgr);
		// TODO Auto-generated constructor stub
	}

	public void start() {
		// initializing the queues
		this.previouspackets = new HashMap<String, Queue<Packet>>();
		super.start();
	}



	public static boolean shouldBeActive(KnowledgeBase kb) {
		if(kb.getKnowledgeBooleanOrFalse("rplauth"))
			return false;

		else
			return true;
	}

	public static String[] subscribedKnowggets() {
		return new String[] { "rplauth" };
	}


	@Override
	public void onNewPacket(Packet p) {
		// TODO Auto-generated method stub
		if(p instanceof RplPacket)
		{
			if(this.previouspackets.containsKey(p.getSrc()))
			{
				if((previouspackets.get(p.getSrc()).contains(p)))
				{
					//override equals method in packet class for appropriate field checking..
					this.getManager().onDetection(this, "RPL Replay Attack", p.getSrc(),p);
					
				}

					this.previouspackets.get(p.getSrc()).add(p);
			}
			else
			{
				this.previouspackets.put(p.getSrc(),new LinkedList<Packet>());
				this.previouspackets.get(p.getSrc()).add(p);
			}		
		}
		else 
			return;

	}

}
