public class JData {
    public String lname;
    public String fname;
    public String patronymic;
    public String gender;
    public String date;
    public String postcode;
    public String city;
    public String street;
    public int house;
    public int apartment;
    public String phone;
    public String login;
    public String password;
    public String color;
    public String userpic;

    public JData() {
    }

    public JData(String lname, String fname, String patronymic, String gender, String dob, String postcode, String city, String street, int house, int apartment, String phone, String login, String password, String color, String userpic) {
        this.lname = lname;
        this.fname = fname;
        this.patronymic = patronymic;
        this.gender = gender;
        this.date = dob;
        this.postcode = postcode;
        this.city = city;
        this.street = street;
        this.house = house;
        this.apartment = apartment;
        this.phone = phone;
        this.login = login;
        this.userpic = userpic;
        this.color = color;
        this.password = password;
    }
 }
