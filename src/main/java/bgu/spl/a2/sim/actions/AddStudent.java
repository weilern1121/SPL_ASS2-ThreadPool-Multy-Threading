package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;


public class AddStudent extends Action {

    private String stdName;
    private String depName;


    public AddStudent (String stdName , String depName){
        super();
        this.stdName=stdName;
        this.depName=depName;
        setActionName("Add Student");
    }

    protected void start(){

        DepartmentPrivateState d= (DepartmentPrivateState)pool.getPrivateState(depName);
        Action createStudent=new Action() {
            @Override
            protected void start() {
                StudentPrivateState stuPS=(StudentPrivateState)ps;
                complete(true);
            }
        };
        Promise p=sendMessage(createStudent,stdName,new StudentPrivateState());
        List<Action>preCondition=new LinkedList<>();
        preCondition.add(createStudent);
        then(preCondition, new callback() {
            @Override
            public void call() {
                if((Boolean)p.get()){
                    Action addStuToDep=new Action() {
                        @Override
                        protected void start() {
                            DepartmentPrivateState dPS1=(DepartmentPrivateState)ps;
                            if(!(d.getStudentList().contains(stdName))){
                                d.getStudentList().add(stdName);
                                complete(true);
                            }
                            else{ complete(false);}
                        }
                    };
                    Promise p1=sendMessage(addStuToDep,depName,new DepartmentPrivateState());
                    LinkedList<Action>preCondition1=new LinkedList<>();
                    preCondition1.add(addStuToDep);
                    then(preCondition1, new callback() {
                        @Override
                        public void call() {
                            complete(true);
                        }
                    });
                }
            }
        });
       /*
        Action createStudent=new Action() {
            @Override
            protected void start() {
                complete("The student actor added to ThreadPool");
            }
        };
        StudentPrivateState newStudent=new StudentPrivateState();
        //?? GAP - not sure how to initialize the long signature
        sendMessage(createStudent,stdName,newStudent);
        LinkedList<Action> preCondition=new LinkedList<>();
        preCondition.add(createStudent);
        then(preCondition,()->{
            ((DepartmentPrivateState)pool.getPrivateState(depName)).addStudent(stdName);
            complete("The student added to Department");
        });
        */
    }

}
