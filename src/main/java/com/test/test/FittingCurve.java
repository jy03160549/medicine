package com.test.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class FittingCurve extends JFrame {
	
	
	private String fileName="";
	List<Double> equation = null;
	private  List<Double> xlists=new ArrayList<Double>();
	private  List<Double> ylists=new ArrayList<Double>();
	private  List<List<Double>> list = new ArrayList<List<Double>>();
	private  List<Double>  xiaochusulv =new ArrayList<Double>();
	private  List<Double>  fenbusulv =new ArrayList<Double>();
	
	private double unfangA;
	private double unfangα;
	private double unfangB;
	private double unfangb;
	
	
	// 设置多项式的次数
	int times = 1;

	public FittingCurve(String title,String fileName,int times) {
		super(title);
		this.fileName=fileName;
		this.times=times;
		readExcel();
		// 使用最小二乘法计算拟合多项式中各项前的系数。
		/*
		 * 请注意： 多项式曲线参数计算 与 Chart图表生成 是分开处理的。 多项式曲线参数计算： 负责计算多项式系数， 返回多项式系数List。
		 * Chart图表生成： 仅仅负责按照给定的数据绘图。 比如对给定的点进行连线。 本实例中，光滑的曲线是用密度很高的点连线绘制出来的。
		 * 由于我们计算出了多项式的系数，所以我们让X轴数据按照很小的步长增大，针对每一个X值，使用多项式计算出Y值， 从而得出点众多的(x,y)组。
		 * 把这些(x, y)组成的点连线绘制出来，则显示出光滑的曲线。 XYSeries为JFreeChart绘图数据集，
		 * 用于绘制一组有关系的数据。 XYSeries对应于X,Y坐标轴数据集， 添加数据方式为： XYSeries s.add(x,y);
		 * XYSeriesCollection 为XYSeries的集合，
		 * 当需要在一个Chart上绘制多条曲线的时候，需要把多条曲线对应的XYSeries添加到XYSeriesCollection
		 * 添加方法：dataset.addSeries(s1); dataset.addSeries(s2);
		 */
		// 多项式的次数从高到低，该函数需要的参数：x轴数据<List>，y轴数据<List>，多项式的次数<2>
		list.add(xlists);
		list.add(ylists);
		getData();
		this.equation = this.getCurveEquation(list.get(0), list.get(1), this.times);

		// 生成Chart
		JFreeChart chart = this.getChart();
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		chartPanel.setMouseZoomable(true, false);
		setContentPane(chartPanel);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FittingCurve demo = new FittingCurve("药效模拟","c:\\Users\\gao\\Desktop\\软件数据.xlsx",1);
		demo.feifangshipart1();
		demo.feifangshipart2();
		demo.feifangshipart3();
		demo.feifangshipart4();
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);

	}

	// 生成chart
	public JFreeChart getChart() {
		// 获取X和Y轴数据集
		XYDataset xydataset = this.getXYDataset();
		// 创建用坐标表示的折线图
		JFreeChart xyChart = ChartFactory.createXYLineChart("moni", "X", "Y", xydataset,
				PlotOrientation.VERTICAL, true, true, false);
		// 生成坐标点点的形状
		XYPlot plot = (XYPlot) xyChart.getPlot();

		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(false);// 坐标点的形状是否可见
			renderer.setBaseShapesFilled(false);
		}
		ValueAxis yAxis = plot.getRangeAxis();
		yAxis.setLowerMargin(2);
		return xyChart;
	}

	// 数据集按照逻辑关系添加到对应的集合
	public XYDataset getXYDataset() {
		// 预设数据点数据集
		XYSeries s2 = new XYSeries("点点连线");
		List<List<Double>> data = this.getData();
		for (int i = 0; i < data.get(0).size(); i++) {
			s2.add(data.get(0).get(i), data.get(1).get(i));
		}
		// 拟合曲线绘制 数据集 XYSeries s1 = new XYSeries("拟合曲线");
		// 获取拟合多项式系数，equation在构造方法中已经实例化
		List<Double> list = this.equation;
		// 获取预设的点数据
		

		// get Max and Min of x;
		List<Double> xList = data.get(0);
		double max = this.getMax(xList);
		double min = this.getMin(xList);
		double step = max - min;
		double x = min;
		double step2 = step / 800.0;
		// 按照多项式的形式 还原多项式，并利用多项式计算给定x时y的值
		for (int i = 0; i < 800; i++) {
			x = x + step2;
			int num = list.size() - 1;
			double temp = 0.0;
			for (int j = 0; j < list.size(); j++) {
				temp = temp + Math.pow(x, (num - j)) * list.get(j);
			}
			s2.add(x, temp);
		}

		// 把预设数据集合拟合数据集添加到XYSeriesCollection
		XYSeriesCollection dataset = new XYSeriesCollection();
//		dataset.addSeries(s1);
		dataset.addSeries(s2);
		return dataset;

	}
	
	public  void readExcel(){
		if(null == fileName) {
			return;
		}
		 File execlfile = new File(fileName);
		 InputStream  execlstream;
		try {
			execlstream = new FileInputStream(execlfile);
			XSSFWorkbook  wb = new XSSFWorkbook(execlstream);
			Sheet sheet = wb.getSheet("Sheet1");
			int count=sheet.getLastRowNum();
			for(int i=1;i<count-1;i++){
				Row row = sheet.getRow(i);
				if ((null == row ) || (null != row &&row.getCell(0)==null)){
					break;
				}
				String x1=row.getCell(0).toString();
				String y1=row.getCell(1).toString();
				xlists.add(Double.parseDouble(x1));
				ylists.add(Math.log(Double.parseDouble(y1)));
			
			}
		    
		    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

	// 模拟设置绘图数据（点）
	public List<List<Double>> getData() {
		return list;

	}
	
	public void feifangshipart1() {
		int n = 0;
		if(!xlists.isEmpty()) {
			n=xlists.size()/3;
		}
		List<Double> xlist=new ArrayList<Double>();
		List<Double> ylist=new ArrayList<Double>();
		for(int i=n*2;i<xlists.size();i++){
			xlist.add(xlists.get(i));
			ylist.add(ylists.get(i));
		}
		xiaochusulv=getCurveEquation(xlist,ylist, 1);
		System.out.println("消除速率常数β:"+xiaochusulv.get(0));
		unfangb=xiaochusulv.get(0);
		double lnB=xiaochusulv.get(1);
		double B=Math.pow(Math.E,xiaochusulv.get(1));
		System.out.println("B:"+B);
		unfangB=B;
		
	}
	
	public void feifangshipart2() {
		List<Double> yuce = new ArrayList<Double>(); 
		int maxCount=0;
		double max=0;
		for(int i=0;i<ylists.size();i++){
			if(max<ylists.get(i)){
				max=ylists.get(i);
				maxCount=i;
			}
		}
		System.out.println("max:"+max);
/*		System.out.println("maxCount:"+maxCount);*/
		System.out.println("maxTime:"+xlists.get(maxCount));
		int n=ylists.size();
		int min=maxCount-n/6;
		int maxx=maxCount+n/6;
		if(maxx>ylists.size()){
			maxx=ylists.size();
		}
		if(min<0){
			min=0;
		}
		for (int i=min;i<=maxx;i++){
			double cr=xlists.get(i)*xiaochusulv.get(0)+xiaochusulv.get(1);
			yuce.add(cr);
		}
		List<Double> crList = new ArrayList<Double>(); 
		List<Double> yrList = new ArrayList<Double>(); 
		for(int i=min;i<=maxx;i++){
			yrList.add(ylists.get(i));
		}
		for (int i=0;i<yuce.size();i++){
			Double cry=  yrList.get(i) - yuce.get(i) ;
			crList.add(Math.log(cry));
		}
		List<Double> xrList = new ArrayList<Double>(); 
		for (int i=min;i<=maxx;i++){
			xrList.add(xlists.get(i));
		}
		List<Double> result=getCurveEquation(xrList,crList, 1);
		double α=-2.303*result.get(0);
		System.out.println("α:"+α);
		unfangα=α;
		double lnA=result.get(1);
		double a=Math.pow(Math.E,result.get(1));
		unfangA=a;
		System.out.println("A:"+a);
		
	}
	
	public void feifangshipart3() {
		double Tb=0.693/unfangb;
		System.out.println("T1/2β:"+Tb);
	}
	
	public void feifangshipart4() {
		double area=unfangA/unfangα+unfangB/unfangb;
		System.out.println("A∪C:"+area);
	}

	// 以下代码为最小二乘法计算多项式系数
	// 最小二乘法多项式拟合
	public List<Double> getCurveEquation(List<Double> x, List<Double> y, int m) {
		if (x.size() != y.size() || x.size() <= m + 1) {
			return new ArrayList<Double>();
		}
		List<Double> result = new ArrayList<Double>();
		List<Double> S = new ArrayList<Double>();
		List<Double> T = new ArrayList<Double>();
		// 计算S0 S1 …… S2m
		for (int i = 0; i <= 2 * m; i++) {
			double si = 0.0;
			for (double xx : x) {
				si = si + Math.pow(xx, i);
			}
			S.add(si);
		}
		// 计算T0 T1 …… Tm
		for (int j = 0; j <= m; j++) {
			double ti = 0.0;
			for (int k = 0; k < y.size(); k++) {
				ti = ti + y.get(k) * Math.pow(x.get(k), j);
			}
			T.add(ti);
		}

		// 把S和T 放入二维数组，作为矩阵
		double[][] matrix = new double[m + 1][m + 2];
		for (int k = 0; k < m + 1; k++) {
			double[] matrixi = matrix[k];
			for (int q = 0; q < m + 1; q++) {
				matrixi[q] = S.get(k + q);
			}
			matrixi[m + 1] = T.get(k);
		}
		for (int p = 0; p < matrix.length; p++) {
			for (int pp = 0; pp < matrix[p].length; pp++) {
				System.out.print("  matrix[" + p + "][" + pp + "]=" + matrix[p][pp]);
			}
			System.out.println();
		}
		// 把矩阵转化为三角矩阵
		matrix = this.matrixConvert(matrix);
		// 计算多项式系数，多项式从高到低排列
		result = this.MatrixCalcu(matrix);
		System.out.println(result);
		return result;
	}

	// 矩阵转换为三角矩阵
	public double[][] matrixConvert(double[][] d) {
		for (int i = 0; i < d.length - 1; i++) {
			double[] dd1 = d[i];
			double num1 = dd1[i];

			for (int j = i; j < d.length - 1; j++) {
				double[] dd2 = d[j + 1];
				double num2 = dd2[i];

				for (int k = 0; k < dd2.length; k++) {
					dd2[k] = (dd2[k] * num1 - dd1[k] * num2);
				}
			}
		}
		for (int ii = 0; ii < d.length; ii++) {
			for (int kk = 0; kk < d[ii].length; kk++)
				System.out.print(d[ii][kk] + "  ");
			System.out.println();
		}
		return d;
	}

	// 计算一元多次方程前面的系数， 其排列为 xm xm-1 …… x0（多项式次数从高到低排列）
	public List<Double> MatrixCalcu(double[][] d) {

		int i = d.length - 1;
		int j = d[0].length - 1;
		List<Double> list = new ArrayList<Double>();
		double res = d[i][j] / d[i][j - 1];
		list.add(res);

		for (int k = i - 1; k >= 0; k--) {
			double num = d[k][j];
			for (int q = j - 1; q > k; q--) {
				num = num - d[k][q] * list.get(j - 1 - q);
			}
			res = num / d[k][k];
			list.add(res);
		}
		return list;
	}

	// 获取List中Double数据的最大最小值
	public double getMax(List<Double> data) {
		double res = data.get(0);
		for (int i = 0; i < data.size() - 1; i++) {
			if (res < data.get(i + 1)) {
				res = data.get(i + 1);
			}
		}
		return res;
	}

	public double getMin(List<Double> data) {
		double res = data.get(0);
		for (int i = 0; i < data.size() - 1; i++) {
			if (res > data.get(i + 1)) {
				res = data.get(i + 1);
			}
		}
		return res;
	}

}