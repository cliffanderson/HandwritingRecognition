package net.cliffanderson.classifytest.obj.node;

import net.cliffanderson.classifytest.obj.NeuralNetwork;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by andersonc12 on 11/28/2015.
 */
public abstract class Node {
    private static Set<Node> allNodes = new HashSet<Node>();
    public static void resetAllNodes() {
        for(Node n : allNodes) {
            n.reset();
        }
    }
    //nodes inputing to this node aka 'children'
    public List<Node> inputs;

    //nodes this node outputs to aka 'parents'
    public final List<Node> outputs;

    //the weights of the connections from input nodes
    public final List<Double> weights;

    //output of this node
    protected Double output = 0.0;

    //error
    protected double error = 0.0;

    //network this is a part of
    protected final NeuralNetwork network;

    public Node(NeuralNetwork network) {
        this.network = network;

        this.inputs = new ArrayList<Node>();
        this.outputs = new ArrayList<Node>();

        this.weights = new ArrayList<Double>();
        allNodes.add(this);
    }

    public void addInput(Node n) {
        inputs.add(n);
        weights.add(generateWeight());

        n.addOutput(this);
    }

    public void reset() {
        this.output = null;
    }

    protected void addOutput(Node n)
    {
        this.outputs.add(n);
    }

    protected double getError()
    {
        return this.error;
    }

    public double getOutput() {
        calculateOutput();
        return this.output;
    }

    public double calculateOutput() {
        if (this.output != null) {
            return this.output;
        }
        //sigmoid of the sum of the inputs * their weights
        double sum = 0.0;

        for (int i = 0; i < inputs.size(); i++) {
            double inputOutput = inputs.get(i).calculateOutput();
            double theWeight = weights.get(i);
            sum += inputOutput * theWeight;
        }

        this.output = sigmoid(sum);
        return this.output;
    }

    public void updateWeights()
    {
        for(int i = 0; i < this.weights.size(); i++)
        {
            double newWeight = this.weights.get(i) + this.network.getLearningRate() * this.error * this.inputs.get(i).getOutput();
            this.weights.set(i, newWeight);
        }

        //update weights of inputs
        for(Node n : this.inputs)
        {
            n.updateWeights();
        }
    }

    public abstract void calculateError(List<Double> weights);

    protected double sigmoid(double v) {
        return 1 / (1 + Math.pow(Math.E, -v));
    }

    protected double generateWeight() {
        return Math.random() * 0.000005;
    }

}
