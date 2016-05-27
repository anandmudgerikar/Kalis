package edu.purdue.idsforiot.modules;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.lang.Class;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import edu.purdue.idsforiot.IDS;
import edu.purdue.idsforiot.IDSforIoTException;
import edu.purdue.idsforiot.Utils;
import edu.purdue.idsforiot.knowledge.KnowledgeBase;
import edu.purdue.idsforiot.packets.Packet;

public final class ModuleManager {

	private Map<String, Class<Module>> allModules;
	private Map<String, DetectionModule> activeDetectionModules;
	private Map<String, SensingModule> activeSensingModules;

	private IDS ids;

	public ModuleManager(IDS ids) {
		this.ids = ids;
		this.allModules = new HashMap<String, Class<Module>>();
		this.activeDetectionModules = new HashMap<String, DetectionModule>();
		this.activeSensingModules = new HashMap<String, SensingModule>();
	}

	public void start() throws IDSforIoTException {
		this.discoverAllModules();

		try {
			// read from config file which modules to start
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("data/config.txt")));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.equals("") || line.startsWith("//"))
					continue; // skip comments
				Class<Module> moduleClass = this.allModules.getOrDefault(line, null);
				if (moduleClass != null) this.activateModule(moduleClass);
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/// Discover all modules, initially inactive
	@SuppressWarnings("unchecked")
	private void discoverAllModules() throws IDSforIoTException {
		for (Class<?> moduleClass : Utils.getClassesInPackage(this.getClass().getPackage().getName())) {
			// only go through non-abstract subclasses of Module
			if (Module.class.isAssignableFrom(moduleClass) && !Modifier.isAbstract(moduleClass.getModifiers()))
				this.allModules.put(moduleClass.getSimpleName(), (Class<Module>)moduleClass);
		}
	}

	private void activateModule(Class<Module> moduleClass) {
		// check if module is already active
		String moduleName = moduleClass.getSimpleName();
		if (this.activeSensingModules.containsKey(moduleName) || this.activeDetectionModules.containsKey(moduleName)) {
			System.out.println("Module " + moduleName + " is already active");
		} else {
			// load the Module from class
			Module module = null;
			try {
				if (DetectionModule.class.isAssignableFrom(moduleClass)) {
					// it is a DetectionModule
					module = (Module) moduleClass.getConstructor(this.getClass()).newInstance(this);
					this.activeDetectionModules.put(moduleName, (DetectionModule)module);
				} else if (SensingModule.class.isAssignableFrom(moduleClass)) {
					// it is a SensingModule
					module = (Module) moduleClass.getConstructor(this.getClass(), KnowledgeBase.class).newInstance(this, this.ids.getKnowledgeBase());
					this.activeSensingModules.put(moduleName, (SensingModule)module);
				} else {
					// it is something else (wrong!)
					throw new IDSforIoTException("Module " + moduleName + " is neither Detection nor Sensing module, and cannot be initialized.");
				}
				module.start();
				System.out.println("Activated module " + moduleName);

			} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IDSforIoTException e) {
				System.out.println(moduleName + ": could not load Module.");
				e.printStackTrace();
				// throw new IDSforIoTException(e);
			}
		}
	}

	private void deactivateModule(String moduleName) {
		// check if module is already inactive
		if (this.activeSensingModules.containsKey(moduleName) || this.activeDetectionModules.containsKey(moduleName)) {
			this.activeDetectionModules.remove(moduleName);
			this.activeSensingModules.remove(moduleName);
			
			// suggests garbage collection (to free memory)
			System.gc();
	
			System.out.println("Deactivated module " + moduleName);
		}
	}

	/// Leverages info from KnowledgeBase to update which Modules are activated/deactivated
	public void updateModules(KnowledgeBase kb, String changedKnowledgePiece) {
		// iterate over all DetectionModules
		for (Class<Module> moduleClass : this.allModules.values()) {
			if (DetectionModule.class.isAssignableFrom(moduleClass)) {
				try {
					// is this module relevant according to current KB?
					Method sba = moduleClass.getMethod("shouldBeActive", KnowledgeBase.class);
					boolean r = (boolean) sba.invoke(null, this.getKnowledgeBase());
					if (r) {
						this.activateModule(moduleClass);
					} else {
						this.deactivateModule(moduleClass.getSimpleName());
					}
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		System.err.format("DETECTED: %s by Entity %s (Module %s) [%s]\n", attackName, suspect,
				module.getClass().getSimpleName(), p.getData());
	}

	public KnowledgeBase getKnowledgeBase() {
		return this.ids.getKnowledgeBase();
	}

}
