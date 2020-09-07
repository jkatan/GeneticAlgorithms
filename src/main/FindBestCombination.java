package main;

import GeneticComponents.Implementations.Mutators.*;
import GeneticComponents.Implementations.ParentSelectors.*;
import GeneticComponents.Implementations.ConditionCheckers.TimeConditionChecker;
import GeneticComponents.Implementations.Reproductors.*;
import GeneticComponents.Implementations.PopulationGenerators.FillAllGenerator;
import GeneticComponents.Implementations.PopulationGenerators.FillParentGenerator;
import GeneticComponents.Implementations.Reproductors.CrossoverManager;
import GeneticComponents.Implementations.Reproductors.OnePointCrossover;
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

    private static MutatorManager mutatorManager;
    private static CrossoverManager crossoverManager;

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

    // Amount of parents that will be selected to cross and generate new children each generation
    private static int parentsAmountToSelect;

    private static String classSelection;

    private static int currentGeneration;

    // Lo que esta en este main son solo cosas que estuve testeando y que luego se reemplazara
    // por la lectura del archivo de configuracion, asi que puedes ignorar esta parte.
    public static void main(String[] args) throws IOException {

        initialPopulationSize = 20;
        double geneMutationProbability = 0.8;

        armasFile = new File("C:/Users/Johnathan/Desktop/Development/facu/sia/fulldata/armas.tsv");
        botasFile = new File("C:/Users/Johnathan/Desktop/Development/facu/sia/fulldata/botas.tsv");
        cascosFile = new File("C:/Users/Johnathan/Desktop/Development/facu/sia/fulldata/cascos.tsv");
        guantesFile = new File("C:/Users/Johnathan/Desktop/Development/facu/sia/fulldata/guantes.tsv");
        pecherasFile = new File("C:/Users/Johnathan/Desktop/Development/facu/sia/fulldata/pecheras.tsv");

        classSelection = "warrior";

        conditionChecker = new TimeConditionChecker(60.0);

        parentSelectorOne = new BoltzmannSelector(12.3, 5.78, 1.5);
        currentGeneration = 0;
        parentSelectorTwo = new EliteSelector();
        parentSelectorPercentage = 0.5;
        parentsAmountToSelect = 6;

        crossoverManager = new CrossoverManager(new AnnularCrossover());
        mutatorManager = new MutatorManager(new CompleteMutator(geneMutationProbability), armasFile, botasFile, cascosFile,
                guantesFile, pecherasFile);
        try {
            List<GameClass> firstGeneration = generateInitialPopulation();
            System.out.println("First generation individuals: ");
            for (GameClass individual : firstGeneration) {
                System.out.println(individual);
            }
            List<GameClass> parents = parentSelectorOne.selectParentsFromPopulation(firstGeneration, 7);
            System.out.println("Selected parents: ");
            for (GameClass parent : parents) {
                System.out.println(parent);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        //findBestCombination();
    }

    public static int getCurrentGeneration() {
        return currentGeneration;
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
            mutatorManager.mutate(children);
            population = selectNextGeneration(population, children, population.size());
            currentGeneration++;
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

    private static List<GameClass> selectNextGeneration(List<GameClass> population, List<GameClass> children, int amount) {

        List<GameClass> newPopulationOne = populationGeneratorOne.generateFromCurrentPopulation(population, children,
                (int) ((double)amount * populationGeneratorPercentage));

        population.removeAll(newPopulationOne);
        children.removeAll(newPopulationOne);

        List<GameClass> newPopulationTwo = populationGeneratorTwo.generateFromCurrentPopulation(population, children,
                (int) ((double)amount * (1.0 - populationGeneratorPercentage)));

        List<GameClass> populationToReturn = new ArrayList<>();
        populationToReturn.addAll(newPopulationOne);
        populationToReturn.addAll(newPopulationTwo);

        return populationToReturn;
    }

    private static List<GameClass> generateInitialPopulation() throws IOException {
        List<GameClass> initialRandomPopulation = new ArrayList<>();
        for (int i = 0; i < initialPopulationSize; i++) {
            System.out.println("Generating individual " + i);
            double randomHeight = Utils.generateRandomHeight();
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

    private static List<Equipment> generateRandomEquipment() throws IOException {
        List<Equipment> randomEquipment = new ArrayList<>();
        randomEquipment.add(Utils.selectRandomEquipmentFromFile(armasFile));
        randomEquipment.add(Utils.selectRandomEquipmentFromFile(botasFile));
        randomEquipment.add(Utils.selectRandomEquipmentFromFile(cascosFile));
        randomEquipment.add(Utils.selectRandomEquipmentFromFile(guantesFile));
        randomEquipment.add(Utils.selectRandomEquipmentFromFile(pecherasFile));
        return randomEquipment;
    }
}
