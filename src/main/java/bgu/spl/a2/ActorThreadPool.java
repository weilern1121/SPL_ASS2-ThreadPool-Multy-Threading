package bgu.spl.a2;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool {

	/**
	 * creates a {@link ActorThreadPool} which has nthreads. Note, threads
	 * should not get started until calling to the {@link #start()} method.
	 *
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 *
	 * @param nthreads
	 *            the number of threads that should be started by this thread
	 *            pool
	 */

	private Vector<Thread> threads;
	private VersionMonitor vm;
	private ConcurrentHashMap<String,AtomicBoolean> actors_available;
	//need to change back to private
	public ConcurrentHashMap<String,ConcurrentLinkedQueue<Action>> actors_queues;
	private ConcurrentHashMap<String,PrivateState>actors_privateState;
	protected AtomicInteger numOfActions;
	private AtomicBoolean isShutDown;


	public ActorThreadPool(int nthreads) {
		if(nthreads<1)
			throw new IllegalArgumentException("nthreads must be positive");
		this.threads=new Vector<Thread>();
		this.actors_available= new ConcurrentHashMap<String, AtomicBoolean>();
		this.actors_privateState= new ConcurrentHashMap<String, PrivateState>();
		this.actors_queues= new ConcurrentHashMap<String, ConcurrentLinkedQueue<Action>>();
		this.vm=new VersionMonitor();
		this.isShutDown=new AtomicBoolean(false);
		this.numOfActions=new AtomicInteger(0);

		for( int i=0; i<nthreads; i++){
			Thread t=new Thread(() -> {
                //while the threadpool is working
                while(!isShutDown.get())
                {
                    //if there are no action to do go to sleep vailer will correct it tommorw
                    if (numOfActions.get()==0) {
                        try {
                        	AtomicBoolean isEmpty=new AtomicBoolean(true);
							for (Map.Entry<String,ConcurrentLinkedQueue<Action>> actor : actors_queues.entrySet())
								isEmpty.compareAndSet(true,actor.getValue().isEmpty());

                            int j = vm.getVersion();
                            vm.await(j);
                        } catch (InterruptedException e) {
                        }
                    }
                    else {
                        //find action to catch
                        for (Map.Entry<String, AtomicBoolean> actor : actors_available.entrySet()) {
                            if (numOfActions.get()>0 && actor.getValue().compareAndSet(true,false)) {
								//this block have to be synchronized- because only one thread can work on actor each time
								synchronized (actors_queues.get(actor.getKey())) {
									if (!actors_queues.get(actor.getKey()).isEmpty()) {
										if (!actors_queues.get(actor.getKey()).isEmpty())
											actors_queues.get(actor.getKey()).poll().handle(this,
													actor.getKey(),
													actors_privateState.get(actor.getKey()));
										vm.inc();
									}
									actor.getValue().compareAndSet(false, true);
								}
							}
                        }
                    }
                }
            });
			threads.add(t);
		}
	}

	/**
	 * submits an action into an actor to be executed by a thread belongs to
	 * this thread pool
	 *
	 * @param action
	 *            the action to execute
	 * @param actorId
	 *            corresponding actor's id
	 * @param actorState
	 *            actor's private state (actor's information)
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState) {

		//the condition is the check if the actor is exists.
		//if not- create it ,and then inc the monitor for update
		if(!(actors_available.containsKey(actorId))){
			actors_privateState.put(actorId,actorState);
			actors_queues.put(actorId,new ConcurrentLinkedQueue<Action>());
			actors_available.put(actorId,new AtomicBoolean(true));
		}

			actors_available.get(actorId).compareAndSet(true, false);
			actors_queues.get(actorId).offer(action);
			if(!action.isStarted())
				this.numOfActions.incrementAndGet();
			actors_available.get(actorId).compareAndSet(false, true);

			vm.inc();
	}

	/**
	 * closes the thread pool - this method interrupts all the threads and waits
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 *
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException
	 *             if the thread that shut down the threads is interrupted
	 */

	public void shutdown() throws InterruptedException {
			isShutDown.compareAndSet(false,true);
			vm.inc();
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		Iterator<Thread> iter = threads.iterator();
		while(iter.hasNext())
			iter.next().start();
		isShutDown.getAndSet(false);
	}

	public Map<String , PrivateState>getActors(){
		//getter have to be synchronized- like shown in class
		synchronized (actors_privateState) {
			return actors_privateState;
		}
	}

	public PrivateState getPrivateState (String actorId){
		//getter have to be synchronized- like shown in class
		synchronized ((actors_privateState)) {
			if (!(actors_privateState.containsKey(actorId)))
				throw new IllegalArgumentException("actorId key not found");
			return actors_privateState.get(actorId);
		}
	}
}
