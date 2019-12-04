/**
 * ���ʂŎg�p���闘�֐��̍������\�b�h���W�߂��N���X�ł��D
 * @author korotan
 * �쐬��:2004.11.25
 */
public class ExtendedUtil {
    /**
	 * �t�@�C���̃T�C�Y�𕶎���ɕϊ����܂��D
	 * @param size �t�@�C���T�C�Y
	 * @return �t�@�C���T�C�Y�̕�����\��
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
	 * �����_��n�ʂ�؂�グ�����l��Ԃ��܂��D
	 * @param val �؂�グ����O�̒l
	 * @param n �؂�グ���錅��
	 * @return �؂�グ��̒l
	 */
	public static double ceil(double val, int n) {
		val = Math.ceil(val * Math.pow(10, n-1)) / Math.pow(10, n-1);
		return val;
	}
	
	
	/**
	 * �����_��n�ʂ�؂�̂Ă����l��Ԃ��܂��D
	 * @param val �؂�̂đO�̒l
	 * @param n �؂�̂Ă��錅��
	 * @return �؂�̂Č�̒l
	 */
	public static double floor(double val, int n) {
		val = Math.floor(val * Math.pow(10, n-1)) / Math.pow(10, n-1);
		return val;
	}
	
	
	/**
	 * �����_��n�ʂ��l�̌ܓ������l��Ԃ��܂��D
	 * @param val �l�̌ܓ�����l
	 * @param n �l�̌ܓ����錅��
	 * @return �l�̌ܓ���̒l
	 */
	public static double round(double val, int n) {
		val = Math.round( val * Math.pow(10, n-1) ) / Math.pow(10, n-1);
		return val;
	}
	
}
