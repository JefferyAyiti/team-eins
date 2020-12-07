import SVG.TestLoadImageUsingClass;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;


public class Main extends Application {


    static Hand[] haende;
    static Spieler[] spieler;
    static TestLoadImageUsingClass loader;
    static double sceneWidth = 600;
    static double sceneHeight = 400;
    static volatile Tisch tisch;
    static Spiellogik spiellogik;
    static double zoomfactor = 1;
    static volatile long resize = 0;
    static int ich = 0;
    static long botPlayTime = 0;
    static int botlevel = 0;
    static Stage classPrimaryStage;

    static String myName;
    static int playMode = 0;



    //Wird später im Menü festgelegt
    static int anzSpieler;


    public static void main(String[] args) {
        loader = new TestLoadImageUsingClass();
        loader.installSvgLoader();
        try {
            initGame();

        } catch (Exception e) {

        }
        launch(args);

    }


    static Image table1;
    static Image image;
    static Image card1;
    static Image card3;
    static Image card4;
    static Image card2;
    static Image card5;
    static Image card6;
    static Image lama;
    static Image blackChipImage;
    static Image whiteChipImage;

    static Image[] cardsArray;



    static GuiHauptmenu hauptmenuGui = new GuiHauptmenu();
    static GuiScoreboard scoreboardGui = new GuiScoreboard();
    static GuiSpieltisch spieltischGui = new GuiSpieltisch();

    /**
     * Erstellt Tisch, Nachzieh- und Abalgestapel, Spieler
     * und startet die erste Runde im Spiel
     */
    static void initGame() {

        //initialisiere Spieler mit handkarten
        haende = new Hand[anzSpieler];
        spieler = new Spieler[anzSpieler];

        //spieler[0]= new Bot("Spieler",2);
        spieler[0] = new Spieler(myName);
        int level;
        String[] botname = {"EZ-", "Mid-", "Hard-"};
        for (int i = 1; i < anzSpieler; i++) {
            level = botlevel == 0 ? (int) (Math.random() * 3 + 1) : botlevel;
            System.out.println(level);
            spieler[i] = new Bot(botname[level - 1] + "Bot " + (i + 1), level);

        }
        tisch = new Tisch(spieler);
        spiellogik = new Spiellogik(tisch);
        spiellogik.initNeueRunde();


    }


    //GUI


    public static void resize() {

        if (System.currentTimeMillis() < resize + 500) {
            GuiZoomLoader.getZoomedImages();
            spieltischGui.buildStage(classPrimaryStage);
        }
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("L.A.M.A - Team Eins");

        table1 = new Image("images/table2.svg");
        classPrimaryStage = primaryStage;

        Main.image = new Image("/images/SVG/Back.svg");
        Main.card1 = new Image("/images/SVG/Card1.svg");
        Main.card3 = new Image("images/SVG/Card3.svg");
        Main.card4 = new Image("images/SVG/Card4.svg");
        Main.card2 = new Image("images/SVG/Card2.svg");
        Main.card5 = new Image("images/SVG/Card5.svg");
        Main.card6 = new Image("images/SVG/Card6.svg");
        Main.lama = new Image("images/SVG/Lama.svg");

        Main.blackChipImage = new Image("/images/SVG/blackChip.svg");
        Main.whiteChipImage = new Image("/images/SVG/whiteChip.svg");
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
                resize();
            });

        }
    }

    static long lastmove = 0;

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
    static Timer bots;
    static Timer resizecheck;

    static void runTimers(Stage ps) {
        resizecheck = new Timer();
        resizecheck.schedule(new MyTask1(), 3000, 500);

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
