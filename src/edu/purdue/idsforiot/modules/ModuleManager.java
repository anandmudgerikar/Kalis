package edu.purdue.idsforiot.modules;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.lang.Class;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import edu.purdue.idsforiot.IDSforIoTException;
import edu.purdue.idsforiot.Utils;
import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.packets.Packet;

public final class ModuleManager {

	// SINGLETON pattern
	private static ModuleManager instance = new ModuleManager();
	public static ModuleManager getInstance() {
		if (instance == null)
			instance = new ModuleManager();
		return instance;
	}

	private Map<String, Module> allModules;
	private Map<String, Module> activeDetectionModules;
	private Map<String, Module> activeSensingModules;
	private Map<String, Module> inactiveModules;

	
	private ModuleManager() {
		this.allModules = new HashMap<String, Module>();
		this.activeDetectionModules = new HashMap<String, Module>();
		this.activeSensingModules = new HashMap<String, Module>();
		this.inactiveModules = new HashMap<String, Module>();
	}


	public void start() throws IDSforIoTException {
		this.loadAllModules();
		
		try {
			// read from config file which modules to start
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("data/config.txt")));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.equals("") || line.startsWith("//")) continue; // skip comments
				this.activateModule(line);
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/// Instantiate all modules, initially inactive
	private void loadAllModules() throws IDSforIoTException {
		for (Class<?> modclass : Utils.getClassesInPackage(this.getClass().getPackage().getName())) {
			// only go through non-abstract subclasses of Module
			if (Module.class.isAssignableFrom(modclass) && !Modifier.isAbstract(modclass.getModifiers()))
				this.loadModule(modclass);
		}
	}
	private Module loadModule(Class<?> moduleClass) throws IDSforIoTException {
		Module module = null;
		try {
			if (DetectionModule.class.isAssignableFrom(moduleClass)) {
				// it is a DetectionModule
				module = (Module) moduleClass.getConstructor(this.getClass()).newInstance(this);
			} else if (SensingModule.class.isAssignableFrom(moduleClass)) {
				// it is a SensingModule
				module = (Module) moduleClass.getConstructor(this.getClass(), KnowledgeBase.class).newInstance(this, KnowledgeBase.getInstance());
			} else {
				// it is something else (wrong!)
				throw new IDSforIoTException("Module " + moduleClass.getSimpleName() + " is neither Detection nor Sensing module, and cannot be initialized.");
			}
			this.allModules.put(moduleClass.getSimpleName(), module);
			this.inactiveModules.put(moduleClass.getSimpleName(), module);

		} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException e) {
			System.out.println(moduleClass.getSimpleName() + ": could not load Module.");
			e.printStackTrace();
			throw new IDSforIoTException(e);
		}
		
		return module;
	}


	private Module activateModule(String moduleName) {
		if (!this.inactiveModules.containsKey(moduleName))
			return null;
		Module module = this.inactiveModules.get(moduleName);
		return this.activateModule(module);
	}
	private Module activateModule(Module module) {
		String moduleName = module.getClass().getSimpleName();
		module.start();
		this.inactiveModules.remove(moduleName);
		if (module instanceof DetectionModule)
			this.activeDetectionModules.put(moduleName, module);
		else
			this.activeSensingModules.put(moduleName, module);
			
		return module;
	}
	private Module deactivateModule(Module module) {
		String moduleName = module.getClass().getSimpleName();
		module.start();
		this.activeDetectionModules.remove(moduleName);
		this.activeSensingModules.remove(moduleName);
		this.inactiveModules.put(moduleName, module);
		return module;
	}

	
	
	
	/// Leverages info from KnowledgeBase to update which Modules are activated/deactivated
	public void updateModules(KnowledgeBase kb, String changedKnowledgePiece) {
		// iterate over all DetectionModules
		for (Module m : this.allModules.values()) {
			if (m instanceof DetectionModule) {
				boolean knowledgeInModule = false;
				boolean knowledgeInKB = false;
				try {
					knowledgeInModule = (boolean) m.getClass().getMethod(changedKnowledgePiece).invoke(m);
					knowledgeInKB = (boolean) KnowledgeBase.class.getMethod(changedKnowledgePiece).invoke(kb);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}

				// is this module relevant according to current KB?
				if (knowledgeInModule == knowledgeInKB) {
					this.activateModule(m);
				} else {
					this.deactivateModule(m);
				}
			}
		}
	}

	
	
	

	
	
	public void onNewPacket(Packet p) {
		// notify all active modules of the new packet, SensingModules first as they could change the active modules
		for (Module m : this.activeSensingModules.values())
			m.onNewPacket(p);
		for (Module m : this.activeDetectionModules.values())
			m.onNewPacket(p);
	}

	/// Called when an attack is detected, providing a packet as extra info
	public void onDetection(Module module, String attackName, String suspect, Packet p) {
		System.err.format("DETECTED: %s attack by Entity %s (Module %s) [%s]\n", attackName, suspect,
				module.getClass().getSimpleName(), p.getData());
	}
	
	
}
