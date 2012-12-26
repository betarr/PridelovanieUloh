package algorithms;

import java.util.List;
import java.util.Map;
import java.util.Set;

import common.Configuration;
import common.JobPart;
import common.Result;
import common.Utils;

public class ActiveSchedule extends Scheduler {
	
	private Configuration config;
	
	private Map<Integer, List<JobPart>> jobPartsByJob;
	
	public ActiveSchedule(Configuration config) {
		this.config = config;
		
		this.jobPartsByJob = Utils.getSortedJobPartsInMapByIndex(this.config.getJobsPartsAsMapByJobs());
	}

	@Override
	public Result schedule() {
		this.initialize();
		// TODO Auto-generated method stub
		return null;
	}


	private void initialize() {
	}

}
