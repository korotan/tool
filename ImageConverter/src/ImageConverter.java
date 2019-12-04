
/**********************************************************************************************************************
 * Image Converter
 *
 * Version History
 * Date         Version     Programmer     Fixed
 * --------------------------------------------------------------------------------------------------------------------
 * 2004/11/23   1.0.0       korotan
 *
 **********************************************************************************************************************/

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.net.*;

/**
 * JPEG画像・GIF画像を□Art（スクウェア アート）またはTABLEアートに変換するツールです．
 * @author korotan
 * @version 1.0.0
 */
public class ImageConverter extends JFrame implements ActionListener, Runnable{
	
	/****************************************/
	/* 　　 　  フィールド定義 　　    　　 */
	/****************************************/
	
	/** ツール名 */
	static final String TOOL_NAME = "Image Converter";
	/** 現在のバージョン */
	static final String VERSION = "1.0.0";
	/** エラーダイアログタイトル */
	static final String ERROR_DIALOG_TITLE = "アプリケーションエラー";
	/** エラーダイアログメッセージ */
	static final String ERROR_DIALOG_MESSAGE = "アプリケーションの実行中にエラーが発生しました。\n処理を終了します。";
	/** エラー情報出力用のログファイル名 */
	static final String LOG_FILE_NAME = "err.log";
	
	/** フレームの横幅 */
	static final int FRAME_WIDTH = 550;
	/** フレームの高さ */
	static final int FRAME_HEIGHT = 550;
	/** フレームの表示位置X座標（フレームの左上端のX座標） */
	static final int FRAME_POSITION_X = 10;
	/** フレームの表示位置Y座標（フレームの左上端のY座標） */
	static final int FRAME_POSITION_Y = 10;
	
	/** スクウェアアート変換モード */
	static final int SQUARE_MODE = 0;
	/** テーブルアート変換モード */
	static final int TABLE_MODE = 1;
	/** スクウェアアートのデフォルトのスクウェアサイズ */
	static final int DEFAULT_SQUARE_SIZE = 1;
	
	/** メニューバー */
	private JMenuBar menuBar;
	/** メニュー */
	private JMenu fileMenu;
	private JMenu helpMenu;
	/** メニューアイテム */
	private JMenuItem openMItem;
	private JMenuItem saveMItem;
	private JMenuItem exitMItem;
	private JMenuItem infoMItem;
	/** ファイル名を表示するラベル */
	private JLabel fileLabel;
	/** 変換前の画像サイズを表示するラベル */
	private JLabel befLabel;
	/** 変換後の画像サイズを表示するラベル */
	private JLabel aftLabel;
	/** 処理状況を表示するラベル */
	private JLabel stateLabel;
	/** スクウェアアート用のラジオボタン */
	private JRadioButton squareRButton;
	/** テーブルアート用のラジオボタン */
	private JRadioButton tableRButton;
	/** 変換ボタン */
	private JButton exeButton;
	/** HTMLソース表示エリア */
	private JTextArea tArea;
	
	/** FileChooser */
	private JFileChooser openFc;
	private JFileChooser saveFc;
	/** 画像変換モード */
	private int cnvMode;
	/** 読み込んだ画像ファイルのFileオブジェクト */
	private File imgFile;
	/** 読み込んだ画像のImageオブジェクト */
	private Image img;
	/** 読み込んだ画像のスクウェアアート オブジェクト */
	private SquareArt sa;
	/** 読み込んだ画像のTABLEアート オブジェクト */
	private TableArt ta;
	
	/****************************************/
	/* 　　 　  コンストラクタ 　　    　　 */
	/****************************************/
	
	/**
	 * フレームを生成します．
	 */
	ImageConverter(){
		// タイトルの設定
		super(TOOL_NAME);
		// タイトルバーに表示するアイコンの設定
		try{
			Toolkit tk = getToolkit();
			URL url = getClass().getResource("image/logo.gif");
        	Image icon = tk.createImage((ImageProducer)url.getContent());
        	this.setIconImage(icon);
        } catch (Exception e) {
            handleError(e);
		}
		// フレームサイズの設定
		this.setBounds(FRAME_POSITION_X, FRAME_POSITION_Y, FRAME_WIDTH, FRAME_HEIGHT);
		
		// メニューバーの作成
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		// メニュー「ファイル」の作成
		fileMenu = new JMenu("ファイル(F)");
		fileMenu.setMnemonic('F');
		menuBar.add(fileMenu);
		// メニュー項目「開く」の作成
		openMItem = new JMenuItem("開く(O)", new ImageIcon(getClass().getResource("image/open.gif")));
		openMItem.setActionCommand("open");
		openMItem.addActionListener(this);
		openMItem.setMnemonic('O');
		openMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		fileMenu.add(openMItem);
		// メニュー項目「HTMLファイルに保存」の作成
		saveMItem = new JMenuItem("HTMLファイルに保存(S)", new ImageIcon(getClass().getResource("image/save.gif")));
		saveMItem.setActionCommand("save");
		saveMItem.addActionListener(this);
		saveMItem.setMnemonic('S');
		saveMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		saveMItem.setEnabled(false);
		fileMenu.add(saveMItem);
		// メニュー項目間に境界線を引く
		fileMenu.addSeparator();
		// メニュー項目「終了」の作成
		exitMItem = new JMenuItem("終了(X)", new ImageIcon(getClass().getResource("image/exit.gif")));
		exitMItem.setActionCommand("exit");
		exitMItem.addActionListener(this);
		exitMItem.setMnemonic('X');
		fileMenu.add(exitMItem);
		// メニュー「ヘルプ」の作成
		helpMenu = new JMenu("ヘルプ(H)");
		helpMenu.setMnemonic('H');
		menuBar.add(helpMenu);
		// メニュー項目「バージョン情報」の作成
		infoMItem = new JMenuItem("バージョン情報(A)");
		infoMItem.setActionCommand("info");
		infoMItem.addActionListener(this);
		infoMItem.setMnemonic('A');
		helpMenu.add(infoMItem);
		
		fileLabel = new JLabel();
		fileLabel.setPreferredSize(new Dimension(250, 20));
		fileLabel.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 12));
		fileLabel.setOpaque(true);
		fileLabel.setBackground(Color.white);
		fileLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		befLabel = new JLabel("サイズ(変換前)：");
		befLabel.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 12));
		aftLabel = new JLabel("サイズ(変換後)：");
		aftLabel.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 12));
		// ファイル情報表示用のパネル
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(3, 1, 2, 2));
		infoPanel.add(fileLabel);
		infoPanel.add(befLabel);
		infoPanel.add(aftLabel);
		
		// 変換モード選択用のラジオボタンの作成
		JPanel modePanel = new JPanel();
		modePanel.setLayout(new GridLayout(2, 1));
		ButtonGroup bGroup = new ButtonGroup();
		// 「■アート」ラジオボタン作成
		squareRButton = new JRadioButton("■アート", true);
		squareRButton.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 13));
		squareRButton.setActionCommand("square");
		squareRButton.addActionListener(this);
		bGroup.add(squareRButton);
		modePanel.add(squareRButton);
		// 「TABLEアート」ラジオボタン作成
		tableRButton = new JRadioButton("TABLEアート", false);
		tableRButton.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 13));
		tableRButton.setActionCommand("table");
		tableRButton.addActionListener(this);
		bGroup.add(tableRButton);
		modePanel.add(tableRButton);
		
		// 「変換」ボタンの作成
		exeButton = new JButton("変換");
		exeButton.addActionListener(this);
		exeButton.setActionCommand("exe");
		exeButton.setPreferredSize(new Dimension(100, 30));
		exeButton.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 13));
		
		// 上部操作領域用のパネル
		JPanel opPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
		opPanel.add(modePanel);
		opPanel.add(exeButton);
		
		// 上部領域用のパネル
		JPanel hdrPanel = new JPanel(new BorderLayout());
		hdrPanel.add(infoPanel, BorderLayout.WEST);
		hdrPanel.add(opPanel, BorderLayout.EAST);
		
		// HTMLソース表示用のテキストエリアの作成
		tArea = new JTextArea();
		tArea.setLineWrap(true);
		tArea.setWrapStyleWord(true);
		tArea.setMargin(new Insets(1,5,1,5));
		tArea.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 12));
		tArea.setEditable(false);
		// スクロールペインを作成してテキストエリアをセット
		JScrollPane scrPane = new JScrollPane(tArea);
		scrPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		// 処理状況表示用ラベルの作成
		stateLabel = new JLabel(" ");
		stateLabel.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 12));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));
		panel.add(hdrPanel, BorderLayout.NORTH);
		panel.add(scrPane, BorderLayout.CENTER);
		panel.add(stateLabel, BorderLayout.SOUTH);
		panel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
		
		// コンテナの作成
		Container cnt = this.getContentPane();
		cnt.setLayout(new BorderLayout());
		cnt.add(panel, BorderLayout.CENTER);
		
		// ×ボタンをクリックしたとき終了
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){System.exit(0);}
		});
		
		// ファイルのドラッグ＆ドロップに対応する
		new DropTarget(tArea, new DropTargetAdapter() {
			// テキストエリアにファイルがドロップされたときの処理
			public void drop(DropTargetDropEvent e) {
				try {
					Transferable tr = e.getTransferable();
					if ( tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor) ){
						// ドロップされたタイプがファイルのリストならドロップを受け付ける
						e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
						// ドロップされたファイルを取得する
						java.util.List fileList = (java.util.List)(tr.getTransferData(DataFlavor.javaFileListFlavor));
						// ドロップされたファイルをURLリストに追加しビューリストを更新する
						Iterator it = fileList.iterator();
						while (it.hasNext()){
						    imgFile = (File)it.next();
							// 処理可能なファイルの場合
							if(isExecutable(imgFile)){
							    // 画像の読み込み
						        loadImage(imgFile);
						        // 画像情報の表示
						        setImageInfo();
						        // HTML表示領域のクリア
						        tArea.setText("");
						        stateLabel.setText("画像ファイルが読み込まれました．");
						        saveMItem.setEnabled(false);
							}
						}
					}
				} catch (Exception excp){
				    handleError(excp);
				}
			}
		});
		
		openFc = new JFileChooser(".");
		saveFc = new JFileChooser(".");
		
		// Look&Feel の設定
		try{
			// Look&Feelをシステム固有のスタイルに変更
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
			SwingUtilities.updateComponentTreeUI(openFc);
			SwingUtilities.updateComponentTreeUI(saveFc);
		} catch(Exception e){
		    handleError(e);
		}
		
		// ファイルフィルタの生成
		DialogFileFilter openFilter[] = {	
			new DialogFileFilter(".jpg;.jpeg" , "JPEG ファイル (*.jpg;*.jpeg)"),
			new DialogFileFilter(".gif" , "GIF ファイル (*.gif)")
		};
		DialogFileFilter saveFilter[] = {
			new DialogFileFilter(".htm;.html" , "HTML ファイル (*.htm;*.html)"),
		};
		// ファイルフィルタの追加
		for( int i = 0 ; i < openFilter.length ; i++ ){
			openFc.addChoosableFileFilter(openFilter[i]);
		}
		for( int i = 0 ; i < saveFilter.length ; i++ ){
		    saveFc.addChoosableFileFilter(saveFilter[i]);
		}
		    
		
		// デフォルトの変換モードの設定
		cnvMode = SQUARE_MODE;
		
		// フレームの表示
		this.setVisible(true);
	}
	
	
	/**************************************/
	/* 　　 　  メソッド定義 　　    　　 */
	/**************************************/
	
	/**
	 * メイン関数
	 * @param args コマンドラインから入力した文字列のリスト
	 */
	public static void main(String[] args){
		// フレームの生成
		ImageConverter art = new ImageConverter();
	}
	
	
	/**
	 * ActionEvent発生時に各種処理を行います．
	 * @param e イベント
	 */
	public void actionPerformed(ActionEvent e){
		String action = e.getActionCommand();
		
		// メニュー項目「開く」が押されたときの処理
		if( action.equals("open") ){
			// ファイルオープンダイアログを表示して，選択されたFileオブジェクトを取得する
			imgFile = selectOpenFile();
			// 処理可能なファイルの場合
			if(isExecutable(imgFile)){
			    // 画像の読み込み
			    loadImage(imgFile);
			    // 画像情報の表示
			    setImageInfo();
			    // HTML表示領域のクリア
			    tArea.setText("");
			    stateLabel.setText("画像ファイルが読み込まれました．");
			    saveMItem.setEnabled(false);
		    }
		}
		
		// メニュー項目「HTMLファイルに保存」が押されたときの処理
		if( action.equals("save") ){
		    // ファイルオープンダイアログを表示して，選択されたFileオブジェクトを取得する
			File saveFile = selectSaveFile();
			
			if(saveFile != null){
			    // 同名のファイルが既に存在するか確認
			    if(saveFile.exists()){
			        // 上書き確認
			        if(isOverwrite(saveFile)){
			            // HTMLソースをファイルに保存
			            saveToFile(saveFile);
			            stateLabel.setText("HTMLファイルに保存されました．");
			        }
			        else{
			            // 保存しない
			        }
			    }
			    else{
			        // HTMLソースをファイルに保存
			        saveToFile(saveFile);
			        stateLabel.setText("HTMLファイルに保存されました．");
			    }
			}
		}
		
		// メニュー項目「終了」が押されたときの処理
		else if( action.equals("exit") ){
			System.exit(0);
		}
		
		// メニュー項目「バージョン情報」が押されたときの処理
		else if( action.equals("info") ){
			openInfoDialog();
		}
		
		// ラジオボタン「■アート」が選択されたときの処理
		else if( action.equals("square") ){
			cnvMode = SQUARE_MODE;
			stateLabel.setText(" ");
			if(sa != null){
			    aftLabel.setText("サイズ(変換後)：" + ExtendedUtil.getSizeString(sa.getSize()));
			}
		}
		
		// ラジオボタン「TABLEアート」が選択されたときの処理
		else if( action.equals("table") ){
			cnvMode = TABLE_MODE;
			stateLabel.setText(" ");
			if(ta != null){
			    aftLabel.setText("サイズ(変換後)：" + ExtendedUtil.getSizeString(ta.getSize()));
			}
		}
		
		// 「変換」ボタンが押されたときの処理
		else if( action.equals("exe") ){
		    if (sa != null && ta != null){
		        // 画像変換処理用のスレッドを生成して実行する
			    Thread th = new Thread(this);
		        th.start();
		    }
		}
	}
	
	
	/**
	 * 画像の変換処理を行います． 
	 */
	public void run(){
	    this.setEnabled(false);
	    stateLabel.setText("変換処理を実行しています...");
	    try{
		    // スクウェアアートの作成
		    if( cnvMode == SQUARE_MODE ){
		        tArea.setText(sa.convert(img));
		    }
		    // TABLEアートの作成
		    else{
		        tArea.setText(ta.convert(img));
		    }
		}catch(Exception e){
		    handleError(e);
		}
		tArea.setCaretPosition(0);
		saveMItem.setEnabled(true);
	    stateLabel.setText("変換処理が終了しました．");
	    this.setEnabled(true);
	}
	
	
	/**
	 * ファイルオープンダイアログを表示して選択されたFileオブジェクトを返します． 
	 * @return 選択されたFileオブジェクトを返します.<BR>
	 * ファイルが選択されなかった場合にはnullを返します.
	 */
	private File selectOpenFile(){
		// 「ファイルを開く」ダイアログボックスの表示
		int intRet = openFc.showOpenDialog(this);
		// 「OK」ボタンが押された場合，選択されたFILEオブジェクトを返す
		if(intRet == JFileChooser.APPROVE_OPTION){
			return openFc.getSelectedFile();
		}
		else {return null;}
	}
	
	
	/**
	 * ファイル保存ダイアログを表示して選択されたFileオブジェクトを返します．
	 * @return 選択されたFileオブジェクトを返します.<BR>
	 * ファイルが選択されなかった場合にはnullを返します.
	 */
	private File selectSaveFile(){
	    // デフォルトで表示される保存ファイル名の設定
		saveFc.setSelectedFile(new File(getSaveFileName(imgFile.getPath())));
		// 「ファイルの保存」ダイアログボックスの表示
		int intRet = saveFc.showSaveDialog(this);
		// 「OK」ボタンが押された場合，選択されたFileオブジェクトを返す
		if(intRet == JFileChooser.APPROVE_OPTION){
			return saveFc.getSelectedFile();
		}
		else {return null;}
	}
	
	
	/**
	 * 画像ファイルの情報を設定します．
	 */
	private void setImageInfo(){
	    try{
	        // SquareArt・TableArtオブジェクトの作成
	        sa = new SquareArt(img, img.getWidth(this), img.getHeight(this), DEFAULT_SQUARE_SIZE);
	        ta = new TableArt(img, img.getWidth(this), img.getHeight(this));
		} catch (Exception e){
		    handleError(e);
		}
		// 画像ファイル情報の設定
	    fileLabel.setText(" " + imgFile.getName());
	    befLabel.setText("サイズ(変換前)：" + ExtendedUtil.getSizeString(imgFile.length())
	            + " (" + img.getWidth(this) + "×" + img.getHeight(this) + " pixel)");
	    if( cnvMode == SQUARE_MODE ){
	        aftLabel.setText("サイズ(変換後)：" + ExtendedUtil.getSizeString(sa.getSize()));
	    }
	    else{
	        aftLabel.setText("サイズ(変換後)：" + ExtendedUtil.getSizeString(ta.getSize()));
	    }
	}
	
	
	/**
	 * 処理可能な形式のファイルかどうかを判定します．
	 * @param file 読み込んだFileオブジェクト
	 * @return 処理可能な形式ならばtrueを，そうでなければFalseを返します
	 */
	private boolean isExecutable(File file){
	    // オブジェクトがnullならば処理しない
	    if(file == null) return false;
	    // ファイルが存在しないならば処理しない
	    if(file.exists() != true){
	        return false;
	    }
	    else{
	        String fileName = file.getName();
	        // 指定された拡張子を持つならば処理する
	        if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".gif")){
	            return true;
	        }
	        // 指定された拡張子を持たないならば処理しない
	        else{
	            return false;
	        }
	    }
	}
	
	
	/**
	 * 画像を読み込みImageオブジェクトを生成します．
	 * @param file 読み込む画像のFileオブジェクト
	 */
	private void loadImage(File file){
		Toolkit tk = getToolkit();
		
		// 画像を読み込む
		img = tk.getImage(file.getAbsolutePath());
		// 画像の読み込み完了を待つ
		MediaTracker mt = new MediaTracker(this);
        mt.addImage(img, 0);
        try{
            mt.waitForAll();
        }catch(Exception e){
            handleError(e);
        }
	}
	
	
	/**
	 * HTMLソース保存用のファイル名を取得します．
	 * @param fileName 読み込んだ画像ファイル名
	 * @return HTMLソース保存用のファイル名
	 */
	private String getSaveFileName(String fileName){
		StringTokenizer st = new StringTokenizer(fileName, ".");
		String str = st.nextToken();
		for(int i = 0; i < st.countTokens() - 1; i++){
		    str = str + "." + st.nextToken();
		}
		return str + ".html";
	}
	
	
	/**
	 * HTMLソースをファイルに保存します．
	 * @param file 保存するFileオブジェクト
	 */
	public void saveToFile(File file){
	    try{
	        FileWriter fw = new FileWriter(file.getPath());
	        PrintWriter out = new PrintWriter(fw);
	        
	        out.println("<HTML>");
	        out.println("<HEAD>");
	        out.println("<TITLE>" + file.getName() + "</TITLE>");
	        out.println("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=x-sjis\">");
	        out.println("</HEAD>");
	        out.println("<BODY>");
	        out.println(tArea.getText());
	        out.println("</BODY>");
	        out.println("</HTML>");
	        out.flush();
	        out.close();
	        fw.close();
	    }catch(Exception e){
	        handleError(e);
        }
	}
	
	
	/**
	 * ダイアログを表示して上書きするかどうかを確認します．
	 * @param file 保存するFileオブジェクト
	 * @return 上書きをするならtrueを，しないならばfalseを返します．
	 */
	private boolean isOverwrite(File file){
		// ダイアログの作成
	    JLabel msgLabel = new JLabel(file.getPath() + " は既に存在します。\n上書きしますか？");
	    msgLabel.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 12));
		JOptionPane optPane = new JOptionPane(msgLabel, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION);
		JDialog dialog = optPane.createDialog(this.getContentPane(), TOOL_NAME);
		
		// Look&Feelをシステム固有のスタイルに変更
		try{
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(dialog);
		} catch(Exception e){
		    handleError(e);
		}
		// 確認ダイアログを表示
		dialog.setVisible(true);
		// 選択したオプションの取得
		int val;
		Object opt = optPane.getValue();
		if(opt instanceof Integer){
		    val = ((Integer)opt).intValue();
		}
		else{
		    val = JOptionPane.CLOSED_OPTION;
		}
		// 上書きするかどうかの判定
		if(val == JOptionPane.YES_OPTION){
		    return true;
		}
		else{
		    return false;
		}
	}
	
	
	/**
	 * バージョン情報のダイアログを表示します．
	 */
	private void openInfoDialog(){
	    // ダイアログの作成
	    String msg = "<html>" + TOOL_NAME + "  Version " + VERSION + "<BR><BR>"
	    			+ "Copyright (C) 2004 by korotan" + "<BR></html>";
	    JLabel msgLabel = new JLabel(msg);
	    msgLabel.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 12));
	    Object[] options = {" OK "};
		JOptionPane optPane = new JOptionPane(msgLabel, JOptionPane.INFORMATION_MESSAGE, 
		        								JOptionPane.DEFAULT_OPTION, null, options, options[0]);
		JDialog dialog = optPane.createDialog(this.getContentPane(), TOOL_NAME);
		
		// Look&Feelをシステム固有のスタイルに変更
		try{
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(dialog);
		} catch(Exception e){
		    handleError(e);
		}
		// ダイアログを表示
		dialog.setVisible(true);
	}
	
	
	/**
	 * エラー処理を行います．
	 * @param excp 例外オブジェクト
	 */
	public void handleError(Exception excp){
	    ErrorHandler eh = new ErrorHandler();
	    // エラーダイアログの表示
	    eh.showErrorDialog(excp, this, ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
	    // エラー情報をログファイルに出力
	    eh.printErrorLog(excp, LOG_FILE_NAME);
	    
	    System.exit(1);
	}
	
}