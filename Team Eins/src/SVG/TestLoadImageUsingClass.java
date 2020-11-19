package SVG;

import SVG.dimension.PrimitiveDimensionProvider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.File;
import java.io.InputStream;

public class TestLoadImageUsingClass {


	public static void installSvgLoader() {
		SvgImageLoaderFactory.install();
	}


	public Image getImg(String path) {

		InputStream imageData = this.getClass().getClassLoader()
				.getResourceAsStream(path);



		Image image;
		if(path.contains("SVG")) {
			image = new Image(imageData, 60, 90, true, true);
		} else
			image = new Image(imageData);
		return image;
	}

	public ImageView getImgView(String path) {

		ImageView imageView = new ImageView(getImg(path));
		imageView.setSmooth(true);

		return imageView;
	}

}
