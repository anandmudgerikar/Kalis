package edu.purdue.idsforiot.modules;

import java.util.Iterator;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.knowledge.TrafficType;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.WifiPacket;

public class ICMPFloodModule extends DetectionModule {

	public ICMPFloodModule(ModuleManager mgr) {
		super(mgr);
	}

	@Override
	public boolean shouldBeActive(KnowledgeBase kb) {

		Iterator<String> iter = kb.getperNodes(TrafficType.ICMPResponse).iterator();
		while (iter.hasNext()) {
			if ((kb.getperNodeTrafficFrequency(TrafficType.ICMPResponse, iter.next())) >= 1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onNewPacket(Packet p) {
		if (!(p instanceof WifiPacket)) return;

		if (KnowledgeBase.getInstance().getperNodeTrafficFrequency(TrafficType.ICMPResponse, p.getDst()) >= 1)
			this.getManager().onDetection(this, "ICMP Flood", p.getDst(), p);
	}
	
}
