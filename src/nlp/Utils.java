package nlp;

import java.io.File;
import java.text.DecimalFormat;

/**
 * @author bit_zt
 * 工具类，存储公共变量和处理函数
 */
public class Utils {
	
	public static String path_testSet = "src/nlp/testingSet.txt";
	public static String path_testAnswer = "src/nlp/testAnswer.txt";
	public static String path_trainSet = "src/nlp/trainingSet.txt";
	public static String path_result = "src/nlp/result.txt";
	public static String path_dictionary = "src/nlp/dictionary.txt";
	
	private static File file_trainSet;
	
	public static File get_file_trainSet(){
		if(file_trainSet == null){
			return create_file_trainSet();
		}else{
			return file_trainSet;
		}
	}
	
	private static File create_file_trainSet() {
		// TODO Auto-generated method stub
		file_trainSet = new File(Utils.path_trainSet);
		return file_trainSet;
	}


	public static String transferStrAnswer(String str_answer) {
		// TODO Auto-generated method stub
		String[] words_ans = str_answer.split("  ");
		StringBuffer ansbuffer = new StringBuffer();
		for(String word : words_ans){
			ansbuffer.append(word+"/");
		}
		String ans = ansbuffer.substring(0, ansbuffer.length());
		System.out.println("answer:  " + ans);
		return ans;
	}

	
	public static void showResultToScreen(double n, double N, double M, 
										long start_time, long end_time) {
		// TODO Auto-generated method stub
		double precision = n/N;
		double recall = n/M;
		long total_time = end_time - start_time;
		double efficiency = n/total_time;
		DecimalFormat df = new DecimalFormat("00.00");
		String divider = "\n----------------------------------------\n";
		System.out.println(divider);
		System.out.println("Precision  p=n/N\n");
		System.out.println("           p=" + String.valueOf(df.format(100*precision)) + "%\n");
		System.out.println("Recall     r=n/M\n");
		System.out.println("           r=" + String.valueOf(df.format(100*recall)) + "%\n");
		System.out.println("Efficiency e=c/t          where c referes to the correct divided word count");
		System.out.println("                          t referes to the time cost");
		System.out.println("           e=" + String.valueOf(df.format(100*efficiency)) + " word per milliseconds");
		System.out.println(divider);
	}
	
}
