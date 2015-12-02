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
    //the weights of the connections from input nodes
    public final double[] weights;
    //nodes inputing to this node aka 'children'
    protected final Node[] inputs;
    //nodes this node outputs to aka 'parents'
    protected final Node[] outputs;
    //network this is a part of
    protected final NeuralNetwork network;
    protected boolean updatedWeights = false;
    //output of this node
    protected Double output = 0.0;
    protected boolean outputCalculated = false;
    //error
    protected double error = 0.0;
    protected boolean errorCalculated = false;
    private int inputCounter = 0;
    private int outputCounter = 0;

    public Node(NeuralNetwork network, int inputs, int outputs)
    {
        this.network = network;

        this.inputs = new Node[inputs];
        this.weights = new double[inputs];

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
        weights[inputCounter++] = generateWeight();

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
        this.outputs[outputCounter++] = n;
    }

    protected double getError()
    {
        return this.error;
    }

    public double getOutput()
    {
        if (outputCalculated)
        {
            return this.output;
        }

        //sigmoid of the sum of the inputs * their weights
        double sum = 0.0;

        for (int i = 0; i < inputs.length; i++)
        {
            double inputOutput = inputs[i].getOutput();
            double theWeight = weights[i];
            sum += inputOutput * theWeight;
        }

        this.output = sigmoid(sum);
        outputCalculated = true;
        return this.output;
    }

    public void updateWeights()
    {
        if (this.updatedWeights) return;

        for (int i = 0; i < this.weights.length; i++)
        {
            double newWeight = this.weights[i] + this.network.getLearningRate() * this.error * this.inputs[i].getOutput();
            this.weights[i] = newWeight;
        }

        this.updatedWeights = true;

        //update weights of inputs
        for (Node n : this.inputs)
        {
            n.updateWeights();
        }
    }

    public abstract void calculateError(double[] weights);

    protected double sigmoid(double v)
    {
        return 1 / (1 + Math.pow(Math.E, -v));
    }

    protected double generateWeight()
    {
        return Math.random() * 0.000005;
    }

}
