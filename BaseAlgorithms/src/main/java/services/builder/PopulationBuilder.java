package services.builder;

/**
 * User: dmitry
 * Date: 28.01.12
 * Time: 20:26
 */
public interface PopulationBuilder {

    /**
     * Builds the initial population.
     * @param populationSize the wishful number of individuals in the population.
     * @return the number of individuals which were actually built.
     */
    public int build(int populationSize);

}
