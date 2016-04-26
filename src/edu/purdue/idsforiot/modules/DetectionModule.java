package edu.purdue.idsforiot.modules;

import edu.purdue.idsforiot.knowledge.KnowledgeDependentEntity;

public abstract class DetectionModule extends Module implements KnowledgeDependentEntity {

	public DetectionModule(ModuleManager mgr) {
		super(mgr);
	}

}
