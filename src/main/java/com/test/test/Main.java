package com.test.test;

import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Main {
	
	 public static void main(String[] args)  
	{  
		 SeletcModel sm=new SeletcModel();
		 //窗口关闭键无效，必须通过退出键退出客户端以便善后  
		 sm.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  
	     //获取本机屏幕横向分辨率  
	     int w = Toolkit.getDefaultToolkit().getScreenSize().width;  
	     //获取本机屏幕纵向分辨率  
	     int h = Toolkit.getDefaultToolkit().getScreenSize().height;  
	     //将窗口置中  
	     sm.setLocation((w - sm.WIDTH)/2, (h - sm.HEIGHT)/2);  
	     
	     sm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
	     //设置客户端窗口为可见  
	     sm.setVisible(true);  
		 
		 
	}
	
	

}
