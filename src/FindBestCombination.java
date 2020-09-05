import GeneticComponents.Implementations.ParentSelectors.EliteSelector;
import GeneticComponents.Implementations.ConditionCheckers.TimeConditionChecker;
import GeneticComponents.Implementations.Reproductors.*;
import GeneticComponents.Interfaces.*;
import Utils.Utils;
import classes.*;
import equipment.Equipment;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class FindBestCombination {

    private static ConditionChecker conditionChecker;

    private static Mutator mutator; // TODO: implement mutators

    // TODO: implement reproductors
    private static CrossoverManager crossoverManager;

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

        initialPopulationSize = 2;

        // This is just for testing, TODO: configuration file
        armasFile = new File("C:/Users/Johnathan/Desktop/Development/facu/sia/fulldata/armas.tsv");
        botasFile = new File("C:/Users/Johnathan/Desktop/Development/facu/sia/fulldata/botas.tsv");
        cascosFile = new File("C:/Users/Johnathan/Desktop/Development/facu/sia/fulldata/cascos.tsv");
        guantesFile = new File("C:/Users/Johnathan/Desktop/Development/facu/sia/fulldata/guantes.tsv");
        pecherasFile = new File("C:/Users/Johnathan/Desktop/Development/facu/sia/fulldata/pecheras.tsv");
        classSelection = "warrior";

        conditionChecker = new TimeConditionChecker(60.0);

        parentSelectorOne = new EliteSelector();
        parentSelectorTwo = new EliteSelector();
        parentSelectorPercentage = 0.5;
        parentsAmountToSelect = 6;

        crossoverManager = new CrossoverManager(new AnnularCrossover());
        try {
            List<GameClass> parents = generateInitialPopulation();
            List<GameClass> children = crossoverManager.cross(parents);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        /*try {
            findBestCombination();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }*/
    }

    private static void findBestCombination() throws IOException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        List<GameClass> parents;
        List<GameClass> children;
        List<GameClass> population = generateInitialPopulation();
        conditionChecker.initialize();
        while (!conditionChecker.isConditionMet()) {
            parents = selectParents(population);
            children = crossoverManager.cross(parents);
            mutator.mutate(children);
            population = selectNextGeneration(parents, children);
        }
    }

    private static List<GameClass> selectParents(List<GameClass> population) {

        int parentsMethodOne = (int)((double)parentsAmountToSelect * parentSelectorPercentage);
        List<GameClass> selectorOneParents = parentSelectorOne.selectParentsFromPopulation(population, parentsMethodOne);

        //To avoid selecting duplicate parents using the second selection method
        population.removeAll(selectorOneParents);

        int parentsMethodTwo = (int)((double)parentsAmountToSelect * (1.0 - parentSelectorPercentage));
        List<GameClass> selectorTwoParents = parentSelectorTwo.selectParentsFromPopulation(population, parentsMethodTwo);

        List <GameClass> parents = new ArrayList<>();
        parents.addAll(selectorOneParents);
        parents.addAll(selectorTwoParents);

        return parents;
    }

    private static List<GameClass> selectNextGeneration(List<GameClass> parents, List<GameClass> children) {

        List<GameClass> newPopulationOne = populationGeneratorOne.generateFromCurrentPopulation(parents, children,
                (int) ((double)populationAmountToSelect * populationGeneratorPercentage));

        parents.removeAll(newPopulationOne);
        children.removeAll(newPopulationOne);

        List<GameClass> newPopulationTwo = populationGeneratorTwo.generateFromCurrentPopulation(parents, children,
                (int) ((double)populationAmountToSelect * (1.0 - populationGeneratorPercentage)));

        List<GameClass> population = new ArrayList<>();
        population.addAll(newPopulationOne);
        population.addAll(newPopulationTwo);

        return population;
    }

    private static List<GameClass> generateInitialPopulation() throws IOException {
        List<GameClass> initialRandomPopulation = new ArrayList<>();
        for (int i = 0; i < initialPopulationSize; i++) {
            System.out.println("Generating individual " + i);
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
        return Utils.getRandomInRange(1.3, 2.0);
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
        int randomItemId = (int) Math.ceil(Utils.getRandomInRange(0, 999999));
        while ((line = br.readLine()) != null) {
            itemAttributes = line.split("\t");
            if (Integer.parseInt(itemAttributes[0]) == randomItemId) {
                int id = Integer.parseInt(itemAttributes[0]);
                double strength = Double.parseDouble(itemAttributes[1]);
                double agility = Double.parseDouble(itemAttributes[2]);
                double expertise = Double.parseDouble(itemAttributes[3]);
                double resistance = Double.parseDouble(itemAttributes[4]);
                double health = Double.parseDouble(itemAttributes[5]);
                br.close();
                return new Equipment(strength, agility, expertise, resistance, health, id);
            }
        }
        br.close();
        return null;
    }
}
