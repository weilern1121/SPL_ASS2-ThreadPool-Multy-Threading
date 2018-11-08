package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class RegIfPlace extends Action {

    private String stuName;
    public RegIfPlace(String stu)
    {
        stuName=stu;
    }
    @Override
    protected void start() {
        CoursePrivateState c=(CoursePrivateState)ps;
        if(c.getAvailableSpots()>0)
        {
            c.getRegStudents().add(stuName);
            c.getregistered().incrementAndGet();
            c.setAvailableSpots(c.getAvailableSpots()-1);
            complete(true);
        }
        else
        {
            complete(false);
        }
    }
}
