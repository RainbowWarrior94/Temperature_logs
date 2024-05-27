import javax.swing.*;

//klasa glówna
public class Temperature_logs {
	// metoda main, która jest punktem wejścia do programu
	public static void main(String[] args) {
		// metoda wykonywania kodu w wątku obsługi zdarzeń
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			// nowa instancja klasy
			WindowView view = new WindowView();
			//ustawienie zamykania okienka
			view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			//wyswietlanie okienka
			view.setVisible(true);
			}
		});
	}
}



