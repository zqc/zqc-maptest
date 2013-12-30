package com.zqc;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrameWindow extends JFrame {
	private static final long serialVersionUID = 3542592715734853687L;
	private final String WINDOW_TITLE = "GBK=============>UTF-8";
	private final int WINDOW_WIDTH = 600;
	private final int WINDOW_HEIGH = 300;
	private final String FILE_ENCODING_BEFORE = "GBK";
	private final String FILE_ENCODING_AFTER = "utf-8";

	private Dimension dim;
	private final int LAYOUT_STAT = FlowLayout.LEFT;
	private JPanel mJpanel;
	private Container mContainer;
	private JButton mJbutton;
	private JTextField mJtextField;
	private JLabel mJlabel;
	private JLabel transFormStat;

	private File textFile;

	private JButton startButton;
	private JFileChooser fDialog;
	private JFrame frame;

	private int transformFlag = 0;
	private FileInputStream in;
	private InputStreamReader isr;
	private BufferedReader br;
	private FileOutputStream out;
	private BufferedWriter bw;

	@SuppressWarnings("deprecation")
	public MainFrameWindow() {

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(WINDOW_TITLE);
		this.mContainer = this.getContentPane();
		this.mContainer.setLayout(null);
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGH);
		this.dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((dim.width - this.getWidth()) / 2, (dim.height - this.getHeight()) / 2);

		mJbutton = new JButton("选择转码文件");
		mJbutton.addActionListener(new OpenFileChooser());
		mJbutton.setBounds(new Rectangle(10, 10, 120, 30));// 参数分别是坐标x，y，宽，高

		
		mJlabel = new JLabel("文件路径：");
		mJlabel.setBounds(new Rectangle(140, 10, 100, 30));

		

		mJtextField = new JTextField("\\");
		mJtextField.setBounds(new Rectangle(210, 10, 300, 30));
		mJtextField.setEditable(false);
		
		startButton = new JButton("开始");
		startButton.addActionListener(new OpenFileChooser());
		startButton.setBounds(new Rectangle(10, 60, 120, 30)); 

		transFormStat = new JLabel("......................");
		transFormStat.setBounds(new Rectangle(10, 140, 80, 30));
		
		if (null == this.textFile || transformFlag == 1) {
			this.startButton.enable(false);
			if (transformFlag == 1) {
				transFormStat.setForeground(Color.RED);
				transFormStat.setText("等待.......");
			}
		} else if (transformFlag == 2) {
			this.startButton.enable(true);
			transFormStat.setForeground(Color.GREEN);
			transFormStat.setText("转码成功");
		}

		this.mContainer.add(mJbutton);
		this.mContainer.add(transFormStat);
		this.mContainer.add(mJlabel);
		this.mContainer.add(mJtextField);
		this.mContainer.add(startButton);

		this.setVisible(true);
	}

	/**
	 * 开始文件编码转换 gbk2312 ================> utf-8
	 */
	private void startEncodeTransform() {

		String backupfilename = textFile.getAbsolutePath() + ".backup";

		File outf = new File(backupfilename);
		textFile.renameTo(outf);

		try {
			in = new java.io.FileInputStream(backupfilename);
			isr = new java.io.InputStreamReader(in, FILE_ENCODING_BEFORE);
			br = (new java.io.BufferedReader(isr));

			// open output stream
			out = new java.io.FileOutputStream(textFile.getAbsolutePath());
			bw = new java.io.BufferedWriter(new java.io.OutputStreamWriter(out, FILE_ENCODING_AFTER));

			char[] buffer = new char[4096];// 大小4M
			int len;
			this.transformFlag = 1;// 正在转换
			this.startButton.enable(false);
			transFormStat.setForeground(Color.RED);
			transFormStat.setText("等待.......");

			while ((len = br.read(buffer)) != -1) {
				bw.write(buffer, 0, len);
				System.out.println("=========>" + len);
			}
			this.transformFlag = 2;// 转码结束

			this.startButton.enable(true);
			transFormStat.setForeground(Color.GREEN);
			transFormStat.setText("转码成功");

		} catch (IOException e) {

		} finally {
			try {
				br.close();
				bw.flush();
				bw.close();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	}

	private class OpenFileChooser implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {

			if (event.getSource() == mJbutton) {
				String msg;
				fDialog = new JFileChooser(); // 文件选择器
				int result = fDialog.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					MainFrameWindow.this.textFile = fDialog.getSelectedFile();
					MainFrameWindow.this.mJtextField.setText(textFile.getAbsolutePath());

				} else
					msg = "File Open Cancelled";
			}

			if (event.getSource() == startButton) {
				if (null == MainFrameWindow.this.textFile) {
					return;
				} else {
					MainFrameWindow.this.startEncodeTransform();
				}

			}
		}

	}

	public static void main(String[] args) {
		MainFrameWindow ms = new MainFrameWindow();
	}
}
