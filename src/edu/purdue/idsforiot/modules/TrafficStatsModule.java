package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.knowledge.TrafficType;
import edu.purdue.idsforiot.packets.Packet;

public class TrafficStatsModule extends SensingModule {

	public TrafficStatsModule(ModuleManager mgr, KnowledgeBase kb) {
		super(mgr, kb);
	}
	
	@Override
	public void onNewPacket(Packet p) {
		// TODO: calculate traffic frequency (packets per second?) for each traffic type listed in the edu.purdue.idsforiot.knowledge.TrafficType enum
		TrafficType type;
		
		if(p.getData() == "S" )
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
		
		float frequency = this.getKnowledgeBase().getTrafficFrequency(type);
		
		if(frequency == 0)
		{
			frequency = 1/5;  //we are calculating these over time periods of 5
		}
		else
		{
			frequency = ((frequency*5) + 1)/5;
		}
			
		// only update the frequency in the KB when difference is more than a threshold (e.g. 2)
		float currFreq = this.getKnowledgeBase().getTrafficFrequency(type);
		if (Math.abs(currFreq - frequency) >= 1)
			this.getKnowledgeBase().setTrafficFrequency(type, frequency);
	}

}
