package edu.purdue.idsforiot;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class Utils {

	/** 
	 * Given a package name, attempts to reflect to find all classes within the package 
	 * on the local file system. 
	 *  
	 * @param packageName 
	 * @return 
	 */  
	@SuppressWarnings("rawtypes")
	public static Set<Class> getClassesInPackage(String packageName) {  
		Set<Class> classes = new HashSet<Class>();  
		String packageNameSlashed = packageName.replace(".", "/");
		// Get a File object for the package  
		URL directoryURL = Thread.currentThread().getContextClassLoader().getResource(packageNameSlashed);  
		if (directoryURL == null) {  
			System.out.println("Could not retrieve URL resource: " + packageNameSlashed);  
			return classes;  
		}  

		String directoryString = directoryURL.getFile();  
		if (directoryString == null) {  
			System.out.println("Could not find directory for URL resource: " + packageNameSlashed);  
			return classes;  
		}  

		File directory = new File(directoryString);  
		if (directory.exists()) {  
			// Get the list of the files contained in the package  
			String[] files = directory.list();  
			for (String fileName : files) {  
				// We are only interested in .class files  
				if (fileName.endsWith(".class")) {  
					// Remove the .class extension  
					fileName = fileName.substring(0, fileName.length() - 6);  
					try {  
						classes.add(Class.forName(packageName + "." + fileName));  
					} catch (ClassNotFoundException e) {  
						System.out.println(packageName + "." + fileName + " does not appear to be a valid class.");  
					}  
				}  
			}  
		} else {  
			System.out.println(packageName + " does not appear to exist as a valid package on the file system."); 
			System.out.println(directoryString);
			directoryString = "/root/kalis/idsforiot/bin/bin/edu/purdue/idsforiot/modules";

			//might be an emdedded device!
			directory = new File(directoryString);
			if (directory.exists()) {  
				// Get the list of the files contained in the package  
				String[] files = directory.list();  
				for (String fileName : files) {  
					// We are only interested in .class files  
					if (fileName.endsWith(".class")) {  
						// Remove the .class extension  
						fileName = fileName.substring(0, fileName.length() - 6);  
						try {  
							classes.add(Class.forName(packageName + "." + fileName));  
						} catch (ClassNotFoundException e) {  
							System.out.println(packageName + "." + fileName + " does not appear to be a valid class.");  
						}  
					}  
				}  
			}
			else
			{
				System.out.println(directoryString);
			}	
		}  
		return classes;  
	}  

}
