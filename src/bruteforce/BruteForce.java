package bruteforce;

import java.util.List;
import java.util.Map;

import common.Configuration;
import common.JobPart;
import common.Result;

public class BruteForce {

	private Configuration config;
	private int sumOfCosts;
	private Map<Integer, List<JobPart>> jobsByMachines;
	
	public BruteForce(Configuration config) {
		this.config = config;
		this.sumOfCosts = this.config.getSumOfCosts();
		this.jobsByMachines = this.config.getJobsPartsAsMapByMachine();
	}
	
//	public Result compute() {
//		int sumOfCosts = this.config.getSumOfCosts();
//		Map<Integer, List<JobPart>> jobPartMap = this.config.getJobsPartsAsMapByMachine();
//		
//		Set<Integer> keys = jobPartMap.keySet();
//		for (Integer key : keys) {
//			List<JobPart> jobPartList = jobPartMap.get(key);
//			for (JobPart jp : jobPartList) {
//				for (int i = 0; i < sumOfCosts; i++) {
//					int jpStartTime = jp.getStartTime();
//					jp.setStartTime(i);
//					if (EverithingIsFine) {
//						putToSolutions();
//					} else {
//						jp.setStartTime(jpStartTime);
//					}
//				}
//			}
//		}
//		
//		return null;
//	}
	
	public Result compute() {
		this.solve(1, 1);
		return null;
	}

	private void solve(int machine, int job) {
		if (machine == this.config.getNumberOfMachines() && job == this.jobsByMachines.get(machine).size()-1) {
			if (jeVhodneRiesenie(machine, job)) {
				System.out.println("moze byt");
			}
		}
		
		if (machine > this.config.getNumberOfMachines()) {
			System.out.println("done");
		}
		JobPart jobPart = this.getJobPart(machine, job);
		if (jobPart.getStartTime() != -1) {
			next(machine, job);
		} else {
			for (int i = 0; i < this.sumOfCosts; i++) {
				if (jeVhodneRiesenie(machine, job, i)) {
					jobPart.setStartTime(i);
					next(machine, job);
				}
			}
			
			jobPart.setStartTime(-1);
		}
	}
	
	private boolean jeVhodneRiesenie(int machine, int job) {
		JobPart jobPart = this.getJobPart(machine, job);
		boolean areJobsInRightPosition = this.areJobsInRightPosition(machine, job, jobPart.getStartTime());
		boolean isJobInConflictWithJobOnMachine = isJobInConflictWithJobOnMachine(machine, job, jobPart.getStartTime());
		return areJobsInRightPosition && !isJobInConflictWithJobOnMachine;
	}
	
	private boolean jeVhodneRiesenie(int machine, int job, int startTime) {
		boolean areJobsInRightPosition = this.areJobsInRightPosition(machine, job, startTime);
		boolean isJobInConflictWithJobOnMachine = isJobInConflictWithJobOnMachine(machine, job, startTime);
		return areJobsInRightPosition && !isJobInConflictWithJobOnMachine;
	}
	
	private boolean areJobsInRightPosition(int machine, int job, int startTime) {
		JobPart jobPart = this.getJobPart(machine, job);
		for (int machineIndex = 1; machineIndex <= machine; machineIndex++) {
			List<JobPart> jobPartList = this.jobsByMachines.get(machineIndex);
			for (int jobIndex = 0; jobIndex < jobPartList.size(); jobIndex++) {
				if (machineIndex < machine || (machineIndex == machine && jobIndex < job-1)) {
					JobPart jp = this.getJobPart(machineIndex, jobIndex);
					if (jp.getJob() == jobPart.getJob()) {
						if (!(jp.getIndex() < jobPart.getIndex() && jp.getStartTime() < startTime)) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	private boolean isJobInConflictWithJobOnMachine(int machine, int job, int startTime) {
		JobPart jobPart = this.getJobPart(machine, job);
		List<JobPart> jobPartList = this.jobsByMachines.get(machine);
		for (int i = 0; i < jobPartList.size(); i++) {
			JobPart jp = this.getJobPart(machine, i);
			if (jp.getStartTime() != -1 && jp.getIndex() != jobPart.getIndex()) {
				if (jp.getStartTime() < startTime && (jp.getStartTime()+jp.getCost()) > startTime) {
					return true;
				}
				if (jp.getStartTime() > startTime && (startTime+jobPart.getCost()) > jp.getStartTime()) {
					return true;
				}
			}
		}
		return false;
	}

	private void next(int machine, int job) {
		List<JobPart> jobPartList = this.jobsByMachines.get(machine);
		if (job < jobPartList.size()-1) {
			solve(machine, job+1);
		} else {
			solve(machine+1, 1);
		}
	}
	
	private JobPart getJobPart(int machine, int job) {
		List<JobPart> jobPartList = this.jobsByMachines.get(machine);
		return jobPartList.get(job);
	}

	public Map<Integer, List<JobPart>> getJobsByMachines() {
		return jobsByMachines;
	}
	
	
}
