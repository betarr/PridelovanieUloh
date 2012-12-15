package bruteforce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import common.Configuration;
import common.JobPart;
import common.Result;
import common.Utils;

public class BruteForce {

	private Configuration config;
	private int sumOfCosts;
	
	public BruteForce(Configuration config) {
		this.config = config;
		this.sumOfCosts = this.config.getSumOfCosts();
	}
	
	public Result compute() {
		List<List<JobPart>> combinationsOfJobParts = Utils.getAllCombinationsOfJobPartByStartTime(this.sumOfCosts, this.config.getJobsParts());
		
		Map<Integer, List<JobPart>> bestCombinationByMachine = null;
		int bestCombinationScore = -1;
		
		for (List<JobPart> listOfJobParts : combinationsOfJobParts) {
			Map<Integer, List<JobPart>> combinationsOfJobPartByMachine = Utils.getJobPartListAsMapByMachine(listOfJobParts);
			Map<Integer, List<JobPart>> combinationsOfJobPartByJob = Utils.getJobPartListAsMapByJob(listOfJobParts);
			
			//this.printOutCombination(combinationsOfJobPartByMachine, "Machine");
			
			if (isCombinationNotConflicted(combinationsOfJobPartByMachine, combinationsOfJobPartByJob)) {
				int combinationScore = getScoreOfCombination(combinationsOfJobPartByMachine);
				if (bestCombinationScore == -1 || combinationScore < bestCombinationScore) {
					bestCombinationScore = combinationScore;
					bestCombinationByMachine = combinationsOfJobPartByMachine;
				}
			}
		}
		
		Result result = transformCombinationToResult(bestCombinationByMachine);
		return result;
	}
	
	private void printOutCombination(
			Map<Integer, List<JobPart>> combinationsOfJobPartByMachine, String type) {
		StringBuilder sb = new StringBuilder();
		Set<Integer> keys = combinationsOfJobPartByMachine.keySet();
		for (Integer key : keys) {
			sb.append(type).append(" ").append(key).append(Utils.LINE_SEPARATOR);
			List<JobPart> list = combinationsOfJobPartByMachine.get(key);
			for (JobPart jp : list) {
				sb.append(jp).append(Utils.LINE_SEPARATOR);
			}
			sb.append(Utils.LINE_SEPARATOR);
		}
		System.out.println(sb.toString());
	}

	private boolean isCombinationNotConflicted(
			Map<Integer, List<JobPart>> jobPartCombinationByMachine,
			Map<Integer, List<JobPart>> jobPartCombinationByJob) {
		boolean isConfictOnSomeMachine = isConflictOnSomeMachine(jobPartCombinationByMachine);
		boolean isConfictOnJobs = isConflictOnJobs(jobPartCombinationByJob);
		return !isConfictOnSomeMachine && !isConfictOnJobs;
	}
	
	private boolean isConflictOnSomeMachine(
			Map<Integer, List<JobPart>> jobPartCombinationByMachine) {
		for (List<JobPart> jobPartList : jobPartCombinationByMachine.values()) {
			if (isConflictOnMachine(jobPartList)) {
				return true;
			}
		}
		return false;
	}

	private boolean isConflictOnMachine(List<JobPart> jobPartList) {
		for (JobPart jp : jobPartList) {
			if (isAnyOtherRunningExceptActualAtSameTime(jobPartList, jp)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAnyOtherRunningExceptActualAtSameTime(
			List<JobPart> jobPartList, JobPart actual) {
		for (JobPart jp : jobPartList) {
			if (!jp.equals(actual) && Utils.areJobPartsInTimeConflict(jp, actual)) {
				return true;
			}
		}
		return false;
	}

	private boolean isConflictOnJobs(Map<Integer, List<JobPart>> jobPartCombinationByJob) {
		Set<Integer> jobsNumber = jobPartCombinationByJob.keySet();
		for (Integer jobNumber : jobsNumber) {
			if (areJobsInConflict(jobPartCombinationByJob.get(jobNumber))) {
				return true;
			}
		}
		return false;
	}

	private boolean areJobsInConflict(List<JobPart> jobPartsList) {
		for (JobPart jp : jobPartsList) {
			if (isJobPartInRightOrderToOthers(jobPartsList, jp)) {
				return true;
			}
		}
		return false;
	}

	private boolean isJobPartInRightOrderToOthers(List<JobPart> jobPartsList,
			JobPart jp) {
		for (JobPart actual : jobPartsList) {
			if (!jp.equals(actual) && Utils.areJobPartsInJobsOrderConflict(jp, actual)) {
				return true;
			}
		}
		return false;
	}

	private int getScoreOfCombination(Map<Integer, List<JobPart>> jobPartCombinationByMachine) {
		List<Integer> costOfMachines = new ArrayList<Integer>();
		for (List<JobPart> jobPartList : jobPartCombinationByMachine.values()) {
			costOfMachines.add(getValueOfCostForJobPartListPerMachine(jobPartList));
		}
		return Collections.max(costOfMachines);
	}
	
	private int getValueOfCostForJobPartListPerMachine(List<JobPart> jobPartList) {
		int max = 0;
		for (JobPart jp : jobPartList) {
			int value = jp.getStartTime() + jp.getCost();
			max = (value < max) ? value : max;
		}
		return max;
	}

	private Result transformCombinationToResult(Map<Integer, List<JobPart>> jobPartCombination) {
		Result result = new Result();
		result.setResult(jobPartCombination);
		return result;
	}
}
