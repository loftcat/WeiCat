/*
 * Copyright (c) 2013 HeBin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
