package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

import java.util.LinkedList;

public class MassRemoveCourseFromRegStu extends Action {

   //called from the dep Private State - during the close course action
    public MassRemoveCourseFromRegStu(){}
    @Override
    protected void start() {
        //the currentprivate state is the course- not the dep
        CoursePrivateState coursePS=(CoursePrivateState)ps;
        LinkedList<String>regStu=new LinkedList<>();
        LinkedList<Action>preActions=new LinkedList<>();
        for (String stu:coursePS.getRegStudents()) {
            regStu.add(stu);
        }
        for (String stu1:regStu) {
            //TODO-not sure that need to sent actorId
            Unregister unregStu=new Unregister(stu1,this.actorID);
            sendMessage(unregStu,this.actorID,new CoursePrivateState());
            preActions.add(unregStu);
        }

        //after unregister all the studs from the course- update the current amount to -1
        then(preActions, new callback() {
            @Override
            public void call() {
                coursePS.setAvailableSpots(-1);
                complete(true);
            }
        });
        }
}
