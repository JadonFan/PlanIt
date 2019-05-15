package calendar;

public interface Event {	
	int getId();
	
	void setId(int id);
	
	String getName();
	
	void setName(String name);
	
	int findRemainingDays();
	
	String reprDue();
}
