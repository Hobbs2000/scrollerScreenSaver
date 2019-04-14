package Graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Calvin on 12/19/2015.
 */
public class Pic
{
    private int x,y;
    private String path;
    private BufferedImage pic;

    /**
     *
     */
    public Pic(int x, int y, String path)
    {
        this.x = x;
        this.y = y;
        this.path = path;
        try
        {
            this.pic = ImageIO.read(getClass().getResource(this.path));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    public void draw(Graphics g)
    {
        g.drawImage(pic, x, y, null);
    }

    /**
     *
     */
    public int getX()
    {
        return this.x;
    }
    public void setX(int newX)
    {
        this.x = newX;
    }

    /**
     *
     */
    public int getY()
    {
        return this.y;
    }
    public void setY(int newY)
    {
        this.y = newY;
    }

    public int getWidth()
    {
        return pic.getWidth();
    }
    public int getHeight()
    {
        return pic.getHeight();
    }

    /**
     *
     */
    public String getPath()
    {
        return this.path;
    }
}

