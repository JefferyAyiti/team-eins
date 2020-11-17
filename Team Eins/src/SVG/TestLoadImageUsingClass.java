package SVG;

import SVG.dimension.PrimitiveDimensionProvider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.InputStream;

public class TestLoadImageUsingClass {


	public static void installSvgLoader() {

	}


	public Image getImg(String path) {
		SvgImageLoaderFactory.install(new PrimitiveDimensionProvider());
		InputStream imageData = this.getClass().getClassLoader()
				.getResourceAsStream(path);

		Image image = new Image(imageData);
		return image;
	}

	public ImageView getImgView(String path) {

		ImageView imageView = new ImageView(getImg(path));
		imageView.setSmooth(true);

		return imageView;
	}

}
