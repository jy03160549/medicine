package com.test.test;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class test {
	private static final String CHART_PATH = "G:\\";  
	private static List<Double> xlist=new ArrayList<Double>();
	private static List<Double> ylist=new ArrayList<Double>();
	private static void testLeastSquareMethodFromApache() {
		WeightedObservedPoints obs = new WeightedObservedPoints();
//		obs.add(0.083,2.02);
//		obs.add(0.167, 2.4);
//		obs.add(0.25, 2.96);
//		obs.add(0, 0);
//		obs.add(1, -1);
//		obs.add(2, -2);
//		obs.add(3, -5);
		double[][] data =new double[xlist.size()][2];
		XYSeries s2 = new XYSeries("点点连线");
		for (int i=0;i<xlist.size();i++) {
			obs.add(xlist.get(i),ylist.get(i));
			s2.add(xlist.get(i),ylist.get(i));
			data[i][0]=xlist.get(i);
			data[i][1]=ylist.get(i);
		}
		
		
		makeLineAndShapeChart(s2);
		// Instantiate a third-degree polynomial fitter.
		PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);

		// Retrieve fitted parameters (coefficients of the polynomial function).
		double[] coeff = fitter.fit(obs.toList());
		for (double c : coeff) {
			System.out.println(c);
		}
	}
	
	/** 
     * 生成折线图 
     */  
    public static void makeLineAndShapeChart(XYSeries data) {  
        String rowKeys = "给药时间";  
        String columnKeys = "healthy group";  
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(data);
 
        createTimeXYChar("折线图", "给药时间", "infected group", dataset, "lineAndShap.png");  
    }  
    
    /** 
     * 折线图 
     *  
     * @param chartTitle 
     * @param x 
     * @param y 
     * @param xyDataset 
     * @param charName 
     * @return 
     */  
    public static String createTimeXYChar(String chartTitle, String x, String y,  
    		XYDataset xyDataset, String charName) {  
  
        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, x, y,  xyDataset, PlotOrientation.VERTICAL, true, true, false);
        //生成坐标点点的形状  
        XYPlot plot = (XYPlot) chart.getPlot();  
          
        XYItemRenderer r = plot.getRenderer();  
           if (r instanceof XYLineAndShapeRenderer) {  
               XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;  
               renderer.setBaseShapesVisible(false);//坐标点的形状是否可见  
               renderer.setBaseShapesFilled(false);  
               }  
           ValueAxis yAxis = plot.getRangeAxis();  
           yAxis.setLowerMargin(2);  
  
        chart.setTextAntiAlias(false);  
        chart.setBackgroundPaint(Color.WHITE);  
        // 设置图标题的字体重新设置title  
        Font font = new Font("隶书", Font.BOLD, 25);  
        TextTitle title = new TextTitle(chartTitle);  
        title.setFont(font);  
        chart.setTitle(title);  
        // 设置面板字体  
        Font labelFont = new Font("SansSerif", Font.TRUETYPE_FONT, 12);  
  
        chart.setBackgroundPaint(Color.WHITE);  
  
        XYPlot categoryplot = (XYPlot) chart.getPlot();  
        // x轴 // 分类轴网格是否可见  
        categoryplot.setDomainGridlinesVisible(true);  
        // y轴 //数据轴网格是否可见  
        categoryplot.setRangeGridlinesVisible(true);  
  
        categoryplot.setRangeGridlinePaint(Color.WHITE);// 虚线色彩  
  
        categoryplot.setDomainGridlinePaint(Color.WHITE);// 虚线色彩  
  
        categoryplot.setBackgroundPaint(Color.lightGray);  
  
        // 设置轴和面板之间的距离  
        // categoryplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));  
  
        ValueAxis domainAxis = categoryplot.getDomainAxis();  
        
        domainAxis.setLabelFont(labelFont);// 轴标题  
  
        domainAxis.setTickLabelFont(labelFont);// 轴数值  
  
//        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 横轴上的  

        // Lable  
        // 45度倾斜  
        // 设置距离图片左端距离  
  
        domainAxis.setLowerMargin(0.0);  
        // 设置距离图片右端距离  
        domainAxis.setUpperMargin(0.0);  
  
        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();  
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());  
        numberaxis.setAutoRangeIncludesZero(true);  
  
        // 获得renderer 注意这里是下嗍造型到lineandshaperenderer！！  
        XYLineAndShapeRenderer lineandshaperenderer = (XYLineAndShapeRenderer) categoryplot.getRenderer();  
  
        lineandshaperenderer.setBaseShapesVisible(true); // series 点（即数据点）可见  
  
        lineandshaperenderer.setBaseLinesVisible(true); // series 点（即数据点）间有连线可见  
  
        // 显示折点数据  
        // lineandshaperenderer.setBaseItemLabelGenerator(new  
        // StandardCategoryItemLabelGenerator());  
        // lineandshaperenderer.setBaseItemLabelsVisible(true);  
  
        FileOutputStream fos_jpg = null;  
        try {  
            isChartPathExist(CHART_PATH);  
            String chartName = CHART_PATH + charName;  
            fos_jpg = new FileOutputStream(chartName);  
  
            // 将报表保存为png文件  
            ChartUtilities.writeChartAsPNG(fos_jpg, chart, 500, 510);  
  
            return chartName;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        } finally {  
            try {  
                fos_jpg.close();  
                System.out.println("create time-createTimeXYChar.");  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    
    /** 
     * 判断文件夹是否存在，如果不存在则新建 
     * @param chartPath 
     */  
    private static void isChartPathExist(String chartPath) {  
        File file = new File(chartPath);  
        if (!file.exists()) {  
            file.mkdirs();  
        // log.info("CHART_PATH="+CHART_PATH+"create.");  
        }  
    }  
    


	public static void main(String[] args) {
		readExcel();
		testLeastSquareMethodFromApache();
	}
	
	public static void readExcel(){
		 File execlfile = new File("c:\\Users\\gao\\Desktop\\软件数据.xlsx");
		 InputStream  execlstream;
		try {
			execlstream = new FileInputStream(execlfile);
			XSSFWorkbook  wb = new XSSFWorkbook(execlstream);
			Sheet sheet = wb.getSheet("Sheet1");
			int count=sheet.getLastRowNum();
			for(int i=1;i<count-1;i++){
				Row row = sheet.getRow(i);
				if (row.getCell(0)==null){
					break;
				}
				String x1=row.getCell(0).toString();
				String y1=row.getCell(1).toString();
				xlist.add(Double.parseDouble(x1));
				ylist.add(Double.parseDouble(y1));
			}
		    
		    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}

}
