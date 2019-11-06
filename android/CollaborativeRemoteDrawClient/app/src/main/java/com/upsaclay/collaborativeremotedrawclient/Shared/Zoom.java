package com.upsaclay.collaborativeremotedrawclient.Shared;

public class Zoom {
	
	public float scale;
	//offset = distance from top left corner
	public int xOffset;
	public int yOffset;
	
	public Zoom(float scale, int xOffset, int yOffset) {
		this.scale = scale;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

}
