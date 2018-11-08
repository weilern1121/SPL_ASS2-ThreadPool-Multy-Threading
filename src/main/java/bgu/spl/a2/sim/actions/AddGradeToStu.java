package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class AddGradeToStu extends Action {
    private String course;
    private Integer grade;
    public AddGradeToStu(String courseName,Integer grade)
    {
        this.course=courseName;
        this.grade=grade;
    }
    @Override
    protected void start() {
        StudentPrivateState s=(StudentPrivateState)ps;
        s.getGrades().put(course,grade);
        complete(true);
    }
}
