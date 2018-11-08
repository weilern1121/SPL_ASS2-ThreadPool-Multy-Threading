package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class ParticipatingInCourse extends Action {

    private  String studentId;
    private String courseName;
    private Integer studentGrade;

    public ParticipatingInCourse(String studentId, String courseName, Integer grade){
        this.studentId=studentId;
        this.courseName=courseName;
        this.studentGrade=grade;
        setActionName("Participate In Course");
    }

    protected void start(){
        //first- get the students grades, new action
        CoursePrivateState c=(CoursePrivateState)pool.getPrivateState(courseName);
        GetStuGrades get=new GetStuGrades();
        Promise<HashMap<String,Integer>>p=sendMessage(get,studentId,new StudentPrivateState());
        List<Action>l=new LinkedList<Action>();
        l.add(get);
        //secondly- check the stu prerequisites to make sure that he can register
        then(l, new callback() {
            @Override
            public void call() {
                boolean isReg=true;
                for (String st:c.getPrequisites()) {
                    if(!(p.get().containsKey(st)))
                        isReg=false;
                }
                //if the stu meets the conditions
                if(isReg)
                {
                    RegIfPlace r=new RegIfPlace(studentId);
                    Promise p1=sendMessage(r,courseName,new CoursePrivateState());
                    LinkedList<Action>l1=new LinkedList<>();
                    l1.add(r);
                    //if p1==true - there was a place in the course and trhe student is registered
                    //then the only thing that left is to add the grades too the student grade list.
                    then(l1, new callback() {
                        @Override
                        public void call() {
                            if((Boolean)p1.get())
                            {
                                AddGradeToStu a=new AddGradeToStu(courseName,studentGrade);
                                Promise p2=sendMessage(a,studentId,new StudentPrivateState());
                                LinkedList<Action>l2=new LinkedList<>();
                                l2.add(a);
                                then(l2, new callback() {
                                    @Override
                                    public void call() {
                                        complete(true);
                                    }
                                });
                            }
                            else{
                                complete(false);
                            }
                        }
                    });
                }
                else{
                    complete(false);
                }
            }
        });
    }

}
