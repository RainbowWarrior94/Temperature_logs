import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class WindowView extends JFrame {

	//deklaracja oraz inicjalizacja zmiennych
    private JButton addUserButton = new JButton("Dodaj użytkownika");
    private JButton addTemperatureButton = new JButton("Dodaj temperaturę");
    private JButton averageTempButton = new JButton("Oblicz średnią temperaturę");
    private JButton saveToDBButton = new JButton("Zapisz do bazy danych");
    private JLabel allData = new JLabel(" Wszystkie dane ");
    
    private JComboBox<String> usersList = new JComboBox<>();  
    private ArrayList<User> users = new ArrayList<>();
    
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    
    String lastName;
    String firstName;
    double temp;

    // Tablica z nazwami kolumn tabeli
    String[] colNames = {"№", "Nazwisko", "Imię", "Temperatura °C", "Temperatura °F", "Data i godzina"};
    // Tworzenie modelu tabeli
    private DefaultTableModel tableModel = new DefaultTableModel(colNames, 0) {
    	// Zakaz edycji komórek
        public boolean isCellEditable(int row, int column) {
        return false;
        }
    };
    // Tworzenie obiektu tabeli przy użyciu utworzonego modelu
    private JTable usersTable = new JTable(tableModel);

    // tworzenie obiektów klas
    getData get_data = new getData();
    
 // Konstruktor klasy WindowView
    public WindowView() {
        super("Temperatura");
        this.setBounds(150, 50, 800, 500);
        this.setContentPane(cardPanel);

        int[] colWidth = {10, 100, 100, 100, 100, 100};
        for (int j = 0; j < colWidth.length; j++) {
            usersTable.getColumnModel().getColumn(j).setPreferredWidth(colWidth[j]);
        }

        JPanel mainPanel = new JPanel(new GridBagLayout()); // tworzenie kontenera dla elementów interfejsu
        GridBagConstraints setProperties = new GridBagConstraints(); // obiekt do ustawiania właściwości komponentów
        // ustawianie właściwości komponentów
        setProperties.anchor = GridBagConstraints.WEST;
        setProperties.insets = new Insets(5, 5, 5, 5);

        setProperties.gridx = 0;
        setProperties.gridy = 2;
        mainPanel.add(addUserButton, setProperties);
        addUserButton.addActionListener(new addUserButton());

        setProperties.gridx = 2;
        setProperties.gridy = 2;
        mainPanel.add(addTemperatureButton, setProperties);
        addTemperatureButton.addActionListener(new addTemperatureButton());

        setProperties.gridx = 4;
        setProperties.gridy = 2;
        mainPanel.add(averageTempButton, setProperties);
        averageTempButton.addActionListener(new averageTempButton());
        
        setProperties.gridx = 6;
        setProperties.gridy = 2;
        mainPanel.add(saveToDBButton, setProperties);
        saveToDBButton.addActionListener(new saveToDBButton());

        setProperties.gridx = 0;
        setProperties.gridy = 4;
        setProperties.gridwidth = 2;
        mainPanel.add(allData, setProperties);
        
        setProperties.gridwidth = 10;
        setProperties.gridx = 0;
        setProperties.gridy = 8;
        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        mainPanel.add(scrollPane, setProperties);
        usersTable.setAutoCreateRowSorter(true); 
        usersTable.setEnabled(true); 
        usersTable.setRowSelectionAllowed(true);
        usersTable.getTableHeader().setReorderingAllowed(false);
        cardPanel.add(mainPanel, "mainPanel");
    }
    
	class addUserButton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			AddUserWindow userWindow = new AddUserWindow(WindowView.this, users);
			userWindow.setVisible(true);
        }
	}
	
	class AddUserWindow extends JDialog {
		//deklaracja oraz inicjalizacja zmiennych
		private JLabel last_name = new JLabel(" Nazwisko ");
		private JTextField input_lname = new JTextField("", 15);
	    private JLabel first_name = new JLabel(" Imię ");
		private JTextField input_fname = new JTextField("", 15);
	    private JLabel temperature = new JLabel(" Temperatura ciała (°C) ");
	    private JTextField input_temperature = new JTextField("", 4);
	    private JButton addUserAction = new JButton("Dodaj");
	    
	    private ArrayList<User> users;

	    public AddUserWindow(JFrame parent, ArrayList<User> users) {
	    	super(parent, "Dodaj użytkownika", true);
		    this.users = users;
 
		    JPanel Panel = new JPanel(new GridBagLayout()); // tworzenie kontenera dla elementów interfejsu
		    GridBagConstraints setProp = new GridBagConstraints(); // obiekt do ustawiania właściwości komponentów
		    
		    // ustawianie właściwości komponentów
		    setProp.anchor = GridBagConstraints.WEST;
	        setProp.insets = new Insets(5, 5, 5, 5);
		
		    setProp.gridx = 0;
	        setProp.gridy = 0;
	        Panel.add(last_name, setProp);
		        
            setProp.gridx = 1;
	        setProp.gridy = 0;
	        Panel.add(input_lname, setProp);
		        
	        setProp.gridx = 0;
		    setProp.gridy = 1;
		    Panel.add(first_name, setProp);
		
		    setProp.gridx = 1;
		    setProp.gridy = 1;
		    Panel.add(input_fname, setProp);
		        
		    setProp.gridx = 0;
		    setProp.gridy = 2;
	        Panel.add(temperature, setProp);
		
	        setProp.gridx = 1;
	        setProp.gridy = 2;
		    Panel.add(input_temperature, setProp);
		        
		    setProp.gridx = 1;
		    setProp.gridy = 6;
		    Panel.add(addUserAction, setProp);
		    addUserAction.addActionListener(new addUserAction());
		        
		    getContentPane().add(Panel);
		    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		    pack();
		    setLocationRelativeTo(parent);
        }
		    
		class addUserAction implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				// Walidacja wprowadzanych danych
				lastName = getData.getText(input_lname.getText());
				if (validateNameInput(lastName)) {
					JOptionPane.showMessageDialog(null, "Nazwisko musi składać się z co najmniej dwóch znaków i nie może zawierać cyfr.");
			    return;
			    }  
			    firstName = getData.getText(input_fname.getText());
				if (validateNameInput(firstName)) {
					JOptionPane.showMessageDialog(null, "Imię musi składać się z co najmniej dwóch znaków i nie może zawierać cyfr.");
			    return;
			    } 	  
				try{
					temp = getData.getDouble(input_temperature.getText());

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Temperatura ciała musi być liczbą rzeczywistą");
				return;
				}		
				if (validateTempInput(temp)) {
					JOptionPane.showMessageDialog(null, "Temperatura musi być większa od zera");
				return;
			    }

				User user = new User(lastName, firstName); //Tworzenie obiektu użytkownika o podanym nazwisku i nazwie
				user.addTemperature(temp); // Dodawanie temperatury
			    users.add(user); // Dodawanie użytkownika do listy użytkowników
			    
			    // Uzyskanie ostatniej temperatury w stopniach Celsjusza i Fahrenheita      
			    double lastTemperatureCelsius = user.getTemperatureCels(user.getTemperaturesCelsius().size() - 1);
			    double lastTemperatureFahrenheit = user.getTemperatureFahr(user.getTemperaturesFahrenheit().size() - 1);

			    Object[] rowData = {tableModel.getRowCount() + 1, user.getLastName(), user.getFirstName(), lastTemperatureCelsius, lastTemperatureFahrenheit, user.getDateTime()};
			    tableModel.addRow(rowData); // Dodawanie wiersza danych do modelu tabeli

			    // Dodawanie użytkownika do listy rozwijanej
			    usersList.addItem(user.getLastName() + ", " + user.getFirstName());
					
			    dispose();		
			}
		}
	}

	class addTemperatureButton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			AddMoreTempWindow moreTempWindow = new AddMoreTempWindow(WindowView.this, users);
			moreTempWindow.setVisible(true);
		}
	}    

	class AddMoreTempWindow extends JDialog {
		//deklaracja oraz inicjalizacja zmiennych
		private JComboBox<String> usersList;
		private JTextField inputTemp = new JTextField("", 4);
		private JButton addTemperatureAction = new JButton("Dodaj temperaturę (°C)");

		private ArrayList<User> users;

		public AddMoreTempWindow(JFrame parent, ArrayList<User> users) {
			super(parent, "Dodaj temperaturę", true);
		    this.users = users;

		    usersList = new JComboBox<>();
		    for (User user : users) {
		    	usersList.addItem(user.getLastName() + ", " + user.getFirstName());
		    }

		    JPanel panel = new JPanel(new GridBagLayout()); // tworzenie kontenera dla elementów interfejsu
		    GridBagConstraints setProperties = new GridBagConstraints(); // obiekt do ustawiania właściwości komponentów
		    // ustawianie właściwości komponentów
		    setProperties.anchor = GridBagConstraints.WEST;
	        setProperties.insets = new Insets(5, 5, 5, 5);

	        setProperties.gridx = 0;
	        setProperties.gridy = 0;
	        panel.add(new JLabel("Wybierz użytkownika:"), setProperties);

	        setProperties.gridx = 1;
	        setProperties.gridy = 0;
	        panel.add(usersList, setProperties);

	        setProperties.gridx = 0;
	        setProperties.gridy = 1;
	        panel.add(new JLabel("Wprowadź temperaturę:"), setProperties);

	        setProperties.gridx = 1;
	        setProperties.gridy = 1;
	        panel.add(inputTemp, setProperties);

	        setProperties.gridx = 0;
	        setProperties.gridy = 6;
	        panel.add(addTemperatureAction, setProperties);
	        addTemperatureAction.addActionListener(new AddTempAction());

	        getContentPane().add(panel);
	        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	        pack();
		    setLocationRelativeTo(parent);
	    }

		class AddTempAction implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				// Walidacja wprowadzanych danych
				String selected_name = (String) usersList.getSelectedItem();
		        if (selected_name == null) {
		        	JOptionPane.showMessageDialog(null, "Wybierz użytkownika z listy", "Błąd", JOptionPane.ERROR_MESSAGE);
		        return;
		        }

		        String[] names = selected_name.split(", ");
		        if (names.length != 2) {
		        	JOptionPane.showMessageDialog(null, "Błąd w pobieraniu nazwiska i imienia", "Błąd", JOptionPane.ERROR_MESSAGE);
		        return;
		        }

		        String selected_lname = names[0];
	            String selected_fname = names[1];
	            double add_temp;
		            
	            try{
	            	add_temp = getData.getDouble(inputTemp.getText());

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Temperatura ciała musi być liczbą rzeczywistą");
				return;
				}
					
				if (validateTempInput(temp)) {
					JOptionPane.showMessageDialog(null, "Temperatura ciała musi być większa od zera");
            	return;
	            }
				
				try {
					// Wyszukiwanie użytkownika na liście według nazwiska i imienia
				    User selected_user = null;
				    for (User user : users) {
				        if (user.getLastName().equals(selected_lname) && user.getFirstName().equals(selected_fname)) {
				            selected_user = user;
				            break;
		                    }
		                }
	                    selected_user.addTemperature(add_temp); // Dodawanie wprowadzonej temperatury dla wybranego użytkownika
	                    // Uzyskanie ostatniej temperatury w stopniach Celsjusza i Fahrenheita  
     			        double lastTempCels = selected_user.getTemperatureCels(selected_user.getTemperaturesCelsius().size() - 1);
			            double lastTempFahr = selected_user.getTemperatureFahr(selected_user.getTemperaturesFahrenheit().size() - 1);
  
						Object[] rowData = {tableModel.getRowCount() + 1, selected_user.getLastName(), selected_user.getFirstName(), lastTempCels, lastTempFahr, selected_user.getDateTime()};
						tableModel.addRow(rowData);// Dodawanie wiersza danych do modelu tabeli
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Temperatura ciała musi być liczbą rzeczywistą większą od zera", "Błąd", JOptionPane.ERROR_MESSAGE);
		        }
				dispose();
	        }
	    }
	}

	class averageTempButton implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			AverageTempWindow averTempWindow = new AverageTempWindow(WindowView.this, users);
			averTempWindow.setVisible(true);
		}
	}

    class AverageTempWindow extends JDialog {
    	//deklaracja oraz inicjalizacja zmiennych
    	private JComboBox<String> usersList = new JComboBox<>();
	    private JButton showAverageTempButton = new JButton("Pokaż średnią temperaturę");
        private JLabel averageCelsTempLabel = new JLabel("Średnia temperatura C: ");
        private JLabel averageFehrTempLabel = new JLabel("Średnia temperatura F: ");

        private ArrayList<User> users;

        public AverageTempWindow(JFrame parent, ArrayList<User> users) {
        	super(parent, "Średnia temperatura użytkownika", true);
	        this.users = users;

	        JPanel panel = new JPanel(new GridBagLayout()); // tworzenie kontenera dla elementów interfejsu
            GridBagConstraints constraints = new GridBagConstraints();// obiekt do ustawiania właściwości komponentów
            constraints.anchor = GridBagConstraints.WEST;
            constraints.insets = new Insets(5, 5, 5, 5);

            //Wypełnianie listy rozwijanej danymi o użytkownikach
            for (User user : users) {
            	usersList.addItem(user.getLastName() + ", " + user.getFirstName());
	        }

	        showAverageTempButton.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		showAverTemp(); //wywołanie metody
            	}
           });

	       // ustawianie właściwości komponentów
	       constraints.gridx = 0;          
	       constraints.gridy = 0;
	       panel.add(new JLabel("Wybierz użytkownika:"), constraints);

           constraints.gridx = 1;
           constraints.gridy = 0;
           panel.add(usersList, constraints);

           constraints.gridx = 0;
           constraints.gridy = 1;
           constraints.gridwidth = 2;
           panel.add(showAverageTempButton, constraints);

           constraints.gridx = 0;
           constraints.gridy = 2;
           panel.add(averageCelsTempLabel, constraints);
	            
           constraints.gridx = 0;
           constraints.gridy = 3;
           panel.add(averageFehrTempLabel, constraints);

           getContentPane().add(panel);
           setDefaultCloseOperation(DISPOSE_ON_CLOSE);
           pack();
           setLocationRelativeTo(parent);
       }
        
       private void showAverTemp() {
    	   int selected_index = usersList.getSelectedIndex(); //pobieranie indeksu wybranego użytkownika
	       
    	   if (selected_index >= 0 && selected_index < users.size()) {
    		   User selected_user = users.get(selected_index); // pobieranie użytkownika według indeksu
               double averageTempC = Temperaturecalc.AverageTemp(selected_user.getTemperaturesCelsius());
               double averageTempF = Temperaturecalc.AverageTemp(selected_user.getTemperaturesFahrenheit());
               averageCelsTempLabel.setText("Średnia temperatura: " + averageTempC + " °C");
               averageFehrTempLabel.setText("Średnia temperatura: " + averageTempF + " °F");
           } else {
               JOptionPane.showMessageDialog(this, "Wybierz użytkownika", "Błąd", JOptionPane.ERROR_MESSAGE);
           }
       }
    }

    class saveToDBButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Connection connect = null;
            PreparedStatement prepStatement = null;

            try {
                Class.forName("org.sqlite.JDBC");
                connect = DriverManager.getConnection("jdbc:sqlite:temperatury.db");

                // Sprawdzanie istnienia tabeli i jej tworzenie w przypadku braku
                createTable(connect);

                // SQL zapytanie do wstawiania danych
                String insert_query = "INSERT INTO uzytkowniki (nazwisko, imie, temperaturaCelsjusz, temperaturaFahrenheit, dataCzas) VALUES (?, ?, ?, ?, ?)";
                prepStatement = connect.prepareStatement(insert_query);

                // Pobieranie modelu danych tabeli
                TableModel table_model = usersTable.getModel();

                for (int i = 0; i < table_model.getRowCount(); i++) {
                    
                    int actualRowNum = usersTable.convertRowIndexToModel(i);//rzeczywisty indeks wiersza w modelu danych
                    String lastName = (String) table_model.getValueAt(actualRowNum, 1);
                    String firstName = (String) table_model.getValueAt(actualRowNum, 2);
                    double temperatureCelsius = (double) table_model.getValueAt(actualRowNum, 3);
                    double temperatureFahrenheit = (double) table_model.getValueAt(actualRowNum, 4);
                    String dateTime = (String) table_model.getValueAt(actualRowNum, 5);
                    
                    // Ustawianie wartości parametrów
                    prepStatement.setString(1, lastName);
                    prepStatement.setString(2, firstName);
                    prepStatement.setDouble(3, temperatureCelsius);
                    prepStatement.setDouble(4, temperatureFahrenheit);
                    prepStatement.setString(5, dateTime);

                    // Wykonanie zapytania
                    prepStatement.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Wszystkie dane zapisane do bazy danych.");
            } catch (ClassNotFoundException | SQLException exp) {
                exp.printStackTrace();
                JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas zapisu do bazy danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
            } finally {
            	//bezpieczne zamykanie interfejsów interakcji z bazą danych
                try {
                    if (prepStatement != null) {
                    	prepStatement.close();
                    }
                    if (connect != null) {
                    	connect.close();
                    }
                } catch (SQLException exp) {
                	JOptionPane.showMessageDialog(null, "Wystąpił wyjątek SQLException: " + exp.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // Funkcja tworzenia tabeli
        private void createTable(Connection connection) throws SQLException {
            String create_query = "CREATE TABLE IF NOT EXISTS uzytkowniki (" 
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT," 
                    + "nazwisko TEXT NOT NULL,"
                    + "imie TEXT NOT NULL,"
                    + "temperaturaCelsjusz DOUBLE NOT NULL,"
                    + "temperaturaFahrenheit DOUBLE NOT NULL,"
                    + "dataCzas TEXT NOT NULL)";
            try (Statement stat = connection.createStatement()) {
                stat.executeUpdate(create_query);
            }
        }
    }
    
    // metody weryfikacji wprowadzanych danych	
    private boolean validateNameInput(String nameInput) {
    	if(nameInput.matches("[\\p{L}'\\- ]+") && nameInput.trim().length() >= 2) {
	    return false;
	    } else {
	    return true;
	    }	
	}
    private boolean validateTempInput(double temp) {
    	if(temp <= 0) {
	    return true;
	    } else {
	    return false;
	    }
    }
}		
    

                     


