package com.hp.score.lang;

import com.hp.score.api.ScoreEvent;
import com.hp.score.vos.StartBranchDataContainer;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * User: maromg
 * Date: 11/06/2014
 */
public class ExecutionRuntimeServices implements Serializable {

    private static final long serialVersionUID = 2557429503280678353L;

    protected static final String EXECUTION_PAUSED = "EXECUTION_PAUSED";
    private static final String BRANCH_DATA = "BRANCH_DATA";

    protected static final String SCORE_EVENTS_QUEUE = "SCORE_EVENTS_QUEUE";

    protected static final String NO_WORKERS_IN_GROUP = "NO_WORKERS_IN_GROUP";

    protected Map<String, Serializable> myMap = new HashMap<>();

    public ExecutionRuntimeServices() {
    }

    public ExecutionRuntimeServices(Map<String, Serializable> parameters) {
        this.myMap = new HashMap<>(parameters);
    }

    public ExecutionRuntimeServices(ExecutionRuntimeServices origin) {
        this.myMap = new HashMap<>(origin.myMap);
    }

    public void pause() {
        myMap.put(EXECUTION_PAUSED, Boolean.TRUE);
    }

    public boolean isPaused() {
        return myMap.containsKey(EXECUTION_PAUSED) && myMap.get(EXECUTION_PAUSED).equals(Boolean.TRUE);
    }

    public void addEvent(String eventType, Serializable eventData) {
        @SuppressWarnings("unchecked")
        Queue<ScoreEvent> eventsQueue = getFromMap(SCORE_EVENTS_QUEUE);
        if (eventsQueue == null) {
            eventsQueue = new ArrayDeque<>();
            myMap.put(SCORE_EVENTS_QUEUE, (ArrayDeque) eventsQueue);
        }
        eventsQueue.add(new ScoreEvent(eventType, eventData));
    }

    public ArrayDeque<ScoreEvent> getEvents() {
        return getFromMap(SCORE_EVENTS_QUEUE);
    }

    public void setNoWorkerInGroup(String groupName){
           myMap.put(NO_WORKERS_IN_GROUP, groupName);
    }

    public String getNoWorkerInGroupName(){
        return getFromMap(NO_WORKERS_IN_GROUP);
    }

    protected <T> T getFromMap(String key) {
        if (myMap.containsKey(key)) {
            Serializable value = myMap.get(key);
            if (value != null) {
                @SuppressWarnings("unchecked")
                T retVal = (T) value;
                return retVal;
            }
        }
        return null;
    }

    public void addBranch(Long startPosition, Long executionPlanId, Map<String, Serializable> context) {
        addBranch(startPosition, executionPlanId, context, this);
    }

    protected void addBranch(Long startPosition, Long executionPlanId, Map<String, Serializable> context, ExecutionRuntimeServices executionRuntimeServices) {
        if (!myMap.containsKey(BRANCH_DATA)) {
            myMap.put(BRANCH_DATA, new ArrayList<StartBranchDataContainer>());
        }
        @SuppressWarnings("unchecked")
        List<StartBranchDataContainer> branchesData = (List<StartBranchDataContainer>) myMap.get(BRANCH_DATA);
        branchesData.add(new StartBranchDataContainer(startPosition, executionPlanId, context, executionRuntimeServices));
    }

    public List<StartBranchDataContainer> getBranchesData() {
        @SuppressWarnings("unchecked")
        List<StartBranchDataContainer> branchesData = (List<StartBranchDataContainer>) myMap.get(BRANCH_DATA);
        return branchesData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExecutionRuntimeServices that = (ExecutionRuntimeServices) o;

        return new EqualsBuilder()
                .append(this.myMap, that.myMap)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.myMap)
                .toHashCode();
    }
}