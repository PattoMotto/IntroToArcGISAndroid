package esritraining.pea.com.workshop3.model;

/**
 * Created by esri5141 on 22/10/2558.
 */
public class CustomModel {
    private String name;
    private String address;
    private double x;
    private double y;

    public CustomModel(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public CustomModel(String name, String address, double x, double y) {
        this.name = name;
        this.address = address;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
