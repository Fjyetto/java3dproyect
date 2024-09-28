import java.util.ArrayList;

public class Real{
    public Real parent = null;
    public String name = "";
    public ArrayList<Real> children = new ArrayList<Real>();
    public Transform transform = new Transform();

    public Real(String setname){
        name = setname;
    }

    /*public ArrayList<T> GetChildren(){
        return children;
    }*/

    public <T extends Real> T Append(T child){
        children.add(child);
        return child;
    }

    public <T extends Real> T getChildAs(int index, Class<T> expectedType) {
        Real child = children.get(index);  // Retrieve the child at the index

        // Check if the child is of the expected type
        if (expectedType.isInstance(child)) {
            return expectedType.cast(child);  // Safe cast to the expected type
        } else {
            throw new IllegalArgumentException("Child at index " + index + " is not of type " + expectedType.getSimpleName());
        }
    }
}