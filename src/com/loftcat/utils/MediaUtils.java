/**   
 * Copyright (c) 2013 by Logan.	
 *   
 * 爱分享-微博客户端，是一款运行在android手机上的开源应用，代码和文档已托管在GitHub上，欢迎爱好者加入
 * 1.授权认证：Oauth2.0认证流程
 * 2.服务器访问操作流程
 * 3.新浪微博SDK和腾讯微博SDK
 * 4.HMAC加密算法
 * 5.SQLite数据库相关操作
 * 6.字符串处理，表情识别
 * 7.JSON解析，XML解析：超链接解析，时间解析等
 * 8.Android UI：样式文件，布局
 * 9.异步加载图片，异步处理数据，多线程  
 * 10.第三方开源框架和插件
 *    
 */
package com.loftcat.utils;
import java.util.HashMap;
import java.util.Map;

/** 
 * 媒体类型工具包
 * @author  @Cundong
 * @weibo   http://weibo.com/liucundong
 * @blog    http://www.liucundong.com
 * @date    Apr 29, 2011 2:50:48 PM
 * @version 1.0
 */
public class MediaUtils
{
	private static Map<String, String> FORMAT_TO_CONTENTTYPE = new HashMap<String, String>();
	
	static
	{
		//音频
		FORMAT_TO_CONTENTTYPE.put( "mp3", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "mid", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "midi", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "asf", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "wm", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "wma", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "wmd", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "amr", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "wav", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "3gpp", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "mod", "audio" );
		FORMAT_TO_CONTENTTYPE.put( "mpc", "audio" );
		
		//视频
		FORMAT_TO_CONTENTTYPE.put( "fla", "video" );
		FORMAT_TO_CONTENTTYPE.put( "flv", "video" );
		FORMAT_TO_CONTENTTYPE.put( "wav", "video" );
		FORMAT_TO_CONTENTTYPE.put( "wmv", "video" );
		FORMAT_TO_CONTENTTYPE.put( "avi", "video" );
		FORMAT_TO_CONTENTTYPE.put( "rm", "video" );
		FORMAT_TO_CONTENTTYPE.put( "rmvb", "video" );
		FORMAT_TO_CONTENTTYPE.put( "3gp", "video" );
		FORMAT_TO_CONTENTTYPE.put( "mp4", "video" );
		FORMAT_TO_CONTENTTYPE.put( "mov", "video" );
		
		//flash
		FORMAT_TO_CONTENTTYPE.put( "swf", "video" );
		
		FORMAT_TO_CONTENTTYPE.put( "null", "video" );
		
		//图片
		FORMAT_TO_CONTENTTYPE.put( "jpg", "photo" );
		FORMAT_TO_CONTENTTYPE.put( "jpeg", "photo" );
		FORMAT_TO_CONTENTTYPE.put( "png", "photo" );
		FORMAT_TO_CONTENTTYPE.put( "bmp", "photo" );
		FORMAT_TO_CONTENTTYPE.put( "gif", "photo" );
	}
	
	/**
	 * 根据根据扩展名获取类型
	 * @param attFormat
	 * @return
	 */
	public static String getContentType( String attFormat )
	{
		String contentType = FORMAT_TO_CONTENTTYPE.get("null");
		
		if ( attFormat != null ) 
		{
			contentType = FORMAT_TO_CONTENTTYPE.get( attFormat.toLowerCase() );
		}
		return contentType;
	}
	
	/**
	 * 判断文件MimeType的method
	 * @param f
	 * @return
	 */
    public static String getMIMEType(String filePath)
    {
        String type = "";
        String fName = FileUtils.getFileName(filePath);
        /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        
        /* 按扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg")
            || end.equals("wav"))
        {
            type = "audio";
        }
        else if (end.equals("3gp") || end.equals("mp4"))
        {
            type = "video";
        }
        else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp"))
        {
            type = "image";
        }
        else if(end.equals("doc") || end.equals("docx"))
        {
            type = "application/msword";
        }
        else if(end.equals("xls"))
        {
            type = "application/vnd.ms-excel";
        }
        else if(end.equals("ppt") || end.equals("pptx") || end.equals("pps") || end.equals("dps"))
        {
            type = "application/vnd.ms-powerpoint";
        }
        else
        {
            type = "*";
        }
        /* 如果无法直接打开，就弹出软件列表给用户选择 */
        type += "/*";
        return type;
    }

}