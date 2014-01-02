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
/**
 * 微博发布平台
 * @author Logan <a href="https://github.com/Logan676/JustSharePro"/>
 *   
 * @version 1.0 
 *  
 */
public class SourceVo implements Vo ,java.io.Serializable{

	private static final long serialVersionUID = -8972443458374235866L;
    private String url;               
    private String relationShip;      
    private String name;              
	public SourceVo(String str) {
		super();
		String[] source = str.split("\"",5);
        url = source[1];
        relationShip = source[3];
        name = source[4].replace(">", "").replace("</a", "");
	}
    
	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getRelationship() {
		return relationShip;
	}


	public void setRelationship(String relationShip) {
		this.relationShip = relationShip;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

    
	@Override
	public String toString() {
		return "Source [url=" + url + ", relationShip=" + relationShip
				+ ", name=" + name + "]";
	}


}
