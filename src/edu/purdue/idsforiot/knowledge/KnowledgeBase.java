package edu.purdue.idsforiot.knowledge;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.purdue.idsforiot.modules.ModuleManager;

public class KnowledgeBase {

	// SINGLETON pattern
	private static KnowledgeBase instance = new KnowledgeBase();

	public static KnowledgeBase getInstance() {
		if (instance == null)
			instance = new KnowledgeBase();
		return instance;
	}

//	private boolean isMultihop = false;
//	private boolean isSinglehop = false;
//	private boolean isMobile = false;

	private Map<TrafficType, Float> trafficFrequency;
	
	private Map<String, Float> currICMPResponseFrequency; // we cant store this in knowgets as it is maintaned for each node

	private Map<String, String> knowggets;

	private KnowledgeBase() {
		knowggets = new HashMap<String, String>();
		currICMPResponseFrequency = new HashMap<String, Float>();
	}

	private void onKnowledgeChanged(String changedKnowledgePiece) {
		ModuleManager.getInstance().updateModules(this, changedKnowledgePiece);
	}

	public <T> void setKnowledge(String label, T value) {
		this.setKnowledge(label, value, null, null);
	}
	public <T> void setKnowledge(String label, T value, String creator) {
		this.setKnowledge(label, value, creator, null);
	}
	public <T> void setKnowledge(String label, T value, String creator, String entity) {
		String key = (entity != null ? entity : ModuleManager.getInstance().getIDSNodeId()) + "$" + label + (entity != null ? "@" + entity : "");
		String newValue = value.toString();
		String oldValue = this.knowggets.getOrDefault(key, null);
		if (oldValue == null || oldValue != newValue) {
			this.knowggets.put(key, newValue);
			this.onKnowledgeChanged(label);
		}
	}

	public String getRawKnowledge(String label, String creator) {
		return this.getRawKnowledge(label, creator, null);
	}
	public String getRawKnowledge(String label, String creator, String entity) {
		String key = (entity != null ? entity : ModuleManager.getInstance().getIDSNodeId()) + "$" + label + (entity != null ? "@" + entity : "");
		return this.knowggets.getOrDefault(key, null);
	}




	/* BASIC KNOWLEDGE GETTERS for primitive types */

	public Integer getKnowledgeInteger(String label) {
		return this.getKnowledgeInteger(label, null, null);
	}
	public Integer getKnowledgeInteger(String label, String creator) {
		return this.getKnowledgeInteger(label, creator, null);
	}
	public Integer getKnowledgeInteger(String label, String creator, String entity) {
		String value = this.getRawKnowledge(label, creator, entity);
		return value != null ? Integer.parseInt(value) : null;
	}

	public Float getKnowledgeFloat(String label) {
		return this.getKnowledgeFloat(label, null, null);
	}
	public Float getKnowledgeFloat(String label, String creator) {
		return this.getKnowledgeFloat(label, creator, null);
	}
	public Float getKnowledgeFloat(String label, String creator, String entity) {
		String value = this.getRawKnowledge(label, creator, entity);
		return value != null ? Float.parseFloat(value) : null;
	}

	public Boolean getKnowledgeBoolean(String label) {
		return this.getKnowledgeBoolean(label, null, null);
	}
	public Boolean getKnowledgeBoolean(String label, String creator) {
		return this.getKnowledgeBoolean(label, creator, null);
	}
	public Boolean getKnowledgeBoolean(String label, String creator, String entity) {
		String value = this.getRawKnowledge(label, creator, entity);
		return value != null ? Boolean.parseBoolean(value) : null;
	}

//	public boolean isMobile() {
//		return isMobile;
//	}
//	public void setMobile(boolean isMobile) {
//		this.isMobile = isMobile;
//		this.onKnowledgeChanged("isMobile");
//	}


	/* UTILITY METHODS for convenience */

	public float getTrafficFrequency(TrafficType trafficType) {
		Float f = this.getKnowledgeFloat("trafficFrequency." + trafficType.toString());
		return f != null ? f : 0.0F;
	}

	public void setTrafficFrequency(TrafficType trafficType, float frequency) {
		this.setKnowledge("trafficFrequency." + trafficType.toString(), frequency);
	}

	// for per node Frequencies
	public float getperNodeTrafficFrequency(TrafficType trafficType, String key) {
		
		if(currICMPResponseFrequency.containsKey(key))
		return currICMPResponseFrequency.get(key);
		else
		return 0.0f;	
	}
	
	public Set<String> getperNodes(TrafficType trafficType) {
		return currICMPResponseFrequency.keySet();
	}

	public void setperNodeTrafficFrequency(TrafficType trafficType, String key, Float value) {
		this.currICMPResponseFrequency.put(key, value);
	}
	
}
