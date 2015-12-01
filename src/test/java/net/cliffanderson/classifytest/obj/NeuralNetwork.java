package net.cliffanderson.classifytest.obj;

import net.cliffanderson.classifytest.obj.node.HiddenNode;
import net.cliffanderson.classifytest.obj.node.InputNode;
import net.cliffanderson.classifytest.obj.node.ResultNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andersonc12 on 11/27/2015.
 */
public class NeuralNetwork
{
    public enum Mode
    {
        TRAIN, TEST;
    }

    public Mode mode;
    private List<ResultNode> resultNodes;
    private List<List<HiddenNode>> hiddenLayerList;
    private List<InputNode> inputNodes;

    //number of inputs in the training set
    private int parameters;

    //the current input vectors
    private int trainingVectorCount;
    private int testingVectorCount;

    //training set
    private DataSet trainingSet;

    //testing set
    private DataSet testingSet;

    //learning rate
    private double learningRate;

    /*
    trainingSet: set for training the network
    testingSet: set for testing the data
    learningRate: the learning rate for the network, between 0 and 1
    hiddenlayers: an array representing the hidden layers, where each value is the number of nodes in that layer
    classes: the number of classification classes
     */
    public NeuralNetwork(DataSet trainingSet, DataSet testingSet, double learningRate, int[] hiddenlayers, int classes) {
        //check number of parameters
        if(trainingSet.getParameters() != testingSet.getParameters())
        {
            System.err.println("Number of training set parameters and testing set parameters must be the same!");
            return;
        }
        //check learning rate
        if(learningRate <= 0.0 || learningRate >= 1.0)
        {
            System.err.println("Learning rate must be between 0 and 1");
            return;
        }

        //check hidden layers
        if(hiddenlayers.length == 0)
        {
            System.err.println("Must have at least one hidden layer");
            return;
        }

        for(int i : hiddenlayers)
        {
            if(i <= 0)
            {
                System.err.println("Invalid number of nodes in hidden layer: " + i);
                return;
            }
        }

        //check classes
        if(classes <= 0)
        {
            System.err.println("Invalid number of classes: " + classes);
            return;
        }

        this.trainingSet = trainingSet;
        this.testingSet = testingSet;
        this.parameters = trainingSet.getParameters();

        this.learningRate = learningRate;


        createNetwork(hiddenlayers, classes);
    }

    private void createNetwork(int[] hiddenlayers, int classes)
    {
        resultNodes = new ArrayList<ResultNode>();
        hiddenLayerList = new ArrayList<List<HiddenNode>>();
        inputNodes = new ArrayList<InputNode>();

        //make result nodes
        for(int i = 0; i < classes; i++)
        {
            resultNodes.add(new ResultNode(this));
        }

        //generate hidden layers
        for(int layer = 0; layer < hiddenlayers.length; layer++)
        {
            for(int nodes = 0; nodes < hiddenlayers[layer]; nodes++)
            {
                if(hiddenLayerList.size() == layer)
                {
                    hiddenLayerList.add(new ArrayList<HiddenNode>());
                }

                hiddenLayerList.get(layer).add(new HiddenNode(this));
            }
        }

        //make input nodes
        for(int i = 0; i < this.parameters; i++)
        {
            inputNodes.add(new InputNode(this));
        }


        //connect result nodes to first layer of hidden nodes
        for(int result = 0; result < resultNodes.size(); result++)
        {
            for(int hidden = 0; hidden < hiddenlayers[0]; hidden++)
            {
                resultNodes.get(result).addInput(hiddenLayerList.get(0).get(hidden));
            }
        }

        //connect the hidden layers together
        for(int layer = 0; layer < hiddenlayers.length - 1; layer++)
        {
            //connect each layer to the next one
            for(int hiddenParent = 0; hiddenParent < hiddenlayers[layer]; hiddenParent++)
            {
                for(int hiddenChild = 0; hiddenChild < hiddenlayers[layer + 1]; hiddenChild++)
                {
                    hiddenLayerList.get(layer).get(hiddenParent).addInput(hiddenLayerList.get(layer + 1).get(hiddenChild));
                }
            }
        }

        //connect last hidden layer to input nodes
        for(int hidden = 0; hidden < hiddenlayers[hiddenlayers.length - 1]; hidden++)
        {
            for(int input = 0; input < parameters; input++)
            {
                hiddenLayerList.get(hiddenLayerList.size() - 1).get(hidden).addInput(inputNodes.get(input));
            }
        }

        System.out.println("Network created");
    }

    //epoch many times at once
    public void train(int epochs)
    {
        for(int i = 0; i < epochs; i++)
        {
            this.epoch();
        }
    }

    //run the entire training set through the network, using back propagation after every input vector
    private void epoch()
    {
       for(int input = 0; input < this.trainingSet.getSize(); input++)
       {
           //update all node outputs
           this.mode = Mode.TRAIN;
           for(int result = 0; result < this.resultNodes.size(); result++)
           {
               this.resultNodes.get(result).calculateOutput();
           }



           //learn

           //used for determining the result node errors
           double[] actual = new double[this.resultNodes.size()];
           actual[this.trainingSet.getInputVector(input).getTarget()] = 1.0;

           //calculate the result node errors
           for(int result = 0; result < this.resultNodes.size(); result++)
           {
               resultNodes.get(result).setError(resultNodes.get(result).getOutput() * (1 - resultNodes.get(result).getOutput()) * (actual[result] - resultNodes.get(result).getOutput()));
           }


           //calculate all hidden node errors

           //calculate result node's children's errors
           for(int result = 0; result < this.resultNodes.size(); result++)
           {
               resultNodes.get(result).calculateChildrensErrors();
           }

           //calculate all hidden node's children's errors
           for(int layer = 0; layer < hiddenLayerList.size() - 1; layer++)
           {
               for(int hidden = 0; hidden < hiddenLayerList.get(layer).size(); hidden++)
               {
                   hiddenLayerList.get(layer).get(hidden).calculateChildrensErrors();
               }
           }





           //calculate and assign new weights
           //calculate and assign new weights for result nodes
           for(int result = 0; result < this.resultNodes.size(); result++)
           {
               resultNodes.get(result).calculateWeights();
           }


           //calculate and assign new weights for hidden nodes
           for(int layer = 0; layer < hiddenLayerList.size(); layer++)
           {
               for(int hidden = 0; hidden < hiddenLayerList.get(layer).size(); hidden++)
               {
                   hiddenLayerList.get(layer).get(0).calculateWeights();
               }
           }
       }
    }

    public double getErrorRate()
    {
        double correctCount = 0;
        double incorrectCount = 0;

        this.mode = Mode.TEST;
        for(int input = 0; input < this.testingSet.getSize(); input++)
        {
            InputVector currentInput = this.testingSet.getInputVector(input);

            //run all values through
            for(int result = 0; result < this.resultNodes.size(); result++)
            {
                this.resultNodes.get(result).calculateOutput();
            }

            //correct class
            int actual = currentInput.getTarget();

            //all guessed classes
            List<Integer> guesses = new ArrayList<Integer>();

            //add all guesses to list
            for(int result = 0; result < this.resultNodes.size(); result++)
            {
                if(this.resultNodes.get(result).getOutput() >= .5)
                {
                    guesses.add(result);
                }
            }

            //if only 1 guess and its correct we have a success, otherwise a failure
            if(guesses.size() == 1 && guesses.get(0) == actual)
            {
                correctCount++;
            }
            else
            {
                incorrectCount++;
            }
        }

        double errorRate = 1.0 - (correctCount / (correctCount + incorrectCount));

        return errorRate;
    }

    //n - the node calling this function (must be an input node)
    //this function is for input nodes getting data from the data set
    public double getInputValue(InputNode n)
    {
        int index = this.inputNodes.indexOf(n);
       // System.out.println("[" + trainingVectorCount + "][" + index + "]");
        if(index == -1)
        {
            System.err.println("Only input nodes can call getInputValue()");
            return 0;
        }

        if(this.mode == Mode.TRAIN) {
            if (index == parameters - 1) {
                double result = this.trainingSet.getInputVector(this.trainingVectorCount).getData()[index];

                //roll over to next vector
                this.trainingVectorCount++;

                if (this.trainingVectorCount == this.trainingSet.getSize()) {
                    this.trainingVectorCount = 0;
                }

                return result;
            }

            return this.trainingSet.getInputVector(this.trainingVectorCount).getData()[index];
        }
        else
        {
            if (index == parameters - 1) {
                double result = this.testingSet.getInputVector(this.testingVectorCount).getData()[index];

                //roll over to next vector
                this.testingVectorCount++;

                if (this.testingVectorCount == this.testingSet.getSize()) {
                    this.testingVectorCount = 0;
                }

                return result;
            }

            return this.testingSet.getInputVector(this.testingVectorCount).getData()[index];
        }
    }

    public double getLearningRate()
    {
        return this.learningRate;
    }


}
