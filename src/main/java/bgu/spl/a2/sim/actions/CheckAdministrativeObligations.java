package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CheckAdministrativeObligations extends Action {

    private String computerName;
    private String[] conditions;
    private String[] StuToCheck;

    public CheckAdministrativeObligations(String computerName, String[] conditions ,String[] StuToCheck){
        this.computerName=computerName;
        this.conditions=conditions;
        this.StuToCheck=StuToCheck;
        setActionName("Administrative Check");
    }
    @Override
    protected void start() {
        DepartmentPrivateState depPS=(DepartmentPrivateState)ps;
        Promise<Map<String,Integer>>pr;
        LinkedList<Action>preActions=new LinkedList<>();
        Map<String,Promise> promises=new HashMap<String,Promise>();
        for (String stu:StuToCheck)
        {
            GetStuGradesHashMap gsghm=new GetStuGradesHashMap();
            preActions.add(gsghm);
            pr=sendMessage(gsghm,stu,new StudentPrivateState());
            promises.put(stu,pr);
        }
        this.then(preActions, new callback() {
            @Override
            public void call() {
                HashMap<String,HashMap<String,Integer>>allStuGradesHashMaps=
                        new HashMap<String,HashMap<String,Integer>>();
                for (String str :promises.keySet()) {
                    allStuGradesHashMaps.put(str,(HashMap)promises.get(str).get());
                }
                Promise<Computer> promiseComputer= Warehouse.getInstance().getComputersMutex(computerName);
                promiseComputer.subscribe(new callback() {
                    @Override
                    public void call() {
                        //got the computer- foreach student calculate and submit the signature
                        Computer computer=promiseComputer.get();
                        LinkedList<Action>l1=new LinkedList<>();
                        for (String stu: allStuGradesHashMaps.keySet())
                        {
                            long stusig=computer.checkAndSign(Arrays.asList(conditions),allStuGradesHashMaps.get(stu));
                            SetStuSig s=new SetStuSig(stusig);
                            l1.add(s);
                            sendMessage(s,stu,new StudentPrivateState());
                        }
                        Warehouse.getInstance().releaseTheComputerMutex(computerName);
                        then(l1, new callback() {
                            @Override
                            public void call() {
                               // complete(true);
                            }
                        });
                    }
                });
                complete(true);
            }

        });
//        Action act=new Action() {
//            @Override
//            protected void start() {
//                Promise<Computer> computer= Warehouse.getInstance().getComputersMutex(computerName);
//                computer.subscribe(()->{
//                    for(int i=0;i<studentsNames.length;i++){
//                        ((StudentPrivateState)pool.getPrivateState(studentsNames[i])).setSignature
//                                (computer.get().checkAndSign(Arrays.asList(conditions),((StudentPrivateState)pool.getPrivateState(studentsNames[i])).getGrades()));
//                    }
//                });
//                Warehouse.getInstance().releaseTheComputerMutex(computerName);
//            }
//        };
//        LinkedList preCond=new LinkedList();
//        preCond.add(act);
//        then(preCond, () -> complete(true));
    }
}
