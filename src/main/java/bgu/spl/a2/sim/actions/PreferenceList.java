package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;
import java.util.List;

public class PreferenceList extends Action {
    private int prefListIndex;
    private String studentId;
    private LinkedList<String> CoursePreference;
    //??GAP -we assume that the gradeList is synchronized with the order of the CoursePreference list
    //I think that we should do get() and not getFirstx
    private LinkedList<Integer> gradeList;

    public PreferenceList (String studentId , List<String>CoursePreference , List<Integer>gradeList , int prefListIndex){
        this.studentId=studentId;
        this.CoursePreference=new LinkedList();
        for(String course : CoursePreference)
            this.CoursePreference.add(course);
        this.gradeList=(LinkedList<Integer>) gradeList;
        this.prefListIndex=prefListIndex;
        setActionName("Preference List");
    }

    protected void start(){
       //the recoursive stop condition- if there are no more course to try to register in pref list
        if(prefListIndex==CoursePreference.size())
            complete(false);
        else{
            ParticipatingInCourse register = new ParticipatingInCourse(studentId, CoursePreference.get(prefListIndex), gradeList.get(prefListIndex));
            LinkedList preCond = new LinkedList();
            Promise p=sendMessage(register,CoursePreference.get(prefListIndex),new CoursePrivateState());
          preCond.add(register);
          then(preCond,()-> {
            //if registered for the first course in coursePrefList
            if((Boolean)p.get())
               complete(true);
            else{
                    //else- there are more courses to try to register-
                    //go to the next place in course list and recurrsevly try to register
                    PreferenceList recurseReg = new PreferenceList(studentId,CoursePreference,gradeList,prefListIndex+1);
                    Promise p1=sendMessage(recurseReg,studentId,new StudentPrivateState());
                    p1.subscribe(new callback() {
                        @Override
                        public void call() {
                            complete(p1.get());
                        }
                    });
                }
            });
        }
    }
}
