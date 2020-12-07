public class GuiZoomLoader {

    static GuiSpieltisch st  = Main.spieltischGui;

    /**
     * Lädt die SVG-Grafiken mit einem Zoom-Faktor neu, sodass
     * diese immer scharf aussehen
     */
    static void getZoomedImages() {


        double factor;
        if (Main.sceneWidth / Main.sceneHeight > 1.5) {
            factor = Main.sceneHeight / 400;
        } else
            factor = Main.sceneWidth / 600;


        factor = Double.min(factor, 2);
        Main.zoomfactor = factor;
        //System.out.println(factor);

        Main.image = Main.loader.getImg("images/SVG/Back.svg", factor);
        Main.card1 = Main.loader.getImg("images/SVG/Card1.svg", factor);
        Main.card2 = Main.loader.getImg("images/SVG/Card2.svg", factor);
        Main.card3 = Main.loader.getImg("images/SVG/Card3.svg", factor);
        Main.card4 = Main.loader.getImg("images/SVG/Card4.svg", factor);
        Main.card5 = Main.loader.getImg("images/SVG/Card5.svg", factor);
        Main.card6 = Main.loader.getImg("images/SVG/Card6.svg", factor);
        Main.lama = Main.loader.getImg("images/SVG/Lama.svg", factor);
        Main.table1 = Main.loader.getImg("images/table2.svg", factor);
    }



}
