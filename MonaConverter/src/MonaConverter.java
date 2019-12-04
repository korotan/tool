/**********************************************************************************************************************
 * モナー コンバータ（2ch顔文字変換ツール）
 *
 * Version History
 * Date			Version		Programmer		Fixed
 * --------------------------------------------------------------------------------------------------------------------
 * 2003/11/20	1.0.0		korotan
 * 2003/11/21	1.0.1		korotan			テキストエリアが更新されている状態でファイルをD&Dしたらフリーズするバグを修正．
 * 											（破棄確認ダイアログを表示させないように修正）
 * 2003/11/22	1.0.2		korotan			顔文字変換テーブルの末尾からマッチング・変換を行うように修正．
 *											ユーザ定義の顔文字変換データファイルの1行目をコメントとして読み飛ばすように修正．
 * 2003/12/30	1.0.3		korotan			システム定義の顔文字変換データファイルをjarファイルの外に出した．
 *                                          空文字への変換を許可した．
 *                                          保存ファイル名に現在開いているファイル名が表示されるようにした．
 *                                          保存時に拡張子を指定しなかった場合、拡張子を自動的に補完するようにした．
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
 * 普通の顔文字を2ch顔文字に変換するツール．
 *
 * @author korotan
 * @version 1.0.3
 */
public class MonaConverter extends JFrame implements ActionListener {
	
	/****************************************/
	/* 　　 　  フィールド定義 　　    　　 */
	/****************************************/
	
	/** ウインドウのタイトルバーに表示されるツール名 */
	static final String TITLE_NAME = "モナー コンバータ （´∀｀ ）";
	/** 更新時にウインドウのタイトルバーに表示されるツール名 */
	static final String UPDATED_TITLE_NAME = "モナー コンバータ （´∀｀*）";
	/** ファイル名が選択されていないときにタイトルバーに表示される文字 */
	static final String NON_FILE_NAME ="無題";
	/** 破棄確認メッセージ */
	static final String DISPOSE_CONFIRM_MESSAGE = "への変更を破棄しますか？";
	/** 保存確認メッセージ */
	static final String SAVE_CONFIRM_MESSAGE = "への変更を保存しますか？";
	/** エラーダイアログタイトル */
	static final String ERROR_DIALOG_TITLE = "アプリケーションエラー";
	/** エラーダイアログメッセージ */
	static final String ERROR_DIALOG_MESSAGE = "アプリケーションの実行中にエラーが発生しました．\n処理を終了します．";
	
	/** フレームの幅 */
	static final int FRAME_WIDTH = 500;
	/** フレームの高さ */
	static final int FRAME_HEIGHT = 400;
	/** フレームの表示位置（フレームの左上端のX,Y座標） */
	static final int FRAME_POSITION = 10;
	
	/** メニューテキストのフォント名 */
	static final String MENU_FONT_NAME = "ＭＳ ゴシック";
	/** メニューテキストのフォントサイズ */
	static final int MENU_FONT_SIZE = 13;
	
	/** テキストエリア内のテキストのフォント名 */
	static final String TAREA_FONT_NAME = "ＭＳ Ｐゴシック";
	/** テキストエリア内のテキストのフォントサイズ */
	static final int TAREA_FONT_SIZE = 12;
	
	/** テキストボタンの幅 */
	static final int BUTTON_WIDTH = 100;
	/** テキストボタンの高さ */
	static final int BUTTON_HEIGHT = 30;
	/** テキストボタンのフォント名 */
	static final String BUTTON_FONT_NAME = "ＭＳ ゴシック";
	/** テキストボタンのフォントサイズ */
	static final int BUTTON_FONT_SIZE = 13;
	
	/** 顔文字変換データのシステム定義ファイル名 */
	static final String SYS_DEF_FILE_NAME = "sys_cnv_def.dat";
	/** 顔文字変換データのユーザ定義ファイル名 */
	static final String USR_DEF_FILE_NAME = "usr_cnv_def.dat";
	/** 顔文字変換定義データファイルにおける区切り文字 */
	static final String DEF_FILE_SEGMENT = ",";
	
	/** 顔文字変換テーブル */
	private ConvertTable convTable;
	
	/** メニューバー */
	private JMenuBar menuBar;
	/** メニュー */
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu setupMenu;
	/** メニューアイテム */
	private JMenuItem newMItem;
	private JMenuItem openMItem;
	private JMenuItem saveMItem;
	private JMenuItem exitMItem;
	private JMenuItem undoMItem;
	private JMenuItem redoMItem;
	private JMenuItem cutMItem;
	private JMenuItem copyMItem;
	private JMenuItem pasteMItem;
	private JMenuItem selectAllMItem;
	private JMenuItem appendPatternMItem;
	
	/** ツールバー */
	private JToolBar toolBar;
	/** ボタン（アイコン） */
	private JButton newButton;
	private JButton openButton;
	private JButton saveButton;
	private JButton exitButton;
	private JButton cutButton;
	private JButton copyButton;
	private JButton pasteButton;
	private JButton undoButton;
	private JButton redoButton;
	/** ボタン（テキスト） */
	private JButton exeButton;
	
	/** テキストエリア */
	private JTextArea tArea;
	/** スクロールペイン */
	private JScrollPane scrPane;
	
	/** FileChooser */
	private JFileChooser fileChooser;
	
	/** テキストエリア内の文字列が更新されたかどうかを表すフラグ */
	private boolean isUpdate;
	/** 読み込んでいるファイルのパス名 */
	private String pathName;
	/** 変換前のテキストエリア内の文字列 */
	private String befStr;
	/** 変換後のテキストエリア内の文字列 */
	private String aftStr;
	
	
	/**************************************/
	/* 　　 　  メソッド定義 　　    　　 */
	/**************************************/
	
	/**
	 * メイン関数
	 * @param args[] コマンドラインからの文字列
	 */
	public static void main(String args[]){
		// フレームの生成
		MonaConverter converter = new MonaConverter(TITLE_NAME + " - " + NON_FILE_NAME);
	}
	
	
	/****************************************/
	/* 　　 　  コンストラクタ 　　    　　 */
	/****************************************/
	
	/**
	 * フレームおよび顔文字変換テーブルを生成します．
	 * @param title タイトルバーに表示する文字列
	 */
	MonaConverter(String title){
		// タイトルの設定
		super(title);
		// タイトルバーに表示するアイコンの設定
		try{
			Toolkit tk = getToolkit();
			URL url = getClass().getResource("icon/logo.gif");
        	Image titleIcon = tk.createImage((ImageProducer)url.getContent());
        	this.setIconImage(titleIcon);
        } catch (Exception e) {
			// エラーメッセージダイアログを表示する
			showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
			System.exit(1);			// プログラムを終了する
		}
		
		// フレームサイズの設定
		this.setBounds(FRAME_POSITION, FRAME_POSITION, FRAME_WIDTH, FRAME_HEIGHT);
		
		// メニューバーの作成
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		// メニュー「File」の作成
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');			// ニーモニックキーの設定
		menuBar.add(fileMenu);				// メニューバーに追加
		
		// メニュー項目「New」の作成
		newMItem = new JMenuItem("New", new ImageIcon(getClass().getResource("icon/new.gif")));
		newMItem.setActionCommand("new");	// アクションコマンドの設定
		newMItem.addActionListener(this);	// イベントリスナを登録
		newMItem.setMnemonic('N');			// ニーモニックキーの設定
		fileMenu.add(newMItem);				// メニューに追加
		// メニュー項目「Open」の作成
		openMItem = new JMenuItem("Open", new ImageIcon(getClass().getResource("icon/open.gif")));
		openMItem.setActionCommand("open");
		openMItem.addActionListener(this);
		openMItem.setMnemonic('O');
		openMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));		// ショートカットキーの設定
		fileMenu.add(openMItem);
		// メニュー項目「Save」の作成
		saveMItem = new JMenuItem("Save", new ImageIcon(getClass().getResource("icon/save.gif")));
		saveMItem.setActionCommand("save");
		saveMItem.addActionListener(this);
		saveMItem.setMnemonic('S');
		saveMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		fileMenu.add(saveMItem);
		
		// メニュー項目間に境界線を引く
		fileMenu.addSeparator();
		
		// メニュー項目「Exit」の作成
		exitMItem = new JMenuItem("Exit", new ImageIcon(getClass().getResource("icon/exit.gif")));
		exitMItem.setActionCommand("exit");
		exitMItem.addActionListener(this);
		exitMItem.setMnemonic('X');
		fileMenu.add(exitMItem);
		
		// メニュー「Edit」の作成
		editMenu = new JMenu("Edit");
		editMenu.setMnemonic('E');
		menuBar.add(editMenu);
		
		// メニュー項目「Undo」の作成
		undoMItem = new JMenuItem("Undo", new ImageIcon(getClass().getResource("icon/undo.gif")));
		undoMItem.setActionCommand("undo");
		undoMItem.addActionListener(this);
		undoMItem.setMnemonic('U');
		undoMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
		undoMItem.setEnabled(false);	// メニュー項目を無効（選択不能）にする
		editMenu.add(undoMItem);
		// メニュー項目「Redo」の作成
		redoMItem = new JMenuItem("Redo", new ImageIcon(getClass().getResource("icon/redo.gif")));
		redoMItem.setActionCommand("redo");
		redoMItem.addActionListener(this);
		redoMItem.setMnemonic('R');
		redoMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Y"));
		redoMItem.setEnabled(false);
		editMenu.add(redoMItem);
		
		// メニュー項目間に境界線を引く
		editMenu.addSeparator();
		
		// メニュー項目「Cut」の作成
		cutMItem = new JMenuItem("Cut", new ImageIcon(getClass().getResource("icon/cut.gif")));
		cutMItem.setActionCommand("cut");
		cutMItem.addActionListener(this);
		cutMItem.setMnemonic('F');
		cutMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
		editMenu.add(cutMItem);
		// メニュー項目「Copy」の作成
		copyMItem = new JMenuItem("Copy", new ImageIcon(getClass().getResource("icon/copy.gif")));
		copyMItem.setActionCommand("copy");
		copyMItem.addActionListener(this);
		copyMItem.setMnemonic('C');
		copyMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
		editMenu.add(copyMItem);
		// メニュー項目「Paste」の作成
		pasteMItem = new JMenuItem("Paste", new ImageIcon(getClass().getResource("icon/paste.gif")));
		pasteMItem.setActionCommand("paste");
		pasteMItem.addActionListener(this);
		pasteMItem.setMnemonic('V');
		pasteMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));
		editMenu.add(pasteMItem);
		
		// メニュー項目間に境界線を引く
		editMenu.addSeparator();
		
		// メニュー項目「Select All」の作成
		selectAllMItem = new JMenuItem("Select All", new ImageIcon("icon/no_icon.gif"));
		selectAllMItem.setActionCommand("selectAll");
		selectAllMItem.addActionListener(this);
		selectAllMItem.setMnemonic('A');
		selectAllMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl A"));
		editMenu.add(selectAllMItem);
		
		// メニュー「Setup」の作成
		setupMenu = new JMenu("Setup");
		setupMenu.setMnemonic('S');
		menuBar.add(setupMenu);
		
		// メニュー項目「変換データの登録」の作成
		appendPatternMItem = new JMenuItem("変換データの登録");
		appendPatternMItem.setActionCommand("appendPattern");
		appendPatternMItem.addActionListener(this);
		appendPatternMItem.setMnemonic('A');
		appendPatternMItem.setFont(new Font(MENU_FONT_NAME, Font.PLAIN, MENU_FONT_SIZE));	// フォントの設定
		setupMenu.add(appendPatternMItem);
		
		
		// ツールバーの作成
		toolBar = new JToolBar();
		//toolBar.setRollover(true);
		// アイコンボタン「New」の作成
		newButton = new JButton(new ImageIcon(getClass().getResource("icon/new.gif")));
		newButton.setActionCommand("new");			// アクションコマンドの設定
		newButton.addActionListener(this);			// イベントリスナを登録
		newButton.setMargin(new Insets(0,0,0,0));	// アイコン画像とボタンのマージンをなくす
		toolBar.add(newButton);
		// アイコンボタン「Open」の作成
		openButton = new JButton(new ImageIcon(getClass().getResource("icon/open.gif")));
		openButton.setActionCommand("open");
		openButton.addActionListener(this);
		openButton.setMargin(new Insets(0,0,0,0));
		toolBar.add(openButton);
		// アイコンボタン「Save」の作成
		saveButton = new JButton(new ImageIcon(getClass().getResource("icon/save.gif")));
		saveButton.setActionCommand("save");
		saveButton.addActionListener(this);
		saveButton.setMargin(new Insets(0,0,0,0));
		toolBar.add(saveButton);
		// アイコンボタン「Exit」の作成
		exitButton = new JButton(new ImageIcon(getClass().getResource("icon/exit.gif")));
		exitButton.setActionCommand("exit");
		exitButton.addActionListener(this);
		exitButton.setMargin(new Insets(0,0,0,0));
		toolBar.add(exitButton);
		
		// アイコンボタン間に境界線を引く
		toolBar.addSeparator();
		
		// アイコンボタン「Cut」の作成
		cutButton = new JButton(new ImageIcon(getClass().getResource("icon/cut.gif")));
		cutButton.setActionCommand("cut");
		cutButton.addActionListener(this);
		cutButton.setMargin(new Insets(0,0,0,0));
		toolBar.add(cutButton);
		// アイコンボタン「Copy」の作成
		copyButton = new JButton(new ImageIcon(getClass().getResource("icon/copy.gif")));
		copyButton.setActionCommand("copy");
		copyButton.addActionListener(this);
		copyButton.setMargin(new Insets(0,0,0,0));
		toolBar.add(copyButton);
		// アイコンボタン「Paste」の作成
		pasteButton = new JButton(new ImageIcon(getClass().getResource("icon/paste.gif")));
		pasteButton.setActionCommand("paste");
		pasteButton.addActionListener(this);
		pasteButton.setMargin(new Insets(0,0,0,0));
		toolBar.add(pasteButton);
		
		// アイコンボタン間に境界線を引く
		toolBar.addSeparator();
		
		// アイコンボタン「Undo」の作成
		undoButton = new JButton(new ImageIcon(getClass().getResource("icon/undo.gif")));
		undoButton.setActionCommand("undo");
		undoButton.addActionListener(this);
		undoButton.setMargin(new Insets(0,0,0,0));
		toolBar.add(undoButton);
		// アイコンボタン「Redo」の作成
		redoButton = new JButton(new ImageIcon(getClass().getResource("icon/redo.gif")));
		redoButton.setActionCommand("redo");
		redoButton.addActionListener(this);
		redoButton.setMargin(new Insets(0,0,0,0));
		toolBar.add(redoButton);
		
		// テキストエリアの作成
		tArea = new JTextArea();
		tArea.setLineWrap(true);				// 折り返しをサポート
		tArea.setWrapStyleWord(true);			// ワード境界で折り返すように設定
		tArea.setMargin(new Insets(1,5,1,5));	// テキストエリアのマージンを設定
		tArea.setFont(new Font(TAREA_FONT_NAME, Font.PLAIN, TAREA_FONT_SIZE));		// フォントの設定
		
		// スクロールペインを作成してテキストエリアをセット
		scrPane = new JScrollPane(tArea);
		
		// 「変換開始」ボタンの作成
		exeButton = new JButton("変換開始", new ImageIcon(getClass().getResource("icon/exe.gif")));
		exeButton.addActionListener(this);
		exeButton.setActionCommand("exe");
		exeButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		exeButton.setFont(new Font(BUTTON_FONT_NAME, Font.PLAIN, BUTTON_FONT_SIZE));
		exeButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.lightGray, Color.gray));
		
		// コンテナの作成
		Container content = this.getContentPane();
		content.setLayout(new BorderLayout());
		content.add(toolBar, BorderLayout.NORTH);
		content.add(scrPane, BorderLayout.CENTER);
		content.add(exeButton, BorderLayout.SOUTH);
		
		
		// フレームの×ボタンをクリックしたときにフレームを閉じないように設定する
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		// フレームの×ボタンをクリックしたときの処理
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				// 更新フラグがONのとき、ファイル保存確認ダイアログを表示する
				if( isUpdate == true ){
					int rc = showSaveConfirmDialog();
					// 「はい」を選択したとき、ファイル保存ダイアログを表示してプログラムを終了する
					if( rc == JOptionPane.YES_OPTION ){
						showFileSaveDialog();
						System.exit(0);
					}
					// 「いいえ」を選択したとき、プログラムを終了する
					else if( rc == JOptionPane.NO_OPTION ) System.exit(0);
				}
				// 更新フラグがOFFのとき、プログラムを終了する
				else System.exit(0);
				// フォーカスをテキストエリアに移動する
				tArea.requestFocus();
			}
		});
		
		
		// テキストエリアに文字がタイプされたときの処理
		tArea.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e){
				updateTitle();
			}
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
						File file = (File)fileList.get(0);
						
						// ドロップされたファイルをテキストエリアに表示する
						// 現在更新中のテキストエリア内の文字は強制的にクリアされる
						pathName = file.toString();
						readFile(pathName);
						
						/*
						// テキストエリア内が更新されている状態でD&Dをするとフリーズするバグが発生
						// 更新フラグがONのとき、ファイル破棄確認ダイアログを表示する
						if( isUpdate == true ){
							int rc = showDisposeConfirmDialog();
							// 「はい」を選択したとき、ファイルを読み込む
							if( rc == JOptionPane.YES_OPTION ){
								pathName = file.toString();
								readFile(pathName);
							}
						}
						// 更新フラグがOFFのとき、ファイルを読み込む
						else{
							pathName = file.toString();
							readFile(pathName);
						}
						*/
						
						tArea.requestFocus();
					}
				} catch (Exception ex) {
					// エラーメッセージダイアログを表示する
					showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
					System.exit(1);			// プログラムを終了する
				}
			}
		});
		
		// FileChooserの設定
		fileChooser = new JFileChooser(".");
		DialogFileFilter filter[] = {			// ファイルフィルタの生成
			new DialogFileFilter(".htm;.html" , "HTML ファイル (*.htm;*.html)"),
			new DialogFileFilter(".txt" , "テキスト ファイル (*.txt)")
		};
		for( int i = 0 ; i < filter.length ; i++ )
			fileChooser.addChoosableFileFilter(filter[i]);		// ファイルフィルタの追加
		
		// Look&Feel の設定
		try{
			// Look&Feelをシステム固有のスタイルに変更
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
			SwingUtilities.updateComponentTreeUI(fileChooser);
		} catch(Exception e){
		    System.exit(1);
		}
		
		// フィールド変数の初期化
		isUpdate = false;
		pathName = NON_FILE_NAME;
		befStr = "";
		aftStr = "";
		
		// 顔文字変換テーブルの作成
		convTable = makeConvertTable(SYS_DEF_FILE_NAME, USR_DEF_FILE_NAME);
		
		// フレームの表示
		this.setVisible(true);
		// テキストエリアにフォーカスを移動させる
		tArea.requestFocus();
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
		
		// メニュー項目「New」が選択されたときの処理
		if( action.equals("new") ){
			// 更新フラグがONのとき、ファイル破棄確認ダイアログを表示する
			if( isUpdate == true ){
				int rc = showDisposeConfirmDialog();
				// 「はい」を選択したとき、テキストエリアをクリアする
				if( rc == JOptionPane.YES_OPTION ) clearTextArea();
			}
			// 更新フラグがOFFのとき、テキストエリアをクリアする
			else clearTextArea();
			tArea.requestFocus();
		}
		
		
		// メニュー項目「Open」が選択されたときの処理
		else if( action.equals("open") ){
			// 更新フラグがONのとき、ファイル破棄確認ダイアログを表示する
			if( isUpdate == true ){
				int rc = showDisposeConfirmDialog();
				// 「はい」を選択したとき、ファイルオープンダイアログを表示する
				if( rc == JOptionPane.YES_OPTION ) showFileOpenDialog();
			}
			// 更新フラグがOFFのとき、ファイルオープンダイアログを表示する
			else showFileOpenDialog();
			tArea.requestFocus();
		}
		
		// メニュー項目「Save」が選択されたときの処理
		else if( action.equals("save") ){
			showFileSaveDialog();		// ファイル保存ダイアログを表示する
			tArea.requestFocus();
		}
		
		
		// メニュー項目「Exit」が選択されたときの処理
		else if( action.equals("exit") ) {
			// 更新フラグがONのとき、ファイル保存確認ダイアログを表示する
			if( isUpdate == true ){
				int rc = showSaveConfirmDialog();
				// 「はい」を選択したとき、ファイル保存ダイアログを表示する
				if( rc == JOptionPane.YES_OPTION ) showFileSaveDialog();
				// 「いいえ」を選択したとき、プログラムを終了する
				else if( rc == JOptionPane.NO_OPTION ) System.exit(0);
			}
			// 更新フラグがOFFのとき、プログラムを終了する
			else System.exit(0);
			// フォーカスをテキストエリアに移動する
			tArea.requestFocus();
		}
		
		// メニュー項目「Undo」が選択されたときの処理
		else if( action.equals("undo") ) {
			tArea.setText(befStr);
			tArea.requestFocus();
		}
		
		// メニュー項目「Redo」が選択されたときの処理
		else if( action.equals("redo") ) {
			tArea.setText(aftStr);
			tArea.requestFocus();
		}
		
		// メニュー項目「Cut」が選択されたときの処理
		else if( action.equals("cut") ) {
			tArea.cut();	// テキストエリア内で選択された文字列を切り取ってクリップボードに転送する
			updateTitle();
			tArea.requestFocus();
		}
		
		// メニュー項目「Copy」が選択されたときの処理
		else if( action.equals("copy") ) {
			tArea.copy();	// テキストエリア内で選択された文字列をクリップボードに転送する
			updateTitle();
			tArea.requestFocus();
		}
		
		// メニュー項目「Paste」が選択されたときの処理
		else if( action.equals("paste") ) {
			tArea.paste();	// クリップボードにコピーされている文字列をテキストエリアに貼り付ける
			updateTitle();
			tArea.requestFocus();
		}
		
		// メニュー項目「SelectAll」が選択されたときの処理
		else if( action.equals("selectAll") ) {
			tArea.selectAll();	// テキストエリア内の全ての文字を選択する
		}
		
		// メニュー項目「変換データの登録」が選択されたときの処理
		else if( action.equals("appendPattern") ) {
			// 顔文字変換データ登録のダイアログの生成
			DataAppendManager manager = new DataAppendManager(this, convTable);
			tArea.requestFocus();
		}
		
		// ボタン「変換開始」が押されたときの処理
		else if( action.equals("exe") ) {
			convertTextArea();		// テキストエリア内の文字列の変換処理を行う
			updateTitle();
			tArea.requestFocus();
		}
	}
	
	
	/**
	 * 顔文字変換定義データファイルを読み込んで顔文字変換テーブルを作成します．
	 * @param sysDefFileName システム定義の顔文字変換データファイル名
	 * @param usrDefFileName ユーザ定義の顔文字変換データファイル名
	 * @return 作成した顔文字変換テーブル
	 */
	private ConvertTable makeConvertTable(String sysDefFileName, String usrDefFileName) {
		ConvertTable convTable = new ConvertTable();	// 顔文字変換テーブル
		
		// システム定義ファイルが存在しない場合は終了
		if ( (new File(sysDefFileName)).exists() != true ){
			// エラーメッセージダイアログを表示する
			showErrorDialog(ERROR_DIALOG_TITLE, "ファイル " + sysDefFileName + " が見つかりません．");
			System.exit(1);		// プログラムを終了する
		}
		// システム定義の顔文字変換データファイルを読み込む
		convTable = readDefFile(convTable, sysDefFileName, DEF_FILE_SEGMENT);
		// ユーザ定義ファイルが存在しない場合は読み込み処理を終了
		if ( (new File(usrDefFileName)).exists() != true ) return convTable;
		// ユーザ定義の顔文字変換データファイルを読み込む
		convTable = readDefFile(convTable, usrDefFileName, DEF_FILE_SEGMENT);
		
		return convTable;
	}
	
	
	/**
	 * 顔文字変換データファイルを読み込んで変換テーブルに追加します．
	 * @param convTable 変換テーブル
	 * @param fileName 顔文字変換データファイル名
	 * @param segment 変換データファイルにおける区切り文字列
	 * @return 読み込み処理後の変換テーブル
	 */
	private ConvertTable readDefFile(ConvertTable convTable, String fileName, String segment){
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader in = new BufferedReader(fr);
			while(true){
				String str = in.readLine();
				if ( str == null ) break;
				// 読み込んだ顔文字のペアを顔文字変換テーブルに追加
				StringTokenizer st = new StringTokenizer(str, segment);
				if( st.countTokens() == 1 ){
					String befStr = st.nextToken();
					String aftStr = "";
					convTable.setElement(befStr, aftStr);
				}
				else if( st.countTokens() >= 2 ){
					String befStr = st.nextToken();
					String aftStr = st.nextToken();
					convTable.setElement(befStr, aftStr);
				}
			}
			in.close();
			fr.close();
		} catch (Exception e) {
			// エラーメッセージダイアログを表示する
			showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
			System.exit(1);			// プログラムを終了する
		}
		return convTable;
	}
	
	
	/**
	 * ファイルオープンダイアログを表示します．
	 */
	private void showFileOpenDialog(){
		int intRet = fileChooser.showOpenDialog(this);		// 「ファイルを開く」ダイアログボックスの表示
		// 「OK」ボタンが押されたときの処理
		if( intRet == JFileChooser.APPROVE_OPTION ){
			File file = fileChooser.getSelectedFile();
			pathName = file.getAbsolutePath();		// 選択されたファイルの絶対パスを取得する
			readFile(pathName);		// ファイルを読み込んでテキストエリアに表示する
		}
	}
	
	
	/**
	 * ファイル保存ダイアログを表示します．
	 */
	private void showFileSaveDialog(){
		fileChooser.setSelectedFile(new File((new File(pathName)).getName()));
		int intRet = fileChooser.showSaveDialog(this);		// 「ファイルの保存」ダイアログボックスの表示
		// 「OK」ボタンが押されたときの処理
		if( intRet == JFileChooser.APPROVE_OPTION ){
			File file = fileChooser.getSelectedFile();		// 選択されたファイル
			DialogFileFilter filter = (DialogFileFilter)fileChooser.getFileFilter();	// 選択されたファイルフィルタ
			// 選択されたファイルの拡張子がファイルフィルタに登録されている拡張子と違う場合
			if( filter.accept(file) != true ){
				// 拡張子を追加
				pathName = file.getAbsolutePath() + (String)filter.getExtList().get(0);
			}
			else pathName = file.getAbsolutePath();
			saveFile(pathName);		// テキストエリア内の文字列をファイルに保存する
		}
	}
	
	
	/**
	 * ファイルを読み込んでテキストエリアに表示します．
	 * @param path 読み込むファイルのパス
	 */
	private void readFile(String path){
		try{
			String str;
			FileReader fr = new FileReader(path);
			BufferedReader in = new BufferedReader(fr);
			
			this.setTitle( TITLE_NAME + " - " +  path );	// タイトルバーの設定
			isUpdate = false;							// 更新フラグをOFFにする
			tArea.setText(in.readLine());				// 1行目の文字列をテキストエリアに書き込む
			while( (str = in.readLine()) != null ){		// 2行目以降の文字列は改行コードを挟んで追加
				tArea.append("\n" + str);
			}
			in.close();
			fr.close();
		}catch(Exception ex){
			// エラーメッセージダイアログを表示する
			showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
			System.exit(1);			// プログラムを終了する
		}
	}
	
	
	/**
	 * テキストエリア内の文字列をファイルに保存します．
	 * @param path 保存するファイルのパス
	 */
	private void saveFile(String path){
		try{
			FileWriter fw = new FileWriter(path);
			PrintWriter out = new PrintWriter(fw);
			
			this.setTitle( TITLE_NAME + " - " + pathName );		// タイトルバーの設定
			isUpdate = false;				// 更新フラグをOFFにする
			out.write(tArea.getText());		// テキストエリア内の文字列をファイルに書き出す
			out.close();
			fw.close();
		} catch(Exception e){
			// エラーメッセージダイアログを表示する
			showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
			System.exit(1);			// プログラムを終了する
		}
	}
	
	
	/**
	 * テキストエリア内の文字列が更新されたときにタイトルの表示を変更します．
	 */
	private void updateTitle(){
		if( isUpdate == false ){
			isUpdate = true;
			this.setTitle(UPDATED_TITLE_NAME + " - " + pathName);	// タイトル名を変更する
		}
	}
	
	
	/**
	 * テキストエリア内の文字列をクリアします．
	 */
	private void clearTextArea(){
		pathName = NON_FILE_NAME;		// パス名の設定
		this.setTitle( TITLE_NAME + " - " + pathName );		// タイトルバーの設定
		isUpdate = false;				// 更新フラグをOFFにする
		tArea.setText("");				// テキストエリア内の文字をすべて消去する
		undoMItem.setEnabled(false);	// メニュー項目「Undo」を無効にする
		redoMItem.setEnabled(false);	// メニュー項目「Redo」を無効にする
	}
	
	
	/**
	 * 顔文字変換処理を行います．
	 */
	private void convertTextArea(){
		befStr = tArea.getText();				// テキストエリア内の文字列の取得
		aftStr = convTable.convert(befStr);		// 顔文字変換処理
		tArea.setText(aftStr);					// 変換後の文字列をテキストエリアに表示
		undoMItem.setEnabled(true);				// メニュー項目「Undo」を有効にする
		redoMItem.setEnabled(true);				// メニュー項目「Redo」を有効にする
	}
	
	
	/**
	 * ファイルの破棄確認ダイアログを表示します．
	 * @return ユーザが選択したオプションを示すint値
	 */
	private int showDisposeConfirmDialog(){
		// ダイアログの作成
		JOptionPane optPane = new JOptionPane(pathName + DISPOSE_CONFIRM_MESSAGE, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
		JDialog dialog = optPane.createDialog(this.getContentPane(), TITLE_NAME);
		
		// ダイアログのLook&FeelをWindowsスタイルに変更
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(dialog);
		} catch(Exception e){
			// エラーメッセージダイアログを表示する
			showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
			System.exit(1);			// プログラムを終了する
		}
		
		// ダイアログを表示
		dialog.setVisible(true);
		
		// 選択したオプションの取得
		Object opt = optPane.getValue();
		if( opt instanceof Integer ) return ((Integer)opt).intValue();
		else return JOptionPane.CLOSED_OPTION;
	}
	
	
	/**
	 * ファイルの保存確認ダイアログを表示します．
	 * @return ユーザが選択したオプションを示すint値
	 */
	private int showSaveConfirmDialog(){
		// ダイアログの作成
		JOptionPane optPane = new JOptionPane(pathName + SAVE_CONFIRM_MESSAGE, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION);
		JDialog dialog = optPane.createDialog(this.getContentPane(), TITLE_NAME);
		
		// ダイアログのLook&FeelをWindowsスタイルに変更
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(dialog);
		} catch(Exception e){
			// エラーメッセージダイアログを表示する
			showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
			System.exit(1);			// プログラムを終了する
		}
		
		// ダイアログを表示
		dialog.setVisible(true);
		
		// 選択したオプションの取得
		Object opt = optPane.getValue();
		if( opt instanceof Integer ) return ((Integer)opt).intValue();
		else return JOptionPane.CLOSED_OPTION;
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



/**
 * ファイルの読み込み・保存ダイアログにおけるファイルフィルタを表すクラスです．
 */
class DialogFileFilter extends javax.swing.filechooser.FileFilter {
	
	/****************************************/
	/* 　　 　  フィールド定義 　　    　　 */
	/****************************************/
	
	/** ファイルフィルタに指定する拡張子の区切り文字 */
	static final String EXTENTION_SEGMENT = ";";
	
	/** フィルタに設定する拡張子のリスト */
	private Vector extList;
	/** フィルタの説明文 */
	private String msg;
	
	
	/****************************************/
	/* 　　 　  コンストラクタ 　　    　　 */
	/****************************************/
	
	/*
	 * フィルタに設定する拡張子とフィルタの説明文を指定してDialogFileFilterを初期化します．
	 * @param ext 拡張子の文字列表現（複数の拡張子を指定する場合は，文字 EXTENTION_SEGMENT で区切る）
	 * @param msg フィルタの説明文
	 */
	public DialogFileFilter(String exts, String msg) {
		// 拡張子の文字列表現からそれぞれの拡張子を抽出してリストに格納する
		this.extList = new Vector();
		StringTokenizer st = new StringTokenizer(exts, EXTENTION_SEGMENT);
		while (st.hasMoreTokens()) {
			extList.add(st.nextToken());
		}
		this.msg = msg;
	}
	
	
	/**************************************/
	/* 　　 　  メソッド定義 　　    　　 */
	/**************************************/
	
	/*
	 * フィルタが指定されたファイルを受け付けるかどうかを返します．
	 * @param file ファイル名
	 * @return このフィルタが指定されたファイルを受け付ける場合にtrueを返します．
	 */
	public boolean accept(File file) {
		if ( file.isDirectory() == true ) return true;
		for( int i = 0; i < extList.size(); i++ ){
			String ext = (String)extList.get(i);
			if( file.getName().endsWith(ext) == true ) return true;
		}
		return false;
	}
	
	
	/*
	 * 拡張子のリストを取得します．
	 * @return 拡張子のリスト
	 */
	public Vector getExtList() {
		return extList;
	}
	
	
	/*
	 * フィルタの説明文を取得します．
	 * @return フィルタの説明文
	 */
	public String getDescription() {
		return msg;
	}
	
}