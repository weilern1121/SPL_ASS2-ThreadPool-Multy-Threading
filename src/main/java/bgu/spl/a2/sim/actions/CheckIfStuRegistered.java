package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class CheckIfStuRegistered extends Action{
    private  String courseName;
    public CheckIfStuRegistered(String courseName){
        this.courseName=courseName;
    }

    @Override
    protected void start() {
        StudentPrivateState std=(StudentPrivateState)ps;
        if(std.getGrades().containsKey(courseName)){
            std.removeCourse(courseName);
            complete(true);
        }
        else
            complete(false);
    }
}
