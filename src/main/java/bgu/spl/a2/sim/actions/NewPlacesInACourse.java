package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class NewPlacesInACourse extends  Action{
    private int newValue;
    private String courseToChange;

    public NewPlacesInACourse(int num ,String courseName)
    {
        this.newValue=num;
        if(courseName==null)
            throw new IllegalArgumentException("NewPlacesInACourse - illegal name input");
        this.courseToChange= courseName;
        setActionName("Add Spaces");

    }

    protected void start(){
        callback callback= new callback(){
            @Override
            public void call() {
                CoursePrivateState course = (CoursePrivateState) pool.getPrivateState(courseToChange);
                if (!(newValue < (course.getAvailableSpots() - course.getAvailableSpots()))) {
                    course.setAvailableSpots(course.getAvailableSpots()+newValue);
                    complete("The new available number is changed");
                }
                else
                    complete("ERROR - the new number is smaller than the previous");
            }
        };
        setContinued(callback);
        sendMessage(this,courseToChange, pool.getPrivateState(courseToChange));
    }
}
