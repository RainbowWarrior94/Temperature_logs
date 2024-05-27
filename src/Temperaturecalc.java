import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Temperaturecalc {

	//metoda, która przelicza temperaturę ze stopni Celsjusza na stopnie Fahrenheita i zaokrągla do dwóch miejsc po przecinku
	public static Double celsToFahr(double tempCels) {
		
		double rawTempFahr = ((tempCels * 9.0)/5.0) + 32.0;
		BigDecimal roundedTemp = new BigDecimal(rawTempFahr).setScale(2, RoundingMode.HALF_UP);
		double tempFahr = roundedTemp.doubleValue(); 
	return tempFahr;
	}

	//metoda, która oblicza średnią temperaturę i zaokrągla wartość do dwóch miejsc po przecinku
	public static double AverageTemp(ArrayList<Double> temperatures) {
      double total = 0.0;
      for (Double temp : temperatures) {
          total += temp;
      }
      double rawAverage = total / temperatures.size();
      BigDecimal roundedAvarageTemp = new BigDecimal(rawAverage).setScale(2, RoundingMode.HALF_UP);
      double averageTemp = roundedAvarageTemp.doubleValue();
    		  
    return averageTemp;
	}


}
