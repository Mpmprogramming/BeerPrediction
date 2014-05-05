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
	
	
	public Result(int tP, int tN, int fN, int fP) {
		TP = tP;
		TN = tN;
		FN = fN;
		FP = fP;
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
		return "Result [TP=" + TP + "\n TN=" + TN + "\n FN=" + FN + "\nFP=" + FP + "\n Accuracy=" + getAccuracy() + "\n Precision=" + getPrecision()
				+ "\n Recall=" + getRecall() + "\n FMeasure=" + getFMeasure() + "]";
	}
	
	
	
	

}
