/**
 * 
 */
package models;

/**
 * @author Michi
 *
 */
public class Result {
	
	private int TP;
	private int TN;
	private int FN;
	private int FP;
	private double avgSentScore;
	
	
	public Result(int tP, int tN, int fN, int fP, double avgSentScore) {
		TP = tP;
		TN = tN;
		FN = fN;
		FP = fP;
		this.avgSentScore = avgSentScore;
	}


	public int getTP() {
		return TP;
	}


	public int getTN() {
		return TN;
	}


	public int getFN() {
		return FN;
	}


	public int getFP() {
		return FP;
	}
	
	public double getAccuracy() {
		return (double) (TP+TN)/(TP+TN+FN+FP);
	}
	
	public double getPrecision() {
		return (double) (TP)/(TP+FP);
	}
	
	public double getRecall() {
		return (double) (TP)/(TP+FN);
	}
	
	public double getFMeasure() {
		return (double) 2*(getPrecision()*getRecall())/(getPrecision()+getRecall());
	}


	@Override
	public String toString() {
		return "Result [\nTP=" + TP + "\nTN=" + TN + "\nFN=" + FN + "\nFP=" + FP + "\nAccuracy=" + getAccuracy() + "\nPrecision=" + getPrecision()
				+ "\nRecall=" + getRecall() + "\nFMeasure=" + getFMeasure() + "\nAvgSentScore=" + avgSentScore +"]";
	}


	public double getAvgSentScore() {
		return avgSentScore;
	}
	
	
	

}
