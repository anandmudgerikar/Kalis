package edu.purdue.idsforiot.modules;

import java.util.HashMap;
import java.util.Map;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.knowledge.TrafficType;
import edu.purdue.idsforiot.packets.Packet;

public class TrafficStatsModule extends SensingModule {

	public TrafficStatsModule(ModuleManager mgr, KnowledgeBase kb) {
		super(mgr, kb);
		currtrafficFrequency = new HashMap<TrafficType, Float>();
	}
	
	private Map<TrafficType, Float> currtrafficFrequency;
	private long prevTimeStamp = 0;
	
	@Override
	public void onNewPacket(Packet p) {
		// TODO: calculate traffic frequency (packets per second?) for each traffic type listed in the edu.purdue.idsforiot.knowledge.TrafficType enum
		TrafficType type;
		
		if((p.getData().contains("S")))
		{
		 type = TrafficType.WiFiSYN; // TODO: get the right type depending on the parameter p
		 
		}
		else if((p.getData().contains(".") ))
		{
			type = TrafficType.WiFiACK;
		}
		else
		{
			type = TrafficType.ZigBeeCTP;
		}
		
		 // TODO: keep track of traffic and compute frequency as packets of this type per second
		
		float count = currtrafficFrequency.getOrDefault(type, 0.0f)*5;
		
		if((p.getTimestamp()-prevTimeStamp) > 5)
			{
				count = 0;
				prevTimeStamp = p.getTimestamp();
				this.getKnowledgeBase().setTrafficFrequency(TrafficType.WiFiSYN,0.0f);
			}
		
		count++;
		currtrafficFrequency.put(type,(count/5));
		
		// only update the frequency in the KB when difference is more than a threshold (e.g. 2)
		float currRecordedFreq = this.getKnowledgeBase().getTrafficFrequency(type);
		float currFreq = currtrafficFrequency.getOrDefault(type, 0.0f);
		if (Math.abs(currFreq - currRecordedFreq ) >= 1)
			this.getKnowledgeBase().setTrafficFrequency(type, currFreq);
		
			
		
	}

}
