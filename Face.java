public class Face {
    public int vertices[]     =new int[3];
    public int vtexcoords[]   =new int[3];
    public int vertexnormals[]=new int[3];

    public Face(int v[], int vt[], int vn[]){
        vertices = v;
        vtexcoords = vt;
        vertexnormals = vn;
    }
}