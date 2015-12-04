package net.cliffanderson.classifytest.obj.node;

import net.cliffanderson.classifytest.obj.NeuralNetwork;

/**
 * Created by Cliff on 11/30/2015.
 */
public class HiddenNode extends Node
{
    public HiddenNode(NeuralNetwork network, int inputs, int outputs)
    {
        super(network, inputs, outputs);
    }

    @Override
    public void addToErrorSum(double errorPortion)
    {
        this.errorSum += errorPortion;
    }

    @Override
    public void computeError()
    {
        this.error = this.getOutput() * (1 - this.getOutput()) * this.errorSum;

        //call other hidden layers
        for(Node n : this.inputs)
        {
            n.computeError();
        }
    }

    @Override
    public void adjustWeights()
    {
        for(int i = 0; i < this.inputWeights.length; i++)
        {
            this.inputWeights[i] = this.inputWeights[i] + this.network.getLearningRate() * this.error * this.inputs[i].getOutput();
        }

        //do this for hidden nodes below
        for(Node n : this.inputs)
        {
            n.adjustWeights();
        }
    }
}
