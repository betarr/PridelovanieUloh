package algorithms.active;

import java.util.ArrayList;
import java.util.List;

public class DelaySolver {

	private List<Integer> rs;
	private List<Integer> deltas;
	private List<Integer> ds;
	private List<Integer> costs;
	private int criticalPathCost = 0;
	
	public DelaySolver() {
		this.rs = new ArrayList<Integer>();
		this.deltas = new ArrayList<Integer>();
		this.ds = new ArrayList<Integer>();
		this.costs = new ArrayList<Integer>();
	}
	
	public void add(int r, int delta, int d, int cost) {
		boolean added = false;
		for (int i = 0; i < rs.size(); i++) {
			if (rs.get(i) > r) {
				this.rs.add(i, r);
				this.deltas.add(i, delta);
				this.ds.add(i, d);
				this.costs.add(i, cost);
				added = true;
				break;
			}
		}
		
		if (!added) {
			this.rs.add(r);
			this.deltas.add(delta);
			this.ds.add(d);
			this.costs.add(cost);
		}
	}
	
	public int calculateDelay() {
		
		int lastTakenTime = 0;
		for (int i = 0; i < rs.size(); i++) {
			int startTime = rs.get(i);
			while (startTime < lastTakenTime) {
				startTime++;
			}
			lastTakenTime = startTime + this.costs.get(i);
		}
		
		return (lastTakenTime > this.criticalPathCost) ? lastTakenTime : 0;
	}

	public List<Integer> getRs() {
		return rs;
	}

	public List<Integer> getDeltas() {
		return deltas;
	}

	public List<Integer> getDs() {
		return ds;
	}

	public List<Integer> getCosts() {
		return costs;
	}

	public void setCriticalPathCost(int critialPathCost) {
		this.criticalPathCost = critialPathCost;
	}
	
}
