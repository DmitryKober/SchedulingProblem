package custom_schedule;

import domain.ga.Processor;
import domain.ga.Task;
import domain.ga.TasksComparator;
import domain.input.GraphRepresentation;
import services.clone.CloneService;

import java.util.*;

/**
 * User: dmitry
 * Date: 03.01.12
 * Time: 20:44
 */
public class CustomScheduleBuilderImpl extends AbstractCustomScheduleBuilder {

    private List<Relation> relations;

    /**
     * Constructor
     */
    public CustomScheduleBuilderImpl (int processorsNumber) {
        super(processorsNumber);
        relations = new ArrayList<Relation>();
    }

    @Override
    public void buildSchedule(long time, int tasksNumber) {
        // begin: amount of tasks distribution
        List<Integer> tasksAmountDistribution = new ArrayList<Integer> ();
        initTasksAmountDistributionList(tasksAmountDistribution);

        int averageTasksNumber = tasksNumber / processorsNumber;
        int tasksRemaining = tasksNumber;
        int consideredProcessorsNumber = processorsNumber;
        boolean generate = true;
        while (tasksRemaining != 0) {

            if (tasksRemaining <= processorsNumber) {
                consideredProcessorsNumber = tasksRemaining;
                averageTasksNumber = 1;
                generate = false;
            }

            for (int i = 0; i < consideredProcessorsNumber; i++) {
                int nextTasksAmount = getTasksNumber(averageTasksNumber, generate);
                tasksAmountDistribution.set(i, tasksAmountDistribution.get(i) + nextTasksAmount);
            }

            int tasksAmountGenerated = getTaskGeneratedAmount(tasksAmountDistribution);
            tasksRemaining = (tasksNumber - tasksAmountGenerated);
            averageTasksNumber = tasksRemaining / consideredProcessorsNumber;
        }
        // end: amount of tasks distribution

        // begin: creates tasks' bounds from processor to processor
        List<List<Long>> tasksBounds = new ArrayList<List<Long>>();
        for (int i = 0; i < tasksAmountDistribution.size(); i++) {
            int tasksAmount = tasksAmountDistribution.get(i);
            List<Long> boundsSet = new ArrayList<Long>();

            if (tasksAmount > 0) { // if there are tasks that have to be placed on this processor
                for (int j = 0; j < (tasksAmount-1); j++) {
                    boolean newBoundGenerated = false; // generating different bounds
                    while (!newBoundGenerated) {
                        long nextBound = getNextBound(time);
                        if (!boundsSet.contains(nextBound)) {
                            boundsSet.add(nextBound);
                            newBoundGenerated = true;
                        }
                    }
                    // TODO: validation is needed!!! Can cause an infinite loop.
                }
                Collections.sort(boundsSet);
                tasksBounds.add(boundsSet);
            }
        }
        // end: creates tasks' bounds from processor to processor

        // begin: creating tasks and placing them on the correct processor
        int nextTaskIdentifier = 0;
        for (int i = 0; i < processorsNumber; i++) {
            List<Long> boundsSet = tasksBounds.get(i);
            long lastBound = 0;
            for (int j = 0; j < boundsSet.size(); j++) {
                Long nextBounds = boundsSet.get(j);
                Task nextTask = new Task(nextTaskIdentifier++, nextBounds - lastBound);
                idealSchedule.addTask(nextTask);
                idealSchedule.addTaskToTheProcessor(i, nextTask, lastBound);
                lastBound = nextBounds;
            }
            idealSchedule.addTaskToTheProcessor(i, new Task(nextTaskIdentifier++, time - lastBound), lastBound);
        }
        // end: creating tasks and placing them on the correct processor
    }

    private long getNextBound(long maxValue) {
        Random generator = new Random(System.currentTimeMillis());
        double doublePart = generator.nextDouble();
        return (long) (doublePart*maxValue);
    }

    /**
     * Returns the number of tasks which have to be generated.
     * @param initialTasksNumber an initial number of tasks.
     * @param generate indicates whether to generate the amount of tasks or just
     * to return the initialTasksNumber
     */
    private int getTasksNumber(int initialTasksNumber, boolean generate) {
        int tasksNumber = initialTasksNumber;

        if (generate) {
            Random generator = new Random(System.currentTimeMillis());
            tasksNumber = 1 + generator.nextInt(initialTasksNumber);
        }

        return tasksNumber;
    }

    /**
     * Initializes the list with 0s.
     */
    private void initTasksAmountDistributionList(List<Integer> taskAmountDistributionList) {
        for (int i = 0; i < processorsNumber; i++) {
            taskAmountDistributionList.add(0);
        }
    }

    /**
     * Returns the amount of tasks that was distributed.
     */
    private int getTaskGeneratedAmount(List<Integer> taskAmountDistributionList) {
        int tasksAmountGenerated = 0;
        for (int i = 0; i < taskAmountDistributionList.size(); i++) {
            tasksAmountGenerated += taskAmountDistributionList.get(i);
        }

        return tasksAmountGenerated;
    }

    /**
     * TasksDAO's method.
     */
    public List<Task> getTasks() {
        buildSchedule(defaultTime, defaultTasksNumber);
        return (List<Task>) CloneService.clone(idealSchedule.getTasks());
    }

    @Override
    public void buildGraph(byte relationsPercentage) {
        assert relationsPercentage >= 0 && relationsPercentage <= 100 : "The relationsPercentage value must be between 0 and 100";

        final int GROUPING_INDEX = 2; // forms the group's processing time. This time forms by multiplying the time of
                                      // longest task in the system by this index.
        // so that a tasks distribution begins from the zeroth processor, it has the longest termination time.
        final long processorsTermTime = idealSchedule.getProcessorTime(0);
        final long longestTaskTime = idealSchedule.getTheLongestTask().getProcessingTime();
        final int groupsNumber = getGroupsNumber(processorsTermTime, longestTaskTime);
        final long groupTime = longestTaskTime * GROUPING_INDEX;

        // TODO: check out data structures. Seems like List is not a good variant here
        // begin: building a groups of groups
        List<Group> groups = new ArrayList<Group>();
        for (int groupIndex = 0; groupIndex < groupsNumber; groupIndex++) {
            long leftBound = groupIndex * groupTime;
            long rightBound = leftBound + groupTime;
            Group group = new Group(groupIndex, leftBound, rightBound);
            groups.add(group);
        }
        // end: building a groups of groups

        // begin: filling each group with tasks
        final Iterator<Processor> processorsIterator = idealSchedule.getProcessorsIterator();
        while (processorsIterator.hasNext()) {
            final Processor processor = processorsIterator.next();
            Iterator<Task> processorTasksIterator = idealSchedule.getProcessorTasksIterator(processor.getIdentifier());
            while (processorTasksIterator.hasNext()) {
                Task task = processorTasksIterator.next();
                long taskEndTime = task.getActBeginningTime() + task.getProcessingTime();

                int groupNumber = getGroupNumber(groups, taskEndTime);
                groups.get(groupNumber).addParticipant(task);
            }
        }
        // end: filling each group with tasks

        // sorting tasks in each group in a descendant order of beginning times
        for (int i = 0; i < groupsNumber; i++) {
            Group group = groups.get(i);
            Collections.sort(group.participants, new TasksComparator());
        }

        // building relations
        // count all possible relations number
        final int allPossibleRelations = getAllPossibleRelationsNumber(groups);

        // determines how many relations should be there in the system
        final int requiredRelations = (int) Math.round(relationsPercentage * (allPossibleRelations / 100.0));

        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < requiredRelations; i++) {
            // randomly choose a group for a parent task
            int parentGroupNumber = random.nextInt(groupsNumber);
            Group parentGroup = groups.get(parentGroupNumber);
            // randomly choose the parent task in the group
            List<Task> parentGroupParticipants = parentGroup.participants;
            Task parentTask = parentGroupParticipants.get(random.nextInt(parentGroupParticipants.size()));

            // randomly choose a group for the descendant task
            Group descendantGroup = groups.get(random.nextInt(groupsNumber));
            List<Task> descendantGroupParticipants = descendantGroup.participants;
            if (descendantGroup.identifier < (groupsNumber-1) ) {
                // last group
                // no chance to build a relation
                --i;
            } else if (descendantGroup.identifier == parentGroup.identifier) {
                // descendant task's beginning time must be less than parent task's end time
                long parentTaskEndTime = parentTask.getActBeginningTime() + parentTask.getProcessingTime();
                for (int k = 0; k < descendantGroupParticipants.size(); k++) {
                    Task task = descendantGroupParticipants.get(k);
                    if (!task.getIdentifier().equals(parentTask.getIdentifier())) {
                        if (task.getActBeginningTime() >= parentTaskEndTime) {
                            Relation relation = new Relation(parentTask, task);
                            relations.add(relation);
                        }
                    }
                }
                //TODO: there could be no such the descendant task
            } else {
                // TODO: it can take very long to find this number of relations!!!!
                // go through the tasks of this group to find a task which actualBeginningTime is greater than or
                // equal to the endTime of the parent task.
            }

            // randomly choose the descendant task in the group

            Task descendantTask = descendantGroupParticipants.get(random.nextInt(descendantGroupParticipants.size()));
        }

    }

    /**
     * Returns all possible relations between generated tasks.
     */
    private int getAllPossibleRelationsNumber(List<Group> groups) {
        int groupsNumber = groups.size();

        int[] groupsParticipantsNumber = new int[groupsNumber];
        for (int i = 0; i < groupsNumber; i++) {
            groupsParticipantsNumber[i] = groups.get(i).participants.size();
        }

        int possibleRelationsNumber = 0;
        // possible relations are:
        // 1. tasks which actBeginningTime are greater than the current task's endTime
        // 2. all the tasks from groups after the current one
        for (int groupIndex = 0; groupIndex < groupsNumber; groupIndex++) {
            // for every group. Take a task from the current group.
            possibleRelationsNumber = getNextGroupsParticipants(groupIndex, groupsParticipantsNumber);
            List<Task> participants = groups.get(groupIndex).participants;
            for (int groupParticipantIndex = 0; groupParticipantIndex < participants.size(); groupParticipantIndex++) {
                Task currentTask = participants.get(groupParticipantIndex);
                long taskEndTime = currentTask.getActBeginningTime() + currentTask.getProcessingTime();
                for (int innerCounter = 0; innerCounter < participants.size(); innerCounter++) {
                    Task task = participants.get(innerCounter);
                    if (task.getActBeginningTime() >= taskEndTime) {
                        possibleRelationsNumber++;
                    }
                }
            }
        }

        return possibleRelationsNumber;
    }

    /**
     * Returns the number of participants in the groups which follows the current group.
     */
    private int getNextGroupsParticipants(int groupIndex, int[] groupsParticipantsNumber) {
        assert groupIndex < (groupsParticipantsNumber.length-1) : "The groupIndex must not exceed (groupsParticipantsNumber.length-1) ";
        int participantsNumber = 0;
        for (int i = (groupIndex+1); i < groupsParticipantsNumber.length; i++) {
            participantsNumber += groupsParticipantsNumber[i];
        }

        return participantsNumber;
    }

    /**
     * Determines a group number the task with the specified endTime should be placed in.
     */
    private int getGroupNumber(List<Group> groups, long endTime) {
        final int groupsNumber = groups.size();
        int resultGroup = 0;
        for (int i = 0; i < groupsNumber; i++) {
            Group currentGroup = groups.get(i);
            if (currentGroup.rightBound < endTime) {
                continue;
            } else {
                resultGroup = i;
                break;
            }
        }

        return resultGroup;
    }


    /**
     * Simulates the Math.ceiling behaviour. The difference is it returns an integer value;
     */
    private int getGroupsNumber(long processorsTermTime, long longestTaskTime) {
        long remaining = processorsTermTime % longestTaskTime;
        int mainPart = (int) (processorsTermTime / longestTaskTime);
        if (remaining > 0) {
            return (mainPart + 1);
        } else {
            return mainPart;
        }
    }

    /**
     * GraphDAO's method.
     */
    public GraphRepresentation getGraph() {
        return null;
    }

    /**
     * Inner class.
     *
     * Represents the relation between two tasks.
     */
    private class Relation {
        private Task parent;
        private Task descendant;

        private Relation (Task parent, Task descendant) {
            this.parent = parent;
            this.descendant = descendant;
        }
    }

    /**
     * Inner class
     *
     */
    private class Group {
        private int identifier;
        private long leftBound;
        private long rightBound;
        private List<Task> participants;

        private Group(int identifier, long leftBound, long rightBound) {
            this.identifier = identifier;
            this.leftBound = leftBound;
            this.rightBound = rightBound;
            participants = new ArrayList<Task>();
        }

        private void addParticipant(Task task) {
            assert !participants.contains(task) : "The group can contain only unique participants";
            participants.add(task);
        }
    }
}
