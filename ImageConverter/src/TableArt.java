import java.awt.*;
import java.awt.image.*;

/**
 * TableArt��\���N���X�ł�.
 */
public class TableArt{
	
	/****************************************/
	/* �@�@ �@  �t�B�[���h��` �@�@    �@�@ */
	/****************************************/
	
	/** �s�N�Z���� */
	private int width;
	/** �s�N�Z������ */
	private int height;
	/** �ϊ���̃f�[�^�T�C�Y */
	private long size;
	
	
	/****************************************/
	/* �@�@ �@  �R���X�g���N�^ �@�@    �@�@ */
	/****************************************/
	
	/**
	 * �I�u�W�F�N�g�����������܂��D
	 * @param width �s�N�Z����
	 * @param height �s�N�Z������
	 */
	public TableArt(Image img, int width, int height){
		this.width = width;
		this.height = height;
		this.size = calcSize(img);
	}
	
	
	/**************************************/
	/* �@�@ �@  ���\�b�h��` �@�@    �@�@ */
	/**************************************/
	
	/**
	 * �ϊ����HTML�\�[�X�̃o�C�g�����v�Z���܂��D
	 * @param img �摜�C���[�W
	 * @return �ϊ����HTML�\�[�X�̃o�C�g��
	 */
	private int calcSize(Image img){
	    int dataSize;
	    
	    dataSize = 61 + (String.valueOf(width)).length() + (String.valueOf(height)).length();
	    dataSize += (width * 27 + 10) * height;
		dataSize += 9;
		
	    return dataSize;
	}
	
	
	/**
	 * �摜�C���[�W���X�N�E�F�A�A�[�g�iHTML�\�[�X�j�ɕϊ����܂��D
	 * @param img �摜�C���[�W
	 * @return �ϊ����HTML�\�[�X
	 */
	public String convert(Image img) throws InterruptedException{
	    StringBuffer bf = new StringBuffer();
	    
	    bf.append("<TABLE WIDTH=" + width + " HEIGHT=" + height + " BORDER=0 CELLSPACING=0 CELLPADDING=0>\n");
	    // �摜����s�N�Z���𒊏o
		PixelGrabber pg = new PixelGrabber(img, 0, 0, width, height, true);
		// �s�N�Z����ǂݍ���
		pg.grabPixels();
		// �摜����s�N�Z���f�[�^���擾
		int px[] = (int[])pg.getPixels();
		// �s�N�Z���f�[�^�𕶎���ɕϊ�����HTML�`���ŏ����o��
		for(int i = 0; i < px.length; i++ ){
			if( i % width == 0 ) bf.append("<TR>");
			// �s�N�Z���f�[�^���J���[�R�[�h�ɕϊ�����HTML�t�@�C���ɏ�������
			bf.append("<TD BGCOLOR=\"#" + toColorCode(px[i]) + "\"></TD>");
			// �s�̐܂�Ԃ�����
			if( i % width == width - 1 ) bf.append("</TR>\n");
		}
		bf.append("</TABLE>");
		
		return bf.toString();
	}
	
	
	/**
	 * �s�N�Z���f�[�^���J���[�R�[�h�ɕϊ����܂��D
	 * @param pixel �s�N�Z���f�[�^
	 * @return �J���[�R�[�h�̕�����\��
	 */
	private String toColorCode(int pixel){
		// �F���̒��o
		int alpha = (pixel >> 24) & 0xff;
		int red = (pixel >> 16) & 0xff;
		int green = (pixel >>  8) & 0xff;
		int blue = (pixel) & 0xff;
		// int�^�̐F����16�i���̕�����ɕϊ�
		String strRed = Integer.toString(red, 16).toUpperCase();
		String strGreen = Integer.toString(green, 16).toUpperCase();
		String strBlue = Integer.toString(blue, 16).toUpperCase();
		// 1���̕������2���ɕϊ�����i�擪��0��₤�j
		if(strRed.length() == 1) strRed = "0" + strRed;
		if(strGreen.length() == 1) strGreen = "0" + strGreen;
		if(strBlue.length() == 1) strBlue = "0" + strBlue;
		
		return strRed + strGreen + strBlue;
	}
	
	
	/**
	 * �s�N�Z�������擾���܂��D
	 * @return �s�N�Z����
	 */
	public int getWidth() {
		return width;
	}
	
	
	/**
	 * �s�N�Z���������擾���܂��D
	 * @return �s�N�Z������
	 */
	public int getHeight() {
		return height;
	}

	
	/**
	 * �ϊ���̃f�[�^�T�C�Y���擾���܂��D
	 * @return �ϊ���̃f�[�^�T�C�Y
	 */
	public long getSize() {
		return size;
	}

}