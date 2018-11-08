package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class OpenNewCourse extends Action{

    private Integer availableSpots;
    private LinkedList<String> prequisites;
    private String depName;
    private String courseName;

    public OpenNewCourse(int availableSpots,String courseName, LinkedList<String> prequisites , String depName){
        if(availableSpots<0||courseName==null||prequisites==null||depName==null)
            throw new IllegalArgumentException("OpenNewCourse- can't initial openCourse action with null element");
        this.availableSpots=availableSpots;
        this.prequisites=prequisites;
        this.courseName=courseName;
        this.depName=depName;
        setActionName("Open Course");
    }

    @Override
    protected void start() {

        DepartmentPrivateState depPS=(DepartmentPrivateState)pool.getPrivateState(depName);
        //there is a need to create new course private state because we work now on the dep private state
        InitializeNewCourse inCourse=new InitializeNewCourse(courseName,prequisites,availableSpots);
        Promise<CoursePrivateState>p=sendMessage(inCourse,courseName,new CoursePrivateState());
        List<Action> l=new LinkedList<Action>();
        l.add(inCourse);
        //after creating the new course - add it to the deep course list
        then(l, new callback() {
            @Override
            public void call() {
                AddCourseToCourseList actcl= new AddCourseToCourseList(courseName);
                Promise p1=sendMessage(actcl,depName,new DepartmentPrivateState());
                List<Action> l1=new LinkedList<Action>();
                l1.add(actcl);
                then(l1, new callback() {
                    @Override
                    public void call() {
                        if((Boolean) p1.get())
                            complete(true);
                        else
                            complete(false);
                    }
                });

            }
        });
    }
}
