package net.cliffanderson.wekatest;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.clusterers.Cobweb;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

import java.io.File;

/**
 * Created by Cliff on 10/7/2015.
 */
public class WekaTest
{
    public static void classifyTest(File arffFile) throws Exception
    {
        //Incremental classification


        //load data
        ArffLoader loader = new ArffLoader();
        loader.setFile(arffFile);

        Instances structure = loader.getStructure();
        structure.setClassIndex(structure.numAttributes() - 1);

        //train NaiveBayes
        NaiveBayesUpdateable nb = new NaiveBayesUpdateable();
        nb.buildClassifier(structure);
        Instance current;
        while((current = loader.getNextInstance(structure)) != null)
        {
            nb.updateClassifier(current);
        }

        //output generated model
        System.out.println(nb);
    }

    public static void clusterTest(File arffFile) throws Exception
    {
        //load data
        ArffLoader loader = new ArffLoader();
        loader.setFile(arffFile);
        Instances structure = loader.getStructure();

        //train cobweb
        Cobweb cw = new Cobweb();
        cw.buildClusterer(structure);
        Instance current;
        while( (current = loader.getNextInstance(structure)) != null)
        {
            cw.updateClusterer(current);
        }
        cw.updateFinished();

        System.out.println(cw);
    }

    public static void neuralNetworkTest(File arffFile) throws Exception
    {
        //load data
        ArffLoader loader = new ArffLoader();
        loader.setFile(arffFile);
        Instances train = loader.getDataSet();
        train.setClassIndex(train.numAttributes() - 1);

        //Instance of neural network
        MultilayerPerceptron mlp = new MultilayerPerceptron();

        //Parameters
        mlp.setLearningRate(0.1);
        mlp.setMomentum(0.2);
        mlp.setTrainingTime(2000);
        mlp.setHiddenLayers("3");
        mlp.buildClassifier(train);


        //evaluation of training data
        Evaluation eval = new Evaluation(train);
        eval.evaluateModel(mlp, train);
        System.out.println("Error rate: " + eval.errorRate()); //training mean root squared error
        System.out.println("Summary: " + eval.toSummaryString()); //summary of training

    }
}
