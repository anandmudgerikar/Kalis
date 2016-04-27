package edu.purdue.idsforiot.knowledge;

import edu.purdue.idsforiot.modules.ModuleManager;

public class KnowledgeBase implements KnowledgeDependentEntity {
	
	// SINGLETON pattern
	private static KnowledgeBase instance = new KnowledgeBase();
	public static KnowledgeBase getInstance() {
		if (instance == null)
			instance = new KnowledgeBase();
		return instance;
	}
	
	
	private boolean isMultihop = false;
	private boolean isSinglehop = false;
	
	
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
	
}
