/**
 * 共通で使用する利便性の高いメソッドを集めたクラスです．
 * @author korotan
 * 作成日:2004.11.25
 */
public class ExtendedUtil {
    /**
	 * ファイルのサイズを文字列に変換します．
	 * @param size ファイルサイズ
	 * @return ファイルサイズの文字列表現
	 */
	public static String getSizeString(long size){
	    String str = "";
	    
	    if(size < 1024){
	        str = size + " B";
	    }
	    else if(size >= 1024 && size < 1024*1024){
	        if (size / 1024 < 100){
	            str = String.valueOf(floor((double)size / 1024, 2)) + " KB";
	        }
	        else{
	            str = (size / 1024) + " KB";
	        }
	    }
	    else{
	        if ((size / (1024 * 1024)) < 100){
	            str = String.valueOf(floor((double)size / (1024 * 1024), 2)) + " MB";
	        }
	        else{
	            str = (size / (1024 * 1024)) + " MB";
	        }
	    }
	    return str;
	}
	
	
	/**
	 * 小数点第n位を切り上げした値を返します．
	 * @param val 切り上げする前の値
	 * @param n 切り上げする桁数
	 * @return 切り上げ後の値
	 */
	public static double ceil(double val, int n) {
		val = Math.ceil(val * Math.pow(10, n-1)) / Math.pow(10, n-1);
		return val;
	}
	
	
	/**
	 * 小数点第n位を切り捨てした値を返します．
	 * @param val 切り捨て前の値
	 * @param n 切り捨てする桁数
	 * @return 切り捨て後の値
	 */
	public static double floor(double val, int n) {
		val = Math.floor(val * Math.pow(10, n-1)) / Math.pow(10, n-1);
		return val;
	}
	
	
	/**
	 * 小数点第n位を四捨五入した値を返します．
	 * @param val 四捨五入する値
	 * @param n 四捨五入する桁数
	 * @return 四捨五入後の値
	 */
	public static double round(double val, int n) {
		val = Math.round( val * Math.pow(10, n-1) ) / Math.pow(10, n-1);
		return val;
	}
	
}
