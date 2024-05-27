import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class User {
	//prywatne zmienne klasy User
    private String lastName;
    private String firstName;
    private ArrayList<Double> temperaturesC;
    private ArrayList<Double> temperaturesF;
    private ArrayList<String> dateTime = new ArrayList<>();

    // konstruktor klasy User i inicjalizacja pól obiektu
    public User(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.temperaturesC = new ArrayList<>();
        this.temperaturesF = new ArrayList<>();
    }
    
    //metoda dodawania temperatury użytkownika
    void addTemperature(double tempCelsius) {
        temperaturesC.add(tempCelsius);
        temperaturesF.add(Temperaturecalc.celsToFahr(tempCelsius));
        dateTime.add(getDateTime());
    }
    
    // metody pobierania wartości pól obiektu User (gettery)
    public String getLastName() {
        return lastName;
    }
    public String getFirstName() {
        return firstName;
    }
    public ArrayList<Double> getTemperaturesCelsius() {
        return temperaturesC;
    }
    public Double getTemperatureCels(int index) {
        return temperaturesC.get(index);
    }

    public ArrayList<Double> getTemperaturesFahrenheit() {
        return temperaturesF;
    }

    public Double getTemperatureFahr(int index) {
        return temperaturesF.get(index);
    }
    public ArrayList<String> getTimestamps() {
        return dateTime;
    }
    public String getDateTime() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateTimeFormat.format(new Date());
    }

}