package bgu.spl.a2;

import java.util.Collection;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * an abstract class that represents an action that may be executed using the
 * {@link ActorThreadPool}
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 * @param <R> the action result type
 */
public abstract class Action<R> {

    private Promise promise;
    private String actionName;
    protected ActorThreadPool pool;
    private callback continued;
    private AtomicInteger counter;
    protected String actorID;
    protected PrivateState ps;

	/**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected abstract void start();
    

    /**
    *
    * start/continue handling the action
    *
    * this method should be called in order to start this action
    * or continue its execution in the case where it has been already started.
    *
    * IMPORTANT: this method is package protected, i.e., only classes inside
    * the same package can access it - you should *not* change it to
    * public/private/protected
    *
    */
   /*package*/ final void handle(ActorThreadPool pool , String actorId, PrivateState actorState){
       if(actorId==null || actorState==null)
           throw new IllegalArgumentException();
       if(continued==null){
           this.pool=pool;
           this.actorID=actorId;
           this.ps=actorState;
           //if(actionName==null)
            //this.actionName="";
           this.counter=new AtomicInteger(0);
           start();
       }
       else
           continued.call();
   }
    
    
    /**
     * add a callback to be executed once *all* the given actions results are
     * resolved
     * 
     * Implementors note: make sure that the callback is running only once when
     * all the given actions completed.
     *
     * @param actions
     * @param callback the callback to execute once all the results are resolved
     */
    protected final void then(Collection<? extends Action<?>> actions, callback callback) {
       	this.continued=callback;
        if(actions==null ||actions.size()==0){
            String actorId=actorID;
            PrivateState PS=ps;
            sendMessage(this, actorId, PS);
        }
        else {
            this.counter.compareAndSet(0,actions.size());
            String actorId=actorID;
            PrivateState PS=ps;
            Iterator<? extends Action<?>> it=actions.iterator();
            while (it.hasNext()) {
            it.next().getResult().subscribe(() -> {
                if (counter.get() > 1) {
                    counter.decrementAndGet();
                } else {
                    sendMessage(this, actorId, PS);
                }
            });
            }
        }
    }

    /**
     * resolve the internal result - should be called by the action derivative
     * once it is done.
     *
     * @param result - the action calculated result
     */
    protected final void complete(R result) {
        if(pool.numOfActions.get()!=0)
            pool.numOfActions.decrementAndGet();
        if(actionName!=null && actionName!="")
            this.ps.addRecord(actionName);
        promise.resolve(result);
    }

    /**
     * @return action's promise (result)
     */
    public final Promise<R> getResult() {
    	//NOTE??? we wonder about adding a isResolve condition
        if(this.promise==null)
            this.promise=new Promise();
        return this.promise;
    }
    
    /**
     * send an action to an other actor
     * 
     * @param action
     * 				the action
     * @param actorId
     * 				actor's id
     * @param actorState
	 * 				actor's private state (actor's information)
	 *    
     * @return promise that will hold the result of the sent action
     */
	public Promise<?> sendMessage(Action<?> action, String actorId, PrivateState actorState){
	    if(action==null| actorId==null|actorState==null){
	        throw new IllegalArgumentException("cannot submit action with null parameter "+action+" "+actorId+" "+actorState);
        }
        pool.submit(action,actorId,actorState);
        return action.promise;
	}

	public void setActionName (String actionName){
             this.actionName=actionName;
    }

    public  String getActionName (){
	        return this.actionName;
    }
    protected boolean isStarted(){
	    return !(this.continued==null);
    }
    public void setContinued(callback callback){this.continued=callback; }

    public Action(){
        this.promise = new Promise();
    }

}
