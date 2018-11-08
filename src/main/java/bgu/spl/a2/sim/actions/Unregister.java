package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Unregister extends Action {

    private  String studentId;
    private String courseName;

    public Unregister(String name, String courseName){
        this.studentId=name;
        this.courseName=courseName;
        setActionName("Unregister");

    }
    @Override
    protected void start() {
        //TODO- make sure that the steps on getting the private state are the same as the order in participating in course
        CoursePrivateState cPS=(CoursePrivateState)pool.getPrivateState(courseName);
        //first- get the grades HashMaps of the student
        GetStuGrades get=new GetStuGrades();
        Promise<HashMap<String,Integer>> p=sendMessage(get,studentId,new StudentPrivateState());
        List<Action> l=new LinkedList<Action>();
        l.add(get);
        then(l, new callback() {
            @Override
            public void call() {
                //secondly- remove the student from the course
                //NOTE- make sure that it's the same PrivateStates order like participatingInCourse
                RemoveStuFromCourse remStu=new RemoveStuFromCourse(studentId);
                Promise p1=sendMessage(remStu,courseName,cPS);
                List<Action> l1=new LinkedList<Action>();
                l1.add(remStu);
                then(l1, new callback() {
                    @Override
                    public void call() {
                        //the student deleted from the course- now delete grade from his grade HMap
                        if((Boolean)p1.get()){
                            CheckIfStuRegistered remCourseFromStu=new CheckIfStuRegistered(courseName);
                            sendMessage(remCourseFromStu,studentId,new StudentPrivateState());
                            List<Action> l2=new LinkedList<Action>();
                            l2.add(remCourseFromStu);
                            then(l2, new callback() {
                                @Override
                                public void call() {
                                    complete(true);
                                }
                            });
                        }
                        else
                            complete(false);
                    }
                });
            }
        });
    }
}
