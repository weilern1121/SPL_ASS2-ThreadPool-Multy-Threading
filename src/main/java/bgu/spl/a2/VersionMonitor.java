package bgu.spl.a2;

/**
 * Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #getVersion()} once you have a version number, you can call
 * {@link #await(int)} with this version number in order to wait until this
 * version number changes.
 *
 * you can also increment the version number by one using the {@link #inc()}
 * method.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class VersionMonitor {

    private int _ver;

    public int getVersion() {
        //getter have to be synchronized- like shown in class
        synchronized (this){
            return this._ver;
        }
    }

    //@PRE: _ver >=0
    //@POST: getVersion()==@PRE (getVersion())+1
    public void inc() {
        //NOTE: there is a need to limit that only one thread can increment
        // the ver without interrupting, as shown in classEven in the class
        synchronized (this){
            this._ver+=1;
            notifyAll();
        }
    }

    public void await(int version) throws InterruptedException {
        //NOTE: there is a need to sync this function, as same as shown in
        //practicle session no.7 - in class checker.
        synchronized (this){
            while(version ==_ver){
                try{
                    this.wait();
                } catch (InterruptedException e){}
            }
        }
    }

    //additional functions
    protected VersionMonitor(){
        this._ver=0;
    }
}
