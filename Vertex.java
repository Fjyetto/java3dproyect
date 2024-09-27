public class Vertex{
    Vector3 p = new Vector3(0.0,0.0,0.0);
    Vector2 uv= new Vector2(0.0,0.0);
    int col = 0xFFFFFF;

    public Vertex(Vector3 po, Vector2 u, int co){
        p = po;
        uv = u;
        col = co;
    }

    public void sUV(Vector2 u){
        uv = u;
    }

    public void sC(int c){
        col = c;
    }
}