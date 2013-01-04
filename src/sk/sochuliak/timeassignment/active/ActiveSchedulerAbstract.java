package sk.sochuliak.timeassignment.active;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sk.sochuliak.timeassignment.common.Configuration;
import sk.sochuliak.timeassignment.common.JobPart;
import sk.sochuliak.timeassignment.common.Result;
import sk.sochuliak.timeassignment.common.Scheduler;



public abstract class ActiveSchedulerAbstract extends Scheduler {
	
	private Configuration config;
	
	public abstract Result schedule();
	
	protected List<JobPart> getListOfFistJobPartByJob(
			Map<Integer, List<JobPart>> jobPartsByJob2) {
		
		List<JobPart> result = new ArrayList<JobPart>();
		for (List<JobPart> jobPartList : jobPartsByJob2.values()) {
			JobPart jp = jobPartList.get(0);
			if (jp != null) {
				result.add(jp);
			}
		}
		return result;
	}
	
	protected List<Integer> getRForJobPartsInOmega(List<JobPart> omega, Graph graph) {
		List<Integer> rList = new ArrayList<Integer>();
		for (JobPart jp : omega) {
			Integer longestPath = graph.getLongestCostFromStartToNodeWithJobPart(jp);
			rList.add(longestPath);
		}
		return rList;
	}
	
	protected List<Integer> getTForJobPartsInOmega(List<JobPart> omega, List<Integer> rList) {
		List<Integer> tList = new ArrayList<Integer>();
		for (int i = 0; i < omega.size(); i++) {
			int r = rList.get(i);
			int cost = omega.get(i).getCost();
			tList.add(r + cost);
		}
		return tList;
	}
	
	protected List<JobPart> getOmega2JobParts(List<JobPart> omega, List<Integer> rList, int minT, int minI) {
		List<JobPart> omega2 = new ArrayList<JobPart>();
		for (int i = 0; i < omega.size(); i++) {
			JobPart jp = omega.get(i);
			if (jp.getMachine() == minI) {
				if (rList.get(i) < minT) {
					omega2.add(jp);
				}
			}
		}
		return omega2;
	}
	
	public Configuration getConfig() {
		return config;
	}

	public void setConfig(Configuration config) {
		this.config = config;
	}

}
