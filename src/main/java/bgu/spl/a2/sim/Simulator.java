/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.sim.actions.*;
import bgu.spl.a2.sim.json.JsonAction;
import bgu.spl.a2.sim.json.JsonComputer;
import bgu.spl.a2.sim.json.JsonInput;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {


    public static ActorThreadPool actorThreadPool;
    public static ConcurrentLinkedQueue<PrivateState> output;
    public static Warehouse warehouse;
    public static int numOfThreads;
    private static CountDownLatch addActionsPerPhaseCounter;


    /**
     * Begin the simulation Should not be called before attachActorThreadPool()
     */
    public static void start() {
        actorThreadPool.start();
    }

    private static LinkedList convertListToLinkedList(List list) {
        LinkedList output = new LinkedList();
        for (Object aList : list)
            output.add(aList);
        return output;
    }

    private static LinkedList<Integer> convertStringListToIntegerList(List<String> list) {
        LinkedList<Integer> output = new LinkedList();
        for (String aList : list){
            if(aList.compareTo("-")==0)
                output.add(-1);
            else
                output.add(Integer.parseInt(aList));
        }
        return output;
    }


    private static String[] convertListToArray(List list) {
        String[] output = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            output[i] = (String) list.get(i);
        return output;
    }


    protected static void addActionsPerPhase(JsonAction jsonAction) {
        //initialize the fields for all actions
        Action currentAction=null;
        PrivateState actorPrivateState;
        String actorId;
        switch (jsonAction.getAction()) {
            case "Open Course": {
                LinkedList<String> prerequisites = convertListToLinkedList(jsonAction.getPrerequisites());
                currentAction = new OpenNewCourse(Integer.parseInt(jsonAction.getSpace()),
                        jsonAction.getCourse(), prerequisites, jsonAction.getDepartment());
                actorId = jsonAction.getDepartment();
                try {
                    actorPrivateState = actorThreadPool.getPrivateState(actorId);
                } catch (IllegalArgumentException e) {
                    actorPrivateState = new DepartmentPrivateState();
                }
            }
            break;

            case "Add Student": {
                currentAction = new AddStudent(jsonAction.getStudent(), jsonAction.getDepartment());
                actorId = jsonAction.getDepartment();

                try {
                    actorPrivateState = actorThreadPool.getPrivateState(actorId);
                } catch (IllegalArgumentException e) {
                    actorPrivateState = new DepartmentPrivateState();
                }
            }
            break;

            case "Participate In Course": {
                //initialize the grade
                Integer grade;
                if (jsonAction.getGrade().get(0) == "-" || jsonAction.getGrade().get(0) == "")
                    grade = -1;
                else
                    grade = Integer.parseInt(jsonAction.getGrade().get(0));
                currentAction = new ParticipatingInCourse(jsonAction.getStudent(), jsonAction.getCourse(), grade);
                actorId = jsonAction.getCourse();
                try {
                    actorPrivateState = actorThreadPool.getPrivateState(actorId);
                } catch (IllegalArgumentException e) {
                    actorPrivateState = new CoursePrivateState();
                }
            }
            break;

            case "Unregister": {
                currentAction = new Unregister(jsonAction.getStudent(), jsonAction.getCourse());
                actorId = jsonAction.getCourse();
                try {
                    actorPrivateState = actorThreadPool.getPrivateState(actorId);
                } catch (IllegalArgumentException e) {
                    actorPrivateState = new CoursePrivateState();
                }
            }
            break;

            case "Administrative Check": {
                String[] studentsNames = convertListToArray(jsonAction.getStudents());
                String[] conditions = convertListToArray(jsonAction.getConditions());
                currentAction = new CheckAdministrativeObligations( jsonAction.getComputer(), conditions, studentsNames);
                actorId = jsonAction.getDepartment();
                try {
                    actorPrivateState = actorThreadPool.getPrivateState(actorId);
                } catch (IllegalArgumentException e) {
                    actorPrivateState = new DepartmentPrivateState();
                }
            }
            break;

            case "Close Course": {
                actorId = jsonAction.getDepartment();
                currentAction = new CloseCourse(jsonAction.getCourse(),jsonAction.getDepartment());
                try {
                    actorPrivateState = actorThreadPool.getPrivateState(actorId);
                } catch (IllegalArgumentException e) {
                    actorPrivateState = new DepartmentPrivateState();
                }
            }
            break;

            //TODO - check what is the exact name of this case
            case "Add Spaces": {
                actorId = jsonAction.getCourse();
                if(jsonAction.getSpace()!=null)
                    currentAction = new NewPlacesInACourse(Integer.parseInt(jsonAction.getSpace()), jsonAction.getCourse());
                if(jsonAction.getNumber()!=null)
                    currentAction = new NewPlacesInACourse(Integer.parseInt(jsonAction.getNumber()), jsonAction.getCourse());
                try {
                    actorPrivateState = actorThreadPool.getPrivateState(actorId);
                } catch (IllegalArgumentException e) {
                    actorPrivateState = new CoursePrivateState();
                }
            }
            break;

            case "Register With Preferences": {

                actorId = jsonAction.getStudent();
                LinkedList<Integer> gradesList = convertStringListToIntegerList(jsonAction.getGrade());
                currentAction = new PreferenceList(jsonAction.getStudent(), jsonAction.getPreferences(), gradesList,0);

                try {
                    actorPrivateState = actorThreadPool.getPrivateState(actorId);
                } catch (IllegalArgumentException e) {
                    actorPrivateState = new StudentPrivateState();
                }
            }
            break;
            default:
                throw new IllegalArgumentException("Simulator don't recognize this action name");
        }
        currentAction.getResult().subscribe(() -> addActionsPerPhaseCounter.countDown());
        actorThreadPool.submit(currentAction, actorId, actorPrivateState);
    }

    /**
     * attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
     *
     * @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
     */
    public static void attachActorThreadPool(ActorThreadPool myActorThreadPool) {
        actorThreadPool = myActorThreadPool;
    }

    /**
     * shut down the simulation
     * returns list of private states
     */
    public static HashMap<String, PrivateState> end() {
        try {
            actorThreadPool.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new HashMap<String, PrivateState>(actorThreadPool.getActors());
    }


    public static int main(String[] args) {
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(args[0]));
            JsonInput inputJson = gson.fromJson(reader, JsonInput.class);

            //initialize threadPool
            attachActorThreadPool(new ActorThreadPool(inputJson.getThreads()));
            //initialize computers
            for (JsonComputer jsonComputer : inputJson.getComputers()) {
                Warehouse.getInstance()
                        .addComputer(jsonComputer.getType(), Long.parseLong(jsonComputer.getSigFail()),
                                Long.parseLong(jsonComputer.getSigSuccess()));
            }
            start();
            //phase 1
            addActionsPerPhaseCounter = new CountDownLatch(inputJson.getPhase1().size());
            if (addActionsPerPhaseCounter.getCount() != 0) {
                System.out.println("--Phase 1 Started!--");
                for (JsonAction actionJson1 : inputJson.getPhase1()) {
                    addActionsPerPhase(actionJson1);
                }
                try {
                    addActionsPerPhaseCounter.await();
                    System.out.println("--Phase 1 Completed!--");
                    //phase 2 - start his run only after phase 1 finished
                    //reSet the CountDownLatch to the size of jason2Array size
                    addActionsPerPhaseCounter = new CountDownLatch(inputJson.getPhase2().size());
                    if (addActionsPerPhaseCounter.getCount() != 0) {
                        System.out.println("--Phase 2 Started!--");
                        for (JsonAction actionJson2 : inputJson.getPhase2()) {
                            addActionsPerPhase(actionJson2);
                        }
                        try {
                            addActionsPerPhaseCounter.await();
                            System.out.println("--Phase 2 Completed!--");
                            //phase 3- start his run only after phase 1 and phase 2 finished
                            //reSet the CountDownLatch to the size of jason3Array size
                            addActionsPerPhaseCounter = new CountDownLatch(inputJson.getPhase3().size());
                            if (addActionsPerPhaseCounter.getCount() != 0) {
                                System.out.println("--Phase 3 Started!--");
                                for (JsonAction actionJson3 : inputJson.getPhase3()) {
                                    addActionsPerPhase(actionJson3);
                                }
                                try {
                                    addActionsPerPhaseCounter.await();
                                    System.out.println("--Phase 3 Completed!--");
                                    //streaming to result.ser after finished all the phases
                                    HashMap<String, PrivateState> result = end();
                                    FileOutputStream fout = new FileOutputStream("result.ser");
                                    try {
                                        ObjectOutputStream oos = new ObjectOutputStream(fout);
                                        oos.writeObject(result);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } catch (InterruptedException ePhase3) {
                                    ePhase3.printStackTrace();
                                }
                            }
                        } catch (InterruptedException ePhase2) {
                            ePhase2.printStackTrace();
                        }
                    } else {
                        end();
                    }
                } catch (InterruptedException ePhase1) {
                    ePhase1.printStackTrace();
                }
            } else {
                end();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //NOTE- not understand why there is a need to return 1 from main and not
        // change the func sign to void
        return 1;
    }
}