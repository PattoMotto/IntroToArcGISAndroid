package esritraining.pea.com.workshop1.model;

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

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
