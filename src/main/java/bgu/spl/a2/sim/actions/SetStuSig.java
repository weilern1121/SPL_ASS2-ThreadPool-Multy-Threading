package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class SetStuSig extends Action{
    private long sig;
    public SetStuSig(long sig){
        this.sig=sig;
    }
    @Override
    protected void start() {
        StudentPrivateState stuPS=(StudentPrivateState)ps;
        stuPS.setSignature(sig);
        complete(true);

    }
}
