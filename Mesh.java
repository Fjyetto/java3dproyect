import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Mesh extends Real{
    public ArrayList<Vertex> vertices = new ArrayList<Vertex>();
    public ArrayList<Integer> tris = new ArrayList<Integer>();

    public Mesh(){
        super("Mesh");
    }


    public static Mesh FromOBJ(String tpath) throws FileNotFoundException{
        Mesh imported = new Mesh();
        try {
            File path = new File(tpath);
            Scanner mRead = new Scanner(path);
            while (mRead.hasNextLine()){
                String line = mRead.nextLine();
                // this is the part where you read https://en.wikipedia.org/wiki/Wavefront_.obj_file#File_format
            }
            mRead.close();
        } catch (FileNotFoundException e){
            System.out.println("Error occured");
            e.printStackTrace();
        }
        return imported;
    }
}