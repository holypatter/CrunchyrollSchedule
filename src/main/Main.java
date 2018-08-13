package main;

import auth.CrunchyrollSessionId;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Series;
import model.SeriesManager;
import parser.SeriesParser;
import providers.HttpSeriesProvider;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Main extends Application {
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 700;
    private static final String SESS_ID_NAME = "sess_id";



    @Override
    public void start(Stage primaryStage) throws Exception{
        startingStage(primaryStage);
    }

    private void startingStage(Stage primaryStage) {
        VBox root = new VBox(8);

        for (Series s : SeriesManager.getInstance()) {
            addSeries(root, s);
        }

        ScrollPane sp = new ScrollPane();
        sp.setContent(root);

        primaryStage.setTitle("Crunchyroll Simulcasts");
        Scene scene = new Scene(sp, WINDOW_WIDTH, WINDOW_HEIGHT);

        scene.setFill(Color.ORANGE);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addSeries(VBox root, Series s) {
        Image image = new Image(s.getImgLargeURL());
        ImageView imageView = new ImageView(image);
        String name = s.getName();
        Text text = new Text(name);
        text.setTranslateX(WINDOW_WIDTH/4);
        Button imageButton = new Button("", imageView);
        imageButton.setTranslateX(WINDOW_WIDTH/4);
        imageButton.setOnAction((event) -> {
            // parse new media data and go into new scene
            generateMedia(s);
        });

        root.getChildren().add(imageButton);
        root.getChildren().add(text);
        root.setAlignment(Pos.CENTER);
    }


    private void generateMedia(Series s) {
        //parse data asynchronously
        Runnable task = () -> runTask(s);

        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void runTask(Series series) {
        Platform.runLater(() -> {
//                MediaParser.parseAllMedia();
        });
    }


    /**
     * Starts new session on Crunchyroll, downloads and parse series data, launch java fx
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        retrieveSessId();
        downloadSeriesData();

        launch(args);
    }


    /**
     * Downloads real time data from current session and parsing the data into SeriesManager
     *
     * @throws Exception
     */
    private static void downloadSeriesData() throws Exception {
        HttpSeriesProvider website = new HttpSeriesProvider();
        String data = website.dataSourceToString();

        SeriesParser.parseAllSeries(data);
    }

    /**
     * Retrieves sess_id from crunchyroll's cookies
     *
     * @throws IOException
     */
    private static void retrieveSessId() throws IOException {
        // establing connection
        URLConnection connection = new URL("http://crunchyroll.com").openConnection();
        Map<String, List<String>> headers = connection.getHeaderFields();
        List<String> values = headers.get("Set-Cookie");

        String sessionID;

        // for each cookie, if the key is "sess_id", store its value into sessionID
        for(Iterator iterator = values.iterator(); iterator.hasNext();) {
            sessionID = getSessId(iterator.next().toString());
            if (!sessionID.equals("")) {
                CrunchyrollSessionId.getInstance().setID(sessionID);
                break;
            }
        }
    }


    /**
     * @param cookie  the cookie to check
     * @return        the session id if it is the right cookie
     */
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
