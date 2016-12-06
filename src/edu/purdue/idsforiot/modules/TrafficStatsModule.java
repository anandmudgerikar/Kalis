package edu.purdue.idsforiot.modules;

import java.util.HashMap;
import java.util.Map;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.knowledge.TrafficType;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.WifiPacket;
import edu.purdue.idsforiot.packets.ZigBeePacket;

public class TrafficStatsModule extends SensingModule {

	private float TIME_WINDOW = 5.0f;
	private float CHANGE_THRESH = 0.1f;

	private Map<TrafficType, Integer> currTrafficFrequency;
	private Map<TrafficType, Map<String, Integer>> currPerNodeFrequency;
	private long prevTimeStamp = 0;

	public TrafficStatsModule(ModuleManager mgr, KnowledgeBase kb) {
		super(mgr, kb);
		currTrafficFrequency = new HashMap<TrafficType, Integer>();
		currPerNodeFrequency = new HashMap<TrafficType, Map<String, Integer>>();
	}

	@Override
	public void onNewPacket(Packet p) {
		// detect traffic type (if supported)
		TrafficType type = null;
		if (p instanceof WifiPacket) {
			WifiPacket wp = (WifiPacket) p;
			if (wp.getProtocol().equals("TCP") && wp.getProtocolType().equals("SYN")) {
				type = TrafficType.TCPSYN;
			} else if (wp.getProtocol().equals("TCP") && wp.getProtocolType().equals("ACK")) {
				type = TrafficType.TCPACK;
			} else if (wp.getProtocol().equals("ICMP") && wp.getProtocolType().equals("Response")) {
				type = TrafficType.ICMPReply;
			}
		} else if (p instanceof ZigBeePacket) {
			type = TrafficType.ZigBeeCTP;
		}
		if (type == null)
			return;

		// Increment overall count for this traffic type
		Integer count = 0;
		if (prevTimeStamp == 0 || (p.getTimestamp() - prevTimeStamp) > TIME_WINDOW) {
			// reset the traffic stats every TIME_WINDOW seconds
			count = 0;
			currPerNodeFrequency = new HashMap<TrafficType, Map<String, Integer>>();
			prevTimeStamp = p.getTimestamp();
		} else {
			count = currTrafficFrequency.getOrDefault(type, 0);
		}
		count++;
		currTrafficFrequency.put(type, count);

		// only update frequency in KB when difference >= threshold (e.g. 1.0)
		float currRecordedFreq = this.getKnowledgeBase().getTrafficFrequency(type);
		float currFreq = currTrafficFrequency.getOrDefault(type, 0) / TIME_WINDOW;
		if (Math.abs(currFreq - currRecordedFreq) >= CHANGE_THRESH)
			this.getKnowledgeBase().setTrafficFrequency(type, currFreq);

		// keep per-node stats (ICMP Echo Reply and ZigbeeCTP for now)
		if (type == TrafficType.ICMPReply || type == TrafficType.ZigBeeCTP) {
			int pernodecount = this.getCurrPerNodeFrequencyOrDefault(type, p.getDst()) + 1;
			setCurrPerNodeFrequency(type, p.getDst(), this.getCurrPerNodeFrequencyOrDefault(type, p.getDst()) + 1);

			// only update frequency in KB when difference >= threshold (e.g. 1.0)
			float currRecordedPerNodeFreq = this.getKnowledgeBase().getTrafficFrequency(type, p.getDst());
			float currPerNodeFreq = pernodecount / TIME_WINDOW;
			if (Math.abs(currRecordedPerNodeFreq - currPerNodeFreq) >= CHANGE_THRESH)
				this.getKnowledgeBase().setTrafficFrequency(type, currPerNodeFreq, p.getDst());
		}
	}

	
	
	private Integer getCurrPerNodeFrequencyOrDefault(TrafficType type, String entity) {
		return currPerNodeFrequency.getOrDefault(type, new HashMap<String, Integer>()).getOrDefault(entity, 0);
	}
	private void setCurrPerNodeFrequency(TrafficType type, String entity, Integer value) {
		if (!currPerNodeFrequency.containsKey(type))
			currPerNodeFrequency.put(type, new HashMap<String, Integer>());
		currPerNodeFrequency.get(type).put(entity, value);
	}

}
