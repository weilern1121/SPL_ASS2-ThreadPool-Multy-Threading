package bgu.spl.a2.sim.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonComputer {

    //serialized fields
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Sig Success")
    @Expose
    private String sigSuccess;
    @SerializedName("Sig Fail")
    @Expose
    private String sigFail;

    //getter per field
    public String getType() {
        return type;
    }

    public String getSigSuccess() {
        return sigSuccess;
    }

    public String getSigFail() {
        return sigFail;
    }

    //setter per field
    public void setType(String type) {
        this.type = type;
    }

    public void setSigFail(String sigFail) {
        this.sigFail = sigFail;
    }

    public void setSigSuccess(String sigSuccess) {
        this.sigSuccess = sigSuccess;
    }
}