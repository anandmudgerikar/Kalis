package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.knowledge.TrafficType;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.WifiPacket;

public class SYNFloodModule extends DetectionModule {

	public SYNFloodModule(ModuleManager mgr) {
		super(mgr);
	}

	@Override
	public boolean shouldBeActive(KnowledgeBase kb) {
		// TODO: determine right threshold for activation/deactivation of this (in terms of packets/second)
		return kb.getTrafficFrequency(TrafficType.WiFiSYN) >= 1;
	}

	@Override
	public void onNewPacket(Packet p) {
		if (!(p instanceof WifiPacket)) return;
		
		// TODO: is this (i.e., when more than N SYN pkts/sec, just alert) the best way to alert of SYN flood attacks?
		if (KnowledgeBase.getInstance().getTrafficFrequency(TrafficType.WiFiSYN) >= 1)
			this.getManager().onDetection(this, "SYN Flood", p.getSrc(), p);
	}

}
