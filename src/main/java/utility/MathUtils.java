package utility;

import java.util.Collection;

public final class MathUtils {
	public static double findMean(double[] dataValues) {
		double sum = 0.0;
		
		for (int i = 0; i < dataValues.length; i++) {
			sum += dataValues[i];
		}
		
		return sum / dataValues.length;
	}
	
	
	public static double findMean(Collection<Double> dataValues) {
		double sum = 0.0;
		
		for (Double val : dataValues) {
			sum += val.doubleValue();
		}
		
		return sum / dataValues.size();
	}
	
	
	public static int findGcd(int a, int b) {
		if (b == 0) {
			return a;
		}
		
		return findGcd(b, a % b);
	}
}
