package edu.purdue.idsforiot.knowledge;

import java.util.HashMap;
import java.util.Map;

import edu.purdue.idsforiot.modules.ModuleManager;

public class KnowledgeBase {
	
	// SINGLETON pattern
	private static KnowledgeBase instance = new KnowledgeBase();
	public static KnowledgeBase getInstance() {
		if (instance == null)
			instance = new KnowledgeBase();
		return instance;
	}
	
	
	private boolean isMultihop = false;
	private boolean isSinglehop = false;
	
	private boolean isMobile = false;
	
	private Map<TrafficType, Float> trafficFrequency;
	
	
	private KnowledgeBase() {
		this.trafficFrequency = new HashMap<TrafficType, Float>();
	}
	
	
	private void onKnowledgeChanged(String changedKnowledgePiece) {
		ModuleManager.getInstance().updateModules(this, changedKnowledgePiece);
	}
	

	public boolean isMultihop() {
		return isMultihop;
	}
	public void setMultihop(boolean isMultihop) {
		this.isMultihop = isMultihop;
		this.onKnowledgeChanged("isMultihop");
	}

	public boolean isSinglehop() {
		return isSinglehop;
	}
	public void setSinglehop(boolean isSinglehop) {
		this.isSinglehop = isSinglehop;
		this.onKnowledgeChanged("isSinglehop");
	}
	
	public boolean isMobile() {
		return isMobile;
	}
	public void setMobile(boolean isMobile) {
		this.isMobile = isMobile;
		this.onKnowledgeChanged("isMobile");
	}
	
	
	public float getTrafficFrequency(TrafficType trafficType) {
		return this.trafficFrequency.getOrDefault(trafficType, 0.0F);
	}
	public void setTrafficFrequency(TrafficType trafficType, float frequency) {
		this.trafficFrequency.put(trafficType, frequency);
		this.onKnowledgeChanged("trafficFrequency");
	}
	
}
