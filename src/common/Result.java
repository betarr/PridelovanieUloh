package common;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Result {

	/**
	 * Result of time assignment.
	 * Key -> machine
	 * Value -> list of job parts
	 */
	private Map<Integer, List<JobPart>> result;

	/**
	 * Return longest used time for job;
	 * 
	 * @return
	 */
	public int getLongestCost() {
		int result = 0;
		Set<Integer> keys = this.result.keySet();
		for (Integer key : keys) {
			List<JobPart> jobPartList = this.result.get(key);
			JobPart jp = Utils.getLatestJobPartOnMachine(jobPartList);
			int endTime = jp.getStartTime() + jp.getCost();
			result = (endTime > result) ? endTime : result;
		}
		return result;
	}
	
}
