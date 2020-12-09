package Main;

import GUI.GuiHauptmenu;
import GUI.GuiScoreboard;
import GUI.GuiSpieltisch;
import GUI.GuiZoomLoader;
import GUI.SVG.TestLoadImageUsingClass;
import RMI.server;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class Main extends Application {


    public static Hand[] haende;
    public static Spieler[] spieler;
    public static TestLoadImageUsingClass loader;
    public static double sceneWidth = 600;
    public static double sceneHeight = 400;
    public static volatile Tisch tisch;
    public static Spiellogik spiellogik;
    public static double zoomfactor = 1;
    public static volatile long resize = 0;
    public static int ich = 0;
    public static long botPlayTime = 0;
    public static int botlevel = 0;
    public static Stage classPrimaryStage;
    public static boolean joined;
    public static String myName;
    public static int playMode = 0;
    public static boolean inMenu = true;
    public static RMI.server server = null;
    public static String uniqueID = UUID.randomUUID().toString();
    public static boolean gameRunning = false;



    //Wird später im Menü festgelegt
    public static int anzSpieler;


    public static void main(String[] args) {
        System.out.println(uniqueID);
        loader = new TestLoadImageUsingClass();
        loader.installSvgLoader();
        launch(args);

    }


    public static Image table1;
    public static Image image;
    public static Image card1;
    public static Image card3;
    public static Image card4;
    public static Image card2;
    public static Image card5;
    public static Image card6;
    public static Image lama;
    public static Image blackChipImage;
    public static Image whiteChipImage;

    public static Image[] cardsArray;



    public static GuiHauptmenu hauptmenuGui = new GuiHauptmenu();
    public static GuiScoreboard scoreboardGui = new GuiScoreboard();
    public static GuiSpieltisch spieltischGui = new GuiSpieltisch();

    /**
     * Erstellt Tisch, Nachzieh- und Abalgestapel, Spieler
     * und startet die erste Runde im Spiel
     */
    public static void initGame() {

        //initialisiere Spieler mit handkarten
        haende = new Hand[anzSpieler];
        spieler = new Spieler[anzSpieler];

        //spieler[0]= new Bot("Spieler",2);

        int i = -1;
        if(server != null) {

            try {
                for (Map.Entry<String, String> entry : server.getClients().entrySet()) {
                    i++;
                    spieler[i] = new Spieler(entry.getValue());
                    System.out.println(i+" "+entry.getValue()+" erzeugt");
                }

            } catch (Exception e) {
            }
        }
        if(i == -1) {
            spieler[0] = new Spieler(myName);
            System.out.println("Main erzeugt");
            i=0;
        }
        int level;
        String[] botname = {"EZ-", "Mid-", "Hard-"};
        for (i++; i < anzSpieler; i++) {
            level = botlevel == 0 ? (int) (Math.random() * 3 + 1) : botlevel;
            spieler[i] = new Bot(botname[level - 1] + "Bot " + (i + 1), level);

        }
        tisch = new Tisch(spieler);
        spiellogik = new Spiellogik(tisch);
        spiellogik.initNeueRunde();

        resize(true);
    }


    //GUI


    public static void resize(boolean init) {

        if (System.currentTimeMillis() < resize + 500 || init) {
            GuiZoomLoader.getZoomedImages();
            spieltischGui.buildStage(classPrimaryStage);
        }
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("L.A.M.A - Team Eins");

        table1 = new Image("GUI/images/table2.svg");
        classPrimaryStage = primaryStage;

        //Main.image = new Image("/GUI.images/GUI.SVG/Back.svg");
        Main.image = loader.getImg("GUI/images/SVG/Back.svg", 1);
        Main.card1 = new Image("/GUI/images/SVG/Card1.svg");
        Main.card3 = new Image("GUI/images/SVG/Card3.svg");
        Main.card4 = new Image("GUI/images/SVG/Card4.svg");
        Main.card2 = new Image("GUI/images/SVG/Card2.svg");
        Main.card5 = new Image("GUI/images/SVG/Card5.svg");
        Main.card6 = new Image("GUI/images/SVG/Card6.svg");
        Main.lama = new Image("GUI/images/SVG/Lama.svg");

        Main.blackChipImage = new Image("/GUI/images/SVG/blackChip.svg");
        Main.whiteChipImage = new Image("/GUI/images/SVG/whiteChip.svg");
        Main.cardsArray = new Image[]{Main.card1, Main.card2, Main.card3, Main.card4, Main.card5,
                Main.card6, null, null, null, Main.lama};


        hauptmenuGui.showSettingsMenu(classPrimaryStage);


    }






    static class MyTask1 extends TimerTask {
        @Override
        public void run() {
            sceneWidth = classPrimaryStage.getScene().getWidth();
            sceneHeight = classPrimaryStage.getScene().getHeight();
            Platform.runLater(() -> {
                resize(false);
            });

        }
    }

    public static long lastmove = 0;

    static class moveCheck extends TimerTask {
        @Override
        public void run() {
            if (tisch.getAktivSpieler() instanceof Bot && !spiellogik.getRundeBeendet()) {
                if (System.currentTimeMillis() - lastmove < botPlayTime) {
                    try {
                        Thread.sleep(botPlayTime - (System.currentTimeMillis() - lastmove));
                    } catch (InterruptedException e) {
                    }
                }
                System.out.println("Spieler '" + tisch.getAktivSpieler().getName() + "' ist dran:");
                ((Bot) tisch.getAktivSpieler()).play();
                Platform.runLater(() -> {
                    spieltischGui.buildStage(classPrimaryStage);
                });
                lastmove = System.currentTimeMillis();


            }
        }
    }

    /**
     * Startet die Timer für Bots und Resize-Check, sobald das Hauptmenü verlassen wurde
     *
     * @param ps Stage
     */
    public static Timer bots;
    public static Timer resizecheck;

    public static void runTimers(Stage ps) {
        resize(true);
        resizecheck = new Timer();
        resizecheck.schedule(new MyTask1(), 0, 500);

        bots = new Timer();
        bots.schedule(new moveCheck(), botPlayTime * 2, botPlayTime);

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
        {
            resize = System.currentTimeMillis();

        };


        ps.widthProperty().addListener(stageSizeListener);
        ps.heightProperty().addListener(stageSizeListener);

        ps.setOnCloseRequest(windowEvent -> {
            bots.cancel();
            resizecheck.cancel();
        });
    }

}
