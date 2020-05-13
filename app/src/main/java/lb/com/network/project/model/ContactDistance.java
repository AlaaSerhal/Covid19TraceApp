package lb.com.network.project.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactDistance {

    @SerializedName("userID1")
    @Expose
    private String userID1;
    @SerializedName("userID2")
    @Expose
    private String userID2;
    @SerializedName("distance")
    @Expose
    private Double distance;

    public String getUserID1() {
        return userID1;
    }

    public void setUserID1(String userID1) {
        this.userID1 = userID1;
    }

    public String getUserID2() {
        return userID2;
    }

    public void setUserID2(String userID2) {
        this.userID2 = userID2;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

}
