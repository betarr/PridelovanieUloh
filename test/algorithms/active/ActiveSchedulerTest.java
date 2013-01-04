package algorithms.active;

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

public class ActiveSchedulerTest {

	@Test
	public void testSchedule() {
		List<List<Integer>> expectedResultList = new ArrayList<List<Integer>>();
		expectedResultList.add(Arrays.asList(new Integer[]{1, 1, 1, null, 3, 3, 3, null, null, null, null, null}));
		expectedResultList.add(Arrays.asList(new Integer[]{2, 2, 3, 3, null, null, null, 1, 1, 1, null, null}));
		expectedResultList.add(Arrays.asList(new Integer[]{null, null, 2, 2, 2, 1, 1, 3, 3, 3, 3, null}));
		
		String configFileName = "testInput.txt";
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
		Scheduler asScheduler = new ActiveScheduler(config);
		Result actualResult = asScheduler.schedule();
		
		assertEquals(expectedResultList, actualResult.getResultInListForm());
	}
}
