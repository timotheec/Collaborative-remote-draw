package com.upsaclay.collaborativeremotedrawclient.Shared;

// Shared class between the client and the server to advantage of the both side using java
// Represent a zoom in 2D space
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
