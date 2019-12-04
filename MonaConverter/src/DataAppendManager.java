import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 * ���[�U��`�̊當���ϊ��f�[�^�̒ǉ��o�^���s���}�l�[�W����\���N���X�ł��D
 */
public class DataAppendManager extends JDialog implements ActionListener {
	
	/****************************************/
	/* �@�@ �@  �t�B�[���h��` �@�@    �@�@ */
	/****************************************/
	
	/** �當���ϊ��f�[�^�̃��[�U��`�t�@�C���� */
	static final String USR_DEF_FILE_NAME = MonaConverter.USR_DEF_FILE_NAME;
	/** �當���ϊ���`�f�[�^�t�@�C���ɂ������؂蕶�� */
	static final String DEF_FILE_SEGMENT = MonaConverter.DEF_FILE_SEGMENT;
	/** �當���ϊ���`�f�[�^�t�@�C���ɂ�����֑������i�ϊ���̊當���̊J�n�����݂̂ɓK�p�j */
	static final char FORBIDDEN_CHAR[] = {','};
	/** �G���[�_�C�A���O�^�C�g�� */
	static final String ERROR_DIALOG_TITLE = "�A�v���P�[�V�����G���[";
	/** �G���[�_�C�A���O���b�Z�[�W */
	static final String ERROR_DIALOG_MESSAGE = "�A�v���P�[�V�����̎��s���ɃG���[���������܂����D\n�������I�����܂��D";
	
	/** �_�C�A���O�̕� */
	static final int DIALOG_WIDTH = 300;
	/** �_�C�A���O�̍��� */
	static final int DIALOG_HEIGHT = 170;
	
	/** ���x���̃t�H���g�� */
	static final String LABEL_FONT_NAME = "�l�r �S�V�b�N";
	/** ���x���̃t�H���g�T�C�Y */
	static final int LABEL_FONT_SIZE = 12;
	
	/** �e�L�X�g�t�B�[���h���̃e�L�X�g�̃t�H���g�� */
	static final String TFIELD_FONT_NAME = "�l�r �o�S�V�b�N";
	/** �e�L�X�g�t�B�[���h���̃e�L�X�g�̃t�H���g�T�C�Y */
	static final int TFIELD_FONT_SIZE = 12;
	
	/** �e�L�X�g�{�^���̕� */
	static final int BUTTON_WIDTH = 100;
	/** �e�L�X�g�{�^���̍��� */
	static final int BUTTON_HEIGHT = 30;
	/** �e�L�X�g�{�^���̃t�H���g�� */
	static final String BUTTON_FONT_NAME = "�l�r �S�V�b�N";
	/** �e�L�X�g�{�^���̃t�H���g�T�C�Y */
	static final int BUTTON_FONT_SIZE = 13;
	
	/** ���x�� */
	JLabel befStrLabel;
	JLabel aftStrLabel;
	/** �e�L�X�g�t�B�[���h */
	JTextField befTField;
	JTextField aftTField;
	/** �{�^�� */
	JButton registButton;
	JButton exitButton;
	
	/** �當���ϊ��e�[�u�� */
	private ConvertTable convTable;
	
	
	/****************************************/
	/* �@�@ �@  �R���X�g���N�^ �@�@    �@�@ */
	/****************************************/
	
	/**
	 * �ǉ������p�̃_�C�A���O�𐶐����܂��D
	 * @param parent �Ăяo�����̃t���[��
	 * @param convTable �當���ϊ��e�[�u��
	 */
	public DataAppendManager(JFrame parent, ConvertTable convTable) {
		// �_�C�A���O�̃^�C�g���ƃ��[�h��ݒ�
		super(parent, "�當���ϊ��f�[�^�̓o�^", true);
		
		// �_�C�A���O�̃T�C�Y�̐ݒ�
		int xPos = (int)(parent.getBounds(new Rectangle())).getX() + 5;		// �_�C�A���O�̕\���ʒu��X���W
		int yPos = (int)(parent.getBounds(new Rectangle())).getY() + 5;		// �_�C�A���O�̕\���ʒu��Y���W
		this.setBounds(xPos, yPos, DIALOG_WIDTH, DIALOG_HEIGHT);
		
		// �f�[�^�ϊ��e�[�u���̎擾
		this.convTable = convTable;
		
		// �ϊ��O�̊當�����͗��̃��x���̍쐬
		befStrLabel = new JLabel("�ϊ��O�̊當�� ");
		befStrLabel.setFont(new Font(LABEL_FONT_NAME, Font.PLAIN, LABEL_FONT_SIZE));	// �t�H���g�̐ݒ�
		// �ϊ��O�̊當�����͗��̃e�L�X�g�t�B�[���h�̍쐬
		befTField = new JTextField();
		befTField.setPreferredSize(new Dimension(120, 20));	// �e�L�X�g�t�B�[���h�̃T�C�Y�̎w��
		befTField.setFont(new Font(LABEL_FONT_NAME, Font.PLAIN, TFIELD_FONT_SIZE));
		// �ϊ��O�̊當�����͗��p�̃p�l���̍쐬
		JPanel btPanel = new JPanel();
		btPanel.setLayout( new FlowLayout(FlowLayout.CENTER, 10, 10) );
		btPanel.add(befStrLabel);
		btPanel.add(befTField);
		
		// �ϊ���̊當�����͗��̃��x���̍쐬
		aftStrLabel = new JLabel("�ϊ���̊當�� ");
		aftStrLabel.setFont(new Font(LABEL_FONT_NAME, Font.PLAIN, LABEL_FONT_SIZE));
		// �ϊ���̊當�����͗��̃e�L�X�g�t�B�[���h�̍쐬
		aftTField = new JTextField();
		aftTField.setPreferredSize(new Dimension(120, 20));
		aftTField.setFont(new Font(LABEL_FONT_NAME, Font.PLAIN, TFIELD_FONT_SIZE));
		// �ϊ���̊當�����͗��p�̃p�l���̍쐬
		JPanel atPanel = new JPanel();
		atPanel.setLayout( new FlowLayout(FlowLayout.CENTER, 10, 10) );
		atPanel.add(aftStrLabel);
		atPanel.add(aftTField);
		
		// �e�L�X�g�t�B�[���h�p�̃p�l���̍쐬
		JPanel tPanel = new JPanel();
		tPanel.setLayout( new BorderLayout() );
		tPanel.add(btPanel, BorderLayout.NORTH);
		tPanel.add(atPanel, BorderLayout.CENTER);
		
		// �u�o�^�v�{�^���̍쐬
		registButton = new JButton("�o�^");
		registButton.addActionListener(this);			// �C�x���g���X�i��o�^
		registButton.setActionCommand("regist");		// �A�N�V�����R�}���h�̐ݒ�
		registButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));			// �{�^���̃T�C�Y�̐ݒ�
		registButton.setFont(new Font(BUTTON_FONT_NAME, Font.PLAIN, BUTTON_FONT_SIZE));		// �{�^���̃t�H���g�̐ݒ�
		
		// �u�L�����Z���v�{�^���̍쐬
		exitButton = new JButton("�L�����Z��");
		exitButton.addActionListener(this);
		exitButton.setActionCommand("exit");
		exitButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		exitButton.setFont(new Font(BUTTON_FONT_NAME, Font.PLAIN, BUTTON_FONT_SIZE));
		
		// �{�^���p�̃p�l���̍쐬
		JPanel bPanel = new JPanel();
		bPanel.setLayout( new FlowLayout(FlowLayout.CENTER, 20, 10) );
		bPanel.add(registButton);
		bPanel.add(exitButton);
		
		// �R���e�i�̍쐬
		Container content = this.getContentPane();
		content.setLayout( new BorderLayout() );
		content.add( tPanel, BorderLayout.CENTER);
		content.add( bPanel, BorderLayout.SOUTH);
		
		
		// �~�{�^�����N���b�N�����Ƃ��I��
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				dispose();		// �_�C�A���O�̔j��
			}
		});
		
		// �t���[���̕\��
		this.setVisible(true);
	}
	
	
	/**************************************/
	/* �@�@ �@  ���\�b�h��` �@�@    �@�@ */
	/**************************************/
	
	/**
	 * ActionEvent�������Ɋe�폈�����s���܂��D
	 * @param e �C�x���g
	 */
	public void actionPerformed(ActionEvent e){
		String action = e.getActionCommand();
		
		// �{�^���u�o�^�v�������ꂽ�Ƃ��̏���
		if( action.equals("regist") ){
			// �e�L�X�g�t�B�[���h���󗓂��ǂ����̃`�F�b�N
			if( befTField.getText().equals("") ){
				showAlertDialog("�x��", "���͗��Ɋ當������͂��ĉ������D");	// �x���_�C�A���O�̕\��
				return;
			}
			// �֑������̃`�F�b�N
			if( aftTField.getText().equals("") != true ){
				for( int i = 0; i < FORBIDDEN_CHAR.length; i++ ){
					if( aftTField.getText().charAt(0) == FORBIDDEN_CHAR[i] ){
						showAlertDialog("�x��", "���̊當���͓o�^�ł��܂���D");	// �x���_�C�A���O�̕\��
						aftTField.setText("");		// �ϊ���̃e�L�X�g�t�B�[���h�̃N���A
						return;
					}
				}
			}
			// �當���ϊ��f�[�^�����[�U��`�t�@�C���ɒǉ��ۑ�����
			String befStr = ConvertTable.toDoubleByteString(befTField.getText());	// �ϊ��O�̊當���i�S�p�ɕϊ��j
			String aftStr = aftTField.getText();	// �ϊ���̊當��
			try {
				FileWriter fw = new FileWriter(USR_DEF_FILE_NAME, true);
				PrintWriter out = new PrintWriter(fw);
				String str = befStr + "," + aftStr;
				out.println(str);
				out.flush();
				out.close();
				fw.close();
			} catch (Exception ex) {
				// �G���[���b�Z�[�W�_�C�A���O��\������
				showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
				System.exit(1);			// �v���O�������I������
			}
			// �o�^�����ϊ��f�[�^���當���ϊ��e�[�u���ɒǉ�����
			convTable.setElement(befStr, aftStr);
			// �e�L�X�g�t�B�[���h���N���A����
			befTField.setText("");
			aftTField.setText("");
		}
		
		
		// �{�^���u�L�����Z���v�������ꂽ�Ƃ��̏���
		if( action.equals("exit") ){
			this.dispose();					// �_�C�A���O�̔j��
		}
	}
	
	
	/**
	 * �x�����b�Z�[�W�_�C�A���O��\�����܂��D
	 * @param title �_�C�A���O�̃^�C�g��
	 * @param msg �x�����b�Z�[�W
	 */
	private void showAlertDialog(String title, String msg){
		try{
			// �_�C�A���O�̍쐬
			JOptionPane optPane = new JOptionPane(msg, JOptionPane.WARNING_MESSAGE);
			JDialog dialog = optPane.createDialog(this.getContentPane(), title);
			
			// �_�C�A���O��Look&Feel��Windows�X�^�C���ɕύX
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(dialog);
			
			// �_�C�A���O��\��
			dialog.setVisible(true);
			
		} catch(Exception e){
			// �G���[���b�Z�[�W�_�C�A���O��\������
			showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
			System.exit(1);			// �v���O�������I������
		}
	}
	
	
	/**
	 * �G���[���b�Z�[�W�_�C�A���O��\�����܂��D
	 * @param title �_�C�A���O�̃^�C�g��
	 * @param msg �G���[���b�Z�[�W
	 */
	private void showErrorDialog(String title, String msg){
		try{
			// �_�C�A���O�̍쐬
			JOptionPane optPane = new JOptionPane(msg, JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optPane.createDialog(this.getContentPane(), title);
			
			// �_�C�A���O��Look&Feel��Windows�X�^�C���ɕύX
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(dialog);
			
			// �_�C�A���O��\��
			dialog.setVisible(true);
			
		} catch(Exception e){
			System.exit(1);
		}
	}
	
}