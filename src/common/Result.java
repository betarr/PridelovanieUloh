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

	public Map<Integer, List<JobPart>> getResult() {
		return result;
	}

	public void setResult(Map<Integer, List<JobPart>> result) {
		this.result = result;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		int maxIndex = this.getMaxStartPlusCostIndex();
		Set<Integer> machinesIndexes = this.result.keySet();
		for (Integer machineIndex : machinesIndexes) {
			sb.append(machineIndex).append(": [");
			List<JobPart> jobPartsList = this.result.get(machineIndex);
			for (int i = 0; i <= maxIndex; i++) {
				sb.append(this.getSymbolForOutputBasedOnJobPartsList(jobPartsList, i)).append(" ");
			}
			sb.append("]").append(Utils.LINE_SEPARATOR);
		}
		return sb.toString();
	}

	private int getMaxStartPlusCostIndex() {
		int maxIndex = 0;
		for (List<JobPart> jobPartList : this.result.values()) {
			int tempMaxIndex = 0;
			for (JobPart jp : jobPartList) {
				int value = jp.getStartTime() + jp.getCost();
				tempMaxIndex = (value > tempMaxIndex) ? value : tempMaxIndex;
			}
			maxIndex = (tempMaxIndex > maxIndex) ? tempMaxIndex : maxIndex;
		}
		return maxIndex;
	}
	
	private String getSymbolForOutputBasedOnJobPartsList(List<JobPart> jobPartsList, int i) {
		for (JobPart jp : jobPartsList) {
			if (i >= jp.getStartTime() && i < jp.getStartTime()+jp.getCost()) {
				return String.valueOf(jp.getJob());
			}
		}
		return "-";
	}
}
