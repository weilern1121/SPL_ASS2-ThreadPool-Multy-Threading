package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class DelCourseFromDepList extends Action {
    @Override
    protected void start() {
        DepartmentPrivateState depPS=(DepartmentPrivateState)ps;
        if(depPS.getCourseList().contains(courseName)){
            depPS.removeCourse(courseName);
            System.out.println("2-DelCourseFromDepList completed true");
            complete(true);
        }
        else
            complete(false);
    }

    private String courseName;
    public DelCourseFromDepList(String courseName){
        this.courseName=courseName;
    }
}
