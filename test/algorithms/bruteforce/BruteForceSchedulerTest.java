package algorithms.bruteforce;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import algorithms.Scheduler;

import common.Configuration;
import common.Result;

public class BruteForceSchedulerTest {

	@Test
	public void testSchedule() {
		List<List<Integer>> expectedResult = new ArrayList<List<Integer>>();
		expectedResult.add(Arrays.asList(new Integer[]{2, 2, 2, null, null, 1, 1, null}));
		expectedResult.add(Arrays.asList(new Integer[]{3, 3, 3, 3, 1, null, null, null}));
		
		String configFileName = "testForBruteForce.txt";
		Configuration config;
		try {
			config = Configuration.loadFromFile(configFileName);
		} catch (NumberFormatException e) {
			System.err.println("File " + configFileName + " is in wrong format.");
			Assert.fail("File " + configFileName + " is in wrong format.");
			return;
		} catch (FileNotFoundException e) {
			System.err.println("File " + configFileName + " not found.");
			Assert.fail("File " + configFileName + " not found.");
			return;
		}
		Scheduler bfScheduler = new BruteForceScheduler(config);
		Result actualResult = bfScheduler.schedule();
		
		assertEquals(expectedResult, actualResult.getResultInListForm());
	}
}
