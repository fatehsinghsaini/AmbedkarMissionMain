
package com.info.work.ambedkarmission.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StateDistrict {

    @SerializedName("states")
    @Expose
    private List<State> states = null;

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

}
