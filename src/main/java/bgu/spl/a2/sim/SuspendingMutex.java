package bgu.spl.a2.sim;
import bgu.spl.a2.Promise;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * this class is related to {@link Computer}
 * it indicates if a computer is free or not
 * 
 * Note: this class can be implemented without any synchronization. 
 * However, using synchronization will be accepted as long as the implementation is blocking free.
 *
 */
public class SuspendingMutex {

	private Computer computer;
	private AtomicBoolean flag;
	private Queue<Promise<Computer>> waitingList;


	public SuspendingMutex (Computer computer){
		this.flag= new AtomicBoolean(true);
		this.waitingList=new LinkedList<>();
		this.computer = computer;
	}

	/**
	 * Computer acquisition procedure
	 * Note that this procedure is non-blocking and should return immediatly
	 *
	 * 
	 * @return a promise for the requested computer
	 */
	public Promise<Computer> down() {
		Promise promise=new Promise();
			if(flag.compareAndSet(true,false)){
				promise.resolve(computer);
			}
			else
				waitingList.add(promise);
			return promise;
	}
	/**
	 * Computer return procedure
	 * releases a computer which becomes available in the warehouse upon completion
	 */
	public void up(){
		if(flag.compareAndSet(false,true)){
			while(!waitingList.isEmpty())
				waitingList.poll().resolve(computer);
		}
	}

}
