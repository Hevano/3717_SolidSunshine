package ca.bcit.emptyproject;

import java.util.Date;

public class MeetUp {
    private String MeetName;
    private String MeetLoc;
    private String MeetWeb;
    private String MeetType;
    private String MeetDate;
    private int Version;

    public MeetUp() {
    }

    public String getMeetName() {
        return MeetName;
    }

    public void setMeetName(String meetName) {
        MeetName = meetName;
    }

    public String getMeetLoc() {
        return MeetLoc;
    }

    public void setMeetLoc(String meetLoc) {
        MeetLoc = meetLoc;
    }

    public String getMeetWeb() {
        return MeetWeb;
    }

    public void setMeetWeb(String meetWeb) {
        MeetWeb = meetWeb;
    }

    public String getMeetType() {
        return MeetType;
    }

    public void setMeetType(String meetType) {
        MeetType = meetType;
    }

    public String getMeetDate() {
        return MeetDate;
    }

    public void setMeetDate(String meetDate) {
        MeetDate = meetDate;
    }


    public int getVersion() {
        return Version;
    }

    public void setVersion(int version) {
        Version = version;
    }
}
