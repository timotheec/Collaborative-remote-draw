package shared;

/**
 * Shared class between the client and the server to advantage of the both side using java
 * Represent a zoom in 2D space
 */
public class Zoom {

	/**
	 * scale of the zoom, multiplies all coordinates
	 */
	public float scale;
	/**
	 * distance from top left corner
	 */
	public int xOffset, yOffset;

	/**
	 * initialize the zoom with given values
	 * @param scale initial scale
	 * @param xOffset initial x offset
	 * @param yOffset initial y offset
	 */
	public Zoom(float scale, int xOffset, int yOffset) {
		this.scale = scale;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

}
