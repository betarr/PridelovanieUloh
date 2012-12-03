package common;

public class JobPart {

	private int job;
	private int machine;
	private int cost;
	
	private int index;
	private int startTime = -1;

	public int getJob() {
		return job;
	}

	public void setJob(int job) {
		this.job = job;
	}

	public int getMachine() {
		return machine;
	}

	public void setMachine(int machine) {
		this.machine = machine;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getName() + " ");
		sb.append("[");
		sb.append("job: ").append(this.job).append(", ");
		sb.append("machine: ").append(this.machine).append(", ");
		sb.append("cost: ").append(this.cost).append(", ");
		sb.append("index: ").append(this.index).append(", ");
		sb.append("start time: ").append(this.startTime);
		sb.append("]");
		return sb.toString();
	}
	
}
