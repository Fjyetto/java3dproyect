public class RealMesh extends Real{
    public Mesh mesh = null;
    public Transform transform = new Transform();

    public RealMesh(Mesh m){
        super("RealMesh");
        mesh = m;
    } 

}