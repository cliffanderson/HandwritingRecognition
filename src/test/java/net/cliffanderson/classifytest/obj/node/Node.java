package net.cliffanderson.classifytest.obj.node;

import net.cliffanderson.classifytest.obj.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andersonc12 on 11/28/2015.
 */
public abstract class Node {
    //nodes inputing to this node aka 'children'
    public List<Node> inputs;

    //nodes this node outputs to aka 'parents'
    public List<Node> outputs;

    //the weights of the connections from input nodes
    public List<Double> weights;

    //output of this node
    protected double output = 0.0;

    //this nodes errors
    protected double error = 0.0;

    //network this is a part of
    protected NeuralNetwork network;

    public Node() {
    }

    public Node(NeuralNetwork network) {
        this.network = network;

        this.inputs = new ArrayList<Node>();
        this.outputs = new ArrayList<Node>();

        this.weights = new ArrayList<Double>();
    }

    public void addInput(Node n) {
        inputs.add(n);
        weights.add(generateWeight());

        n.addOutput(this);
    }

    public void addOutput(Node n)
    {
        this.outputs.add(n);
    }

    public double getOutput() {
        return this.output;
    }

    public double calculateOutput() {
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

    public double getError()
    {
        return this.error;
    }

    public void setError(double error)
    {

        this.error = error;


    }

    public void calculateChildrensErrors()
    {
        for(int child = 0; child < this.inputs.size(); child++) {

            Node childNode = this.inputs.get(child);

            double sum = 0.0;

            for(int i = 0; i < childNode.outputs.size(); i++)
            {
                sum += childNode.outputs.get(i).getError() * this.weights.get(child);
            }


            childNode.setError(childNode.getOutput() * (1.0-childNode.getOutput()) * sum);
        }
    }

    public void calculateWeights()
    {
        for(int i = 0; i < this.weights.size() || i < this.inputs.size(); i++)
        {
            double newWeight = this.weights.get(i) + (this.network.getLearningRate() * this.error * ((double) this.inputs.get(i).getOutput()));


            this.weights.set(i, newWeight);
        }
    }

    protected double sigmoid(double v) {
        return 1 / (1 + Math.pow(Math.E, -v));
    }

    protected double generateWeight() {
        return Math.random() * 0.000005;
    }

}
