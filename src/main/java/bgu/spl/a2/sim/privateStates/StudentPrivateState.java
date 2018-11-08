package bgu.spl.a2.sim.privateStates;

import bgu.spl.a2.PrivateState;

import java.util.HashMap;

/**
 * this class describe student private state
 */
public class StudentPrivateState extends PrivateState{

	private HashMap<String, Integer> grades;
	private long signature;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public StudentPrivateState() {
		this.grades=new HashMap<String, Integer>();
		//??GAP - not sure how to get this long signature
		this.signature=0;
	}

	public HashMap<String, Integer> getGrades() {
		return grades;
	}

	public long getSignature() {
		return signature;
	}

	public void setSignature(long num){
		this.signature=num;
	}

	public void removeCourse (String courseName){
		if(courseName==null)
			throw  new IllegalArgumentException("addGrade- illegal input");
		if(grades.containsKey(courseName))
			grades.remove(courseName);
	}


}
