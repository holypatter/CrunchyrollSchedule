package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Main extends Application {
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 500;
    private static final String SESS_ID_NAME = "sess_id";

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Crunchyroll Simulcasts");
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setFill(Color.ORANGE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) throws Exception {
        // establing connection
        URLConnection connection = new URL("http://crunchyroll.com").openConnection();
        Map<String, List<String>> headers = connection.getHeaderFields();
        List<String> values = headers.get("Set-Cookie");

        String sessionid;

        // for each cookie, if the key is "sess_id", store its value into sessionid
        for(Iterator iterator = values.iterator(); iterator.hasNext();) {
            sessionid = getSessId(iterator.next().toString());
            if (!sessionid.equals("")) {
                break;
            }
        }

        launch(args);

    }

    public static String getSessId(String cookie) {
        if (cookie.contains(SESS_ID_NAME)) {
            int index = cookie.indexOf(SESS_ID_NAME);
            String testID = cookie.substring(index+SESS_ID_NAME.length()+1, cookie.indexOf(";", index));
            System.out.println(testID);
            return testID;
        }
        return "";
    }

    private class DownloadSimulcastSeries {

    }

    private class DownloadMediaForSeries {

    }
}
