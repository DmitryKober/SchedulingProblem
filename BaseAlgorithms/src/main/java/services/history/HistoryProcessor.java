package services.history;

import domain.ga.History;
import domain.ga.HistoryEvent;

import java.util.Iterator;
import java.util.List;

/**
 * User: dmitry
 * Date: 12.02.12
 * Time: 17:09
 */
public class HistoryProcessor {

    /**
     * Checks, if a specified history contains an event a specified task
     * is a nature of.
     */
    public static boolean isTaskProcessed (final History history, final Integer taskProcessed) {
        assert history != null : "Can't process a null history";
        assert taskProcessed != null : "The history can't contain a null object";

        Iterator<HistoryEvent> eventsIterator = history.iterator();

        while (eventsIterator.hasNext()) {
            HistoryEvent historyEvent = eventsIterator.next();
            if (historyEvent.getTask().equals(taskProcessed)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks, if a specified event presents in the history.
     */
    public static boolean isEventTookPlace(final History history, final HistoryEvent event) {
        assert history != null : "Can't process a null history";
        assert event != null : "The history can't contain a null event";

        if (history.contains(event)) {
            return true;
        }

        return false;
    }

    /**
     * Adds a new event to the history.
     * Checks it there is already such an event.
     */
    public static void addEvent(History history,
                                final Integer taskProcessed,
                                final int processor,
                                final List<Integer> readyTasks,
                                final List<Integer> parentBoundChanges) {
        assert isTaskProcessed(history, taskProcessed) : "There is already such a task";

        HistoryEvent event = new HistoryEvent(taskProcessed, processor, readyTasks, parentBoundChanges);
        history.add(event);
    }

    /**
     * Changes the history: branches the history from the event's point (if the event took place).
     */
    public static void branch(final History history,
                              final Integer taskProcessed,
                              final int processor,
                              final List<Integer> readyTasks,
                              final List<Integer> parentBoundChanges) {
        assert history != null : "Can't process a null history";
        assert isTaskProcessed(history, taskProcessed) : "Can't branch from the event that didn't take place";

        int historyLength = history.size();

        for (int i = 0; i < historyLength; i++) {
            HistoryEvent historyEvent = history.get(i);
            if (historyEvent.getTask().equals(taskProcessed)) {
                removeRange(history, i, historyLength);
                break;
            }
        }

        HistoryEvent event = new HistoryEvent(taskProcessed, processor, readyTasks, parentBoundChanges);
        history.add(event);
    }

    /**
     * Removes history events between the startPosition and the endPosition.
     */
    private static void removeRange (final History history,
                                     final int startPosition,
                                     final int endPosition) {
        assert history != null : "Can't process a null history";
        int historySize = history.size();
        assert (startPosition >= 0) && (startPosition < historySize) : "The startPosition must be in range [0, historySize)";
        assert (endPosition >= startPosition) && (endPosition < historySize) : "The endPosition mus be in range [startPosition, historySize)";

        int removalIterations = endPosition - startPosition;
        for (int i = 0; i < removalIterations; i++) {
            history.remove(startPosition);
        }

    }

}
