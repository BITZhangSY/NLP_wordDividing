package nlp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;


/**
 * @author bit_zt
 * 字典类，对于HashMap<>字典集运用单例模式
 */
public class Dictionary {
	
	private static HashMap<String, String> dic;
	
	private static int Word_MaxLen;
	
	private Dictionary(){}
	
	private static int[] nums;
	
	public static HashMap<String, String> getDic(){
		
		if(dic == null){
			return createDic();
		}else{
			return dic;
		}
	}
	
	public static int getWordMaxLength(){
		return Word_MaxLen;
	}

	/**
	 * 如果字典文件不存在，则遍历测试集文件构建字典文件。
	 * 运用字典文件构建字典HashMap<>
	 * @return
	 */
	private static HashMap<String, String> createDic() {
		// TODO Auto-generated method stub
		HashMap<String, String> hashMap = new HashMap<>();
		
		File file = new File(Utils.path_trainSet);
		
		File file2 = new File(Utils.path_dictionary);
		
		String test_maxlen_word = "";
		try {
			
			String str = "";
			if(!file2.exists()){					//字典文件不存在
				
				InputStreamReader in = new InputStreamReader(new FileInputStream(file),"unicode");
				BufferedReader reader = new BufferedReader(in);
				
				file2.createNewFile();
				OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file2), "unicode");
				BufferedWriter writer = new BufferedWriter(out);
				
				while((str = reader.readLine())!=null){
					
					String[] words = str.split("  ");
					for(String word : words){
						if(!hashMap.containsKey(word)){
							hashMap.put(word, word);
							writer.write(word + "/");
							writer.flush();
							Word_MaxLen = word.length() > Word_MaxLen ? word.length() : Word_MaxLen;
						}
					}
				}
				
				reader.close();
				writer.close();
			}else{									//字典文件存在
				InputStreamReader in = new InputStreamReader(new FileInputStream(file2),"unicode");
				BufferedReader reader = new BufferedReader(in);
				String temp = reader.readLine();
				
				String[] words = temp.split("/");
				for(String word : words){
					hashMap.put(word, word);
					Word_MaxLen = word.length() > Word_MaxLen ? word.length() : Word_MaxLen;
				}
			}
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
		}
		
//		System.out.print(String.valueOf(hashMap.size()) + "\n");
		System.out.print("\n");
		return hashMap;
	}

}
