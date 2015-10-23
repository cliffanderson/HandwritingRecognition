package net.cliffanderson.handwriting;

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
public class HandwritingRecognition
{
    public static final String USER_HOME = System.getProperty("user.home");

    public static URL imageDataSet;
    public static URL imageDataSetLabels;

    public static void main(String[] args)
    {
        timeProgram();
        setup();


        /*
        HandwritingRecognition hr = new HandwritingRecognition();

        hr.decodeImages(new File(USER_HOME + File.separator + "dataset-images"),
                new File(USER_HOME + File.separator + "dataset-image-labels"),
                new File(USER_HOME + File.separator + "handwriting-images"));
        */
    }

    public void decodeImages(File encodedImages, File encodedImageLabels, File outputDir, boolean createImages, boolean createArffFile)
    {
        if(!createImages && !createArffFile) return;

        //quick check
        if(!outputDir.exists())
        {
            outputDir.mkdirs();
        }

        if(!encodedImages.exists())
        {
            downloadFile(imageDataSet, encodedImages);
        }

        if(!encodedImageLabels.exists())
        {
            downloadFile(imageDataSetLabels, encodedImageLabels);
        }

        //delete sub folders if they exist, then recreate them
        for(int digit = 0; digit <= 9; digit++)
        {
            File folder = new File(outputDir, String.valueOf(digit));
            if(folder.exists())
            {
                //if it exists, delete it
                try
                {
                    FileUtils.deleteDirectory(folder);
                }
                catch (IOException e)
                {
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

        File arffFile = new File(outputDir + File.separator + "handwriting.arff");
        BufferedWriter out;

        try
        {
            imageFileIn = new FileInputStream(encodedImages);
            imageIn = new DataInputStream(imageFileIn);

            labelFileIn = new FileInputStream(encodedImageLabels);
            labelIn = new DataInputStream(labelFileIn);

            out = new BufferedWriter(new FileWriter(arffFile));

            if(createArffFile)
            {
                //arff file setup
                out.write("@RELATION handwriting");
                out.write("@ATTRIBUTE digit NUMERIC");
                out.write("@ATTRIBUTE pixels STRING");
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("The encoded image file or the encoded label file does not exist!");
            e.printStackTrace();
            return;
        }
        catch (IOException e)
        {
            System.err.println("The arff file writer could not be created");
            e.printStackTrace();
            return;
        }





        //read in all the bytes

        //each image is created then saved in the appropriate folder (0-9, whatever the corresponding label is
        try
        {
            //read in magic number
            int imageMagic = imageIn.readInt();
            int labelMagic = labelIn.readInt();
            System.out.println("Magic number for encoded image file: " + imageMagic);
            System.out.println("Magic number for encoded label file: " + labelMagic);

            //read dataset info
            int numberOfImages = imageIn.readInt();
            int numberOfLabels = labelIn.readInt();

            int imageWidth = imageIn.readInt();
            int imageHeight = imageIn.readInt();


            //create all the images
            for(int img = 0; img < numberOfImages || img < numberOfLabels; img++)
            {
                if(createImages) {
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

                    if ((img + 1) % 1000 == 0) {
                        System.out.println((img + 1) + " images complete");
                    }
                }

                if(createArffFile)
                {
                    //loop through data
                    for(int y = 0; y < imageHeight; y++)
                    {
                        for(int x = 0; x < imageWidth; x++) {
                            byte color = imageIn.readByte(); //color of this pixel

                            if (color < 0) {
                                color += 128; //we want unsigned bytes
                            }


                        }

                    }


                }
            }

            if(out != null) {
                out.flush();
                out.close();
            }

        }
        catch (IOException e)
        {
            System.err.println("Error reading the encoded image file!");
            e.printStackTrace();
            return;
        }
    }

    /*
    Download a file from the net and save it
     */
    public void downloadFile(URL url, File file)
    {
        try
        {
            FileUtils.copyURLToFile(url, file);
        }
        catch (IOException e)
        {
            System.err.println("There was an error downloading: " + url.toString());
            e.printStackTrace();
            return;
        }
    }

    /*
    Setup constants
     */
    public static void setup()
    {
        try
        {
            imageDataSet = new URL("https://www.dropbox.com/s/4zorba0tnjz7n73/train-images.idx3-ubyte?dl=1");
            imageDataSetLabels = new URL("https://www.dropbox.com/s/8heouwxnrhqdks0/train-labels.idx1-ubyte?dl=1");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    /*
    I wonder if this works
     */
    public static void timeProgram()
    {
        final long startTime = System.currentTimeMillis();

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                System.out.println("\n\nTotal running time: " + (System.currentTimeMillis() - startTime)/1000 + " seconds");
            }
        });
    }
}
