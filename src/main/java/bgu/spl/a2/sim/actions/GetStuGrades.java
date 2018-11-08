package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.HashMap;

public class GetStuGrades extends Action{
    @Override
    protected void start() {
        StudentPrivateState s=(StudentPrivateState)ps;
        HashMap<String,Integer>g=new HashMap<>();
        for (String st:s.getGrades().keySet()) {
            g.put(st,s.getGrades().get(st));
        }
        complete(g);
    }

    public GetStuGrades()
    {

    }


}
