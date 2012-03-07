package domain.ga;

import org.springframework.beans.factory.annotation.Autowired;
import services.hash.HashFunctionService;

/**
 * User: dmitry
 * Time: 19:26
 */
public class Individual {

    private Integer identifier;      // identifier. Is calculated via a hash-function.
    private Schedule schedule;       // idealSchedule
    private int hash;                // unique idealSchedule representation

    private History history;

    @Autowired
    private HashFunctionService hashFunctionService;

    public Individual (Integer identifier, int processorsNumber) {
        schedule = new Schedule(processorsNumber);
        this.history = new History();
        this.identifier = identifier;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public int getHash() {
        return hash;
    }

    public void evaluateHash() {
        this.hash = hashFunctionService.getHashCode(this);
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }
}
