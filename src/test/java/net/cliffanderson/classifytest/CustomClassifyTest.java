package net.cliffanderson.classifytest;

import java.io.File;

/**
 * Created by andersonc12 on 11/6/2015.
 */
public class CustomClassifyTest
{
    public static void neuralNetwork(File trainData, File testData, int epochs, double learningRate)
    {

/*
        System.out.println("Training data file: " + trainData.getAbsolutePath());
        List<InputVector> data = loadData(trainData, 49);

        System.out.println("Testing data file: " + testData.getAbsolutePath());
        List<InputVector> testingData = loadData(testData, 49);


        double[] hiddenNodes = new double[9];
        double[][] hiddenNodeWeights = new double[49][9];

        double[] resultNodes = new double[10];
        double[][] resultNodeWeights = new double[9][10];

        //populate the hidden nodes with inputWeights
        populateWeights(hiddenNodeWeights);
        populateWeights(resultNodeWeights);

        //set values for hidden nodes
        System.out.println("Starting hidden node inputWeights: " + Arrays.toString(hiddenNodeWeights[43]));

        for(int training = 0; training < epochs; training++)
        {
            for(InputVector input : data)
            {
                //train hidden layer
                for(int hidden = 0; hidden < hiddenNodes.length; hidden++)
                {
                    double sum = 0;

                    for(int pixel = 0; pixel < input.getData().length; pixel++)
                    {
                        sum += input.getData()[pixel] * hiddenNodeWeights[pixel][hidden];
                    }

                    hiddenNodes[hidden] = sigmoid(sum);
                }

                //train result nodes
                for(int result = 0; result < resultNodes.length; result++)
                {
                    double sum = 0;

                    for(int hidden = 0; hidden < hiddenNodes.length; hidden++)
                    {
                        sum += hiddenNodes[hidden] * resultNodeWeights[hidden][result];
                    }

                    resultNodes[result] = sigmoid(sum);
                }


                //learn via back propigation
                double[] actual = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
                actual[Integer.parseInt(input.getTarget())] = 1.0;

                double[] resultErrors = new double[resultNodes.length];
                double[] hiddenErrors = new double[hiddenNodes.length];

                //calculate result node errors
                for(int result = 0; result < resultErrors.length; result++)
                {
                    resultErrors[result] = resultNodes[result] * (1 - resultNodes[result]) * (actual[result] - resultNodes[result]);
                }

                //calculate hidden node errors
                for(int hidden = 0; hidden < hiddenErrors.length; hidden++)
                {
                    double errorSum = 0.0;

                    for(int result = 0; result < resultErrors.length; result++)
                    {
                        errorSum += resultErrors[result] * resultNodeWeights[hidden][result];
                    }

                    hiddenErrors[hidden] = hiddenNodes[hidden] * (1.0 - hiddenNodes[hidden]) * errorSum;
                }


                //adjust inputWeights from hidden to result
                for(int hidden = 0; hidden < hiddenNodes.length; hidden++)
                {
                    for(int result = 0; result < resultNodes.length; result++)
                    {
                        resultNodeWeights[hidden][result] = resultNodeWeights[hidden][result] + learningRate * resultErrors[result] * hiddenNodes[hidden];
                    }
                }

                //adjust inputWeights from input to hidden
                for(int inputNode = 0; inputNode < input.getData().length; inputNode++)
                {
                   // System.out.println("\n\n" + inputNode + "\n\n");
                    for(int hidden = 0; hidden < hiddenNodes.length; hidden++)
                    {
                        hiddenNodeWeights[inputNode][hidden] = hiddenNodeWeights[inputNode][hidden] + learningRate * hiddenErrors[hidden] * input.getData()[inputNode];
                        double delta = learningRate * hiddenErrors[hidden] * input.getData()[inputNode];
                        //System.out.println(delta);
                    }


                }
                //System.out.println("Ending hidden node inputWeights:   " + Arrays.toString(hiddenNodeWeights[43]));


                //debug
                //return;
            }




            //at the end of each epoch evaluate result nodes vs input
            double correctCount = 0;
            double incorrectCount = 0;

            //train hidden layer
            for(InputVector input : testingData) {
                for (int hidden = 0; hidden < hiddenNodes.length; hidden++) {
                    double sum = 0;

                    for (int pixel = 0; pixel < input.getData().length; pixel++) {
                        sum += input.getData()[pixel] * hiddenNodeWeights[pixel][hidden];
                    }

                    hiddenNodes[hidden] = sigmoid(sum);
                }

                //train result nodes
                for (int result = 0; result < resultNodes.length; result++) {
                    double sum = 0;

                    for (int hidden = 0; hidden < hiddenNodes.length; hidden++) {
                        sum += hiddenNodes[hidden] * resultNodeWeights[hidden][result];
                    }

                    resultNodes[result] = sigmoid(sum);
                }


                //character that is in the picture
                int actual = Integer.parseInt(input.getTarget());

                //list of guesses the network made
                List<Integer> output = new ArrayList<Integer>();

                //loop through result nodes
                for(int result = 0; result < 10; result++)
                {
                    //if value is greater than .5, its a yes
                    if(resultNodes[result] >= .5)
                    {
                        output.add(result);
                    }
                }

                //if there is only 1 guess and its equal to the real value, its correct
                if(output.size() == 1 && output.get(0) == actual)
                {
                    correctCount++;
                }
                else
                {
                    incorrectCount++;
                }

                //compute success rate

            }

            double errorRate = 1 - (correctCount / (correctCount + incorrectCount));

            System.out.println("After epoch " + training + "   Error rate: " + errorRate * 100 + "%");

        }*/

    }
}