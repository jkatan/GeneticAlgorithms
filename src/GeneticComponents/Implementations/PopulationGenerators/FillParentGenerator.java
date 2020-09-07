package GeneticComponents.Implementations.PopulationGenerators;

import GeneticComponents.Interfaces.PopulationGenerator;
import classes.GameClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FillParentGenerator implements PopulationGenerator {

    @Override
    public List<GameClass> generateFromCurrentPopulation(List<GameClass> population, List<GameClass> children, int amountToGenerate) {
        List<GameClass> populationToReturn = new ArrayList<>();

        List<GameClass> childrenCopy = new ArrayList<>(children);
        Collections.shuffle(childrenCopy);

        if (children.size() > amountToGenerate) {
            PopulationGeneratorManager.addIndividualsToPopulation(childrenCopy, populationToReturn, amountToGenerate);
        } else {
            populationToReturn.addAll(children);
            int individualsLeftToAdd = amountToGenerate - children.size();
            List<GameClass> populationCopy = new ArrayList<>(population);
            Collections.shuffle(populationCopy);

            PopulationGeneratorManager.addIndividualsToPopulation(populationCopy, populationToReturn, individualsLeftToAdd);
        }

        return populationToReturn;
    }
}
