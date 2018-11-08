package bgu.spl.a2.sim;

import bgu.spl.a2.Promise;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * represents a warehouse that holds a finite amount of computers
 *  and their suspended mutexes.
 * 
 */
public class Warehouse {

    //singleton
        private static class WarehouseSingletonHolder {
            private static Warehouse instance = new Warehouse();
        }

        public static Warehouse getInstance() {
            return WarehouseSingletonHolder.instance;
        }

    //the class
    private HashMap<String , SuspendingMutex> computersMutex;

    private Warehouse() {
        this.computersMutex = new HashMap<>();
    }

    public Promise getComputersMutex(String computerName){
        if(computersMutex.containsKey(computerName)){
            Promise promise=computersMutex.get(computerName).down();
            return promise;
        }
        else
            throw new IllegalArgumentException("getcomputersMutex- computer not found");
    }

    public void releaseTheComputerMutex(String computerName){
        if(computersMutex.containsKey(computerName)){
            computersMutex.get(computerName).up();
        }
        else
            throw new IllegalArgumentException("releaseTheComputerMutex- computer not found");
    }

    public void addComputer(String computerType,long successsig, long failsig){
        if(computerType==null)
            throw new NullPointerException("addComputer- null computerName");
        computersMutex.put(computerType,new SuspendingMutex(new Computer(computerType, successsig,  failsig)));
        }
    }

