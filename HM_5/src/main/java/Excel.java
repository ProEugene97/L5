import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import static java.lang.Integer.parseInt;

public class Excel {

    private static final String url = "jdbc:mysql://localhost:3306/userdb?verifyServerCertificate=false&useSSL=false&requireSSL=false&useLegacyDatetimeCode=false&amp&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "root";
    private static String[] List = {"Имя", "Фамилия", "Отчество", "Возраст", "Пол", "Дата Рождения", "ИНН", "Почтовый индекс", "Страна", "Область", "Город", "Улица", "Дом", "Квартира"};

    public static void main(String[] args) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Новый лист");
        int rowNum = 0;
        Row row = sheet.createRow(rowNum);
        for (int i = 0; i != List.length; ++i) {
            row.createCell(i).setCellValue(List[i]);
        }
        int size = 1 + (int) (Math.random()*30);
        List<Data> dataList = fillData(size);
        for (Data data : dataList) {
            createSheetHeader(sheet, ++rowNum, data);
        }
        File file = new File("Data.xls");
        try {
            System.setErr(new PrintStream(new File("log.txt")));
            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
        } catch (Exception e) {
                e.printStackTrace();
        }
        System.err.println("Файл создан:" + file.getAbsolutePath());
    }

    private static int randBetween(int start, int end){
        return start + (int) Math.round(Math.random()*(end - start));
    }

    private static String read_file(String path, int line_number) {
        String fileContent = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String sub;
            int i = 1;
            while ((sub = br.readLine()) != null) {
                if(i == line_number) {
                    fileContent = sub;
                    break;
                }
                ++i;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    private static void write_name(Data data) {
        int number = randBetween(1, 30);
        if (randBetween(0, 1) == 1) {
            String name = read_file("src/main/resources/MaleName.txt", number);
            data.set_name(name);
            data.set_sex("М");
        } else {
            String name = read_file("src/main/resources/FemaleName.txt", number);
            data.set_name(name);
            data.set_sex("Ж");
        }
    }

    private static void write_surname(Data data) {
        int number = randBetween(1, 30);
        if (data.get_sex().equals("М")) {
            String surname = read_file("src/main/resources/MaleSurname.txt", number);
            data.set_surname(surname);
        } else {
            String surname = read_file("src/main/resources/FemaleSurname.txt", number);
            data.set_surname(surname);
        }
    }

    private static void write_patronymic(Data data){
        int number = randBetween(1, 30);
        if (data.get_sex().equals("М")) {
            String patronymic = read_file("src/main/resources/MalePatronymic.txt", number);
            data.set_patronymic(patronymic);
        } else {
            String patronymic = read_file("src/main/resources/FemalePatronymic.txt", number);
            data.set_patronymic(patronymic);
        }
    }

    private static void write_country(Data data) {
        int number = randBetween(1, 30);
        String country = read_file("src/main/resources/Countries.txt", number);
        data.set_country(country);
    }
    private static void write_city(Data data) {
        int number = randBetween(1, 30);
        String city = read_file("src/main/resources/Cities.txt", number);
        data.set_city(city);
    }

    private static void write_street(Data data) {
        int number = randBetween(1, 30);
        String street = read_file("src/main/resources/Streets.txt", number);
        data.set_street(street);
    }

    private static void write_region(Data data) {
        int number = randBetween(1, 30);
        String region = read_file("src/main/resources/Regions.txt", number);
        data.set_region(region);
    }

    private static void write_dob(Data data) {
        GregorianCalendar gc = new GregorianCalendar();
        int year = randBetween(1920, 2000);
        gc.set(Calendar.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR));
        gc.set(Calendar.DAY_OF_MONTH, dayOfYear);
        data.set_dob(gc);
    }

    //вычисление возраста
    private static void write_age(Data data) {
        Calendar calendar = Calendar.getInstance();
        int age = calendar.get(Calendar.YEAR) - data.get_dob().get(data.get_dob().YEAR);
        if (((data.get_dob().get(data.get_dob().MONTH)) - calendar.get(Calendar.MONTH) > 0) | (((data.get_dob().get(data.get_dob().MONTH)) - calendar.get(Calendar.MONTH) > 0) & ((data.get_dob().get(data.get_dob().DAY_OF_MONTH)) - calendar.get(Calendar.DAY_OF_MONTH) >= 0))) {
            data.set_age(age - 1);
        } else data.set_age(age);
    }

    private static void write_postcode(Data data) {
        int number = randBetween(1000000, 10000000-1);
        data.set_postcode(number);
    }

    private static void write_apartment(Data data) {
        int number = randBetween(1, 1000);
        data.set_apartment(number);
    }

    private static void write_house(Data data) {
        int number = randBetween(1, 100);
        data.set_house(number);
    }

    //генерация ИНН
    private static void write_itn(Data data) {
        int branch_number = randBetween(10, 51);
        long itn = 770000000000L + branch_number*100000000L;
        int k = 100;
        for (int i = 1; i <= 6; ++i) {
            int n = randBetween(0, 9);
            itn += n*k;
            k *= 10;
        }
        int[] array = new int[10];
        k = 1;
        for (int i = 0; i != 10; ++i) {
            array[i] = (int)(itn/100) % (10*k);
            k *= 10;
        }
        // коэфиценты для второго контрольного числа
        int [] coefficients2 = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
        int sum2 = 0;
        for (int i = 0; i != 10; ++i) {
            sum2 += coefficients2[i] * array[i];
        }
        int num_kontrol2 = sum2 % 11;
        itn += num_kontrol2*10;
        // коэфиценты для первого контрольного числа
        int [] coefficients1 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
        int sum1 = 0;
        for (int i = 0; i != 10; ++i) {
            sum1 += coefficients1[i] * array[i];
        }
        sum1 = coefficients1[10] * num_kontrol2;
        int num_kontrol1 = sum1 % 11;
        itn += num_kontrol1;
        data.set_itn(itn);
    }

    private static List<Data> fillData(int size) {
        List<Data> data = new ArrayList<>();
        try {
            getObject(data, size);
            put_in_db(data);
            return data;
        } catch (UnirestException e) {
            e.printStackTrace();
            System.out.println("No connection");
            get_from_db(data);
            if (data.size() != 0)
                return data;
            for (int i = 0; i != size; ++i) {
                data.add(new Data());
                write_name(data.get(i));
                write_surname(data.get(i));
                write_patronymic(data.get(i));
                write_country(data.get(i));
                write_region(data.get(i));
                write_city(data.get(i));
                write_street(data.get(i));
                write_dob(data.get(i));
                write_age(data.get(i));
                write_postcode(data.get(i));
                write_apartment(data.get(i));
                write_house(data.get(i));
                write_itn(data.get(i));
            }
            return data;
        }
    }
    private static void createSheetHeader(HSSFSheet sheet, int rowNum, Data data) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(data.get_name());
        row.createCell(1).setCellValue(data.get_surname());
        row.createCell(2).setCellValue(data.get_patronymic());
        row.createCell(3).setCellValue(data.get_age());
        row.createCell(4).setCellValue(data.get_sex());
        row.createCell(5).setCellValue(data.get_dob().get(Calendar.DAY_OF_MONTH) + "-" + (data.get_dob().get(Calendar.MONTH) + 1) + "-" + data.get_dob().get(Calendar.YEAR));
        row.createCell(6).setCellValue(String.valueOf(data.get_itn()));
        row.createCell(7).setCellValue(data.get_postcode());
        row.createCell(8).setCellValue(data.get_country());
        row.createCell(9).setCellValue(data.get_region());
        row.createCell(10).setCellValue(data.get_city());
        row.createCell(11).setCellValue(data.get_street());
        row.createCell(12).setCellValue(data.get_house());
        row.createCell(13).setCellValue(data.get_apartment());
    }
    private static void getObject(List<Data> data, int size) throws UnirestException {
        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        for (int i = 0; i < size; ++i) {
            HttpResponse<JData> response =
                    Unirest.get("https://randus.org/api.php").asObject(JData.class);
            JData jdata = response.getBody();
            data.add(new Data());
            data.get(i).set_name(jdata.lname);
            data.get(i).set_surname(jdata.fname);
            data.get(i).set_patronymic(jdata.patronymic);
            data.get(i).set_sex(jdata.gender);
            data.get(i).set_postcode(parseInt(jdata.postcode));
            data.get(i).set_city(jdata.city);
            data.get(i).set_street(jdata.street);
            data.get(i).set_house(jdata.house);
            data.get(i).set_apartment(jdata.apartment);
            write_country(data.get(i));
            write_region(data.get(i));
            write_itn(data.get(i));
            write_dob(data.get(i));
            write_age(data.get(i));
        }
    }
    private static void put_in_db(List<Data> data){
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, user, password);
            Statement stmt = connection.createStatement();
            for (int i = 0; i < data.size(); ++i) {
                String query = "INSERT INTO users_table(name, surname, patronymic, age, sex, dob, itn, postcode, country, city, street, house, apartment, region) VALUES('" + data.get(i).get_name() + "','" + data.get(i).get_surname() + "','" + data.get(i).get_patronymic()+ "','" + String.valueOf(data.get(i).get_age()) + "','"+ data.get(i).get_sex()+"','" + data.get(i).get_dob().get(Calendar.YEAR) + "-" + (data.get(i).get_dob().get(Calendar.MONTH) + 1) + "-" + data.get(i).get_dob().get(Calendar.DAY_OF_MONTH) + "','" + String.valueOf(data.get(i).get_itn()) + "','"+ String.valueOf(data.get(i).get_postcode()) + "','" + data.get(i).get_country() + "','" + data.get(i).get_city() + "','" +  data.get(i).get_street() + "','" + String.valueOf(data.get(i).get_house()) + "','" + String.valueOf(data.get(i).get_apartment()) + "','" + data.get(i).get_region()+ "');";
                stmt.execute(query);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private static void get_from_db(List<Data> data){
        Connection connection;
        String query = "SELECT * FROM users_table";
        try {
            connection = DriverManager.getConnection(url, user, password);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            for (int i = 0; rs.next(); ++i) {
                data.add(new Data());
                data.get(i).set_name(rs.getString("name"));
                data.get(i).set_surname(rs.getString("surname"));
                data.get(i).set_patronymic(rs.getString("patronymic"));
                data.get(i).set_sex(rs.getString("sex"));
                data.get(i).set_postcode(rs.getInt("postcode"));
                data.get(i).set_city(rs.getString("city"));
                data.get(i).set_street(rs.getString("street"));
                data.get(i).set_house(rs.getInt("house"));
                data.get(i).set_apartment(rs.getInt("apartment"));
                data.get(i).set_country(rs.getString("country"));
                data.get(i).set_region(rs.getString("region"));
                data.get(i).set_itn(Long.parseLong(rs.getString("itn")));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                cal.setTime(sdf.parse(rs.getString("dob")));
                data.get(i).set_dob(cal);
                data.get(i).set_age(rs.getInt("age"));
            }
    } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }

    }
}
