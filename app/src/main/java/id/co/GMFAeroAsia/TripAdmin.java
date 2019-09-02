package id.co.GMFAeroAsia;

public class TripAdmin {
    private int id;
    private int id_bantek;

    private String name;
    private String division;

    private String departure_station;
    private String arrival_station;
    private String departure_city;
    private String arrival_city;
    private String departure_date;
    private String return_date;
    private String type_bantek;
    private String sppd_image;
    private String tiket_image;
    private String invoice_image;
    private String voucher_image;
    private String aml_image;

    private String status;

    public TripAdmin(int id, int id_bantek, String division, String name, String departure_station, String arrival_station, String departure_city, String arrival_city, String departure_date, String return_date, String type_bantek, String sppd_image, String tiket_image, String invoice_image, String voucher_image, String aml_image, String status) {
        this.id = id;
        this.id_bantek = id_bantek;

        this.name = name;
        this.division = division;

        this.departure_station = departure_station;
        this.arrival_station = arrival_station;
        this.departure_city = departure_city;
        this.arrival_city = arrival_city;
        this.departure_date = departure_date;
        this.return_date = return_date;
        this.type_bantek = type_bantek;
        this.sppd_image = sppd_image;
        this.tiket_image = tiket_image;
        this.invoice_image = invoice_image;
        this.voucher_image = voucher_image;
        this.aml_image = aml_image;
        this.status = status;
    }

    public int getId_admin() {
        return id;
    }

    public int getId_bantek() {
        return id_bantek;
    }

    public String getName_admin() {
        return name;
    }

    public String getDivision_admin() {
        return division;
    }

    public String getDeparture_station() {
        return departure_station;
    }

    public String getArrival_station() {
        return arrival_station;
    }

    public String getDeparture_city() { return departure_city; }

    public String getArrival_city() {
        return arrival_city;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public String getReturn_date() {
        return return_date;
    }

    public String getType_bantek() {
        return type_bantek;
    }

    public String getSppd_image() {
        return sppd_image;
    }

    public String getTiket_image() {
        return tiket_image;
    }

    public String getInvoice_image() {
        return invoice_image;
    }

    public String getVoucher_image() {
        return voucher_image;
    }

    public String getAml_image() {
        return aml_image;
    }

    public String getStatus(){
        return status;
    }




}
