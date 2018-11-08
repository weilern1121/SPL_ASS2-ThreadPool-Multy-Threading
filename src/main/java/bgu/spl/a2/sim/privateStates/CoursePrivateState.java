package bgu.spl.a2.sim.privateStates;

import bgu.spl.a2.PrivateState;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState{

	private Integer availableSpots;
	private AtomicInteger registered;
	private List<String> regStudents;
	private List<String> prequisites;
	private AtomicBoolean canRegister;
	private String depName;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public CoursePrivateState() {
		this.availableSpots=0;
		this.registered=new AtomicInteger(0);
		this.regStudents=new LinkedList<String>();
		this.prequisites=new LinkedList<String>();
		this.canRegister=new AtomicBoolean(true);
	}

	public Integer getAvailableSpots() {
		return availableSpots;
	}

	public Integer getRegistered() {
		return registered.get();
	}

	public AtomicInteger getregistered() {
		return registered;
	}


	public List<String> getRegStudents() {
		return regStudents;
	}

	public List<String> getPrequisites() {
		return prequisites;
	}

	//our funcs

	public void setAvailableSpots(Integer num){
		this.availableSpots=num;
		if(num==-1)
			canRegister.getAndSet(false);
	}

	public void setPrequisites (LinkedList<String> prequisites){
		this.prequisites=prequisites;
	}

	public  void removeStudentFromCourse(String studentName){
		if(studentName==null)
			throw new IllegalArgumentException("removeStudentFromCourse");
		regStudents.remove(studentName);
		//registered.decrementAndGet();
	}

	public void setDepName(String name) {
		this.depName = name;
	}

}
