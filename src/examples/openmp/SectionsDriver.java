package examples.openmp;

import java.util.Arrays;

public class SectionsDriver {
	public static void main (String[] args) {
		float[] aFloats = {(float) 4.8, (float) 5.2, (float) 4.5, (float) 4.75, (float) 4.7};
		SumToTextRound.trace("Single Threaded Case");
		SumToTextRound.trace(aFloats);
		SumToTextRound.sectionSumAndToText(aFloats);
		SumToTextRound.trace("Multi Threaded Case");
		float[] aFloatsParallel =  {(float) 4.8, (float) 5.2, (float) 4.5, (float) 4.75, (float) 4.7};

		SumToTextRound.trace(aFloatsParallel);
		ParallelSumToTextRound.sectionSumAndToText(aFloatsParallel);
		SumToTextRound.trace(aFloatsParallel);


	}

}
