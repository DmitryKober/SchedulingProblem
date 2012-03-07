package services.fitness;

import domain.ga.Individual;

/**
 * User: dmitry
 * Date: 28.01.12
 * Time: 19:15
 */

public interface IndividualFitnessFunction {

    public long evaluate(Individual individual);

}
