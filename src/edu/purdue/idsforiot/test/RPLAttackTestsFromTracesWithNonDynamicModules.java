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

public class RPLAttackTestsFromTracesWithNonDynamicModules {

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
	public void testIntegrityAttack() throws InvocationTargetException {
		String expected = "DETECTED: RPL Integrity Attack by Entity fe80::ff:fe00:ccbe (Module RPLIntegrityModule) [RPL3]\n";

		try {
			this.ids.start("", "data/rpl_integrity_attack.txt",false);
			assertEquals(expected, errContent.toString());
		} catch (IDSforIoTException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testDelayAttack() throws InvocationTargetException {
		String expected = "DETECTED: RPL Delay Attack by Entity fe80::ff:fe00:ccbe (Module RPLDelayModule) [RPL2]\n"+
				"DETECTED: RPL Delay Attack by Entity fe80::ff:fe00:aabe (Module RPLDelayModule) [RPL2]\n";

		try {
			this.ids.start("", "data/rpl_delay_attack.txt",false);
			assertEquals(expected, errContent.toString());
		} catch (IDSforIoTException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testRPLSelectiveForwardingAttack() throws InvocationTargetException {
		String expected = "DETECTED: RPL Selective Forwarding Attack by Entity fe80::ff:fe00:aabe (Module RPLSelectiveForwardingModule) [RPL]\n";

		try {
			this.ids.start("", "data/rpl_selforwarding_attack.txt",false);
			assertEquals(expected, errContent.toString());
		} catch (IDSforIoTException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testRPLReplayAttack() throws InvocationTargetException {
		String expected = "DETECTED: RPL Replay Attack by Entity fe80::ff:fe00:ccbe (Module RPLReplayModule) [RPL]\n"+
				"DETECTED: RPL Replay Attack by Entity fe80::ff:fe00:aabe (Module RPLReplayModule) [RPL]\n";

		try {
			this.ids.start("", "data/rpl_replay_attack.txt",false);
			assertEquals(expected, errContent.toString());
		} catch (IDSforIoTException e) {
			e.printStackTrace();
			fail();
		}
	}

}