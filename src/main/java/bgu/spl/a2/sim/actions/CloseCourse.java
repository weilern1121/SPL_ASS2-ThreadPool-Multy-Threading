package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class CloseCourse extends Action {
    private String courseName;
    private String depName;
    public CloseCourse(String courseToCloseName , String depName){
        this.courseName=courseToCloseName;
        this.depName=depName;
        setActionName("Close Course");
    }

    @Override
    protected void start() {
        DepartmentPrivateState depPS=(DepartmentPrivateState)ps;
        //first- unregister all the registered students
        //using a new action class because the need to do the delete from the course PrivateState
        MassRemoveCourseFromRegStu remCourse=new MassRemoveCourseFromRegStu();
        Promise<Boolean>p=sendMessage(remCourse,courseName,new CoursePrivateState());
        List<Action> l=new LinkedList<Action>();
        l.add(remCourse);
       //then- the course have -1 available spots and all the register students are unregister via
        //MassRemoveCourseFromRegStu actions ->unregister action.
        //now the only thing that left is to remove the course from the dep list
        then(l, new callback() {
            @Override
            public void call() {
                depPS.removeCourse(courseName);
                complete(true);
            }
        });
    }
}