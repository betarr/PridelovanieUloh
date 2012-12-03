package common;

import java.util.List;

public class Utils {
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	public static JobPart getLatestJobPartOnMachine(List<JobPart> jobPartList) {
		JobPart result = null;
		int maxStartTime = 0;
		for (JobPart jp : jobPartList) {
			result = (jp.getStartTime() > maxStartTime) ? jp : result;
		}
		return result;
	}

	public static int getTotalCostOfJobParts(List<JobPart> jobPartList) {
		int result = 0;
		for (JobPart jp : jobPartList) {
			result += jp.getCost();
		}
		return result;
	}
}
