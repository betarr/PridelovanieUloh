package algorithms.activebrute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import algorithms.active.ActiveSchedulerAbstract;
import algorithms.active.Graph;
import algorithms.active.Node;

import common.Configuration;
import common.JobPart;
import common.Result;
import common.Utils;

public class ActiveBruteScheduler extends ActiveSchedulerAbstract {
	
	private Map<Integer, List<JobPart>> jobPartsByJob;
	
	public ActiveBruteScheduler(Configuration config) {
		this.setConfig(config);
		this.jobPartsByJob = Utils.getSortedJobPartsInMapByIndex(this.getConfig().getJobsPartsAsMapByJobs());
	}

	@Override
	public Result schedule() {
		List<Result> finalResults = new ArrayList<Result>();
		
		List<Result> currentResults = new ArrayList<Result>();
		List<List<JobPart>> omegas = new ArrayList<List<JobPart>>();
		List<Graph> graphs = new ArrayList<Graph>();
		
		currentResults.add(new Result());
		omegas.add(this.getListOfFistJobPartByJob(this.jobPartsByJob));
		graphs.add(new Graph(this.jobPartsByJob));
		
		while (!omegas.isEmpty()) {
			if (omegas.size() != graphs.size()) {
				System.err.println("Omegas and graphs list can not have different sizes");
			}
			
			List<JobPart> omega = omegas.get(0);
			Graph graph = graphs.get(0);
			Result result = currentResults.get(0);
			
			omegas.remove(omega);
			graphs.remove(graph);
			currentResults.remove(result);
			
			if (omega.isEmpty()) {
				continue;
			}
			
			List<Integer> rList = this.getRForJobPartsInOmega(omega, graph);
			if (omega.size() != rList.size()) {
				System.err.println("Omega list and r list can not have different sizes");
				return null;
			}
			
			List<Integer> tList = this.getTForJobPartsInOmega(omega, rList);
			int minIIndex = -1;
			int minT = Integer.MAX_VALUE;
			for (int j = 0; j < tList.size(); j++) {
				if (tList.get(j) < minT) {
					minT = tList.get(j);
					minIIndex = j;
				}
			}
			int minI = omega.get(minIIndex).getMachine();
			
			List<JobPart> omega2 = this.getOmega2JobParts(omega, rList, minT, minI);
			
			for (JobPart toResult : omega2) {
				Result newResult = new Result(result);
				newResult.addToResult(toResult, omega, rList);
				Graph newGraph = new Graph(graph, this.jobPartsByJob);
				newGraph.addYEdgesToGraph(toResult);
				List<JobPart> newOmega = new ArrayList<JobPart>();
				for (JobPart jp : omega) {
					if (jp.equals(toResult)) {
						Node jpNode = newGraph.getJPNodeByJobPart(jp);
						Node nextNode = newGraph.getNodeSuccessorOnXEdge(jpNode);
						if (!nextNode.isV()) {
							newOmega.add(nextNode.getJobPart());
						}
					} else {
						newOmega.add(jp);
					}
				}
				
				if (newOmega.isEmpty()) {
					finalResults.add(newResult);
				} else {
					omegas.add(newOmega);
					graphs.add(newGraph);
					currentResults.add(newResult);
				}
			}
		}
		
		return this.getBestResultFromList(finalResults);
	}

	private Result getBestResultFromList(List<Result> results) {
		int minTotalCost = Integer.MAX_VALUE;
		Result minResult = null;
		for (Result result : results) {
			int currentTotalCost = result.getMaxStartPlusCostIndex();
			if (currentTotalCost < minTotalCost) {
				minTotalCost = currentTotalCost;
				minResult = result;
			}
		}
		return minResult;
	}
	
	

}
