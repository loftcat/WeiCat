package com.loftcat.weibo.bean;


public class BackgroundVo implements Vo {
	private int smallImage;
	private int largeImage;

	public BackgroundVo(int smallImage, int largeImage) {
		this.smallImage = smallImage;
		this.largeImage = largeImage;
	}

	public int getSmallImage() {
		return smallImage;
	}

	public int getLargeImage() {
		return largeImage;
	}

}
