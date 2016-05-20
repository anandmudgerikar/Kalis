package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.knowledge.TrafficType;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.WifiPacket;

import java.util.Iterator;

public class SmurfModule extends DetectionModule {

	public SmurfModule(ModuleManager mgr) {
		super(mgr);
	}

	@Override
	public boolean shouldBeActive(KnowledgeBase kb) {
		// TODO: determine right threshold for activation/deactivation of this (in terms of packets/second)

		Boolean mh = kb.getKnowledgeBoolean("multihop");
		if(mh != null && mh.booleanValue())
		{
			Iterator<String> iter = kb.getperNodes(TrafficType.ICMPResponse).iterator();

			while(iter.hasNext())
			{
				if(kb.getperNodeTrafficFrequency(TrafficType.ICMPResponse, iter.next()) >= 1)
				{
					return true;
				}
			}
			return false;
		}
		else
			return false;
	}

	@Override
	public void onNewPacket(Packet p) {
		if (!(p instanceof WifiPacket)) return;

		// TODO: is this (i.e., when more than N SYN pkts/sec, just alert) the best way to alert of SYN flood attacks?
		if (KnowledgeBase.getInstance().getperNodeTrafficFrequency(TrafficType.ICMPResponse, p.getDst()) >= 1)
			this.getManager().onDetection(this, "Smurf", p.getDst(), p);
	}
}