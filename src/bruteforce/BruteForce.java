package bruteforce;

import java.util.List;
import java.util.Map;

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
		
		Map<Integer, List<JobPart>> bestCombination = null;
		int bestCombinationScore = -1;
		
		for (List<JobPart> listOfJobParts : combinationsOfJobParts) {
			Map<Integer, List<JobPart>> combinationsOfJobPartByMachine = Utils.getJobPartListAsMapByMachine(listOfJobParts);
			if (jeTatoKonfiguraciaNekonfliktna(combinationsOfJobPartByMachine)) {
				int combinationScore = ziskajSkoreKonfiguracie(combinationsOfJobPartByMachine);
				if (bestCombinationScore == -1 || combinationScore < bestCombinationScore) {
					bestCombinationScore = combinationScore;
					bestCombination = combinationsOfJobPartByMachine;
				}
			}
			
			for (JobPart jobPart : listOfJobParts) {
				System.out.println(jobPart);
			}
			System.out.println();
		}
		
		Result result = pretransformujNajlepsiuKombinaciuNaResult(bestCombination);
		return result;
	}	
}
