package nlp;

import java.util.HashMap;


/**
 * @author bit_zt
 * 基于字典的分词模型，包括BMM FMM，具体算法参见PDF
 */
public class DictionaryModel {
	
	private String str_source;
	private int Word_MaxLength;
	private HashMap<String, String> dic;
	
	private int p; 				//当前开始的字符串位置
	private int temp_maxlen;		//当前可能切分的最大长度
	private int length_raw;			//还未被切分的长度
	private int next_p;
	
	public DictionaryModel(String str_source) {
		// TODO Auto-generated constructor stub
		this.str_source = str_source;
		this.dic = Dictionary.getDic();
		this.Word_MaxLength = Dictionary.getWordMaxLength();
	}
	
	public String BMM_method(){
		
		String target = "";
		StringBuffer str_buffer = new StringBuffer(str_source);
		str_buffer.insert(str_buffer.length(), "/");
		p = str_buffer.length() - 2;
		next_p = str_buffer.length() - 1;
		
		while(true){
			temp_maxlen = Word_MaxLength;
			length_raw = p + 1;
			
			if(length_raw == 1){									/* 到达末尾 */
				break;
			}else{	
				if(length_raw < temp_maxlen){
					temp_maxlen = length_raw;
				}
				while(temp_maxlen!=1){
					String temp = str_buffer.substring(p-temp_maxlen+1, p+1);
					if(dic.containsKey(temp)){
						
						str_buffer.insert(p-temp_maxlen+1, "/");
						next_p = p - temp_maxlen;				/* 切法与FMM不同 */
						break;
					}else{
						temp_maxlen-=1;
					}
					if(temp_maxlen == 1){
						if(p == 0){
							break;
						}
						str_buffer.insert(p-temp_maxlen+1, "/");
						String word_p = str_buffer.substring(p-1, p);
						if(!dic.containsKey(word_p)){
							dic.put(word_p, word_p);
						}
						next_p = p - temp_maxlen;				/* 切法与FMM不同 */
					}
				}
				p = next_p;
				if(next_p == -1){
					break;
				}
			}
		}
		target = String.valueOf(str_buffer);
		while(target.substring(0,1).equals("/")){
			target = target.substring(1,target.length());
			
		}
		System.out.println("output:  " + target);
		return target;
	}
	
	public String FMM_method(){
		
		String target = "";
		StringBuffer str_buffer = new StringBuffer(str_source);
		p = 0;
		next_p = 0;
		
		while(true){
			temp_maxlen = Word_MaxLength;
			length_raw = str_buffer.length() - p;
			
			if(length_raw == 1){										/* 到达末尾 */
				str_buffer.insert(str_buffer.length(),"/");
				break;
			}else{
				if(length_raw < temp_maxlen){
					temp_maxlen = length_raw;
				}
				while(temp_maxlen!=1){
					String temp = str_buffer.substring(p, p+temp_maxlen);
					if(dic.containsKey(temp)){
						str_buffer.insert(temp_maxlen+p, "/");
						next_p = temp_maxlen + p + 1;
						break;
					}else{
						temp_maxlen-=1;
					}
				}
				if(temp_maxlen == 1){
					str_buffer.insert(temp_maxlen+p, "/");
					String word_p = str_buffer.substring(p, p+1);
					if(!dic.containsKey(word_p)){
						dic.put(word_p,word_p);
					}
					next_p = temp_maxlen + p + 1;
				}	
			}
			p = next_p;
		}
		
		target = String.valueOf(str_buffer);
		System.out.println("output:  " + target);
		return target;
	}
	
	
}
