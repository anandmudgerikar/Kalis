package edu.purdue.idsforiot.knowledge;

import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.purdue.idsforiot.IDS;

public class KnowledgeBase {

	private IDS ids;
	private NavigableMap<String, String> knowggets;

	public KnowledgeBase(IDS ids) {
		this.ids = ids;
		knowggets = new TreeMap<String, String>();
	}

	private void onKnowledgeChanged(String changedKnowledgePiece) {
		this.ids.getModuleManager().updateModules(this, changedKnowledgePiece);
	}

	private String composeKey(String label, String creator, String entity) {
		return (creator != null ? creator : this.ids.getIDSNodeId()) + "$" + label
				+ (entity != null ? "@" + entity : "");
	}
	
	public <T> void setKnowledge(String label, T value) {
		this.setKnowledge(label, value, null, null);
	}

	public <T> void setKnowledge(String label, T value, String creator) {
		this.setKnowledge(label, value, creator, null);
	}

	public <T> void setKnowledge(String label, T value, String creator, String entity) {
		String key = composeKey(label, creator, entity);
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
		String key = composeKey(label, creator, entity);
		return this.knowggets.getOrDefault(key, null);
	}
	

	public SortedMap<String, String> getRawKnowledgeByPrefix(String prefix) {
		return this.knowggets.subMap(prefix, prefix + Character.MAX_VALUE);
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

	public boolean getKnowledgeBooleanOrFalse(String label) {
		Boolean b = this.getKnowledgeBoolean(label, null, null);
		return b != null ? b.booleanValue() : false;
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

	
	
	/* UTILITY METHODS for convenience */

	public float getTrafficFrequency(TrafficType trafficType) {
		return this.getTrafficFrequency(trafficType, null);
	}
	public float getTrafficFrequency(TrafficType trafficType, String entity) {
		Float f = this.getKnowledgeFloat("trafficFrequency." + trafficType.toString(), null, entity);
		return f != null ? f : 0.0F;
	}
	
	public SortedMap<String, String> getAllPerNodeTrafficFrequencies(TrafficType trafficType) {
		return this.getRawKnowledgeByPrefix(this.composeKey("trafficFrequency." + trafficType.toString(), null, null));
	}

	public void setTrafficFrequency(TrafficType trafficType, float frequency) {
		this.setTrafficFrequency(trafficType, frequency, null);
	}
	public void setTrafficFrequency(TrafficType trafficType, float frequency, String entity) {
		this.setKnowledge("trafficFrequency." + trafficType.toString(), frequency, null, entity);
	}
	

}
