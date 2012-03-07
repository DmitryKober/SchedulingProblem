package domain.ga;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: dmitry
 * Date: 17.12.11
 * Time: 1:31
 */

/**
 * The population contains only unique individuals.
 */
public class Population {

    private List<Individual> individuals;
    private List<Integer> register;  // individuals' identifiers register

    {
        individuals = new ArrayList<Individual>();
        register = new ArrayList<Integer>();
    }

    /**
     * Adds a specified individual to the population if there is no
     * such an individual.
     *
     * Returns <b>true</b> if the addition successfully proceeded, otherwise <b>false</b>.
     */
    public boolean addIndividual(Individual individual) {
        if (unique(individual)) {
            individuals.add(individual);
            register.add(individual.getIdentifier());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes a specified individual from the population, if it presents there.
     *
     * @return <b>true</b> if the deletion succeeded, otherwise <b>false</b>
     */
    public boolean removeIndividual(Individual individual) {
        int identifier = individual.getIdentifier();

        if (register.contains(identifier)) {
            individuals.remove(individual);
            register.remove(identifier);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tests if the population such an individual is unique throughout the population..
     *
     * @return <b>true</b> if the population doesn't contain such an individual yet,
     * <b>false</b> otherwise.
     */
    private boolean unique(Individual individual) {
        return !(register.contains(individual.getIdentifier()));
    }

    /**
     * Returns an individual with a specified identifier if it presents in the population,
     * null otherwise.
     */
    public Individual getIndividual(Integer identifier) {
        assert identifier != null : "Identifier must not be null";
        if (register.contains(identifier)) {
            Iterator<Individual> iterator = individuals.iterator();
            while (iterator.hasNext()) {
                Individual nextIndividual = iterator.next();
                if (nextIndividual.getIdentifier().equals(identifier)) {
                    return nextIndividual;
                }
            }
        }

        return null;

    }

    /**
     * Returns the individuals list's size;
     */
    public int getPopulationSize() {
        return individuals.size();
    }

    // TODO: implement selection and evolution operators
}
