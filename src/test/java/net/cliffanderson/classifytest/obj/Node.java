package net.cliffanderson.classifytest.obj;

import java.util.List;

/**
 * Created by andersonc12 on 11/28/2015.
 */
public class Node
{
    //nodes inputing to this node
    List<Node> inputs;

    //the weights of the connections from input nodes
    List<Double> weights;

    //output of this node
    double output = 0.0;

    //network this is a part of
    private NeuralNetwork network;

    public Node(NeuralNetwork network)
    {
        this.network = network;
    }

    public void addInput(Node n)
    {
        inputs.add(n);
        weights.add(generateWeight());
    }

    public double getOutput()
    {
        if(inputs.size() == 0)
        {
            //this in an input node
            return this.network.getInputValue(this);
        }
        else
        {
            //sigmoid of the sum of the inputs * their weights
            double sum = 0.0;

            for(int i = 0; i < inputs.size(); i++)
            {
                sum += inputs.get(i).getOutput() * weights.get(i);
            }

            this.output = sigmoid(sum);
            return this.output;
        }
    }

    private double sigmoid(double v)
    {
        return 1 / (1 + Math.pow(Math.E, -v));
    }

    private double generateWeight()
    {
        return Math.random() * 0.000005;
    }
}
