package nlp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author bit_zt
 * 统计分词模型，运用二阶马尔科夫模型，对BMM FMM 分词结果进行概率计算，取结果较大者为可靠
 */
public class StatisticalModel {

	private String str_source;
	
	
	private Double p;
	
	public StatisticalModel(String str_source) {
		// TODO Auto-generated constructor stub
		this.str_source = str_source;
		p = Double.valueOf(1);
	}
	
	public void cal(){
		
		DictionaryModel dictionaryModel = new DictionaryModel(str_source);
		
		String[] Fmm_wordResult = dictionaryModel.FMM_method().split("/");
		String[] Bmm_wordResult = dictionaryModel.BMM_method().split("/");
		
		Double temp;
		System.out.println("the result of Statistical model based on bigram-Markov is as below:");
		cal_Probability(true, Fmm_wordResult);
		temp = p;
		cal_Probability(false, Bmm_wordResult);
		if(temp > p){
			System.out.println("Comparing the two statistics above, the FMM result is more reliable.");
		}else{
			System.out.println("Comparing the two statistics above, the BMM result is more reliable.");
		}
	}

	private void cal_Probability(boolean is_Fmm, String[] words) {
		// TODO Auto-generated method stub
		
		String word_forward = "";
		String word_now = "";
		
		//控制各线程全部完成后，进行结果显示
		CountDownLatch cLatch = new CountDownLatch(words.length);
		
		for(String word : words){
			word_now = word;
			//多线程进行结果统计
			new Count_Thread(word_forward,word_now, cLatch).start();
			word_forward = word;
		}
		
		DecimalFormat dFormat = new DecimalFormat("00.00");
		
		if(is_Fmm){
			try {
				cLatch.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("	the Probability of FMM :	Pfmm = " + p);
		}else{
			try {
				cLatch.await();	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("	the Probability of BMM :	Pbmm = " + p);
		}
		p = Double.valueOf(1);
	}
	
	class Count_Thread extends Thread{
		
		private String word_forward;			//WORDi-1
		private String word_now;				//WORDi
		
		private double count_Denominator;		//分母计数	#(Wi-1,Wi)
		private double count_Molecular;			//分子计数	#(Wi-1)
		
		private File file;
		
		private CountDownLatch cLatch;
		
		public Count_Thread(String word_forward, String word_now, CountDownLatch cLatch) {
			// TODO Auto-generated constructor stub
			this.word_forward = word_forward;
			this.word_now =  word_now;
			this.cLatch = cLatch;
			count_Denominator = 0;
			count_Molecular = 0;
			file = Utils.get_file_trainSet();
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String str_oneline = "";
			InputStreamReader in;
			BufferedReader reader = null;
			try {
				in = new InputStreamReader(new FileInputStream(file), "unicode");
				reader = new BufferedReader(in);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				System.out.println("thread initialize exception");
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("thread initialize exception");
				e.printStackTrace();
			}
			
			if(word_forward.equals("")){					// 计算 p(w1) = #W1/#AllWords
				try {
					while((str_oneline = reader.readLine())!=null){
						String[] temp = str_oneline.split("  ");
						count_Molecular += temp.length;		
						for(String word : temp){
							if(temp.equals(word_now)){
								count_Molecular += 1;
							}
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("thread readline() exception");
					e.printStackTrace();
				}
			
			}else{										// 计算 p(wi|wi-1) = #(Wi-1,Wi)/#(Wi-1)
				try {
					while((str_oneline = reader.readLine())!=null){
						String temp_wordForward = "";
						String temp_wordNow = "";
						String[] temp = str_oneline.split("  ");
						for(String word : temp){
							if(word.equals(word_now) && temp_wordForward.equals(word_forward)){
								count_Molecular += 1;
							}else if(word.equals(word_forward)){
								count_Denominator += 1; 
							}
							temp_wordForward = word;
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("thread readline() exception");
					e.printStackTrace();
				}
				
			}
			
			//平滑处理，避免出现分子或分母为0的情况
			if(count_Denominator == 0){
				count_Denominator += 1;
			}
			if(count_Molecular == 0){
				count_Molecular += 1;
			}
			
			
			synchronized (p) {							//加锁修改
				p*= count_Molecular/count_Denominator;
			}
			
			cLatch.countDown();
		}
	}
}
