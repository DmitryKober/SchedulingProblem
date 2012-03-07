package services.hash;

import domain.ga.Individual;
import domain.ga.Processor;
import domain.ga.Schedule;
import domain.ga.Task;

import java.util.Iterator;

/**
 * User: dmitry
 * Date: 27.11.11
 * Time: 20:24
 */
public class HashFunctionServiceImpl implements HashFunctionService {

    /**
     * Java standard hash function
     *
     * @param individual an individual the hash is calculated for
     * @return the hash code
     */
    public int getHashCode(Individual individual) {
        Schedule schedule = individual.getSchedule();
        StringBuilder str = new StringBuilder();

        final Iterator<Processor> processorsIterator = schedule.getProcessorsIterator();
        while (processorsIterator.hasNext()) {
            final Processor nextProcessor = processorsIterator.next();
            final Iterator<Task> tasksIterator = nextProcessor.iterator();
            while (tasksIterator.hasNext()) {
                final Task nextTask = tasksIterator.next();
                str.append(nextTask.getIdentifier().intValue());
            }
        }

		String toDecode = str.toString();
		return toDecode.hashCode();
    }

}
