package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;

public abstract class SensingModule extends Module {
	
	private KnowledgeBase kb;

	public SensingModule(ModuleManager mgr, KnowledgeBase kb) {
		super(mgr);
		this.kb = kb;
	}


	protected KnowledgeBase getKnowledgeBase() {
		return this.kb;
	}
	
}
