package com.hp.oo.enginefacade.execution.log;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.lang.Object;import java.lang.Override;import java.lang.String;import java.util.Date;

/**
 * User: zruya
 * Date: 25/12/12
 * Time: 17:30
 */
public class StepInfo implements Serializable{
    private String stepId;
    private String stepName;
    private String path;
    private String responseType;
    private Date startTime;
    private Date endTime;
    private boolean paused;

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StepInfo)) return false;

        StepInfo that = (StepInfo) o;

        return new EqualsBuilder()
                .append(this.stepName, that.stepName)
                .append(this.path, that.path)
                .append(this.responseType, that.responseType)
                .append(this.startTime, that.startTime)
                .append(this.endTime, that.endTime)
                .append(this.stepId, that.stepId)
                .append(this.paused, that.paused)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.path)
                .append(this.stepId)
                .append(this.startTime)
                .append(this.endTime)
                .append(this.responseType)
                .append(this.stepName)
                .append(this.paused)
                .toHashCode();
    }
}