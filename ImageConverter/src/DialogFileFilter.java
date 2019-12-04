import java.io.*;
import java.util.*;

/**
 * �t�@�C���̓ǂݍ��݁E�ۑ��_�C�A���O�ɂ�����t�@�C���t�B���^��\���N���X�ł��D
 */
class DialogFileFilter extends javax.swing.filechooser.FileFilter {
	
	/****************************************/
	/* �@�@ �@  �t�B�[���h��` �@�@    �@�@ */
	/****************************************/
	
	/** �t�@�C���t�B���^�Ɏw�肷��g���q�̋�؂蕶�� */
	static final String EXTENTION_SEGMENT = ";";
	
	/** �t�B���^�ɐݒ肷��g���q�̃��X�g */
	private Vector extList;
	/** �t�B���^�̐����� */
	private String msg;
	
	
	/****************************************/
	/* �@�@ �@  �R���X�g���N�^ �@�@    �@�@ */
	/****************************************/
	
	/*
	 * �t�B���^�ɐݒ肷��g���q�ƃt�B���^�̐��������w�肵��DialogFileFilter�����������܂��D
	 * @param ext �g���q�̕�����\���i�����̊g���q���w�肷��ꍇ�́C���� EXTENTION_SEGMENT �ŋ�؂�j
	 * @param msg �t�B���^�̐�����
	 */
	public DialogFileFilter(String exts, String msg) {
		// �g���q�̕�����\�����炻�ꂼ��̊g���q�𒊏o���ă��X�g�Ɋi�[����
		this.extList = new Vector();
		StringTokenizer st = new StringTokenizer(exts, EXTENTION_SEGMENT);
		while (st.hasMoreTokens()) {
			extList.add(st.nextToken());
		}
		this.msg = msg;
	}
	
	
	/**************************************/
	/* �@�@ �@  ���\�b�h��` �@�@    �@�@ */
	/**************************************/
	
	/*
	 * �t�B���^���w�肳�ꂽ�t�@�C�����󂯕t���邩�ǂ�����Ԃ��܂��D
	 * @param file �t�@�C����
	 * @return ���̃t�B���^���w�肳�ꂽ�t�@�C�����󂯕t����ꍇ��true��Ԃ��܂��D
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
	 * �g���q�̃��X�g���擾���܂��D
	 * @return �g���q�̃��X�g
	 */
	public Vector getExtList() {
		return extList;
	}
	
	
	/*
	 * �t�B���^�̐��������擾���܂��D
	 * @return �t�B���^�̐�����
	 */
	public String getDescription() {
		return msg;
	}
	
}