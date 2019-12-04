import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

/**
 * �G���[�������s���N���X�ł��D
 * @author korotan
 * �쐬��:2004.11.23
 */
public class ErrorHandler{
    
    /**
	 * �G���[�_�C�A���O��\�����܂��D
	 * @param excp ����������O�I�u�W�F�N�g
	 * @param frame �Ăяo������JFrame�I�u�W�F�N�g
	 * @param title �_�C�A���O�̃^�C�g��
	 * @param msg �o�̓��b�Z�[�W
	 */
	public void showErrorDialog(Exception excp, JFrame frame, String title, String msg){
		try{
		    // �_�C�A���O�̍쐬
		    JLabel msgLabel = new JLabel(msg);
		    msgLabel.setFont(new Font("�l�r �S�V�b�N", Font.PLAIN, 12));
			JOptionPane optPane = new JOptionPane(msgLabel, JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optPane.createDialog(frame.getContentPane(), title);
			// Look&Feel���V�X�e���ŗL�̃X�^�C���ɕύX
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(dialog);
			// �_�C�A���O��\��
			dialog.setVisible(true);
		} catch(Exception e){
			System.exit(1);
		}
	}
	
	
	/**
	 * �G���[�������O�t�@�C���ɏo�͂��܂��D
	 * @param excp ����������O�I�u�W�F�N�g
	 * @param fileName ���O�t�@�C����
	 */
	public void printErrorLog(Exception excp, String fileName){
	    try{
	        FileWriter fw = new FileWriter(fileName, true);
	        PrintWriter out = new PrintWriter(fw);
	        
	        out.println("--" + new Date());
	        out.flush();
	        excp.printStackTrace(out);
	        out.println();
	        out.close();
	        fw.close();
	    }catch(Exception e){
			System.exit(1);
        }
	}
    
}
