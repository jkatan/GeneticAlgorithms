import GeneticComponents.Implementations.ParentSelectors.EliteSelector;
import GeneticComponents.Implementations.ConditionCheckers.TimeConditionChecker;
import GeneticComponents.Interfaces.*;
import classes.*;
import equipment.Equipment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FindBestCombination {

    private static ConditionChecker conditionChecker;

    private static Mutator mutator; // TODO: implement mutators

    private static Reproductor reproductor; // TODO: implement reproductors

    // TODO: implement the rest of the selectors
    private static ParentSelector parentSelectorOne;
    private static ParentSelector parentSelectorTwo;
    private static double parentSelectorPercentage;

    // TODO: implement population generators
    private static PopulationGenerator populationGeneratorOne;
    private static PopulationGenerator populationGeneratorTwo;
    private static double populationGeneratorPercentage;

    private static File armasFile;
    private static File botasFile;
    private static File cascosFile;
    private static File guantesFile;
    private static File pecherasFile;

    private static int initialPopulationSize;

    // At the end of each generation, we select new individuals from the set of {Children + Parents}.
    // This variable determines how many individuals will be selected from that set each generation
    private static int populationAmountToSelect;

    // Amount of parents that will be selected to cross and generate new children each generation
    private static int parentsAmountToSelect;

    private static String classSelection;

    public static void main(String[] args) throws IOException {
        initialPopulationSize = 200;

        armasFile = new File(args[1]);
        botasFile = new File(args[2]);
        cascosFile = new File(args[3]);
        guantesFile = new File(args[4]);
        pecherasFile = new File(args[5]);
        classSelection = args[6];

        conditionChecker = new TimeConditionChecker(60.0);

        parentSelectorOne = new EliteSelector();
        parentSelectorTwo = new EliteSelector();
        parentSelectorPercentage = 0.5;
        parentsAmountToSelect = 100;

        findBestCombination();
    }

    private static void findBestCombination() throws IOException {
        List<GameClass> parents;
        List<GameClass> children;
        List<GameClass> population = generateInitialPopulation();
        conditionChecker.initialize();
        while (!conditionChecker.isConditionMet()) {
            parents = selectParents(population);
            children = reproductor.cross(parents);
            mutator.mutate(children);
            population = selectNextGeneration(parents, children);
        }
    }

    private static List<GameClass> selectParents(List<GameClass> population) {

        List<GameClass> selectorOneParents = parentSelectorOne.selectParentsFromPopulation(population,
                (int) (parentsAmountToSelect * parentSelectorPercentage));

        //To avoid selecting duplicate parents using the second selection method
        population.removeAll(selectorOneParents);

        List<GameClass> selectorTwoParents = parentSelectorTwo.selectParentsFromPopulation(population,
                (int) (parentsAmountToSelect * (1 - populationGeneratorPercentage)));

        List <GameClass> parents = new ArrayList<>();
        parents.addAll(selectorOneParents);
        parents.addAll(selectorTwoParents);

        return parents;
    }

    private static List<GameClass> selectNextGeneration(List<GameClass> parents, List<GameClass> children) {

        List<GameClass> newPopulationOne = populationGeneratorOne.generateFromCurrentPopulation(parents, children,
                (int) (populationAmountToSelect * populationGeneratorPercentage));

        parents.removeAll(newPopulationOne);
        children.removeAll(newPopulationOne);

        List<GameClass> newPopulationTwo = populationGeneratorTwo.generateFromCurrentPopulation(parents, children,
                (int) (populationAmountToSelect * (1 - populationGeneratorPercentage)));

        List<GameClass> population = new ArrayList<>();
        population.addAll(newPopulationOne);
        population.addAll(newPopulationTwo);

        return population;
    }

    private static List<GameClass> generateInitialPopulation() throws IOException {
        List<GameClass> initialRandomPopulation = new ArrayList<>();
        for (int i = 0; i < initialPopulationSize; i++) {
            double randomHeight = generateRandomHeight();
            List<Equipment> randomEquipment = generateRandomEquipment();
            GameClass randomIndividual = createCharacterClassSelection(randomHeight, randomEquipment);
            initialRandomPopulation.add(randomIndividual);
        }

        return initialRandomPopulation;
    }

    private static GameClass createCharacterClassSelection(double height, List<Equipment> equipment) {
        GameClass characterToCreate = null;
        switch (classSelection) {
            case "archer":
                characterToCreate = new Archer(height, equipment);
                break;
            case "defender":
                characterToCreate = new Defender(height, equipment);
                break;
            case "infiltrator":
                characterToCreate = new Infiltrator(height, equipment);
                break;
            case "warrior":
                characterToCreate = new Warrior(height, equipment);
                break;
        }
        return characterToCreate;
    }

    private static double generateRandomHeight() {
        return getRandomNumber(1.3, 2.0);
    }

    private static List<Equipment> generateRandomEquipment() throws IOException {
        List<Equipment> randomEquipment = new ArrayList<>();
        randomEquipment.add(selectRandomEquipmentFromFile(armasFile));
        randomEquipment.add(selectRandomEquipmentFromFile(botasFile));
        randomEquipment.add(selectRandomEquipmentFromFile(cascosFile));
        randomEquipment.add(selectRandomEquipmentFromFile(guantesFile));
        randomEquipment.add(selectRandomEquipmentFromFile(pecherasFile));
        return randomEquipment;
    }

    private static Equipment selectRandomEquipmentFromFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine(); // Consumes first line that contains the header
        String line;
        String[] itemAttributes;
        int randomItemId = (int) getRandomNumber(0, 999999);
        while ((line = br.readLine()) != null) {
            itemAttributes = line.split("\t");
            if (Integer.parseInt(itemAttributes[0]) == randomItemId) {
                double strength = Double.parseDouble(itemAttributes[1]);
                double agility = Double.parseDouble(itemAttributes[2]);
                double expertise = Double.parseDouble(itemAttributes[3]);
                double resistance = Double.parseDouble(itemAttributes[4]);
                double health = Double.parseDouble(itemAttributes[5]);
                return new Equipment(strength, agility, expertise, resistance, health);
            }
        }
        return null;
    }

    private static double getRandomNumber(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }
}
