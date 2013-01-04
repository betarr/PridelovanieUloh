package sk.sochuliak.taskassignment.common;

import static org.junit.Assert.*;

import org.junit.Test;

import sk.sochuliak.timeassignment.common.JobPart;
import sk.sochuliak.timeassignment.common.Result;

public class ResultTest {

	@Test
	public void testCopyContructor() {
		Result expectedResult = this.buildResult();
		Result actualResult = new Result(expectedResult);
		assertEquals(expectedResult.getResultInListForm(), actualResult.getResultInListForm());
	}

	private Result buildResult() {
		Result result = new Result();
		int[] machineIndexes = new int[]{1, 1, 2, 2, 2, 3, 3, 3};
		int[] jobIndexes = new int[]{1, 3, 2, 3, 1, 2, 1, 3};
		int[] costs = new int[]{3, 3, 2, 2, 3, 3, 2, 4};
		int[] startTimes = new int[]{0, 4, 0, 2, 7, 2, 5, 7}; 
		
		for (int i = 0; i < machineIndexes.length; i++) {
			JobPart jp = new JobPart();
			jp.setMachine(machineIndexes[i]);
			jp.setJob(jobIndexes[i]);
			jp.setCost(costs[i]);
			jp.setStartTime(startTimes[i]);
			result.addToResult(jp);
		}
		return result;
	}
}
