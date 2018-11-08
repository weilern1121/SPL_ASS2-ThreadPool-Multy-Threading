package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class RemoveStuFromCourse extends Action {
    private String stuName;

    public RemoveStuFromCourse(String stuName){
        this.stuName=stuName;
    }

    @Override
    protected void start() {
        CoursePrivateState course=(CoursePrivateState)ps;
        if(course.getRegStudents().contains(stuName)){
            course.removeStudentFromCourse(stuName);
            course.getregistered().decrementAndGet();
            course.setAvailableSpots(course.getAvailableSpots()+1);
            complete(true);
        }
        else
            complete(false);
    }
}
