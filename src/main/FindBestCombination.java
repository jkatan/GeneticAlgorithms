package main;

import GeneticComponents.Implementations.Mutators.*;
import GeneticComponents.Implementations.ParentSelectors.*;
import GeneticComponents.Implementations.ConditionCheckers.TimeConditionChecker;
import GeneticComponents.Implementations.Reproductors.*;
import GeneticComponents.Implementations.PopulationGenerators.FillAllGenerator;
import GeneticComponents.Implementations.PopulationGenerators.FillParentGenerator;
import GeneticComponents.Implementations.ConditionCheckers.*;
import GeneticComponents.Implementations.ParentSelectors.EliteSelector;
import GeneticComponents.Implementations.Reproductors.CrossoverManager;
import GeneticComponents.Implementations.Reproductors.OnePointCrossover;
import GeneticComponents.Interfaces.*;
import Utils.Utils;
import classes.*;
import equipment.Equipment;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FindBestCombination {

    private static ConditionChecker conditionChecker;

    private static MutatorManager mutatorManager;
    private static CrossoverManager crossoverManager;

    private static ParentSelector parentSelectorOne;
    private static ParentSelector parentSelectorTwo;
    private static double parentSelectorPercentage;

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

    private static String graphOption;
    private static List<Double> yGraphData;
    private static List<Integer> xGraphData;

    public static void main(String[] args) throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String geneticAlgConfigPath = "src/config.properties";
        final Properties properties = new Properties();
        properties.load(new FileInputStream(geneticAlgConfigPath));
        yGraphData = new ArrayList<>();
        xGraphData = new ArrayList<>();

        initialPopulationSize = Integer.parseInt(properties.getProperty("initialPopSize"));

        armasFile = new File(properties.getProperty("equipmentWeapons"));
        botasFile = new File(properties.getProperty("equipmentBoots"));
        cascosFile = new File(properties.getProperty("equipmentHelms"));
        guantesFile = new File(properties.getProperty("equipmentGloves"));
        pecherasFile = new File(properties.getProperty("equipmentCuirass"));

        classSelection = properties.getProperty("class");

        currentGeneration = Integer.parseInt(properties.getProperty("startingGeneration"));

        String value = properties.getProperty("value");
        String cutoff = properties.getProperty("cutoff");
        switch (cutoff) {
            case "time":
                conditionChecker = new TimeConditionChecker(Double.parseDouble(value));
                break;
            case "gens":
                conditionChecker = new GenerationConditionChecker(Integer.parseInt(value));
                break;
            case "asol":
                conditionChecker = new AcceptableSolutionConditionChecker(Double.parseDouble(value));
                break;
            case "structure":
                conditionChecker = new StructureConditionChecker(
                        Integer.parseInt(value), Double.parseDouble(properties.getProperty("significant")));
                break;
            case "content":
                conditionChecker = new ContentConditionChecker(Integer.parseInt(value));
                break;
            default:
                throw new IllegalArgumentException("No condition checker provided, iterations would never stop!");
        }

        String selectorOne = properties.getProperty("selectorOne");
        String selectorTwo = properties.getProperty("selectorTwo");
        parentSelectorPercentage = Double.parseDouble(properties.getProperty("parentSelectorPercentage"));
        parentsAmountToSelect = Integer.parseInt(properties.getProperty("parentsAmountToSelect"));
        switch (selectorOne) {
            case "elite":
                parentSelectorOne = new EliteSelector();
                break;
            case "roulette":
                parentSelectorOne = new RouletteWheelSelector();
                break;
            case "universal":
                parentSelectorOne = new UniversalSelector();
                break;
            case "boltzmann":
                parentSelectorOne = new BoltzmannSelector(
                        Double.parseDouble(properties.getProperty("t_0")),
                        Double.parseDouble(properties.getProperty("t_c")),
                        Double.parseDouble(properties.getProperty("k"))
                );
                break;
            case "dett":
                parentSelectorOne = new DeterministicTournamentSelector(Integer.parseInt(properties.getProperty("nTourPart")));
                break;
            case "probt":
                parentSelectorOne = new ProbabilisticTournamentSelector();
                break;
            case "ranking":
                parentSelectorOne = new RankingSelector();
            default:
                throw new IllegalArgumentException("No parent selector chosen for selector 1!");
        }
        switch (selectorTwo) {
            case "elite":
                parentSelectorTwo = new EliteSelector();
                break;
            case "roulette":
                parentSelectorTwo = new RouletteWheelSelector();
                break;
            case "universal":
                parentSelectorTwo = new UniversalSelector();
                break;
            case "boltzmann":
                parentSelectorTwo = new BoltzmannSelector(
                        Double.parseDouble(properties.getProperty("t_0")),
                        Double.parseDouble(properties.getProperty("t_c")),
                        Double.parseDouble(properties.getProperty("k"))
                );
                break;
            case "dett":
                parentSelectorTwo = new DeterministicTournamentSelector(Integer.parseInt(properties.getProperty("nTourPart")));
                break;
            case "probt":
                parentSelectorTwo = new ProbabilisticTournamentSelector();
                break;
            case "ranking":
                parentSelectorTwo = new RankingSelector();
                break;
            default:
                throw new IllegalArgumentException("No parent selector chosen for selector 2!");
        }

        String algImplOne = properties.getProperty("algImplOne");
        String algImplTwo = properties.getProperty("algImplTwo");
        populationGeneratorPercentage = Double.parseDouble(properties.getProperty("algImplPercentage"));
        switch (algImplOne) {
            case "fa":
                populationGeneratorOne = new FillAllGenerator();
                break;
            case "fp":
                populationGeneratorOne = new FillParentGenerator();
                break;
            default:
                throw new IllegalArgumentException("No algorithm implementation 1!");
        }

        switch (algImplTwo) {
            case "fa":
                populationGeneratorTwo = new FillAllGenerator();
                break;
            case "fp":
                populationGeneratorTwo = new FillParentGenerator();
                break;
            default:
                throw new IllegalArgumentException("No algorithm implementation 2!");
        }

        switch (properties.getProperty("mutation")) {
            case "gen":
                mutatorManager = new MutatorManager(
                        new GeneMutator(Double.parseDouble(properties.getProperty("geneMutProb"))),
                        armasFile, botasFile, cascosFile, guantesFile, pecherasFile);
                break;
            case "limmgen":
                mutatorManager = new MutatorManager(
                        new LimitedMultiGeneMutator(Double.parseDouble(properties.getProperty("geneMutProb"))),
                        armasFile, botasFile, cascosFile, guantesFile, pecherasFile);
                break;
            case "unimgen":
                mutatorManager = new MutatorManager(
                        new UniformMultiGeneMutator(Double.parseDouble(properties.getProperty("geneMutProb"))),
                        armasFile, botasFile, cascosFile, guantesFile, pecherasFile);
                break;
            case "complete":
                mutatorManager = new MutatorManager(
                        new CompleteMutator(Double.parseDouble(properties.getProperty("geneMutProb"))),
                        armasFile, botasFile, cascosFile, guantesFile, pecherasFile);
                break;
            default:
                throw new IllegalArgumentException("No mutator selected!");
        }

        switch (properties.getProperty("cross")) {
            case "pcross":
                crossoverManager = new CrossoverManager(new OnePointCrossover());
                break;
            case "2pcross":
                crossoverManager = new CrossoverManager(new TwoPointCrossover());
                break;
            case "across":
                crossoverManager = new CrossoverManager(new AnnularCrossover());
                break;
            case "unicross":
                crossoverManager = new CrossoverManager(new UniformCrossover());
                break;
            default:
                throw new IllegalArgumentException("No crossover chosen!");
        }

        graphOption = properties.getProperty("graph");
        if (!graphOption.equals("max") && !graphOption.equals("avg")) {
            throw new IllegalArgumentException("No graph option chosen!");
        }

        findBestCombination();
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

        xGraphData.add(currentGeneration);
        yGraphData.add(getBestFitness(population));

        double[][] initialGraphData = getGraphData();

        // Create Chart
        String chartTitle = "";
        String charYTitle = "";
        if (graphOption.equals("max")) {
            chartTitle = "Max fitness per generation";
            charYTitle = "max fitness";
        } else if (graphOption.equals("avg")) {
            chartTitle = "Average fitness per generation";
            charYTitle = "average fitness";
        }

        final XYChart chart = QuickChart.getChart(chartTitle, "generation", charYTitle, "graph", initialGraphData[0], initialGraphData[1]);

        // Show it
        final SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(chart);
        sw.displayChart();

        while (!conditionChecker.isConditionMet()) {
            parents = selectParents(population);
            children = crossoverManager.cross(parents);
            mutatorManager.mutate(children);
            population = selectNextGeneration(population, children, initialPopulationSize);

            currentGeneration++;
            xGraphData.add(currentGeneration);

            if (conditionChecker.requiresFitnessToUpdate()) {
                conditionChecker.update(population);
            } else {
                conditionChecker.update(null);
            }

            if (graphOption.equals("max")) {
                yGraphData.add(getBestFitness(population));
            } else if (graphOption.equals("avg")) {
                yGraphData.add(getAverageFitness(population));
            }

            final double[][] data = getGraphData();

            chart.updateXYSeries("graph", data[0], data[1], null);
            sw.repaintChart();
        }

        double maxPerformance = 0.0;
        GameClass bestIndividual = null;
        for (GameClass individual : population) {
            if (individual.getBestPerformance() > maxPerformance) {
                maxPerformance = individual.getBestPerformance();
                bestIndividual = individual;
            }
        }

        System.out.println("Best individual: ");
        System.out.println(bestIndividual);
    }

    private static double[][] getGraphData() {
        double[] xData = new double[xGraphData.size()];
        double[] yData = new double[yGraphData.size()];

        for (int i=0; i<xData.length; i++) {
            xData[i] = xGraphData.get(i);
            yData[i] = yGraphData.get(i);
        }

        return new double[][] {xData, yData};
    }

    private static double getBestFitness(List<GameClass> population) {
        double bestFitness = 0;
        for (GameClass i: population) {
            double individualFitness = i.getBestPerformance();
            if (individualFitness > bestFitness) {
                bestFitness = individualFitness;
            }
        }
        return bestFitness;
    }

    private static double getAverageFitness(List<GameClass> population) {
        double averageFitness = 0.0;
        for (GameClass individual : population) {
            averageFitness += individual.getBestPerformance();
        }
        return averageFitness/population.size();
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
