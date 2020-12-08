package GUI.SVG;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.InputStream;

public class TestLoadImageUsingClass {


    public static void installSvgLoader() {
        SvgImageLoaderFactory.install();
    }


    public Image getImg(String path) {

        return getImg(path, 1);
    }

    public Image getImg(String path, double factor) {

        InputStream imageData = this.getClass().getClassLoader()
                .getResourceAsStream(path);


        Image image;
        if (!path.contains("table")) {
            if (!path.contains("Back"))
                image = new Image(imageData, 80 * factor, 80*1.5 * factor, true, true);
            else
                image = new Image(imageData, 40 * factor, 40*1.5 * factor, true, false);
        } else
            image = new Image(imageData);
        return image;
    }


}
