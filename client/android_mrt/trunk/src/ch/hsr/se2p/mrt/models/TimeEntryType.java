package ch.hsr.se2p.mrt.models;

public class TimeEntryType {
	private Integer id;
	private String name;
	
	public TimeEntryType(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String toString(){
		return name;
	}
	
	
}
