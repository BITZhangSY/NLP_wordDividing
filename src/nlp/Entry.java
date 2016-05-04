package nlp;

import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

/**
 * @author bit_zt
 * NLP Project one 
 * 控制界面入口
 */
public class Entry {

		public static void main(String[] args) {
		// TODO Auto-generated method stub
			
			WordSegmentation dMethod = new WordSegmentation(Utils.path_result, Utils.path_testSet, 
					Utils.path_testAnswer);
			
			JFrame frame = new JFrame();
			Panel entry = new Panel();
			Label label=new Label();
			Button Fmm = new Button();
			Button Bmm = new Button();
			Button statistical = new Button();
			label.setText("请选择分词方法,结果将在控制台中显示");
			entry.setLayout(null);
			label.setSize(250, 50);
			label.setLocation(35, 30);
			Fmm.setLabel("FMM");
			Bmm.setLabel("BMM");
			statistical.setLabel("Statistical");
			Fmm.setSize(100, 30);
			Fmm.setLocation(100, 100);
			Bmm.setSize(100, 30);
			Bmm.setLocation(100, 150);
			statistical.setSize(100, 30);
			statistical.setLocation(100, 200);
			
			frame.add(entry);
			entry.add(label);
			entry.add(Fmm);
			entry.add(Bmm);
			entry.add(statistical);
			frame.setSize(300,300);
			Fmm.addActionListener(new ActionListener(){
			@Override
				public void actionPerformed(ActionEvent e) {
					JFrame frame1 = new JFrame();
					Panel panel2 = new Panel();
					panel2.setLayout(null);
					Label label1=new Label();
					Button fmm_file = new Button();
					label1.setText("请选择");
					fmm_file.setLabel("从文件中读入");
					label1.setSize(100, 30);
					label1.setLocation(130, 10);
					fmm_file.setSize(150, 30);
					fmm_file.setLocation(80, 50);
					frame1.add(panel2);
					panel2.add(label1);
					panel2.add(fmm_file);
					frame1.setSize(300, 150);
					fmm_file.addActionListener(new ActionListener(){
		
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							dMethod.start(0);
							frame1.dispose();
							frame.setVisible(true);
						}
					});
					frame.setVisible(false);
					frame1.setVisible(true);
			}});
			
			Bmm.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JFrame frame1 = new JFrame();
					Panel panel2 = new Panel();
					panel2.setLayout(null);
					Label label1=new Label();
					Button bmm_file = new Button();
					label1.setText("请选择");
					bmm_file.setLabel("从文件中读入");
					label1.setSize(100, 30);
					label1.setLocation(130, 10);
					bmm_file.setSize(150, 30);
					bmm_file.setLocation(80, 50);
					frame1.add(panel2);
					panel2.add(label1);
					panel2.add(bmm_file);
					frame1.setSize(300, 150);
					bmm_file.addActionListener(new ActionListener(){
		
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							dMethod.start(1);
							frame1.dispose();
							frame.setVisible(true);
						}
						
					});
					frame.setVisible(false);
					frame1.setVisible(true);
				}
			});
			
			statistical.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JFrame frame1 = new JFrame();
					Panel panel2 = new Panel();
					panel2.setLayout(null);
					Label label1=new Label();
					Button statistical_file = new Button();
					label1.setText("请选择");
					statistical_file.setLabel("从文件中读入");
					label1.setSize(100, 30);
					label1.setLocation(130, 10);
					statistical_file.setSize(150, 30);
					statistical_file.setLocation(80, 50);
					frame1.add(panel2);
					panel2.add(label1);
					panel2.add(statistical_file);
					frame1.setSize(300, 150);
					statistical_file.addActionListener(new ActionListener(){
		
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							dMethod.start(2);
							frame1.dispose();
							frame.setVisible(true);	
						}
					});
					frame.setVisible(false);
					frame1.setVisible(true);
				}
			});
		
			frame.setVisible(true);
	}
}

