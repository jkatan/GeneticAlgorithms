package GeneticComponents.Interfaces;

public interface ConditionChecker {

    /* Initialize the condition checker status */
    void initialize();

    /* Update the condition checker status. */
    void update(Double newValue);

    /* Check if algorithm should end */
    boolean isConditionMet();
}
