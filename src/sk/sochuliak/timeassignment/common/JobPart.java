package sk.sochuliak.timeassignment.common;

public class JobPart {

	private int job;
	private int machine;
	/**
	 * Trvanie ulohy.
	 */
	private int cost;
	
	/**
	 * Poradie.
	 */
	private int index;
	private int startTime;
	
	public JobPart() {
		this.setJob(-1);
		this.setMachine(-1);
		this.setCost(-1);
		this.setIndex(-1);
		this.setStartTime(-1);
	}
	
	public JobPart(JobPart jobPart) {
		this.setJob(jobPart.getJob());
		this.setMachine(jobPart.getMachine());
		this.setCost(jobPart.getCost());
		this.setIndex(jobPart.getIndex());
		this.setStartTime(jobPart.getStartTime());
	}

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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cost;
		result = prime * result + index;
		result = prime * result + job;
		result = prime * result + machine;
//		result = prime * result + startTime;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobPart other = (JobPart) obj;
		if (cost != other.cost)
			return false;
		if (index != other.index)
			return false;
		if (job != other.job)
			return false;
		if (machine != other.machine)
			return false;
//		if (startTime != other.startTime)
//			return false;
		return true;
	}
	
}
