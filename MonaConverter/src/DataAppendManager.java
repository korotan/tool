import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ユーザ定義の顔文字変換データの追加登録を行うマネージャを表すクラスです．
 */
public class DataAppendManager extends JDialog implements ActionListener {
	
	/****************************************/
	/* 　　 　  フィールド定義 　　    　　 */
	/****************************************/
	
	/** 顔文字変換データのユーザ定義ファイル名 */
	static final String USR_DEF_FILE_NAME = MonaConverter.USR_DEF_FILE_NAME;
	/** 顔文字変換定義データファイルにおける区切り文字 */
	static final String DEF_FILE_SEGMENT = MonaConverter.DEF_FILE_SEGMENT;
	/** 顔文字変換定義データファイルにおける禁則文字（変換後の顔文字の開始文字のみに適用） */
	static final char FORBIDDEN_CHAR[] = {','};
	/** エラーダイアログタイトル */
	static final String ERROR_DIALOG_TITLE = "アプリケーションエラー";
	/** エラーダイアログメッセージ */
	static final String ERROR_DIALOG_MESSAGE = "アプリケーションの実行中にエラーが発生しました．\n処理を終了します．";
	
	/** ダイアログの幅 */
	static final int DIALOG_WIDTH = 300;
	/** ダイアログの高さ */
	static final int DIALOG_HEIGHT = 170;
	
	/** ラベルのフォント名 */
	static final String LABEL_FONT_NAME = "ＭＳ ゴシック";
	/** ラベルのフォントサイズ */
	static final int LABEL_FONT_SIZE = 12;
	
	/** テキストフィールド内のテキストのフォント名 */
	static final String TFIELD_FONT_NAME = "ＭＳ Ｐゴシック";
	/** テキストフィールド内のテキストのフォントサイズ */
	static final int TFIELD_FONT_SIZE = 12;
	
	/** テキストボタンの幅 */
	static final int BUTTON_WIDTH = 100;
	/** テキストボタンの高さ */
	static final int BUTTON_HEIGHT = 30;
	/** テキストボタンのフォント名 */
	static final String BUTTON_FONT_NAME = "ＭＳ ゴシック";
	/** テキストボタンのフォントサイズ */
	static final int BUTTON_FONT_SIZE = 13;
	
	/** ラベル */
	JLabel befStrLabel;
	JLabel aftStrLabel;
	/** テキストフィールド */
	JTextField befTField;
	JTextField aftTField;
	/** ボタン */
	JButton registButton;
	JButton exitButton;
	
	/** 顔文字変換テーブル */
	private ConvertTable convTable;
	
	
	/****************************************/
	/* 　　 　  コンストラクタ 　　    　　 */
	/****************************************/
	
	/**
	 * 追加処理用のダイアログを生成します．
	 * @param parent 呼び出し元のフレーム
	 * @param convTable 顔文字変換テーブル
	 */
	public DataAppendManager(JFrame parent, ConvertTable convTable) {
		// ダイアログのタイトルとモードを設定
		super(parent, "顔文字変換データの登録", true);
		
		// ダイアログのサイズの設定
		int xPos = (int)(parent.getBounds(new Rectangle())).getX() + 5;		// ダイアログの表示位置のX座標
		int yPos = (int)(parent.getBounds(new Rectangle())).getY() + 5;		// ダイアログの表示位置のY座標
		this.setBounds(xPos, yPos, DIALOG_WIDTH, DIALOG_HEIGHT);
		
		// データ変換テーブルの取得
		this.convTable = convTable;
		
		// 変換前の顔文字入力欄のラベルの作成
		befStrLabel = new JLabel("変換前の顔文字 ");
		befStrLabel.setFont(new Font(LABEL_FONT_NAME, Font.PLAIN, LABEL_FONT_SIZE));	// フォントの設定
		// 変換前の顔文字入力欄のテキストフィールドの作成
		befTField = new JTextField();
		befTField.setPreferredSize(new Dimension(120, 20));	// テキストフィールドのサイズの指定
		befTField.setFont(new Font(LABEL_FONT_NAME, Font.PLAIN, TFIELD_FONT_SIZE));
		// 変換前の顔文字入力欄用のパネルの作成
		JPanel btPanel = new JPanel();
		btPanel.setLayout( new FlowLayout(FlowLayout.CENTER, 10, 10) );
		btPanel.add(befStrLabel);
		btPanel.add(befTField);
		
		// 変換後の顔文字入力欄のラベルの作成
		aftStrLabel = new JLabel("変換後の顔文字 ");
		aftStrLabel.setFont(new Font(LABEL_FONT_NAME, Font.PLAIN, LABEL_FONT_SIZE));
		// 変換後の顔文字入力欄のテキストフィールドの作成
		aftTField = new JTextField();
		aftTField.setPreferredSize(new Dimension(120, 20));
		aftTField.setFont(new Font(LABEL_FONT_NAME, Font.PLAIN, TFIELD_FONT_SIZE));
		// 変換後の顔文字入力欄用のパネルの作成
		JPanel atPanel = new JPanel();
		atPanel.setLayout( new FlowLayout(FlowLayout.CENTER, 10, 10) );
		atPanel.add(aftStrLabel);
		atPanel.add(aftTField);
		
		// テキストフィールド用のパネルの作成
		JPanel tPanel = new JPanel();
		tPanel.setLayout( new BorderLayout() );
		tPanel.add(btPanel, BorderLayout.NORTH);
		tPanel.add(atPanel, BorderLayout.CENTER);
		
		// 「登録」ボタンの作成
		registButton = new JButton("登録");
		registButton.addActionListener(this);			// イベントリスナを登録
		registButton.setActionCommand("regist");		// アクションコマンドの設定
		registButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));			// ボタンのサイズの設定
		registButton.setFont(new Font(BUTTON_FONT_NAME, Font.PLAIN, BUTTON_FONT_SIZE));		// ボタンのフォントの設定
		
		// 「キャンセル」ボタンの作成
		exitButton = new JButton("キャンセル");
		exitButton.addActionListener(this);
		exitButton.setActionCommand("exit");
		exitButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		exitButton.setFont(new Font(BUTTON_FONT_NAME, Font.PLAIN, BUTTON_FONT_SIZE));
		
		// ボタン用のパネルの作成
		JPanel bPanel = new JPanel();
		bPanel.setLayout( new FlowLayout(FlowLayout.CENTER, 20, 10) );
		bPanel.add(registButton);
		bPanel.add(exitButton);
		
		// コンテナの作成
		Container content = this.getContentPane();
		content.setLayout( new BorderLayout() );
		content.add( tPanel, BorderLayout.CENTER);
		content.add( bPanel, BorderLayout.SOUTH);
		
		
		// ×ボタンをクリックしたとき終了
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				dispose();		// ダイアログの破棄
			}
		});
		
		// フレームの表示
		this.setVisible(true);
	}
	
	
	/**************************************/
	/* 　　 　  メソッド定義 　　    　　 */
	/**************************************/
	
	/**
	 * ActionEvent発生時に各種処理を行います．
	 * @param e イベント
	 */
	public void actionPerformed(ActionEvent e){
		String action = e.getActionCommand();
		
		// ボタン「登録」が押されたときの処理
		if( action.equals("regist") ){
			// テキストフィールドが空欄かどうかのチェック
			if( befTField.getText().equals("") ){
				showAlertDialog("警告", "入力欄に顔文字を入力して下さい．");	// 警告ダイアログの表示
				return;
			}
			// 禁則文字のチェック
			if( aftTField.getText().equals("") != true ){
				for( int i = 0; i < FORBIDDEN_CHAR.length; i++ ){
					if( aftTField.getText().charAt(0) == FORBIDDEN_CHAR[i] ){
						showAlertDialog("警告", "この顔文字は登録できません．");	// 警告ダイアログの表示
						aftTField.setText("");		// 変換後のテキストフィールドのクリア
						return;
					}
				}
			}
			// 顔文字変換データをユーザ定義ファイルに追加保存する
			String befStr = ConvertTable.toDoubleByteString(befTField.getText());	// 変換前の顔文字（全角に変換）
			String aftStr = aftTField.getText();	// 変換後の顔文字
			try {
				FileWriter fw = new FileWriter(USR_DEF_FILE_NAME, true);
				PrintWriter out = new PrintWriter(fw);
				String str = befStr + "," + aftStr;
				out.println(str);
				out.flush();
				out.close();
				fw.close();
			} catch (Exception ex) {
				// エラーメッセージダイアログを表示する
				showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
				System.exit(1);			// プログラムを終了する
			}
			// 登録した変換データを顔文字変換テーブルに追加する
			convTable.setElement(befStr, aftStr);
			// テキストフィールドをクリアする
			befTField.setText("");
			aftTField.setText("");
		}
		
		
		// ボタン「キャンセル」が押されたときの処理
		if( action.equals("exit") ){
			this.dispose();					// ダイアログの破棄
		}
	}
	
	
	/**
	 * 警告メッセージダイアログを表示します．
	 * @param title ダイアログのタイトル
	 * @param msg 警告メッセージ
	 */
	private void showAlertDialog(String title, String msg){
		try{
			// ダイアログの作成
			JOptionPane optPane = new JOptionPane(msg, JOptionPane.WARNING_MESSAGE);
			JDialog dialog = optPane.createDialog(this.getContentPane(), title);
			
			// ダイアログのLook&FeelをWindowsスタイルに変更
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(dialog);
			
			// ダイアログを表示
			dialog.setVisible(true);
			
		} catch(Exception e){
			// エラーメッセージダイアログを表示する
			showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
			System.exit(1);			// プログラムを終了する
		}
	}
	
	
	/**
	 * エラーメッセージダイアログを表示します．
	 * @param title ダイアログのタイトル
	 * @param msg エラーメッセージ
	 */
	private void showErrorDialog(String title, String msg){
		try{
			// ダイアログの作成
			JOptionPane optPane = new JOptionPane(msg, JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optPane.createDialog(this.getContentPane(), title);
			
			// ダイアログのLook&FeelをWindowsスタイルに変更
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(dialog);
			
			// ダイアログを表示
			dialog.setVisible(true);
			
		} catch(Exception e){
			System.exit(1);
		}
	}
	
}