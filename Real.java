import java.util.ArrayList;

public class Real{
    public Real parent = null;
    public String name = "";
    public ArrayList<Real> children = new ArrayList<Real>();
    public Transform transform = new Transform();

    public Real(String setname){
        name = setname;
    }

    public ArrayList<Real> GetChildren(){
        return children;
    }

    public <T extends Real> T Append(T child){
        children.add(child);
        return child;
    }
}