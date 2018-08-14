package main;

import auth.CrunchyrollSessionId;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.EpisodeManager;
import model.Series;
import model.SeriesManager;
import org.json.JSONException;
import parser.MediaParser;
import parser.SeriesParser;
import parser.exceptions.AllMissingDataException;
import providers.HttpMediaProvider;
import providers.HttpSeriesProvider;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Main extends Application {
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 700;
    private static final String SESS_ID_NAME = "sess_id";
    private static final int LARGE_FONT_SIZE = 20;

    @Override
    public void start(Stage primaryStage) throws Exception{
        startingStage(primaryStage);
    }

    /**
     * initializing starting scene
     *
     * @param primaryStage    primary stage
     */
    private void startingStage(Stage primaryStage) {
        VBox root = new VBox(8);

        for (Series series : SeriesManager.getInstance()) {
            setUpSeries(primaryStage, root, series);
        }

        ScrollPane sp = new ScrollPane();
        sp.setContent(root);

        primaryStage.setTitle("Crunchyroll Simulcasts");
        final Scene mainScene = new Scene(sp, WINDOW_WIDTH, WINDOW_HEIGHT);

        mainScene.setFill(Color.ORANGE);

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    /**
     * adding each series to the scene
     *
     * @param primaryStage    primary stage
     * @param root            scene to add series to
     * @param series          series to add
     */
    private void setUpSeries(Stage primaryStage, VBox root, Series series) {
        Image largeImg = new Image(series.getImgLargeURL());
        ImageView largeImgView = new ImageView(largeImg);
        String name = series.getName();
        Text text = new Text(name);
        text.setTranslateX(WINDOW_WIDTH/4);
        Button imageButton = new Button("", largeImgView);
        imageButton.setTranslateX(WINDOW_WIDTH/4);
        imageButton.setOnAction((event) -> {
            // parse new media data
            generateMedia(series);
            // set new scene for stage
            setNewScene(primaryStage, series);
        });

        root.getChildren().add(imageButton);
        root.getChildren().add(text);
        root.setAlignment(Pos.CENTER);
    }

    /**
     * sets up a new scene when user clicks on the button for a series
     *
     * @param primaryStage  primary stage
     * @param series        series user clicked on
     */
    private void setNewScene(Stage primaryStage, Series series) {
        HBox topPart = addingTopSection(series);
        VBox vBox = addingBottomPartToTopPart(primaryStage, series, topPart);

        final Scene sideScene = new Scene(vBox, WINDOW_WIDTH+500, WINDOW_HEIGHT);

        primaryStage.setScene(sideScene);
        primaryStage.setX(WINDOW_WIDTH-400);
        primaryStage.show();
    }

    /**
     * Completing the layout of the scene
     *
     * @param primaryStage    primary stage
     * @param series          current series
     * @param topPart         part to add onto
     * @return                the completed node
     */
    private VBox addingBottomPartToTopPart(Stage primaryStage, Series series, HBox topPart) {
        VBox vBox = new VBox(20);
        Label desc = new Label(series.getDescription());
        desc.setFont(new Font(15));
        desc.setWrapText(true);
        Button back = new Button("Back");
        back.setOnAction((event1) -> {
//            startingStage(primaryStage);
            returnToMain(primaryStage);
        });
        vBox.getChildren().add(topPart);
        vBox.getChildren().add(desc);
        vBox.getChildren().add(back);
        return vBox;
    }

    private void returnToMain(Stage primaryStage) {
        //parse data asynchronously
        Runnable task = () -> runTask(primaryStage);

        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void runTask(Stage stage) {
        Platform.runLater(() -> {
            startingStage(stage);
        });
    }

    /**
     * Completes layout for the top section of the scene
     *
     * @param series    current series
     * @return          top section as a node
     */
    private HBox addingTopSection(Series series) {
        String seriesID = series.getId();
        HBox topPart = new HBox(20);
        Image fullImg = new Image(series.getImgFullURL());
        ImageView fullImgView = new ImageView(fullImg);
        // assuming the next episode will be released a week after
        String[]parts = EpisodeManager.getInstance().getLatestEpisode(seriesID).getPremiumAvailableTime().split("T|-");
        String premTime = parts[0] + "-" + parts[1] + "-" + parts[2] + " " + parts[3];
        Timestamp ts = Timestamp.valueOf(premTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ts.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, +7);


        Text epNum = new Text("Current Episode: " +
                EpisodeManager.getInstance().getLatestEpisode(seriesID).getEpNum() //instance is null here if parse data as a background thread
                + "\n\n\n" + EpisodeManager.getInstance().getLatestEpisode(seriesID).getName() +
                "\n\n\n\n\n" + "Next episode will be released on: " +
                "\n\n\n" + calendar.getTime());
        epNum.setFont(new Font(LARGE_FONT_SIZE));
        topPart.getChildren().add(fullImgView);
        topPart.getChildren().add(epNum);
        return topPart;
    }


    //tried parsing data asynchronously but turns out EpisodeManager is not actually instantiated
    // because it exits in a different thread
    // Solution can be to return the parsed data as a list

    /**
     * parses data from crunchyroll api into EpisodeManager
     *
     * @param series   series for the episode
     */
    private void generateMedia(Series series) {
//        //parse data asynchronously
//        Runnable task = () -> runTask(s);
//
//        Thread backgroundThread = new Thread(task);
//        backgroundThread.setDaemon(true);
//        backgroundThread.start();
        HttpMediaProvider website = new HttpMediaProvider();
        website.setSeriesId(series.getId());
        try {
            String data = website.dataSourceToString();
            MediaParser.parseAllMedia(data, series);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (AllMissingDataException e) {
            e.printStackTrace();
        }
        if (EpisodeManager.getInstance().getLatestEpisode(series.getId()).getName() != "") {
            System.out.println("successfully parsed data");
        }
    }

//    private void runTask(Series series) {
//        Platform.runLater(() -> {
//            HttpMediaProvider website = new HttpMediaProvider();
//            website.setSeriesId(series.getId());
//            try {
//                String data = website.dataSourceToString();
//                MediaParser.parseAllMedia(data, series);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (AllMissingDataException e) {
//                e.printStackTrace();
//            }
//            if (EpisodeManager.getInstance().getLatestEpisode(series.getId()).getName() != "") {
//                System.out.println("successfully parsed data");
//            }
//        });
//    }


    /**
     * Starts new session on Crunchyroll, downloads and parse series data, launch java fx
     */
    public static void main(String[] args) throws Exception {
        retrieveSessId();
        downloadSeriesData();

        launch(args);
    }


    /**
     * Downloads real time data from current session and parsing the data into SeriesManager
     */
    private static void downloadSeriesData() throws Exception {
        HttpSeriesProvider website = new HttpSeriesProvider();
        String data = website.dataSourceToString();

        SeriesParser.parseAllSeries(data);
    }

    /**
     * Retrieves sess_id from crunchyroll's cookies
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
            System.out.println("Your session id: " + testID);
            return testID;
        }
        return "";
    }
}
