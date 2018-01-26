package com.test.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.jfree.ui.RefineryUtilities;

public class SeletcModel extends JFrame implements ActionListener{

	//窗口宽度  
    final int WIDTH = 320;  
    //窗口高度  
    final int HEIGHT = 300;  
	
	
	
	//房室模式按钮  
    JButton btnHouse = new JButton("房室模式");  
    
    
    
    //非房室模式按钮  
    JButton btnUnHouse = new JButton("非房室模式");  
    
    JButton openFile=new JButton("选择文件");  
    
    private String fileName=null;
    
    
    public SeletcModel (){
    	setTitle("药物");
		setSize(WIDTH, HEIGHT);

		 //不可缩放  
        setResizable(false);  
        //设置布局:不适用默认布局，完全自定义  
        setLayout(null);  
         
		//设置按钮大小和位置  
		btnHouse.setBounds(20, 50, 100, 60);  
		btnUnHouse.setBounds(180, 50, 100, 60);
		
		//btnHouse.setBorder(BorderFactory.createRaisedBevelBorder());
		btnHouse.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				FittingCurve demo = new FittingCurve("药效模拟",fileName,1);
				demo.feifangshipart1();
				demo.feifangshipart2();
				demo.feifangshipart3();
				demo.feifangshipart4();
				demo.pack();
				RefineryUtilities.centerFrameOnScreen(demo);
				demo.setVisible(true);

			}
		});
		
		btnUnHouse.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		openFile.setBounds(100, 150, 100, 60);
		openFile.addActionListener(this); 
		
		this.add(btnHouse);
		this.add(btnUnHouse);
		this.add(openFile);
		
		
		
		
		
	}


	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JFileChooser jfc=new JFileChooser();  
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
        jfc.showDialog(new JLabel(), "选择");  
        File file=jfc.getSelectedFile();  
        if(file.isDirectory()){  
            System.out.println("文件夹:"+file.getAbsolutePath());  
        }else if(file.isFile()){ 
        	fileName=file.getAbsolutePath();
            System.out.println("文件:"+file.getAbsolutePath());  
        }  
        System.out.println(jfc.getSelectedFile().getName());  
		
	}
	
}
