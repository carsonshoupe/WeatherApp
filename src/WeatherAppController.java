import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class WeatherAppController implements Initializable {
	@FXML TextField cityTF1;
	@FXML TextField cityTF2;
	@FXML TextArea weatherTA1;
	@FXML TextArea weatherTA2;
	@FXML ProgressIndicator progressIndicator1;
	@FXML ProgressIndicator progressIndicator2;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cityTF1.setOnKeyPressed(keyEvent -> {
	        if (keyEvent.getCode() == KeyCode.ENTER)  {
	            getWeatherClick();
	        }
		});
		cityTF2.setOnKeyPressed(keyEvent -> {
	        if (keyEvent.getCode() == KeyCode.ENTER)  {
	            getWeatherClick();
	        }
		});
	}
	
	@FXML public void getWeatherClick() {
		weatherTA1.clear();
		weatherTA2.clear();
		progressIndicator1.setVisible(true);
		progressIndicator2.setVisible(true);
		progressIndicator1.setProgress(0);
		progressIndicator2.setProgress(0);
		
		WeatherCall wc1 = new WeatherCall(this.cityTF1.getText());
		WeatherCall wc2 = new WeatherCall(this.cityTF2.getText());
		
		Thread t1 = new Thread(wc1);
		Thread t2 = new Thread(wc2);
		
		t1.start();
		t2.start();
		
		progressIndicator1.setProgress(.5);
		progressIndicator2.setProgress(.5);
		
		Thread updateUserInterface1 = new Thread( () -> {
			while (!wc1.getIsReady()) {
				try {
					System.out.println("waiting on wc1...");
					synchronized(wc1) {wc1.wait();}
				} catch (InterruptedException ie) {}
			}
			System.out.println("wc1 finally ready!");
			progressIndicator1.setProgress(1);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
			
			progressIndicator1.setVisible(false);
			weatherTA1.setText(wc1.getWeather());
		});
		updateUserInterface1.start();
		
		Thread updateUserInterface2 = new Thread( () -> {
			while (!wc2.getIsReady()) {
				try {
					System.out.println("Waiting on wc2...");
					synchronized(wc2) {wc2.wait();}
				} catch (InterruptedException ie) {}
			}
			System.out.println("wc2 finally ready!");
			progressIndicator2.setProgress(1);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
			
			progressIndicator2.setVisible(false);
			weatherTA2.setText(wc2.getWeather());
		});
		updateUserInterface2.start();
	}
	
	
	

}
