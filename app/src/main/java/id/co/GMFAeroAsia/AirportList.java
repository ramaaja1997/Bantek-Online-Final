package id.co.GMFAeroAsia;



public class AirportList {
    private int id_airport;
    private String location;
    private String code;
    private String name;
    private String city;

    public AirportList(int id_airport, String location, String code, String name, String city) {
        this.id_airport = id_airport;
        this.location = location;
        this.code = code;
        this.name = name;
        this.city = city;
    }

    public int getId() {
        return id_airport;
    }

    public String getLocation() {
        return location;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCity() { return city; }
}

