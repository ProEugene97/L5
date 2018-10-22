import java.util.Calendar;

public class Data {
    private String name;
    private String surname;
    private String patronymic;
    private int age;
    private String sex;
    private Calendar dob;
    private long itn;
    private int postcode;
    private String country;
    private String region;
    private String city;
    private String street;
    private int house;
    private int apartment;

    public Data() {
    }

    public Data(String name, String surname, String patronymic, int age, String sex, Calendar dob, long itn, int postcode, String country, String region, String city, String street, int house, int apartment) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.age = age;
        this.sex = sex;
        this.dob = dob;
        this.itn = itn;
        this.postcode = postcode;
        this.country = country;
        this.region = region;
        this.city = city;
        this.street = street;
        this.house = house;
        this.apartment = apartment;
    }

    String get_name() {
        return name;
    }

    void set_name(String name) {
        this.name = name;
    }

    String get_surname() {
        return surname;
    }

    void set_surname(String surname) {
        this.surname = surname;
    }

    String get_patronymic() {
        return patronymic;
    }

    void set_patronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    int get_age() {
        return age;
    }

    void set_age(int age) {
        this.age = age;
    }

    String get_sex() {
        return sex;
    }

    void set_sex(String sex) {
        this.sex = sex;
    }

    Calendar get_dob() {
        return dob;
    }

    void set_dob(Calendar dob) {
        this.dob = dob;
    }

    long get_itn() {
        return itn;
    }

    void set_itn(long itn) {
        this.itn = itn;
    }

    int get_postcode() {
        return postcode;
    }

    void set_postcode(int postcode) {
        this.postcode = postcode;
    }

    String get_country() {
        return country;
    }

    void set_country(String country) {
        this.country = country;
    }

    String get_region() {
        return region;
    }

    void set_region(String region) {
        this.region = region;
    }

    String get_city() {
        return city;
    }

    void set_city(String city) {
        this.city = city;
    }

    String get_street() {
        return street;
    }

    void set_street(String street) {
        this.street = street;
    }

    int get_house() {
        return house;
    }

    void set_house(int house) {
        this.house = house;
    }

    int get_apartment() {
        return apartment;
    }

    void set_apartment(int apartment) {
        this.apartment = apartment;
    }
}

