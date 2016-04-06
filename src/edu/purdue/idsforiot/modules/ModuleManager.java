package edu.purdue.idsforiot.modules;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.Class;

import edu.purdue.idsforiot.packets.Packet;

public final class ModuleManager {

	// SINGLETON pattern
	private static ModuleManager instance = new ModuleManager();

	private ModuleManager() {
		this.modules = new ArrayList<Module>();
	}

	public static ModuleManager getInstance() {
		if (instance == null)
			instance = new ModuleManager();
		return instance;
	}

	private List<Module> modules;

	public void start() throws InstantiationException, IllegalAccessException {
		// read from config file which modules to start for now
		try {
			FileInputStream configstream = new FileInputStream("data/config.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(configstream));
			String line;
			while ((line = br.readLine()) != null) {
				// TODO: use reflection to match the string "ModuleName" with
				// the class ModuleName and start that module
				Module defmodule = (Module) Class.forName("edu.purdue.idsforiot.modules." + line).newInstance();
				this.modules.add(defmodule);
				defmodule.start();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("No such Module exists.");
			e.printStackTrace();
		}
	}

	public void onNewPacket(Packet p) {
		// notify all active modules of the new packet
		for (Module m : this.modules)
			m.onNewPacket(p);
	}

}
