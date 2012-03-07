package services.hash;

import domain.ga.Individual;

/**
 * User: dmitry
 * Date: 27.11.11
 * Time: 20:14
 */
public interface HashFunctionService {

    public int getHashCode(Individual individual);

}
