import java.io.*;
import java.util.*;

/**
 * �當���ϊ��e�[�u����\���N���X�ł��D
 */
public class ConvertTable{
	
	/****************************************/
	/* �@�@ �@  �t�B�[���h��` �@�@    �@�@ */
	/****************************************/
	
	/** ���p/�S�p�����Ή��e�[�u�� */
	static final char CHAR_PAIR_TABLE[][] = {
		{' ', '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', 
			'<', '=', '>', '?', '@', '[', ']', '^', '_', '`', '{', '|', '}', '\\'},
		{'�@', '�I', '�h', '��', '��', '��', '��', '�f', '�i', '�j', '��', '�{', '�C', '�|', '�D', '�^', '�F', '�G', 
			'��', '��', '��', '�H', '��', '�m', '�n', '�O', '�Q', '�M', '�o', '�b', '�p', '��'}
	};
	
	/** �ϊ��O�̊當���̃��X�g */
	private Vector befList;
	/** �ϊ���̊當���̃��X�g */
	private Vector aftList;
	
	/** �o�^����Ă���當���ϊ��̑��� */
	private int elementNum;
	
	
	/****************************************/
	/* �@�@ �@  �R���X�g���N�^ �@�@    �@�@ */
	/****************************************/
	
	/**
	 * ConvertTable�����������܂��D
	 */
	ConvertTable(){
		this.befList = new Vector();
		this.aftList = new Vector();
		this.elementNum = 0;
	}
	
	
	/**
	 * �ϊ��O�ƌ�̊當���̃��X�g���w�肵��ConvertTable�����������܂��D
	 * @param befList �ϊ��O�̊當���̃��X�g
	 * @param aftList �ϊ���̊當���̃��X�g
	 */
	ConvertTable(Vector befList, Vector aftList){
		this.befList = befList;
		this.aftList = aftList;
		this.elementNum = befList.size();
	}
	
	
	/**************************************/
	/* �@�@ �@  ���\�b�h��` �@�@    �@�@ */
	/**************************************/
	
	/**
	 * ������str���̊當����ϊ����܂��D<BR>
	 * �當�����X�g�̖�������}�b�`���O���s���܂��D
	 * @param str �ϊ��ΏۂƂȂ镶����
	 * @return �ϊ�������̕�����
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
	 * �ϊ�����當���̑g�����X�g�ɒǉ����܂��D
	 * @param befStr �ϊ��O�̊當��
	 * @param aftStr �ϊ���̊當��
	 */
	public void setElement(String befStr, String aftStr){
		befList.add(befStr);
		aftList.add(aftStr);
		elementNum++;
	}
	
	
	/**
	 * �ϊ��O�̊當�����X�g���擾���܂��D
	 * @return �ϊ��O�̊當�����X�g
	 */
	public Vector getBefList(){
		return befList;
	}
	
	
	/**
	 * �ϊ���̊當�����X�g���擾���܂��D
	 * @return �ϊ���̊當�����X�g
	 */
	public Vector getAftList(){
		return aftList;
	}
	
	
	/**
	 * �o�^����Ă���當���ϊ��̑������擾���܂��D
	 * @return �o�^����Ă���當���ϊ��̑���
	 */
	public int getElementNum(){
		return elementNum;
	}
	
	
	/** 
	 * ������str���̓���̕�����oldStr��ʂ̕�����newStr�ɒu�������������Ԃ��܂��D
	 * @param str �u���ΏۂƂȂ镶����S��
	 * @param oldStr �u�����镶����
	 * @param newStr �u����̕�����
	 * @return �u����̕�����S��
	 */
	private String replaceString(String str, String oldStr, String newStr) {
		String replacedStr = "";		// �u����̕�����S��
		int beginIndex = 0;				// �u��������̌����J�n�ʒu
		int replacedIndex = 0;			// �u�����J�n����ʒu
		
		String doubleByteStr = toDoubleByteString(str);		// ������str���̔��p������S�p�ɕϊ�����������
		
		while(true){
			// �u��������̈ʒu������
			replacedIndex = doubleByteStr.indexOf(oldStr, beginIndex);
			// �u�����镶���񂪑��݂��Ȃ��ꍇ�ɂ͏������I������
			if( replacedIndex == -1 ){
				replacedStr = replacedStr + str.substring(beginIndex);
				break;
			}
			// �u������
			replacedStr = replacedStr + str.substring(beginIndex, replacedIndex) + newStr;
			beginIndex = replacedIndex + oldStr.length();
		}
		return replacedStr;
	}
	
	
	/**
	 * ������str���̔��p�p��������S�p�p�������ɕϊ����܂��D
	 * @param �ϊ����镶����
	 * @return �ϊ���̕�����
	 */
	public static String toDoubleByteString(String str) {
		char ch[] = new char[str.length()];
		for( int i = 0; i < str.length(); i++ ){
			ch[i] = toDoubleByteChar(str.charAt(i));
		}
		return String.copyValueOf(ch);
	}
	
	
	/**
	 * ���p�p��������S�p�p�������ɕϊ����܂��D
	 * @param ch �ϊ����锼�p�p������
	 * @return �ϊ���̑S�p�p������
	 */
	public static char toDoubleByteChar(char ch) {
		if( (ch - 'a') >= 0 && (ch - 'a') < 26 )
			ch = (char)(ch - 'a' + '��');
		if( (ch - 'A') >= 0 && (ch - 'A') < 26 )
			ch = (char)(ch - 'A' + '�`');
		else if( (ch - '0') >= 0 && (ch - '0') < 10 )
			ch = (char)(ch - '0' + '�O');
		else{
			for( int i = 0; i < CHAR_PAIR_TABLE[0].length; i++ ){
				if( ch == CHAR_PAIR_TABLE[0][i] ) {ch = CHAR_PAIR_TABLE[1][i]; break;}
			}
		}
		return ch;
	}
	
}