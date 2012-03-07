package services.fitness;

import domain.ga.Population;

/**
 * User: dmitry
 * Date: 28.01.12
 * Time: 19:22
 */
public interface PopulationFitnessFunction {

    public double evaluate (Population population);

}
