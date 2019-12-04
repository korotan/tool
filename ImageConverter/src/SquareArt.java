import java.awt.*;
import java.awt.image.*;

/**
 * □Artを表すクラスです.
 */
public class SquareArt{
	
	/****************************************/
	/* 　　 　  フィールド定義 　　    　　 */
	/****************************************/
	
	/** ピクセル幅 */
	private int width;
	/** ピクセル高さ */
	private int height;
	/** スクウェアのサイズ */
	private int pixel;
	/** 変換後のデータサイズ */
	private long size;
	
	
	/****************************************/
	/* 　　 　  コンストラクタ 　　    　　 */
	/****************************************/
	
	/**
	 * オブジェクトを初期化します．
	 * @param width ピクセル幅
	 * @param height ピクセル高さ
	 * @param size スクウェアのサイズ
	 */
	public SquareArt(Image img, int width, int height, int size) throws InterruptedException{
		this.width = width;
		this.height = height;
		this.pixel = size;
		this.size = calcSize(img);
	}
	
	
	/**************************************/
	/* 　　 　  メソッド定義 　　    　　 */
	/**************************************/
	
	/**
	 * 変換後のHTMLソースのバイト数を計算します．
	 * @param img 画像イメージ
	 * @return 変換後のHTMLソースのバイト数
	 */
	private int calcSize(Image img) throws InterruptedException{
	    int dataSize;
	    
	    dataSize = 32 + (String.valueOf(pixel)).length();
	    // 画像からピクセルを抽出
		PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, true);
		// ピクセルを読み込む
		pg.grabPixels();
		// 画像からピクセルデータを取得
		int px[] = (int[])pg.getPixels();
		// ピクセルデータを文字列に変換してバイト数を計算する
		for(int i = 0; i < px.length; i++ ){
			if( i % width != 0 && px[i] == px[i-1] ) dataSize += 2;
			else{
				if(i % width != 0) dataSize += 7;
				dataSize += 24;
			}
			if( i % width == width - 1 ){
			    dataSize += 12;
			}
		}
		dataSize += 13;
		
	    return dataSize;
	}
	
	
	/**
	 * 画像イメージをスクウェアアート（HTMLソース）に変換します．
	 * @param img 画像イメージ
	 * @return 変換されたHTMLソース
	 */
	public String convert(Image img) throws InterruptedException{
	    StringBuffer bf = new StringBuffer();
	    
	    bf.append("<FONT STYLE=\"font-size:" + pixel + "px\"><TT>\n");
	    // 画像からピクセルを抽出
		PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, true);
		// ピクセルを読み込む
		pg.grabPixels();
		// 画像からピクセルデータを取得
		int px[] = (int[])pg.getPixels();
		// ピクセルデータを文字列に変換してHTML形式で書き出す
		for(int i = 0; i < px.length; i++ ){
			// 同じ色が連続している場合には処理を簡略化する
			if( i % width != 0 && px[i] == px[i-1] ) bf.append("■");
			// ピクセルデータをカラーコードに変換
			else{
				if(i % width != 0) bf.append("</FONT>");
				bf.append("<FONT COLOR=\"#" + toColorCode(px[i]) + "\">■");
			}
			// 行の折り返し処理
			if( i % width == width - 1 ){
			    bf.append("</FONT><BR>\n");
			}
		}
		bf.append("</TT></FONT>");
		
		return bf.toString();
	}
	
	
	/**
	 * ピクセルデータをカラーコードに変換します．
	 * @param pixel ピクセルデータ
	 * @return カラーコードの文字列表現
	 */
	private String toColorCode(int pixel){
		// 色情報の抽出
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >>  8) & 0xff;
		int blue = (pixel) & 0xff;
		// int型の色情報を16進数の文字列に変換
		String strRed = Integer.toString(red, 16).toUpperCase();
		String strGreen = Integer.toString(green, 16).toUpperCase();
		String strBlue = Integer.toString(blue, 16).toUpperCase();
		// 1桁の文字列を2桁に変換する（先頭に0を補う）
		if(strRed.length() == 1) strRed = "0" + strRed;
		if(strGreen.length() == 1) strGreen = "0" + strGreen;
		if(strBlue.length() == 1) strBlue = "0" + strBlue;
		
		return strRed + strGreen + strBlue;
	}
	
	
	/**
	 * ピクセル幅を取得します．
	 * @return ピクセル幅
	 */
	public int getWidth() {
		return width;
	}
	
	
	/**
	 * ピクセル高さを取得します．
	 * @return ピクセル高さ
	 */
	public int getHeight() {
		return height;
	}
	
	
	/**
	 * スクウェアのサイズを取得します．
	 * @return スクウェアのサイズ
	 */
	public int getPixel() {
		return pixel;
	}
	
	
	/**
	 * スクウェアのサイズを設定します．
	 * @param size スクウェアのサイズ
	 */
	public void setPixel(int size) {
		this.pixel = size;
	}
	
	
	/**
	 * 変換後のデータサイズを取得します．
	 * @return 変換後のデータサイズ
	 */
	public long getSize() {
		return size;
	}

}