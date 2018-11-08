package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.HashMap;

public class GetStuGradesHashMap extends Action{
    public GetStuGradesHashMap(){}
    @Override
    protected void start() {
        StudentPrivateState stuPS=(StudentPrivateState)ps;
        HashMap<String,Integer>output=new HashMap<String,Integer>();
        for (String s:stuPS.getGrades().keySet()) {
            output.put(s,stuPS.getGrades().get(s));
        }
        complete(output);
    }
}
