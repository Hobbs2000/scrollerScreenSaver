
import sun.util.BuddhistCalendar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import Graphics.*;

/**
 * Created by Calvin on 12/19/2015.
 */
public class ScrollerMain extends Canvas implements Runnable
{

    private boolean running = false;
    private Thread mainThread;
    private JFrame frame;

    public String name = "Scroller tester";
    private int width = 1920;
    private int height = 1080;

    private Random numGen = new Random();

    //Background images
    private Pic image1;
    private Pic image4;
    private Pic image5;
    private Pic image6;

    private Pic floatingIsland1;

    //Foreground images
    private Pic image2;
    private Pic image3;

    //All the images for the persons walking animation
    int personX = 150;
    int personY = 550;
    private Pic[] walkingPerson = {
            new Pic(personX, personY, "/Walking_1.png"),
            new Pic(personX, personY, "/Walking_2.png"),
            new Pic(personX, personY, "/Walking_3.png"),
            new Pic(personX, personY, "/Walking_4.png"),
            new Pic(personX, personY, "/Walking_5.png"),
            new Pic(personX, personY, "/Walking_6.png"),
            new Pic(personX, personY, "/Walking_7.png"),
            new Pic(personX, personY, "/Walking_8.png"),
            new Pic(personX, personY, "/Walking_9.png")
    };


    /**
     *
     */
    public ScrollerMain()
    {
        //Dimension size = new Dimension(width, height);
        //setPreferredSize(size);
        frame = new JFrame(name);

        //Get all images setup
        //Images are in the order in which they will be drawn
        image1 = new Pic(0,0, "/MainPixelBackground.png");
        image4 = new Pic(image1.getWidth(),0, "/MainPixelBackground.png");

        image5 = new Pic(0,0, "/IslandBackgroundPart.png");
        image6 = new Pic(image5.getWidth()+800, 0, "/IslandBackgroundPart.png");

        floatingIsland1 = new Pic(2800,200, "/FloatingIsland.png");

        image2 = new Pic(0,0, "/PixelForeground_1.png");
        image3 = new Pic(image2.getWidth()-3,0, "/PixelForeground_1.png");

    }

    /**
     *  Automatically run, mostly sets up the JFrame for the program
     */
    public static void main(String[] args)
    {
        ScrollerMain scroll = new ScrollerMain();
        scroll.frame.setResizable(false);
        scroll.frame.setLocationRelativeTo(null);
        scroll.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        scroll.frame.setUndecorated(true);
        scroll.frame.add(scroll);
        scroll.frame.pack();
        scroll.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        scroll.frame.setVisible(true);

        scroll.requestFocusInWindow();
        scroll.start();
    }

    /**
     *  Kicks off the program by starting the mainThread
     */
    public synchronized void start()
    {
        running = true;

        mainThread = new Thread(this, "Screen");
        mainThread.start();
    }


    @Override
    public void run()
    {
        double initial_X = MouseInfo.getPointerInfo().getLocation().getX();
        double initial_Y = MouseInfo.getPointerInfo().getLocation().getY();
        while (running)
        {
            update();
            render();

            //Checks to see if the mouse has moved since the program has started, and will exit the program if so
            if (initial_X != MouseInfo.getPointerInfo().getLocation().getX() || initial_Y != MouseInfo.getPointerInfo().getLocation().getY())
            {
                stop();
            }

            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }

    /**
     *  Updates everything in the program before it is all rendered
     */
    int floatingIsland_num = 0;
    int num = 0;
    private int walkingIndex = 0;
    public void update()
    {

        //Move background
        image1.setX(image1.getX()-1);
        image4.setX(image4.getX()-1);

        if (image1.getX() < (image1.getWidth()*-1))
        {
            image1.setX((image1.getWidth())-5);
        }
        if (image4.getX() < (image4.getWidth()*-1))
        {
            image4.setX((image4.getWidth())-5);
        }

        image5.setX(image5.getX()-2);
        image6.setX(image6.getX()-2);

        if (image5.getX() < (image5.getWidth()*-1))
        {
            image5.setX((image5.getWidth()+800));
        }
        if (image6.getX() < (image6.getWidth()*-1))
        {
            image6.setX((image6.getWidth()+800));
        }

        //Move floating island
        floatingIsland1.setX(floatingIsland1.getX()-3);
        floatingIsland_num++;
        if (floatingIsland1.getX() < (floatingIsland1.getWidth()*-1))
        {
            int max = 3500;
            int min = 2400;
            int newX = this.numGen.nextInt(max-min)+min;
            floatingIsland1.setX(newX);
        }
        //Have retro floating up and down effect for floating island
        if (floatingIsland_num == 50)
        {
            floatingIsland1.setY(floatingIsland1.getY()+3);
        }
        else if (floatingIsland_num == 100)
        {
            floatingIsland1.setY(floatingIsland1.getY()-3);
            floatingIsland_num = 0;
        }

        //Move foreground
        image2.setX(image2.getX()-5);
        image3.setX(image3.getX()-5);

        if (image2.getX() < (image2.getWidth()*-1))
        {
            image2.setX((image2.getWidth())-5);
        }
        if (image3.getX() < (image3.getWidth()*-1))
        {
            image3.setX((image3.getWidth())-5);
        }

        //Animate person by changing the index that will be displayed
        num++;
        if (num % 6 == 0)
        {
            walkingIndex++;
        }
        if (walkingIndex >= walkingPerson.length)
        {
            walkingIndex = 0;
        }
        if (num >= 46)
        {
            num = 0;
        }
    }

    /**
     *  Draw everything to the screen and deal with most all graphics parts
     */
    public void render()
    {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null)
        {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        image1.draw(g);
        image4.draw(g);
        image5.draw(g);
        image6.draw(g);
        floatingIsland1.draw(g);
        image2.draw(g);
        image3.draw(g);

        walkingPerson[walkingIndex].draw(g);

        bs.show();
    }

    /**
     *  Will close the window and end the Thread
     */
    public synchronized void stop()
    {
        running = false;
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

        try
        {
            mainThread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }
}
