package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class AddCourseToCourseList extends Action {
   private String courseName;
   public AddCourseToCourseList(String courseName){
       this.courseName=courseName;
   }
    @Override
    protected void start() {
        DepartmentPrivateState d=(DepartmentPrivateState)ps;
        if(!(d.getCourseList().contains(courseName))){
            d.getCourseList().add(courseName);
            complete(true);
        }
        else{
            complete(false);
        }

    }
}
