package edu.purdue.idsforiot.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.purdue.idsforiot.IDS;
import edu.purdue.idsforiot.IDSforIoTException;

public class AttackTestsFromTracesWithNonDynamicModules {

	protected IDS ids;
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUp() throws Exception {
		this.ids = new IDS();
	}

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
	}

	@Test
	public void testSelectiveForwarding() throws InvocationTargetException {
		String expected = "DETECTED: Selective Forwarding by Entity 0x01 (Module SelectiveForwardingModule) [e]\n"
				+ "DETECTED: Selective Forwarding by Entity 0x03 (Module SelectiveForwardingModule) [g]\n"
				+ "DETECTED: Selective Forwarding by Entity 0x03 (Module SelectiveForwardingModule) [h]\n"
				+ "DETECTED: Selective Forwarding by Entity 0x01 (Module SelectiveForwardingModule) [l]\n";

		try {
			this.ids.start("", "data/ZigBeeSelFwd.txt");
			assertEquals(expected, errContent.toString());
		} catch (IDSforIoTException e) {
			e.printStackTrace();
			fail();
		}
	}

	
	@Test
	public void testICMPReplyFlood() throws InvocationTargetException {
		String expected = "DETECTED: ICMP Flood on 10.0.0.13 by Entity ? (Module ICMPFloodModule) [e]\n"
				+ "DETECTED: Smurf on 10.0.0.13 by Entity ? (Module SmurfModule) [e]\n";

		try {
			this.ids.start("", "data/ICMPFlood.txt", false);
			assertEquals(expected, errContent.toString());
		} catch (IDSforIoTException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testSmurf() throws InvocationTargetException {
		String expected = "DETECTED: ICMP Flood on 10.0.0.13 by Entity ? (Module ICMPFloodModule) [i]\n"
				+ "DETECTED: Smurf on 10.0.0.13 by Entity ? (Module SmurfModule) [i]\n";

		try {
			this.ids.start("", "data/Smurf.txt");
			assertEquals(expected, errContent.toString());
		} catch (IDSforIoTException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testTCPSYNFlood() throws InvocationTargetException {
		String expected = "DETECTED: SYN Flood by Entity 10.0.0.12 (Module SYNFloodModule) [e]\n";

		try {
			this.ids.start("", "data/WifiTCPSYNFlood.txt");
			assertEquals(expected, errContent.toString());
		} catch (IDSforIoTException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testZigBeeJamming() throws InvocationTargetException {
		String expected = "DETECTED: Zigbee Jamming on node 01 (Module ZigBeeJammingModule) [e]\n";

		try {
			this.ids.start("", "data/ZigBeeJamming.txt");
			assertEquals(expected, errContent.toString());
		} catch (IDSforIoTException e) {
			e.printStackTrace();
			fail();
		}
	}

}
