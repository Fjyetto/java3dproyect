import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Shader{
    BufferedImage Albedo = null;

    public Shader(){
        // waow;
    }

    public int shade(Vector2 UV, IntVector2 SPos){
        //UV.print();
        //return (new Color(255*((SPos.x+SPos.y)%2), 0, 255*((SPos.x+SPos.y)%2))).getRGB();
        if (Albedo!=null){
            int x = (int)(Albedo.getWidth()*UV.x)%Albedo.getWidth();
            int y = (int)(Albedo.getHeight()*(1-UV.y))%Albedo.getHeight();
            return Albedo.getRGB(x, y);
        }else
        {
            return (new Color((int)(255*UV.x), (int)(255*UV.y), 255*((SPos.x+SPos.y)%2))).getRGB();
        }
    }
}