public class Camera extends Transform{
    public double scale = 1.0;
    public double zscale = 1.0;

    public Camera(){
        
    }

    public Vector3 Project(Vector3 point){
        Vector3 p1 = MultiplyWV(point);
        p1 = p1.Multiply(scale);
        double zm = 1.0/(1.0+p1.z*zscale);
        p1 = p1.Multiply(new Vector3(zm,zm,1.0));
        return p1;
    }
}