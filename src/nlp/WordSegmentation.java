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
import java.util.HashMap;

/**
 * @author bit_zt
 * 分词模型
 * 
 * 精确率 P = n/N 	N为结果总词数
 * 召回率 R = n/M		M为答案总词数
 * 
 * 分词时以每一行（即一个句子）为单位进行分词
 * 比较结果时，对每个句子，比较答案和结果，划分相同的词计入n
 */
public class WordSegmentation {

	private double N;
	private double M;
	private double n;
	
	private String result_path;
	private String source_path;
	private String answer_path;
	
	private long start_time;
	private long end_time;
	private int word_count;
	
	/**
	 * @param result: the path of output File name
	 * @param source: the path of testSet File name
	 * @param answer: the path of testAnswer File name
	 */
	public WordSegmentation(String result,String source,String answer) {
		// TODO Auto-generated constructor stub
		this.result_path = result;
		this.source_path = source;
		this.answer_path = answer;
		N = 0;
		M = 0;
		n = 0;
	}
	
	public void start(int flag){
		
		File source = new File(source_path);
		File answer = new File(answer_path);
		
		String str_source = "";
		String str_answer = "";
		
		InputStreamReader in_source;
		InputStreamReader in_answer;
		try {
			in_answer = new InputStreamReader(new FileInputStream(answer),"unicode");
			in_source = new InputStreamReader(new FileInputStream(source),"unicode");
			BufferedReader reader_source = new BufferedReader(in_source);
			BufferedReader reader_answer = new BufferedReader(in_answer);
			
			if(flag == 0 || flag == 1){
				start_time = System.currentTimeMillis();
				while((str_source = reader_source.readLine())!=null){
					str_answer = reader_answer.readLine();
					
					str_source = wordSegmentBasedOnDictionary(str_source,flag);	
					str_answer = Utils.transferStrAnswer(str_answer);
					
					compare(str_source,str_answer);
				}
				end_time = System.currentTimeMillis();
				Utils.showResultToScreen(n,N,M,start_time,end_time);
			}else{
				while((str_source = reader_source.readLine())!=null){
					wordSegmentWithStatisticalMethod(str_source);
				}
			}
			
			
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * @param str_result: single result sentence that has been splited by '/' 
	 * @param str_answer: single answer ...
	 * 
	 * 算法：
	 * 	1>	初始化，隐指针r_pH,a_pH为0，指针r_p,a_p分别指向第一个'/'
	 *  2>	若 r_p=a_p && r_pH=a_pH，则 M+=1,N+=1,n+=1 转4>。否则转3>
	 *  3>	若 r_p<a_p,r_pH=r_p，删去r_p N+=1
	 *  	若 a_p<r_p,a_pH=a_p，删去a_p M+=1
	 *  	转5>
	 *  4>  a_pH=a_p, r_pH=r_p，删去a_p,r_p
	 *  5>	调整a_p,r_p，若都指向字符串尾则结束算法，否则转2>	
	 */
	private void compare(String str_result, String str_answer) {
		// TODO Auto-generated method stub
		
		/* 1> */
		int result_pHidden = 0;
		int answer_pHidden = 0;
		int result_p = str_result.indexOf("/");
		int answer_p = str_answer.indexOf("/");
		
		StringBuffer str_res = new StringBuffer(str_result);
		StringBuffer str_ans = new StringBuffer(str_answer);
		
		while(true){
			
			if(result_p == -1){								/* 如果result 到达末尾，则只操作answer */
				str_ans.deleteCharAt(answer_p);
				answer_p = str_ans.indexOf("/");
				M+=1;
			}else if(answer_p == -1){						/* 如果answer 到达末尾，则只操作result */
				str_res.deleteCharAt(result_p);
				result_p = str_res.indexOf("/");
				N+=1;
			}else{											/* 否则，正常操作 */	
				if(result_p == answer_p){					/* 2> */
					if(result_pHidden == answer_pHidden){
						n+=1;	
					}
					M+=1;
					N+=1;
					answer_pHidden = answer_p;				/* 4> */
					result_pHidden = result_p;
					str_res.deleteCharAt(result_p);
					str_ans.deleteCharAt(answer_p);
				}else if(result_p < answer_p){				/* 3> */				
					result_pHidden = result_p;
					str_res.deleteCharAt(result_p);
					N+=1;
				}else{
					answer_pHidden = answer_p;
					str_ans.deleteCharAt(answer_p);
					M+=1;
				}
				
				result_p = str_res.indexOf("/");
				answer_p = str_ans.indexOf("/");
			}
			if(result_p == -1 && answer_p == -1){
				break;
			}
		}	
	}

	/*
	 * 将测试句按字典法拆解为词组	
	 * 算法：
	 * 	参见PDF	/自然语言理解/chap_02
	 */
	private String wordSegmentBasedOnDictionary(String str_source, int flag) {
		// TODO Auto-generated method stub
		
		DictionaryModel dictionaryModel = new DictionaryModel(str_source);
		
		if(flag == 0){			//FMM
			return dictionaryModel.FMM_method();
		}else{					//BMM
			return dictionaryModel.BMM_method();
		}
	}
	
	/*
	 * 将测试句按统计方法拆解为词组
	 */
	private void wordSegmentWithStatisticalMethod(String str_source) {
		// TODO Auto-generated method stub

		StatisticalModel statisticalModel = new StatisticalModel(str_source);
		statisticalModel.cal();
	}

}
