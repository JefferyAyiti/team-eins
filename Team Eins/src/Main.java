import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class Main extends Application implements EventHandler {

    private static int anzSpieler= (int) 2 + (int)(Math.random() * ((6 - 2) + 1));


    public static void main(String[] args)  {
        try {
            initGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //launch(args);
    }

    private static void initGame() throws Exception {
        //initialisiere GrundTisch
        Tisch tisch = new Tisch(anzSpieler);
        tisch.initNachziehstapel();
        tisch.mischenNachziehstapel();

        //initialisiere Spieler mit handkarten
        Hand hand1 = new Hand();
        Hand hand2 = new Hand();
        Hand hand3 = new Hand();
        Hand hand4 = new Hand();
        Hand hand5 = new Hand();
        Hand hand6 = new Hand();

        Spieler spieler1 = new Spieler(hand1, "Spieler1", tisch);
        Spieler spieler2 = new Spieler(hand2, "Spieler2", tisch);
        Spieler spieler3 = new Spieler(hand3, "Spieler3", tisch);
        Spieler spieler4 = new Spieler(hand4, "Spieler4", tisch);
        Spieler spieler5 = new Spieler(hand5, "Spieler5", tisch);
        Spieler spieler6 = new Spieler(hand6, "Spieler6", tisch);

        initSpieler(spieler1, hand1, tisch);
        initSpieler(spieler2, hand2, tisch);
        initSpieler(spieler3, hand3, tisch);
        initSpieler(spieler4, hand4, tisch);
        initSpieler(spieler5, hand5, tisch);
        initSpieler(spieler6, hand6, tisch);


        //gebe jeden Spieler (anzSpieler) 6 Karten in Reihenfolge
        //Außerdem werden Spieler die mitspielen als isplaying markiert.
        for(int i = 0; i<6; i++){
            switch (anzSpieler){
                case 2:
                    hand1.addKarte(tisch.karteZiehen());
                    hand2.addKarte(tisch.karteZiehen());

                    spieler1.setisPlaying(true);
                    spieler2.setisPlaying(true);
                    break;
                case 3:
                    hand1.addKarte(tisch.karteZiehen());
                    hand2.addKarte(tisch.karteZiehen());
                    hand3.addKarte(tisch.karteZiehen());

                    spieler1.setisPlaying(true);
                    spieler2.setisPlaying(true);
                    spieler3.setisPlaying(true);
                    break;
                case 4:
                    hand1.addKarte(tisch.karteZiehen());
                    hand2.addKarte(tisch.karteZiehen());
                    hand3.addKarte(tisch.karteZiehen());
                    hand4.addKarte(tisch.karteZiehen());

                    spieler1.setisPlaying(true);
                    spieler2.setisPlaying(true);
                    spieler3.setisPlaying(true);
                    spieler4.setisPlaying(true);
                    break;
                case 5:
                    hand1.addKarte(tisch.karteZiehen());
                    hand2.addKarte(tisch.karteZiehen());
                    hand3.addKarte(tisch.karteZiehen());
                    hand4.addKarte(tisch.karteZiehen());
                    hand5.addKarte(tisch.karteZiehen());

                    spieler1.setisPlaying(true);
                    spieler2.setisPlaying(true);
                    spieler3.setisPlaying(true);
                    spieler4.setisPlaying(true);
                    spieler5.setisPlaying(true);
                    break;
                case 6:
                    hand1.addKarte(tisch.karteZiehen());
                    hand2.addKarte(tisch.karteZiehen());
                    hand3.addKarte(tisch.karteZiehen());
                    hand4.addKarte(tisch.karteZiehen());
                    hand5.addKarte(tisch.karteZiehen());
                    hand6.addKarte(tisch.karteZiehen());

                    spieler1.setisPlaying(true);
                    spieler2.setisPlaying(true);
                    spieler3.setisPlaying(true);
                    spieler4.setisPlaying(true);
                    spieler5.setisPlaying(true);
                    spieler6.setisPlaying(true);
                    break;
            }

        }

        tisch.karteAblegen(tisch.karteZiehen()); //Ablagestapel
    }

    private static void initSpieler(Spieler spieler, Hand hand, Tisch tisch) {
        hand.setSpieler(spieler);
        hand.setTisch(tisch);
    }


    @Override
    public void start(Stage stage) throws Exception {

    }

    @Override
    public void handle(Event event) {

    }
}
