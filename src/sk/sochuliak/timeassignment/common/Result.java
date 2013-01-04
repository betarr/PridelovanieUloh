package sk.sochuliak.timeassignment.common;

import java.util.ArrayList;
import java.util.HashMap;
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
	
	public Result() {
		this.result = new HashMap<Integer, List<JobPart>>();
	}
	
	public Result(Result result) {
		this();
		Map<Integer, List<JobPart>> resultMap = result.getResult();
		for (Integer machineIndex : resultMap.keySet()) {
			List<JobPart> jobPartList = new ArrayList<JobPart>();
			for (JobPart jp : resultMap.get(machineIndex)) {
				jobPartList.add(new JobPart(jp));
			}
			this.result.put(machineIndex, jobPartList);
		}
	}

	public Map<Integer, List<JobPart>> getResult() {
		return result;
	}

	public void setResult(Map<Integer, List<JobPart>> result) {
		this.result = result;
	}
	
	public void addToResult(JobPart jobPart, List<JobPart> omega, List<Integer> rList) {
		for (int i = 0; i < omega.size(); i++) {
			if (omega.get(i).equals(jobPart)) {
				jobPart.setStartTime(rList.get(i));
				break;
			}
		}
		
		List<JobPart> jobPartList = this.result.get(jobPart.getMachine());
		if (jobPartList == null) {
			jobPartList = new ArrayList<JobPart>();
		}
		jobPartList.add(jobPart);
		this.result.put(jobPart.getMachine(), jobPartList);
	}
	
	public void addToResult(JobPart jobPart) {
		List<JobPart> jobPartList = this.result.get(jobPart.getMachine());
		if (jobPartList == null) {
			jobPartList = new ArrayList<JobPart>();
		}
		jobPartList.add(jobPart);
		this.result.put(jobPart.getMachine(), jobPartList);
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

	public int getMaxStartPlusCostIndex() {
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
	
	public List<List<Integer>> getResultInListForm() {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		Set<Integer> machinesIndexes = this.result.keySet();
		int maxIndex = this.getMaxStartPlusCostIndex();
		for (Integer machineIndex : machinesIndexes) {
			List<JobPart> jobPartsList = this.result.get(machineIndex);
			List<Integer> machineList = new ArrayList<Integer>();
			for (int i = 0; i <= maxIndex; i++) {
				String symbol = this.getSymbolForOutputBasedOnJobPartsList(jobPartsList, i);
				try {
					machineList.add(Integer.parseInt(symbol));
				} catch (NumberFormatException e) {
					machineList.add(null);
				}
			}
			result.add(machineList);
		}
		return result;
	}
	
	
}
