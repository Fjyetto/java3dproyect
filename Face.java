public class Face {
    public Vertex vertices[]      =new Vertex[3];
    public Vector2 vtexcoords[]   =new Vector2[3];
    public Vector3 vertexnormals[]=new Vector3[3];

    public Face(Vertex v[], Vector2 vt[], Vector3 vn[]){
        vertices = v;
        vtexcoords = vt;
        vertexnormals = vn;
    }
}