package application.model;

import java.util.Map;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// Traditional, Modern, European, Southwest, Mountain, Victorian, and Country.
public abstract class House implements Cloneable, Comparable<House>, Customizable {
	protected final double TAX = 0.05;
	protected StringProperty style;
	protected DoubleProperty numOfBedrooms;
	protected DoubleProperty numOfBathrooms;
	protected DoubleProperty totalArea;
	protected DoubleProperty templateBasicRate;
	
	public House(String style) {
		this.style = new SimpleStringProperty(style);
		this.totalArea = new SimpleDoubleProperty(2000);
		this.templateBasicRate = new SimpleDoubleProperty(0);
	}
	
	public double getCost(Map<String, Double> customizations) {
		double templateBasicRate = this.templateBasicRate.get();
		
		if (customizations.get("area") > 3000) {
			templateBasicRate *= 1.5;
		}
		
		double cost = templateBasicRate +
			(800 * getExtraBedrooms(customizations.get("numOfBedrooms"))) +
			(500 * getExtraBathrooms(customizations.get("numOfBathrooms")));
		
		return cost + (cost * TAX);
	}
	
	private double getExtraBedrooms(double numOfBedrooms) {
		double extraBedrooms = numOfBedrooms - this.getBedrooms();
		return extraBedrooms >= 0 ? extraBedrooms : 0;
	}
	
	private double getExtraBathrooms(double numOfBathrooms) {
		double extraBathrooms = numOfBathrooms - this.getBathrooms();
		return extraBathrooms >= 0 ? extraBathrooms : 0;
	}
	
	@Override
	public int compareTo(House house) {
		if (this.style.get() == house.style.get()						&&
			this.numOfBedrooms.get() == house.numOfBedrooms.get()		&&
			this.numOfBathrooms.get() == house.numOfBathrooms.get()		&&
			this.totalArea.get() == house.totalArea.get()) {
			return 0;
		}
		
		if (this.templateBasicRate.get() > house.templateBasicRate.get()) {
			return 1;
		} else if (this.templateBasicRate.get() <= house.templateBasicRate.get()) {
			return -1;
		}
		
		return 0;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	@Override
	public String toString() {
		return this.style.get();
	}
	
	public String getStyle() {
		return this.style.get();
	}
	
	@Override
	public void setBedrooms(double numOfBedrooms) {
		this.numOfBedrooms = new SimpleDoubleProperty(numOfBedrooms);
	}
	
	public double getBedrooms() {
		return this.numOfBedrooms.get();
	}
	
	@Override
	public void setBathrooms(double numOfBathrooms) {
		this.numOfBathrooms = new SimpleDoubleProperty(numOfBathrooms);
	}
	
	public double getBathrooms() {
		return this.numOfBathrooms.get();
	}
	
	@Override
	public void setArea(double area) {
		this.totalArea = new SimpleDoubleProperty(area);
	}
	
	public double getArea() {
		return this.totalArea.get();
	}
	
	@Override
	public void setTemplateBasicRate(double templateBasicRate) {
		this.templateBasicRate = new SimpleDoubleProperty(templateBasicRate);
	}
	
	public double getTemplateBasicRate() {
		return this.templateBasicRate.get();
	}
}
