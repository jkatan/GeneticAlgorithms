package GeneticComponents.Implementations.ParentSelectors;

import GeneticComponents.Interfaces.ParentSelector;
import Utils.Utils;
import classes.GameClass;

import java.util.ArrayList;
import java.util.List;

public class DeterministicTournamentSelector implements ParentSelector {

    // Should be passed in the configuration file
    private final int amountOfTournamentParticipants;

    public DeterministicTournamentSelector(int amountOfTournamentParticipants) {
        this.amountOfTournamentParticipants = amountOfTournamentParticipants;
    }

    @Override
    public List<GameClass> selectParentsFromPopulation(List<GameClass> population, int parentsAmount) {
        List<GameClass> parentsToSelect = new ArrayList<>();
        for (int i=0; i<parentsAmount; i++) {
            List<GameClass> populationCopy = new ArrayList<>(population);
            List<GameClass> randomlyPickedIndividuals = new ArrayList<>();
            for (int j = 0; j< amountOfTournamentParticipants; j++) {
                int randomIndex = (int) Math.ceil(Utils.getRandomInRange(0.0, populationCopy.size() - 1));
                randomlyPickedIndividuals.add(populationCopy.remove(randomIndex));
            }

            GameClass bestIndividual = ParentSelectorManager.getBestIndividual(randomlyPickedIndividuals);
            parentsToSelect.add(bestIndividual);
        }

        return parentsToSelect;
    }
}
