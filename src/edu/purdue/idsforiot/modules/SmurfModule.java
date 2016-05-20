package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.knowledge.TrafficType;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.WifiPacket;

public class SmurfModule extends DetectionModule {

	public SmurfModule(ModuleManager mgr) {
		super(mgr);
	}

	@Override
	public boolean shouldBeActive(KnowledgeBase kb) {
		if (!kb.getKnowledgeBooleanOrFalse("multihop"))
			return false;

		for (String value : kb.getAllPerNodeTrafficFrequencies(TrafficType.ICMPReply).values()) {
			if (Float.parseFloat(value) >= .8)
				return true;
		}
		return false;
	}

	@Override
	public void onNewPacket(Packet p) {
		if (!(p instanceof WifiPacket))
			return;

		if (this.getManager().getKnowledgeBase().getTrafficFrequency(TrafficType.ICMPReply, p.getDst()) >= 1)
			this.getManager().onDetection(this, "Smurf on " + p.getDst(), "?", p);
	}
}