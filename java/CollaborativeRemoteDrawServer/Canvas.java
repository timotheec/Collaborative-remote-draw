import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Canvas extends JPanel {
	
	private final static int WIDTH = 500;
	private final static int HEIGHT = 500;
	
	private File file;
	private BufferedImage image;

	public void setImage(String imagePath) throws IOException {
		file = new File(imagePath);
		image = ImageIO.read(file);
		repaint();
		setSize(new Dimension(image.getWidth(), image.getHeight()));
		revalidate();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (image != null)
			g.drawImage(image, 0, 0, null);
	}

	@Override
	public Dimension getPreferredSize() {
		if (image != null)
			return new Dimension(image.getWidth(), image.getHeight());
		return new Dimension(WIDTH, HEIGHT);
	}
	
}
