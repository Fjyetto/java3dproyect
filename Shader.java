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
        return (new Color((int)(255*UV.y), (int)(255*UV.x), 255*((SPos.x+SPos.y)%2))).getRGB();
    }
}