import java.io.*;
import java.util.*;

/**
 * 顔文字変換テーブルを表すクラスです．
 */
public class ConvertTable{
	
	/****************************************/
	/* 　　 　  フィールド定義 　　    　　 */
	/****************************************/
	
	/** 半角/全角文字対応テーブル */
	static final char CHAR_PAIR_TABLE[][] = {
		{' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', 
			'<', '=', '>', '?', '@', '[', ']', '^', '_', '`', '{', '|', '}', '\\'},
		{'　', '！', '”', '＃', '＄', '％', '＆', '’', '（', '）', '＊', '＋', '，', '－', '．', '／', '：', '；', 
			'＜', '＝', '＞', '？', '＠', '［', '］', '＾', '＿', '｀', '｛', '｜', '｝', '￥'}
	};
	
	/** 変換前の顔文字のリスト */
	private Vector befList;
	/** 変換後の顔文字のリスト */
	private Vector aftList;
	
	/** 登録されている顔文字変換の総数 */
	private int elementNum;
	
	
	/****************************************/
	/* 　　 　  コンストラクタ 　　    　　 */
	/****************************************/
	
	/**
	 * ConvertTableを初期化します．
	 */
	ConvertTable(){
		this.befList = new Vector();
		this.aftList = new Vector();
		this.elementNum = 0;
	}
	
	
	/**
	 * 変換前と後の顔文字のリストを指定してConvertTableを初期化します．
	 * @param befList 変換前の顔文字のリスト
	 * @param aftList 変換後の顔文字のリスト
	 */
	ConvertTable(Vector befList, Vector aftList){
		this.befList = befList;
		this.aftList = aftList;
		this.elementNum = befList.size();
	}
	
	
	/**************************************/
	/* 　　 　  メソッド定義 　　    　　 */
	/**************************************/
	
	/**
	 * 文字列str内の顔文字を変換します．<BR>
	 * 顔文字リストの末尾からマッチングを行います．
	 * @param str 変換対象となる文字列
	 * @return 変換処理後の文字列
	 */
	public String convert(String str){
		for( int i = elementNum - 1; i >= 0; i-- ){
			String befStr = (String)befList.get(i);
			String aftStr = (String)aftList.get(i);
			str = replaceString(str, befStr, aftStr);
		}
		return str;
	}
	
	
	/**
	 * 変換する顔文字の組をリストに追加します．
	 * @param befStr 変換前の顔文字
	 * @param aftStr 変換後の顔文字
	 */
	public void setElement(String befStr, String aftStr){
		befList.add(befStr);
		aftList.add(aftStr);
		elementNum++;
	}
	
	
	/**
	 * 変換前の顔文字リストを取得します．
	 * @return 変換前の顔文字リスト
	 */
	public Vector getBefList(){
		return befList;
	}
	
	
	/**
	 * 変換後の顔文字リストを取得します．
	 * @return 変換後の顔文字リスト
	 */
	public Vector getAftList(){
		return aftList;
	}
	
	
	/**
	 * 登録されている顔文字変換の総数を取得します．
	 * @return 登録されている顔文字変換の総数
	 */
	public int getElementNum(){
		return elementNum;
	}
	
	
	/** 
	 * 文字列str内の特定の文字列oldStrを別の文字列newStrに置換した文字列を返します．
	 * @param str 置換対象となる文字列全体
	 * @param oldStr 置換する文字列
	 * @param newStr 置換後の文字列
	 * @return 置換後の文字列全体
	 */
	private String replaceString(String str, String oldStr, String newStr) {
		String replacedStr = "";		// 置換後の文字列全体
		int beginIndex = 0;				// 置換文字列の検索開始位置
		int replacedIndex = 0;			// 置換を開始する位置
		
		String doubleByteStr = toDoubleByteString(str);		// 文字列str内の半角文字を全角に変換した文字列
		
		while(true){
			// 置換文字列の位置を検索
			replacedIndex = doubleByteStr.indexOf(oldStr, beginIndex);
			// 置換する文字列が存在しない場合には処理を終了する
			if( replacedIndex == -1 ){
				replacedStr = replacedStr + str.substring(beginIndex);
				break;
			}
			// 置換処理
			replacedStr = replacedStr + str.substring(beginIndex, replacedIndex) + newStr;
			beginIndex = replacedIndex + oldStr.length();
		}
		return replacedStr;
	}
	
	
	/**
	 * 文字列str内の半角英数文字を全角英数文字に変換します．
	 * @param 変換する文字列
	 * @return 変換後の文字列
	 */
	public static String toDoubleByteString(String str) {
		char ch[] = new char[str.length()];
		for( int i = 0; i < str.length(); i++ ){
			ch[i] = toDoubleByteChar(str.charAt(i));
		}
		return String.copyValueOf(ch);
	}
	
	
	/**
	 * 半角英数文字を全角英数文字に変換します．
	 * @param ch 変換する半角英数文字
	 * @return 変換後の全角英数文字
	 */
	public static char toDoubleByteChar(char ch) {
		if( (ch - 'a') >= 0 && (ch - 'a') < 26 )
			ch = (char)(ch - 'a' + 'ａ');
		if( (ch - 'A') >= 0 && (ch - 'A') < 26 )
			ch = (char)(ch - 'A' + 'Ａ');
		else if( (ch - '0') >= 0 && (ch - '0') < 10 )
			ch = (char)(ch - '0' + '０');
		else{
			for( int i = 0; i < CHAR_PAIR_TABLE[0].length; i++ ){
				if( ch == CHAR_PAIR_TABLE[0][i] ) {ch = CHAR_PAIR_TABLE[1][i]; break;}
			}
		}
		return ch;
	}
	
}