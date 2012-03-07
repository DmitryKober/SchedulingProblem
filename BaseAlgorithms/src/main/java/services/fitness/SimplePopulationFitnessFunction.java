package services.fitness;

import domain.ga.Population;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * User: dmitry
 * Date: 28.01.12
 * Time: 19:27
 */
public class SimplePopulationFitnessFunction implements PopulationFitnessFunction {

    @Autowired
    private IndividualFitnessFunction individualFitnessFunction;

    /**
     * Returns the average value of the individuals' fitness functions.
     */
    public double evaluate(Population population) {
        int populationSize = population.getPopulationSize();

        double averageTime = 0;
        for (int i = 0; i < populationSize; i++) {
            averageTime += individualFitnessFunction.evaluate(population.getIndividual(i)) / (double) populationSize;
        }

        return averageTime;
    }
}
