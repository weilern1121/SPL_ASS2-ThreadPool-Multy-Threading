package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

import java.util.LinkedList;

public class InitializeNewCourse extends Action {
    private Integer availableSpots;
    private LinkedList<String> prequisites;
    private String courseName;

    public  InitializeNewCourse(String courseName, LinkedList<String>prequisites,Integer availableSpots){
        this.availableSpots=availableSpots;
        this.prequisites=prequisites;
        this.courseName=courseName;
    }

    @Override
    protected void start() {
        CoursePrivateState c=(CoursePrivateState)ps;
        c.setAvailableSpots(availableSpots);
        c.setPrequisites(prequisites);
        c.setDepName(courseName);
        complete(c);
    }
}
