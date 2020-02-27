package ca.bcit.emptyproject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("LOCATION")
    @Expose
    private String LOCATION;
    public String getLOCATION() {
        return LOCATION;
    }
    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    @SerializedName("NAME")
    @Expose
    private String NAME;
    public String getNAME() {
        return NAME;
    }
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    @SerializedName("FACILITY_TYPE")
    @Expose
    private String FACILITY_TYPE;
    public String getFACILITY_TYPE() {
        return FACILITY_TYPE;
    }
    public void setFACILITY_TYPE(String FACILITY_TYPE) {
        this.FACILITY_TYPE = FACILITY_TYPE;
    }

    @SerializedName("WEBLINK")
    @Expose
    private String WEBLINK;
    public String getWEBLINK() {
        return WEBLINK;
    }
    public void setWEBLINK(String WEBLINK) {
        this.WEBLINK = WEBLINK;
    }
}
