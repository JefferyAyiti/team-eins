package sample;


import javafx.embed.swing.SwingFXUtils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import javafx.scene.image.WritableImage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ImageTest implements Initializable {
    @FXML
    ImageView githubImage = new ImageView();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();

        try(InputStream file = getClass().getResourceAsStream("/images/cards/PNG/Back.png")){
            TranscoderInput transIn = new TranscoderInput(file);
            try {
                transcoder.transcode(transIn, null);
                Image img = SwingFXUtils.toFXImage(transcoder.getBufferedImage(), null);
                githubImage.setImage(img);
            } catch (TranscoderException ex) {
                ex.printStackTrace();
            }
        }
        catch(
                IOException io){ io.printStackTrace();
        }
    }
}
