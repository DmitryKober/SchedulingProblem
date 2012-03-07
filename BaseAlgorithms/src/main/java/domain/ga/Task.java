package domain.ga;

import java.io.Serializable;

/**
 * User: dmitry
 * Date: 27.11.11
 * Time: 18:35
 */
public class Task implements Serializable {
    public static final Integer DEFAULT_VALUE = new Integer(-1);
    public static final Integer DEFAULT_EARLY_BEGINNING_TIME = new Integer(0);

    private Integer identifier;       // identifier
    private long processingTime;      // task's execution time
    private long earlyBeginningTime;  // early beginning time according to the parent tasks
    private long actBeginningTime;    // actual beginning time
    private int parentBound;          // parent tasks which are not done yet

    /**
     * Constructor
     *
     */
    public Task (Integer identifier, long processingTime) {
        this.identifier = identifier;
        this.processingTime = processingTime;
    }

    public Integer getIdentifier() {
        return this.identifier;
    }

    public long getProcessingTime() {
        return this.processingTime;
    }

    public long getEarlyBeginningTime() {
        return earlyBeginningTime;
    }

    public void setEarlyBeginningTime(long earlyBeginningTime) {
        this.earlyBeginningTime = earlyBeginningTime;
    }

    public long getActBeginningTime() {
        return actBeginningTime;
    }

    public void setActBeginningTime(long actBeginningTime) {
        this.actBeginningTime = actBeginningTime;
    }

    public int getParentBound() {
        return parentBound;
    }

    public void setParentBound(int parentBound) {
        this.parentBound = parentBound;
    }

    /**
     * Custom clone method.
     */
    public Task clone() {
        Task clone = null;
        try {
            clone = (Task) super.clone();
            // setting default values. These fields will have
            // different values for each clone
            clone.setActBeginningTime(DEFAULT_VALUE);
            clone.setEarlyBeginningTime(DEFAULT_EARLY_BEGINNING_TIME);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return clone;
    }
}
