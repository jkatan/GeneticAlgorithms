package GeneticComponents.Interfaces;

import classes.GameClass;

import java.util.List;

public interface ConditionChecker {

    /**
     * Initialize the condition checker status
     */
    void initialize();

    /**
     * Update the condition checker status.
     * @param population Most condition checkers need population's fitness to determine cutoff.
     */
    void update(List<GameClass> population);

    /**
     * Checks if condition checker requires fitness to update.
     * @return <tt>true</tt> if method update requires fitness
     */
    boolean requiresFitnessToUpdate();

    /**
     * Check if algorithm should end.
     * @return <tt>true</tt> if cutoff condition is met
     */
    boolean isConditionMet();
}
