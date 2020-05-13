package lb.com.network.project.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class State {

    @SerializedName("userID")
    @Expose
    private String userID;
    @SerializedName("isILL")
    @Expose
    private Integer isIll;
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Integer getIsIll() {
        return isIll;
    }

    public void setIsIll(Integer isIll) {
        this.isIll = isIll;
    }

}
