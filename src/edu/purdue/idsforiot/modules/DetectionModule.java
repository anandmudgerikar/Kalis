package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.knowledge.KnowledgeBase;

public abstract class DetectionModule extends Module {

	public DetectionModule(ModuleManager mgr) {
		super(mgr);
	}

	
	public abstract boolean shouldBeActive(KnowledgeBase kb);
	
}
