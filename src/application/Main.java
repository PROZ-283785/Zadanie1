package application;
	
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import javax.tools.JavaFileObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		Optional<ButtonType> result = (new LogonDialog("Logowanie",  "Logowanie do systemu STYLEman")).showAndWait();
		result.ifPresent(response -> {
			if(response.getButtonData() == ButtonData.CANCEL_CLOSE) {
				System.out.println("Zamknieto");
			}
			
		});

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}


class LogonDialog{
	
	private String title;
	private String welcomeMassage;
	
	private Dialog<ButtonType> dialog = new Dialog<ButtonType>();
	private GridPane grid = new GridPane();
	private ChoiceBox<String> environment = new ChoiceBox<String>();
	private ComboBox<String> user = new ComboBox<String>();
	private PasswordField pass = new PasswordField();
	
	private ButtonType loginButton = new ButtonType("Logon", ButtonData.OK_DONE);
	private ButtonType cancelButton = new ButtonType("Anuluj", ButtonData.CANCEL_CLOSE);
	private Node logButton;
	
	
	private static int boxWidth = 200;
	
	LogonDialog(String _title, String _welcomeMassage) {
		title = _title;
		welcomeMassage = _welcomeMassage;
		initialize();
	}
	
	public void setLogButton(Boolean value) {
		logButton = dialog.getDialogPane().lookupButton(loginButton);
		logButton.setDisable(value);
	}
	
	
	private void setDialog() {
		dialog.setTitle(title);
		dialog.setHeaderText(welcomeMassage);
		dialog.getDialogPane().getButtonTypes().addAll(loginButton, cancelButton);
	}
	
	
	private void setGrid() {
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 20, 20));
		grid.add(new Label("Œrodowisko: "), 0, 0);
		grid.add(environment, 1, 0);
		grid.add(new Label("U¿ytkownicy: "), 0, 1);
		grid.add(user, 1, 1);
		grid.add(new Label("Has³o: "), 0, 2);
		grid.add(pass, 1, 2);
	}
	
	
	
	private ObservableList<String> getData(String key){
		
		JSONParser parser = new JSONParser();  
		JSONArray arr = new JSONArray();
		try {
			Object object = parser.parse(new FileReader("database.txt"));
			 
			JSONObject jsonObject = (JSONObject) object;
			arr = (JSONArray) jsonObject.get(key);
		
		}  catch (IOException e1) {
			
			e1.printStackTrace();
		}
			catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		
		ObservableList<String> values = FXCollections.observableArrayList();
		for(int i = 0; i < arr.size();  ++i)
		{
			values.add((String) arr.get(i));
		}
		return values;
	}
	
	private void setEnvironment() {
		
		environment.setMinWidth(boxWidth);
		
		ObservableList<String> values = getData("Srodowisko");
		
		environment.setItems(values);
		
		environment.valueProperty().addListener((observable, oldVal, newVal) -> env_Changed(newVal));
		
	}
	
	
	
	private void checkIfAllDataSet() {
		boolean envNotSet = environment.getSelectionModel().isEmpty();	
		boolean usSet = user.getValue() != null && !user.getValue().equals("");
		boolean pNotSet = pass.getText().equals("");
		boolean isSet = !envNotSet && usSet && !pNotSet;  
		//System.out.printf("%s %s %s %s %s\n", value ? "true" : " false", env ? "true" : " false", us ? "true" : " false",p ? "true": "false" ,user.getValue());
		if(isSet) {
			setLogButton(false);
		}
		else
		{
			setLogButton(true);
		}
	}
	
	
	
	private void env_Changed(String newVal) {
		
		ObservableList<String> values = getData(newVal);
		user.setItems(values);
		
		checkIfAllDataSet();
	}
	
	
	private void pass_Changed(String newVal) {
		checkIfAllDataSet();		
		
	}
	
	private void user_Changed(String newVal) {
		checkIfAllDataSet();
		
	}
	
		
	private void setUser() {
		user.setMinWidth(boxWidth);
		
		user.setEditable(true);
		user.valueProperty().addListener((observable ,oldVal, newVal)->user_Changed(newVal));
	}
	
	private void setPass() {
		pass.setMinWidth(boxWidth);
		pass.textProperty().addListener((observable ,oldVal, newVal)->pass_Changed(newVal));
	}
	
	private void initialize() {
		
		
		
		/*Image image = null;
		try {
			 image = new Image("man.png");
		}
		catch(NullPointerException e) {
			try {
			
				FileWriter file = new FileWriter("kek.txt");
				file.write("siemsson");
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			System.out.println("kek");
		}
		
		ImageView imageView = new ImageView(image); 
		dialog.setGraphic(imageView);*/
	/*	JSONObject obj = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			
			
			
			array.add("Jan.Kowalski");
			array.add("Kacper.Wnuk");
			
			obj.put("Produkcyjne", array);
			array = new JSONArray();
			
			array.add("Andrzej.Swatowksi");
			array.add("Arek.Sikorski");
			
			obj.put("Testowe", array);
			array = new JSONArray();
			
			array.add("Michal.Tepper");
			array.add("Adam.Sobieski");
			
			obj.put("Deweloperskie", array);
			array = new JSONArray();
			
			array.add("Produkcyjne");
			array.add("Testowe");
			array.add("Deweloperskie");
			
			obj.put("Srodowisko", array);
			
			
			FileWriter file = new FileWriter("kek.txt");
			file.write(obj.toJSONString());
		
			file.close();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
		*/
		setDialog();
		setEnvironment();
		setUser();
		setPass();
		setLogButton(true);
		setGrid();
		dialog.getDialogPane().setContent(grid);
	}
	
	
	private boolean isPassCorrect(String env, String usr, String passwd) {
		
		ObservableList<String> users = getData(env);
		
		return users.contains(usr) ? true : false;
	}
	
	private String resultConverter(ButtonType buttonType) {
		
		if(buttonType == loginButton) {
			if(isPassCorrect(environment.getValue(), user.getValue(), pass.getText())) {
			
				return pass.getText();
			}
		}
		
		
		return null;
	}
	
	public  Optional<ButtonType> showAndWait() {
		return dialog.showAndWait()
	}

}









