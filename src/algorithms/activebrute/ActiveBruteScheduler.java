package algorithms.activebrute;

import java.util.ArrayList;
import java.util.List;

import algorithms.Scheduler;

import common.Configuration;
import common.Result;

public class ActiveBruteScheduler extends Scheduler {
	
	private Configuration config;
	private List<Result> results;
	
	public ActiveBruteScheduler(Configuration config) {
		this.config = config;
		this.results = new ArrayList<Result>();
	}



	@Override
	public Result schedule() {
		// TODO Auto-generated method stub
		return null;
	}

}
