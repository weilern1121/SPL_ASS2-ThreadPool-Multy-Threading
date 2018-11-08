package bgu.spl.a2.sim.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonInput {

    //serialized fields per field in the json
    @SerializedName("threads")
    @Expose
    private Integer threads;
    @SerializedName("Computers")
    @Expose
    private List<JsonComputer> computers = null;
    @SerializedName("Phase 1")
    @Expose
    private List<JsonAction> phase1 = null;
    @SerializedName("Phase 2")
    @Expose
    private List<JsonAction> phase2 = null;
    @SerializedName("Phase 3")
    @Expose
    private List<JsonAction> phase3 = null;

    //getter per field
    public Integer getThreads() {   return threads;}

    public List<JsonComputer> getComputers() {  return computers;}

    public List<JsonAction> getPhase1() { return phase1; }

    public List<JsonAction> getPhase2() { return phase2; }

    public List<JsonAction> getPhase3() { return phase3; }


    //setter per field
    public void setThreads(Integer threads) {   this.threads = threads;}

    public void setComputers(List<JsonComputer> computers) {    this.computers = computers;}

    public void setPhase1(List<JsonAction> phase1) {    this.phase1 = phase1;}

    public void setPhase2(List<JsonAction> phase2) {    this.phase2 = phase2;}

    public void setPhase3(List<JsonAction> phase3) {    this.phase3 = phase3;}

}