package net.cliffanderson.classifytest.obj.node;

import net.cliffanderson.classifytest.obj.InputVector;
import net.cliffanderson.classifytest.obj.NeuralNetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cliff on 12/1/2015.
 */
public class ClassifyNode extends Node
{
    public ClassifyNode(NeuralNetwork network, int inputs, int outputs)
    {
        super(network, inputs, outputs);
    }

    public void train()
    {
        Node.resetAllNodes();

        //step 1: calculate outputs for all nodes in network (done recursively)
        for(Node n : this.inputs)
        {
            n.getOutput();
        }

        //step 2: compute error sums of all nodes
        for(int i = 0; i < this.inputs.length; i++)
        {
            this.inputs[i].addToErrorSum(this.network.getTarget() == i ? 1 : 0);
        }

        //step 3: compute errors for all nodes in the network
        for(Node n : this.inputs)
        {
            n.computeError();
        }

        //step 4: adjust the weights for all nodes in the network
        for(Node n : this.inputs)
        {
            n.adjustWeights();
        }
    }

    public boolean correct()
    {
        //calculate outputs for all nodes in network and tell if the guess was correct or not
        for(Node n : this.inputs)
        {
            n.getOutput();
        }

        //find guesses
        List<Integer> guesses = new ArrayList<Integer>();
        for(int i = 0; i < this.inputs.length; i++)
        {
            if(this.inputs[i].getOutput() > .5)
            {
                guesses.add(i);
            }
        }

        //tell if the guess was correct
        if(guesses.size() == 1 && guesses.get(0) == this.network.getTarget())
        {
            //yes, correct
            return true;
        }
        else
        {
            //otherwise, false
            return false;
        }
    }


    @Override
    public void addToErrorSum(double errorPortion)
    {
        //don't do this for classify node
    }

    @Override
    public void computeError()
    {
        //don't do this for classify node
    }

    @Override
    public void adjustWeights()
    {
        //don't do this for classify node
    }
}
