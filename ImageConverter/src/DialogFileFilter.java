import java.io.*;
import java.util.*;

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