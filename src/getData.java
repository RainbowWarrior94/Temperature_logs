
public class getData {
    
	// metoda, która zwraca ciąg wejściowy bez zmian
	public static String getText(String input) {
        return input;  	        
    }
	
	// metoda, która konwertuje ciąg wejściowy na liczbę zmiennoprzecinkową
    public static double getDouble(String input) throws NumberFormatException {
        return Double.parseDouble(input);
    }
}