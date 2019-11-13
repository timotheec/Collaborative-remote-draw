package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 *  Class that keep persistent states of the app between runs.
 *
 */

public class AppConfig {

	private String imagePath = null;
	private int defaultBackgroundHeight = 1000;
	private int defaultBackgroundWidth = 1000;

	private AppConfig() {
	}

	private static AppConfig INSTANCE = null;

	public static synchronized AppConfig getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AppConfig();
		}
		return INSTANCE;
	}

	private String toJSON() {
		// TODO : convert the class into JSON
		return "";
	}

	public static void save() {
		// TODO : save the class to the config file
	}

	private static AppConfig fromJSON(String json) {
		// TODO : populate class from JSON string
		return null;
	}

	private static AppConfig load() {
		// TODO : populate class from the json config file (if exists)
		return null;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public BufferedImage getImage() {
		BufferedImage image = null;

		try {
			image = ImageIO.read(new File(imagePath));
		} catch (NullPointerException e) {
			image = new BufferedImage(defaultBackgroundWidth, defaultBackgroundHeight, BufferedImage.TYPE_4BYTE_ABGR);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}

		return image;
	}

}
