package GeneticComponents.Interfaces;

public interface ConditionChecker {

    /* Initialize the condition checker status */
    void initialize();

    /* Check if algorithm should end */
    Boolean isConditionMet();
}
