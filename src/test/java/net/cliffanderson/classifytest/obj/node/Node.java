package net.cliffanderson.classifytest.obj.node;

import net.cliffanderson.classifytest.obj.NeuralNetwork;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by andersonc12 on 11/28/2015.
 */
public abstract class Node
{
    private static Set<Node> allNodes = new HashSet<Node>();

    //nodes inputing to this node aka 'children'
    protected final Node[] inputs;

    //the Weights of the connections from input nodes
    public final double[] inputWeights;


    //nodes this node outputs to aka 'parents'
    protected final Node[] outputs;

    //network this is a part of
    protected final NeuralNetwork network;

    protected boolean updatedWeights = false;
    protected boolean errorCalculated = false;
    protected boolean outputCalculated = false;


    protected double errorSum = 0.0;


    //output of this node
    protected Double output = 0.0;

    //error
    public double error = 0.0;

    private int inputCounter = 0;
    private int outputCounter = 0;


    public Node(NeuralNetwork network, int inputs, int outputs)
    {
        this.network = network;

        this.inputs = new Node[inputs];
        this.inputWeights = new double[inputs];

        this.outputs = new Node[outputs];

        allNodes.add(this);
    }

    public static void resetAllNodes()
    {
        for (Node n : allNodes)
        {
            n.reset();
        }
    }

    public void addInput(Node n)
    {
        inputs[inputCounter] = n;
        inputWeights[inputCounter++] = generateWeight();

        n.addOutput(this);
    }

    public void reset()
    {
        outputCalculated = false;
        errorCalculated = false;
        updatedWeights = false;
    }

    protected void addOutput(Node n)
    {
        this.outputs[outputCounter] = n;
        outputCounter++;
    }

    public double getOutput()
    {
        if (outputCalculated)
        {
            return this.output;
        }

        //sigmoid of the sum of the inputs * their inputWeights
        double sum = 0.0;

        for (int i = 0; i < inputs.length; i++)
        {
            double inputOutput = inputs[i].getOutput();
            double theWeight = inputWeights[i];
            sum += inputOutput * theWeight;
        }

        this.output = sigmoid(sum);
        outputCalculated = true;
        return this.output;
    }

    protected abstract void addToErrorSum(double errorPortion);
    protected abstract void computeError();
    protected abstract void adjustWeights();


    protected double sigmoid(double v)
    {
        return 1 / (1 + Math.pow(Math.E, -v));
    }

    protected double generateWeight()
    {
        return Math.random() * 0.000005;
    }

}
