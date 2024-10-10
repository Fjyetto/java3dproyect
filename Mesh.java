import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Mesh{
    public ArrayList<Vertex> vertices = new ArrayList<Vertex>();
    public ArrayList<Vector3> vertexnormals = new ArrayList<Vector3>();
    public ArrayList<Vector2> vtexcoords = new ArrayList<Vector2>();
    public ArrayList<Face> faces = new ArrayList<Face>();

    public Mesh(){
        //super("Mesh");
    }


    public static Mesh FromOBJ(String tpath) throws FileNotFoundException{
        Mesh imported = new Mesh();
        try {
            File path = new File(tpath);
            Scanner mRead = new Scanner(path);
            while (mRead.hasNextLine()){
                String line = mRead.nextLine();
                if (line.charAt(0)=='#'){
                    System.out.println("comment: "+line);
                }else{
                    System.out.println(line);
                    String[] tokens = line.split(" ");
                    switch(tokens[0]){
                        case "v":
                            if (tokens.length>=4){
                                imported.vertices.add(new Vertex(new Vector3(
                                    Float.parseFloat(tokens[1]),Float.parseFloat(tokens[2]),Float.parseFloat(tokens[3])
                                    )));
                            }
                            break;
                        case "vt":
                            if (tokens.length>=3){
                                imported.vtexcoords.add(new Vector2(
                                    Float.parseFloat(tokens[1]),Float.parseFloat(tokens[2])
                                    ));
                            }
                            break;
                        case "vn":
                            if (tokens.length>=4){
                                imported.vertexnormals.add(new Vector3(
                                    Float.parseFloat(tokens[1]),Float.parseFloat(tokens[2]),Float.parseFloat(tokens[3])
                                    ));
                            }
                            break;
                        case "f":
                            if (tokens.length>=4){
                                int vertl[] = new int[3]; // Vert list
                                int verttl[] = new int[3]; // Vert UV list
                                int vertnl[] = new int[3]; // Vert normal list
                                int ti = 0;

                                for (String ctok: tokens){
                                    if (ctok.charAt(0)=='f'){
                                        //System.out.println("GET OUT");
                                        continue;
                                    }
                                    String[] parts=ctok.split("/");
                                    //System.out.println(ctok+' '+parts[0]+' '+parts[1]+' '+parts[2]);
                                    switch(parts.length){
                                        case 1:
                                            vertl[ti]=Integer.valueOf(parts[0])-1;
                                            if (Integer.valueOf(parts[0])-1>imported.vertices.size()) System.out.println("THIS INDEX "+parts[0]+" IS OUT OF THE RANGE "+imported.vertices.size());
                                            break;
                                        case 2:
                                            vertl[ti]=Integer.valueOf(parts[0])-1;
                                            if (Integer.valueOf(parts[0])-1>imported.vertices.size()) System.out.println("THIS INDEX "+parts[0]+" IS OUT OF THE RANGE "+imported.vertices.size());
                                            verttl[ti]=Integer.valueOf(parts[1])-1;
                                            break;
                                        case 3:
                                            vertl[ti]=Integer.valueOf(parts[0])-1;
                                            if (Integer.valueOf(parts[0])-1>imported.vertices.size()) System.out.println("THIS INDEX "+parts[0]+" IS OUT OF THE RANGE "+imported.vertices.size());
                                            try{
                                                //verttl[ti]=Integer.valueOf(parts[1])-1;
                                            } catch (NumberFormatException e) {
                                                //verttl[ti]=0;
                                            }
                                            //vertnl[ti]=Integer.valueOf(parts[2])-1;
                                            break;
                                    }
                                    ti+=1;
                                }
                                imported.faces.add(new Face(vertl,verttl,vertnl));
                            }
                            break;
                    }
                }
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