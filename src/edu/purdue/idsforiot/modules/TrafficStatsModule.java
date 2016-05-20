package edu.purdue.idsforiot.modules;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.knowledge.TrafficType;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.WifiPacket;
import edu.purdue.idsforiot.packets.ZigBeePacket;

public class TrafficStatsModule extends SensingModule {

	public TrafficStatsModule(ModuleManager mgr, KnowledgeBase kb) {
		super(mgr, kb);
		currTrafficFrequency = new HashMap<TrafficType, Float>();
		currICMPResponseFrequency = new HashMap<String, Float>();
	}

	private Map<TrafficType, Float> currTrafficFrequency;
	private Map<String, Float> currICMPResponseFrequency;
	private long prevTimeStamp = 0;

	@Override
	public void onNewPacket(Packet p) {
		TrafficType type = null;

		if (p instanceof WifiPacket) {
			WifiPacket wp = (WifiPacket) p;
			if ((p.getData().contains("S"))) {
				type = TrafficType.WiFiSYN;
			} else if ((p.getData().contains("."))) {
				type = TrafficType.WiFiACK;
			} else if (wp.getProtocol().equals("ICMP") && wp.getProtocolType().equals("Response")) {
				type = TrafficType.ICMPResponse;
			}
		} else if (p instanceof ZigBeePacket) {
			type = TrafficType.ZigBeeCTP;
		}

		if (type == null)
			return;

		float count = currTrafficFrequency.getOrDefault(type, 0.0f) * 5;

		if ((p.getTimestamp() - prevTimeStamp) > 5) { // reset the traffic stats
														// every 5 seconds
			count = 0;
			prevTimeStamp = p.getTimestamp();
			this.getKnowledgeBase().setTrafficFrequency(TrafficType.WiFiSYN, 0.0f);

			Set<String> set = currICMPResponseFrequency.keySet();
			Iterator<String> iter = set.iterator();
			while (iter.hasNext()) {
				currICMPResponseFrequency.put(iter.next(), 0.0f);
			}
		}

		count++;
		currTrafficFrequency.put(type, (count / 5));

		// Keep track of ICMP echo responses
		if (type == TrafficType.ICMPResponse) {
			System.out.println("Recevied an ICMP packet");
			if (currICMPResponseFrequency.containsKey(p.getDst())) {
				Float temp;
				temp = (currICMPResponseFrequency.get(p.getDst()) * 5);
				temp++;
				currICMPResponseFrequency.put(p.getDst(), (temp / 5));
			} else {
				Float temp = 0.0f;
				temp++;
				currICMPResponseFrequency.put(p.getDst(), (temp / 5));
			}

		}

		// only update the frequency in the KB when difference is more than a
		// threshold (e.g. 2)
		float currRecordedFreq = this.getKnowledgeBase().getTrafficFrequency(type);
		float currFreq = currTrafficFrequency.getOrDefault(type, 0.0f);
		if (Math.abs(currFreq - currRecordedFreq) >= 1)
			this.getKnowledgeBase().setTrafficFrequency(type, currFreq);

		// per node statistics, For now it is just for ICMP responses but it
		// should be for all types of packets
		if (type == TrafficType.ICMPResponse) {
			float currRecordedICMPFreq = this.getKnowledgeBase().getperNodeTrafficFrequency(TrafficType.ICMPResponse,
					p.getDst());
			if (Math.abs(currRecordedICMPFreq - (currICMPResponseFrequency.get(p.getDst()))) >= 1) {
				this.getKnowledgeBase().setperNodeTrafficFrequency(type, p.getDst(),
						currICMPResponseFrequency.get(p.getDst()));
			}
		}
	}

}
