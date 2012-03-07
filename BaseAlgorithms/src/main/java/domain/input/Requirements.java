package domain.input;

/**
 * User: dmitry
 * Date: 12.02.12
 * Time: 20:55
 */
public class Requirements {

    private int initialPopulationSize;
    private int processorsAmount;

    public int getInitialPopulationSize() {
        return initialPopulationSize;
    }

    public void setInitialPopulationSize(int initialPopulationSize) {
        this.initialPopulationSize = initialPopulationSize;
    }

    public int getProcessorsAmount() {
        return processorsAmount;
    }

    public void setProcessorsAmount(int processorsAmount) {
        this.processorsAmount = processorsAmount;
    }
}
