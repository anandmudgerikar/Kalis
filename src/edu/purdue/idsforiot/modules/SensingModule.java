package edu.purdue.idsforiot.modules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.knowledge.TrafficType;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.WifiPacket;
import edu.purdue.idsforiot.packets.ZigBeePacket;
import edu.purdue.idsforiot.packets.Edge;

public abstract class SensingModule extends Module {
	
	private KnowledgeBase kb;
	private Map<Edge,Integer> rssiHistory;
	

	public SensingModule(ModuleManager mgr, KnowledgeBase kb) {
		super(mgr);
		this.kb = kb;
		kb.setSinglehop(true);
		this.rssiHistory = new HashMap<Edge,Integer>();
	}


	protected KnowledgeBase getKnowledgeBase() {
		return this.kb;
	}
	
	protected void checkTopology()
	{
		int trav_len = 0;
		
	}
	
	@Override
	public void onNewPacket(Packet p) {
		if (!(p instanceof ZigBeePacket)) return;
		
		//recording RSSI values for all edges
		Edge edge = new Edge(p.getSrc(),p.getDst());
		
		if(rssiHistory.containsKey(edge)) //if old edge
		{
			if(rssiHistory.get(edge) != ((ZigBeePacket) p).getRSSI()) // mobility
			{
				this.rssiHistory.put(edge,((ZigBeePacket) p).getRSSI());
				kb.setMobile(true);
			}	
		}
		else // new edge
		{
			//checking topology
			Set<Edge> set = rssiHistory.keySet();
		    Iterator<Edge> iter = set.iterator();
		    while (iter.hasNext()) {
		        Edge temp = (Edge) iter.next();
		        if(temp.getDest() != p.getDst())
		        {
		        	kb.setMultihop(true);
		        }
		      }
		    
		    this.rssiHistory.put(edge,((ZigBeePacket) p).getRSSI());
		}
	}
	
}

	