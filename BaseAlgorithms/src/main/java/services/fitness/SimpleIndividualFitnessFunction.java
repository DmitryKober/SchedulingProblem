package services.fitness;

import domain.ga.Individual;
import domain.ga.Schedule;

/**
 * User: dmitry
 * Date: 28.01.12
 * Time: 19:18
 */
public class SimpleIndividualFitnessFunction implements IndividualFitnessFunction {

    /**
     * Returns the time of the most engaged processor
     */
    public long evaluate(Individual individual) {
        Schedule schedule = individual.getSchedule();
        int mostEngagedProcessorIndex =  schedule.getMostEngagedProcessor();
        return schedule.getProcessorTime(mostEngagedProcessorIndex);
    }

}
