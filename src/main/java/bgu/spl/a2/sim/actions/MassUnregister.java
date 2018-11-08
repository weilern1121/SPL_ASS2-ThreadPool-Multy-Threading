package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class MassUnregister extends Action {
    public  MassUnregister(){}
    @Override
    protected void start() {
        CoursePrivateState coursePS=(CoursePrivateState)ps;
        coursePS.setAvailableSpots(-1);
        complete(true);

    }
}
