public class Vertex{
    Vector3 p = new Vector3(0.0,0.0,0.0);
    int col = 0xFFFFFF;

    public Vertex(Vector3 po){
        p = po;
    }

    public void sC(int c){
        col = c;
    }
}