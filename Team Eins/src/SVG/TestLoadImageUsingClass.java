package SVG;

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
		if(!path.contains("table")) {
			image = new Image(imageData, 55*factor, 82.5*factor, true, true);
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
