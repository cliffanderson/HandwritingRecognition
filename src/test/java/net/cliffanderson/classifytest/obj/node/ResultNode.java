package net.cliffanderson.classifytest.obj.node;

import net.cliffanderson.classifytest.obj.InputVector;
import net.cliffanderson.classifytest.obj.NeuralNetwork;

/**
 * Created by Cliff on 11/30/2015.
 */
public class ResultNode extends Node
{
    private double[] oldWeights;
    public ResultNode(NeuralNetwork network, int inputs, int outputs)
    {
        super(network, inputs, outputs);
        oldWeights = new double[inputs];
    }

    @Override
    public void addToErrorSum(double errorPortion)
    {
        //for result nodes the errorPortion is the expected value for this node
        this.error = this.getOutput() * (1 - this.getOutput()) * (errorPortion - this.getOutput());
       // System.out.println("result node error: " + this.error);

        //do this for all input nodes (hidden nodes) passing it the error of this node * the weight going to the hidden node
        for(int i = 0; i < this.inputs.length; i++)
        {
            this.inputs[i].addToErrorSum(this.error * this.inputWeights[i]);
        }
    }

    @Override
    public void computeError()
    {
        //error for result node has already been computed in the addToErrorSum method
        //so just call all hidden nodes
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
                //System.out.println("Result node Weights changed!  new: " + this.inputWeights[i]);
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
