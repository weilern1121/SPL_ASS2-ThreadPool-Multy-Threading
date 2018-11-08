package bgu.spl.a2.sim;

import java.util.List;
import java.util.Map;

public class Computer {

	String computerType;
	long failSig;
	long successSig;
	
	public Computer(String computerType , long failSig , long successSig) {
		this.computerType = computerType;
		this.failSig=failSig;
		this.successSig=successSig;
	}
	
	/**
	 * this method checks if the courses' grades fulfill the conditions
	 * @param courses
	 * 							courses that should be pass
	 * @param coursesGrades
	 * 							courses' grade
	 * @return a signature if couersesGrades grades meet the conditions
	 */
	public long checkAndSign(List<String> courses, Map<String, Integer> coursesGrades){
		// foreach course in courselist- if don't exists or grade is minu- 56 ->errorsig
		for (int i=0; i<courses.size(); i++){
			if(!(coursesGrades.containsKey(courses.get(i))) ||
					coursesGrades.get(courses.get(i))<56)
				return  failSig;
		}
		return successSig;
	}
}
