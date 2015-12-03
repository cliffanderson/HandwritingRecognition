package net.cliffanderson.classifytest.obj.node;

import net.cliffanderson.classifytest.obj.NeuralNetwork;

/**
 * Created by Cliff on 11/30/2015.
 */
public class HiddenNode extends Node
{
    private double[] oldWeights;

    public HiddenNode(NeuralNetwork network, int inputs, int outputs)
    {
        super(network, inputs, outputs);
        this.oldWeights = new double[inputs];
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
       // System.out.println("Hidden node error: " + this.error);
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

        //compare old weights and new weights to see if they changed
        for(int i = 0; i < this.oldWeights.length; i++)
        {
            if(this.oldWeights[i] != this.inputWeights[i])
            {
               // System.out.println("hidden node Weights changed");
                break;
            }
        }

        //copy new weights over old weights
        for(int i = 0; i < this.oldWeights.length; i++)
        {
            this.oldWeights[i] = this.inputWeights[i];
        }

        //do this for hidden nodes below
        for(Node n : this.inputs)
        {
            n.adjustWeights();
        }
    }
}
