package edu.purdue.idsforiot.modules;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.Class;
import java.lang.reflect.InvocationTargetException;

import edu.purdue.idsforiot.IDSforIoTException;
import edu.purdue.idsforiot.packets.Packet;
import edu.purdue.idsforiot.packets.WifiPacket;

public final class ModuleManager {

	// SINGLETON pattern
	private static ModuleManager instance = new ModuleManager();

	public static ModuleManager getInstance() {
		if (instance == null)
			instance = new ModuleManager();
		return instance;
	}

	private List<Module> modules;

	private ModuleManager() {
		this.modules = new ArrayList<Module>();
	}

	public void start() throws IDSforIoTException {
		try {
			// read from config file which modules to start
			FileInputStream configstream = new FileInputStream("data/config.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(configstream));
			String line;
			while ((line = br.readLine()) != null) {
				try {
					Module defmodule = (Module) Class.forName("edu.purdue.idsforiot.modules." + line)
							.getConstructor(this.getClass()).newInstance(this);
					this.modules.add(defmodule);
					defmodule.start();

				} catch (ClassNotFoundException e) {
					System.out.println(line + ": no such Module exists.");
					e.printStackTrace();
				} catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException
						| SecurityException | InstantiationException | IllegalAccessException e) {
					System.out.println(line + ": could not load Module.");
					e.printStackTrace();
					br.close();
					throw new IDSforIoTException(e);
				}
			}
			br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onNewPacket(Packet p) {
		// notify all active modules of the new packet
		for (Module m : this.modules)
			m.onNewPacket(p);
	}

	public void onNewPacket(WifiPacket p) {
		// notify all active modules of the new packet
		for (Module m : this.modules)
			m.onNewPacket(p);
	}

	/// Called when an attack is detected, optionally providing a packet as
	/// extra info
	public void onDetection(Module module, String attackName, String suspect, Packet p) {
		System.err.format("DETECTED: %s attack by Entity %s (Module %s) [%s]\n", attackName, suspect,
				module.getClass().getSimpleName(), p.getData());
	}

}
