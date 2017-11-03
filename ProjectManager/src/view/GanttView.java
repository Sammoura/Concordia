package view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JFrame;

import model.Activity;
import model.Project;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;

public class GanttView extends JFrame {

	private Project gantProj;
	
    public GanttView(String title,  Project proj) {

        super(title);
        setGantProj(proj);
        final IntervalCategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        
        // add the chart to a panel
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(950, 1000)); //changes size of window
        setContentPane(chartPanel);
    }

    /**
     * Creates a sample dataset for a Gantt chart.
     *
     * @return The dataset.
     */
    public IntervalCategoryDataset createDataset() {
  
  		/*Task is polymorphic : Task(java.lang.String description, java.util.Date start, java.util.Date end) 	 
    	Creates a new task.
    	Task(java.lang.String description, TimePeriod duration)
    	Creates a new task. */
    	
    	ArrayList<Activity> projActs =  getGantProj().getProjectActivities();
    	

        final TaskSeries s1 = new TaskSeries("Scheduled");
    	for(Activity act: projActs){
    		Calendar startDate = getDate(act.getStartDate());
    		Calendar finishDate = getDate(act.getFinishDate());
    		
    		s1.add(new Task(act.getName(),
    	               new SimpleTimePeriod(date(startDate.get(Calendar.DAY_OF_MONTH), startDate.get(Calendar.MONTH), startDate.get(Calendar.YEAR)),
    	                                    date(finishDate.get(Calendar.DAY_OF_MONTH), finishDate.get(Calendar.MONTH), finishDate.get(Calendar.YEAR)))));
    	}
        TaskSeriesCollection collection = new TaskSeriesCollection();
        
        collection.add(s1);
        
        return collection;
    }

    /**
     * Utility method for creating <code>Date</code> objects.
     *
     * @param day  the date.
     * @param month  the month.
     * @param year  the year.
     *
     * @return a date.
     */
    private Date date(final int day, final int month, final int year) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        final Date result = calendar.getTime();
        return result;

    }
        
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createChart(final IntervalCategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createGanttChart(
            getGantProj().getName() +" 	Gantt Chart ",  	// chart title
            "ACTIVITY",              					// domain axis label
            "DATE",              						// range axis label
            dataset,             						// data
            true,                						// include legend
            false,                						// tooltips
            false                						// urls
        );    
//        chart.getCategoryPlot().getDomainAxis().setMaxCategoryLabelWidthRatio(10.0f);
        return chart;    
    }
    
    public Calendar getDate(String startDate){
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.CANADA);
		try {
			cal.setTime(sdf.parse(startDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cal;
	}

	public Project getGantProj() {
		return gantProj;
	}

	public void setGantProj(Project proj) {
		this.gantProj = proj;
	}
       
}
