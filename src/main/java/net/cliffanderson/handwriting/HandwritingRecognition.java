package net.cliffanderson.handwriting;

import net.cliffanderson.classifytest.obj.DataSet;
import net.cliffanderson.classifytest.obj.NeuralNetwork;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by andersonc12 on 9/26/2015.
 */
public class HandwritingRecognition {
    public static int count = 0;
    public static final String USER_HOME = System.getProperty("user.home");

    public static URL trainingImagesURL;
    public static URL trainingImageLabelsURL;
    public static URL testingImagesURL;
    public static URL testingImageLabelsURL;

    public static File TRAIN_IMAGES;
    public static File TRAIN_LABELS;
    public static File TEST_IMAGES;
    public static File TEST_LABELS;

    public static void main(String[] args) {
        timeProgram();
        setup();


        HandwritingRecognition hr = new HandwritingRecognition();

        //training sets
        /*
        hr.decodeImages(new File(USER_HOME + File.separator + "dataset-images"),
                new File(USER_HOME + File.separator + "dataset-image-labels"),
                new File(USER_HOME + File.separator + "handwriting-images"),
                false,
                false);

        //testing sets
        hr.decodeImages(new File(USER_HOME + File.separator + "dataset-test-images"),
                new File(USER_HOME + File.separator + "dataset-test-image-labels"),
                new File(USER_HOME + File.separator + "handwriting-test-images"),
                false,
                false);


        try {
            createDataFile(TEST_IMAGES, TEST_LABELS, new File(USER_HOME + File.separator + "desktop" + File.separator + "testingData.txt"));
            CustomClassifyTest.neuralNetwork(new File(USER_HOME + File.separator + "desktop" + File.separator + "trainingData.txt"),
                    new File(USER_HOME + File.separator + "desktop" + File.separator + "testingData.txt"),
                  200, 0.1);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

       // createDataFile(TRAIN_IMAGES, TRAIN_LABELS, new File(USER_HOME + File.separator + "desktop" + File.separator + "trainingData.txt"));


        File trainingData = new File(USER_HOME + File.separator + "desktop" + File.separator + "trainingData.txt");
        File testingData =  new File(USER_HOME + File.separator + "desktop" + File.separator + "testingData.txt");

        DataSet training = new DataSet(trainingData, 49);
        DataSet testing = new DataSet(testingData, 49);

        NeuralNetwork network = new NeuralNetwork(training, testing, 0.2, new int[]{9}, 10);
        while(true)
        {
            network.train(1);
            System.out.println("Error rate: " + network.getErrorRate() * 100 + "%");
        }


    }

    public static void createDataFile(File encodedImages, File encodedLabels, File output) {
        //setup input stream
        InputStream imageFileIn;
        DataInputStream imageIn;

        InputStream labelFileIn;
        DataInputStream labelIn;

        BufferedWriter out;

        try {
            imageFileIn = new FileInputStream(encodedImages);
            imageIn = new DataInputStream(imageFileIn);

            labelFileIn = new FileInputStream(encodedLabels);
            labelIn = new DataInputStream(labelFileIn);

            out = new BufferedWriter(new FileWriter(output));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }


        //each image is created then saved in the appropriate folder (0-9, whatever the corresponding label is
        try {
            //read in magic number
            int imageMagic = imageIn.readInt();
            int labelMagic = labelIn.readInt();
            System.out.println("Magic number for encoded image file: " + imageMagic);
            System.out.println("Magic number for encoded label file: " + labelMagic);

            //read dataset info
            int numberOfImages = imageIn.readInt();
            int numberOfLabels = labelIn.readInt();

            System.out.println("Number of images: " + numberOfImages);
            System.out.println("Number of labels: " + numberOfLabels);

            int imageWidth = imageIn.readInt();
            int imageHeight = imageIn.readInt();

            //create all the images
            for (int img = 0; img < numberOfImages || img < numberOfLabels; img++)
            {
                //reduce the image by a factor of 4 to dramatically reduce the size of the input to the network
                int[][] reducedImage = new int[7][7];

                //loop through data
                for (int y = 0; y < imageHeight; y++) {
                    for (int x = 0; x < imageWidth; x++) {
                        byte color = imageIn.readByte(); //color of this pixel

                        if (color < 0) {
                            color += 128; //we want unsigned bytes
                        }

                        //add color value to reduced image array
                        reducedImage[x / 4][y / 4] += color;
                    }
                }

                //set every color value in the reduced image to 1/4 of its current value (the average of the color values)
                for (int y = 0; y < imageHeight / 4; y++) {
                    for (int x = 0; x < imageWidth / 4; x++) {
                        out.write(String.valueOf(reducedImage[x][y] / 16) + ",");
                    }
                }

                out.write(String.valueOf(labelIn.readByte()));
                out.write('\n');


                if ((img + 1) % 1000 == 0) {
                    System.out.println((img + 1) + " images parsed");
                }
            }

            if (out != null) {
                out.flush();
                out.close();
            }

        } catch (IOException e) {
            System.err.println("Error reading the encoded image file!");
            e.printStackTrace();
            return;
        }
    }

    public void createImages(File encodedImages, File encodedImageLabels, File outputDir) {

        //quick check
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        //delete sub folders if they exist, then recreate them, but
        //only if we chose to create images
        for (int digit = 0; digit <= 9; digit++) {
            File folder = new File(outputDir, String.valueOf(digit));
            if (folder.exists()) {
                //if it exists, delete it
                try {
                    FileUtils.deleteDirectory(folder);
                } catch (IOException e) {
                    System.err.println("Error deleting sub folder pre image parsing: " + folder.getAbsolutePath());
                    e.printStackTrace();
                    return;
                }
            }

            //create it
            folder.mkdirs();
        }


        //setup input stream
        InputStream imageFileIn;
        DataInputStream imageIn;

        InputStream labelFileIn;
        DataInputStream labelIn;

        try {
            imageFileIn = new FileInputStream(encodedImages);
            imageIn = new DataInputStream(imageFileIn);

            labelFileIn = new FileInputStream(encodedImageLabels);
            labelIn = new DataInputStream(labelFileIn);

            /*
            if (createArffFile) {
                //arff file setup
                out.write("@RELATION handwriting\n");

                //every pixel gets it's own attribute
                for (int i = 0; i < 7 * 7; i++) {
                    out.write("@ATTRIBUTE pixel" + i + " NUMERIC\n");
                }
                out.write("@ATTRIBUTE digit NUMERIC\n");


                out.write("@data\n");
            }*/
        } catch (FileNotFoundException e) {
            System.err.println("The encoded image file or the encoded label file does not exist!");
            e.printStackTrace();
            return;
        } catch (IOException e) {
            System.err.println("The arff file writer could not be created");
            e.printStackTrace();
            return;
        }

        //read in all the bytes

        //each image is created then saved in the appropriate folder (0-9, whatever the corresponding label is
        try {
            //read in magic number
            int imageMagic = imageIn.readInt();
            int labelMagic = labelIn.readInt();
            System.out.println("Magic number for encoded image file: " + imageMagic);
            System.out.println("Magic number for encoded label file: " + labelMagic);

            //read dataset info
            int numberOfImages = imageIn.readInt();
            int numberOfLabels = labelIn.readInt();

            System.out.println("Number of images: " + numberOfImages);
            System.out.println("Number of labels: " + numberOfLabels);

            int imageWidth = imageIn.readInt();
            int imageHeight = imageIn.readInt();


            //create all the images
            for (int img = 0; img < numberOfImages || img < numberOfLabels; img++) {

                    //create an image
                    BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
                    for (int y = 0; y < imageHeight; y++) {
                        for (int x = 0; x < imageWidth; x++) {
                            byte color = imageIn.readByte(); //color of this pixel

                            if (color < 0) {
                                color += 128; //we want unsigned bytes
                            }

                            image.setRGB(x, y, new Color(color, color, color).getRGB()); //all rgb the same value for grayscale
                        }
                    }

                    String label = String.valueOf(labelIn.readByte());
                    ImageIO.write(image, "PNG", new File(outputDir + File.separator + label + File.separator + String.valueOf(Math.random()) + ".png"));

                /*
                if (createArffFile) {
                    //reduce the image by a factor of 4 to dramatically reduce the size of the input to the network
                    int[][] reducedImage = new int[7][7];

                    //loop through data
                    for (int y = 0; y < imageHeight; y++) {
                        for (int x = 0; x < imageWidth; x++) {
                            byte color = imageIn.readByte(); //color of this pixel

                            if (color < 0) {
                                color += 128; //we want unsigned bytes
                            }

                            //add color value to reduced image array
                            reducedImage[x % 4][y % 4] += color;
                        }
                    }

                    //set every color value in the reduced image to 1/4 of its current value (the average of the color values)
                    for (int y = 0; y < imageHeight / 4; y++) {
                        for (int x = 0; x < imageWidth / 4; x++) {
                            out.write(String.valueOf(reducedImage[x][y] / 4) + ",");
                        }
                    }
                    out.write(String.valueOf(labelIn.readByte()));

                    out.write('\n');

                }*/

                if ((img + 1) % 1000 == 0) {
                    System.out.println((img + 1) + " images parsed");
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the encoded image file!");
            e.printStackTrace();
            return;
        }
    }

    /*
    Download a file from the net and save it
     */
    public static void downloadFile(URL url, File file) {
        try {
            FileUtils.copyURLToFile(url, file);
        } catch (IOException e) {
            System.err.println("There was an error downloading: " + url.toString());
            e.printStackTrace();
            return;
        }
    }

    /*
    Setup constants
     */
    public static void setup() {
        //set urls
        try {
            trainingImagesURL = new URL("https://www.dropbox.com/s/4zorba0tnjz7n73/train-images.idx3-ubyte?dl=1");
            trainingImageLabelsURL = new URL("https://www.dropbox.com/s/8heouwxnrhqdks0/train-labels.idx1-ubyte?dl=1");

            testingImagesURL = new URL("https://www.dropbox.com/s/oe2bxp7md3xnkt6/t10k-images.idx3-ubyte?dl=1");
            testingImageLabelsURL = new URL("https://www.dropbox.com/s/0pn38n24sspyevi/t10k-labels.idx1-ubyte?dl=1");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //set file paths
        TRAIN_IMAGES = new File(USER_HOME + File.separator + "dataset-images");
        TRAIN_LABELS = new File(USER_HOME + File.separator + "dataset-image-labels");
        TEST_IMAGES = new File(USER_HOME + File.separator + "dataset-test-images");
        TEST_LABELS = new File(USER_HOME + File.separator + "dataset-test-image-labels");

        //verify all data files exist and if not, download them
        if (!TRAIN_IMAGES.exists()) {
            downloadFile(trainingImagesURL, TRAIN_IMAGES);
        }

        if (!TRAIN_LABELS.exists()) {
            downloadFile(trainingImageLabelsURL, TRAIN_LABELS);
        }

        if(!TEST_IMAGES.exists())
        {
            downloadFile(testingImagesURL, TEST_IMAGES);
        }

        if(!TEST_LABELS.exists())
        {
            downloadFile(testingImagesURL, TEST_LABELS);
        }
    }

    /*
    I wonder if this works
     */
    public static void timeProgram() {
        final long startTime = System.currentTimeMillis();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println("\n\nTotal running time: " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
            }
        });
    }
}
