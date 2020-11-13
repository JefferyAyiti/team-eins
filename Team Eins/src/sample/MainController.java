package sample;


import javafx.embed.swing.SwingFXUtils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    ImageView backImg = new ImageView();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();

        try(InputStream file = getClass().getResourceAsStream("/cards/SVG/Back.svg")){
            TranscoderInput transIn = new TranscoderInput(file);
            try {
                transcoder.transcode(transIn, null);
                Image img = SwingFXUtils.toFXImage(transcoder.getBufferedImage(), null);
                backImg.setImage(img);
            } catch (TranscoderException ex) {
                ex.printStackTrace();
            }
        }
        catch(
                IOException io){ io.printStackTrace();
        }
    }
}
