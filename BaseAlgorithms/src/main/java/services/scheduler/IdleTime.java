package services.scheduler;

/**
 * Represents the idle on the processor
 */
public class IdleTime {

    private int processorIndex; // a processor the idle is placed on
    private long idleTime;

    public IdleTime(int processorIndex, long idleTime) {
        this.processorIndex = processorIndex;
        this.idleTime = idleTime;
    }

    public int getProcessorIndex() {
        return processorIndex;
    }

    public void setProcessorIndex(int processorIndex) {
        this.processorIndex = processorIndex;
    }

    public long getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(long idleTime) {
        this.idleTime = idleTime;
    }
}