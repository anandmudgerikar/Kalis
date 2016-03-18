
import java.io.*;

import net.tinyos.packet.*;
import net.tinyos.util.*;
import net.tinyos.message.*;

public class Listen {
	public static void main(String args[]) throws IOException {
		String source = null;
		PacketSource reader;
		if (args.length == 2 && args[0].equals("-comm")) {
			source = args[1];
		}
		else if (args.length > 0) {
			System.err.println("usage: java net.tinyos.tools.Listen [-comm PACKETSOURCE]");
			System.err.println("       (default packet source from MOTECOM environment variable)");
			System.exit(2);
		}
		if (source == null) {	
			reader = BuildSource.makePacketSource();
		}
		else {
			reader = BuildSource.makePacketSource(source);
		}
		if (reader == null) {
			System.err.println("Invalid packet source (check your MOTECOM environment variable)");
			System.exit(2);
		}

		File file = new File("/home/odroid/tinyos-main/project/ids/data/Rawpacketcapture.txt");
		OutputStream fileWriter = new FileOutputStream(file,true);

		File file2 = new File("/home/odroid/tinyos-main/project/ids/data/CSVpacketcapture.txt");
		FileOutputStream fileWriter2 = new FileOutputStream(file2,true);

		try {
			reader.open(PrintStreamMessenger.err);
			for (;;) {
				byte[] packet = reader.readPacket();

				fileWriter.write(packet, 0, 10);

				//System.out.println(packet[0]);


				//Dump.printPacket(fileWriter, packet); //print writer is not supported, change this later**
				//fileWriter.write();
				System.out.println();
				System.out.flush();
			}
		}
		catch (IOException e) {
			System.err.println("Error on " + reader.getName() + ": " + e);
		}
	}
}
