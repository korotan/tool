
/**********************************************************************************************************************
 * Image Converter
 *
 * Version History
 * Date         Version     Programmer     Fixed
 * --------------------------------------------------------------------------------------------------------------------
 * 2004/11/23   1.0.0       korotan
 *
 **********************************************************************************************************************/

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.net.*;

/**
 * JPEG�摜�EGIF�摜����Art�i�X�N�E�F�A �A�[�g�j�܂���TABLE�A�[�g�ɕϊ�����c�[���ł��D
 * @author korotan
 * @version 1.0.0
 */
public class ImageConverter extends JFrame implements ActionListener, Runnable{
	
	/****************************************/
	/* �@�@ �@  �t�B�[���h��` �@�@    �@�@ */
	/****************************************/
	
	/** �c�[���� */
	static final String TOOL_NAME = "Image Converter";
	/** ���݂̃o�[�W���� */
	static final String VERSION = "1.0.0";
	/** �G���[�_�C�A���O�^�C�g�� */
	static final String ERROR_DIALOG_TITLE = "�A�v���P�[�V�����G���[";
	/** �G���[�_�C�A���O���b�Z�[�W */
	static final String ERROR_DIALOG_MESSAGE = "�A�v���P�[�V�����̎��s���ɃG���[���������܂����B\n�������I�����܂��B";
	/** �G���[���o�͗p�̃��O�t�@�C���� */
	static final String LOG_FILE_NAME = "err.log";
	
	/** �t���[���̉��� */
	static final int FRAME_WIDTH = 550;
	/** �t���[���̍��� */
	static final int FRAME_HEIGHT = 550;
	/** �t���[���̕\���ʒuX���W�i�t���[���̍���[��X���W�j */
	static final int FRAME_POSITION_X = 10;
	/** �t���[���̕\���ʒuY���W�i�t���[���̍���[��Y���W�j */
	static final int FRAME_POSITION_Y = 10;
	
	/** �X�N�E�F�A�A�[�g�ϊ����[�h */
	static final int SQUARE_MODE = 0;
	/** �e�[�u���A�[�g�ϊ����[�h */
	static final int TABLE_MODE = 1;
	/** �X�N�E�F�A�A�[�g�̃f�t�H���g�̃X�N�E�F�A�T�C�Y */
	static final int DEFAULT_SQUARE_SIZE = 1;
	
	/** ���j���[�o�[ */
	private JMenuBar menuBar;
	/** ���j���[ */
	private JMenu fileMenu;
	private JMenu helpMenu;
	/** ���j���[�A�C�e�� */
	private JMenuItem openMItem;
	private JMenuItem saveMItem;
	private JMenuItem exitMItem;
	private JMenuItem infoMItem;
	/** �t�@�C������\�����郉�x�� */
	private JLabel fileLabel;
	/** �ϊ��O�̉摜�T�C�Y��\�����郉�x�� */
	private JLabel befLabel;
	/** �ϊ���̉摜�T�C�Y��\�����郉�x�� */
	private JLabel aftLabel;
	/** �����󋵂�\�����郉�x�� */
	private JLabel stateLabel;
	/** �X�N�E�F�A�A�[�g�p�̃��W�I�{�^�� */
	private JRadioButton squareRButton;
	/** �e�[�u���A�[�g�p�̃��W�I�{�^�� */
	private JRadioButton tableRButton;
	/** �ϊ��{�^�� */
	private JButton exeButton;
	/** HTML�\�[�X�\���G���A */
	private JTextArea tArea;
	
	/** FileChooser */
	private JFileChooser openFc;
	private JFileChooser saveFc;
	/** �摜�ϊ����[�h */
	private int cnvMode;
	/** �ǂݍ��񂾉摜�t�@�C����File�I�u�W�F�N�g */
	private File imgFile;
	/** �ǂݍ��񂾉摜��Image�I�u�W�F�N�g */
	private Image img;
	/** �ǂݍ��񂾉摜�̃X�N�E�F�A�A�[�g �I�u�W�F�N�g */
	private SquareArt sa;
	/** �ǂݍ��񂾉摜��TABLE�A�[�g �I�u�W�F�N�g */
	private TableArt ta;
	
	/****************************************/
	/* �@�@ �@  �R���X�g���N�^ �@�@    �@�@ */
	/****************************************/
	
	/**
	 * �t���[���𐶐����܂��D
	 */
	ImageConverter(){
		// �^�C�g���̐ݒ�
		super(TOOL_NAME);
		// �^�C�g���o�[�ɕ\������A�C�R���̐ݒ�
		try{
			Toolkit tk = getToolkit();
			URL url = getClass().getResource("image/logo.gif");
        	Image icon = tk.createImage((ImageProducer)url.getContent());
        	this.setIconImage(icon);
        } catch (Exception e) {
            handleError(e);
		}
		// �t���[���T�C�Y�̐ݒ�
		this.setBounds(FRAME_POSITION_X, FRAME_POSITION_Y, FRAME_WIDTH, FRAME_HEIGHT);
		
		// ���j���[�o�[�̍쐬
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		// ���j���[�u�t�@�C���v�̍쐬
		fileMenu = new JMenu("�t�@�C��(F)");
		fileMenu.setMnemonic('F');
		menuBar.add(fileMenu);
		// ���j���[���ځu�J���v�̍쐬
		openMItem = new JMenuItem("�J��(O)", new ImageIcon(getClass().getResource("image/open.gif")));
		openMItem.setActionCommand("open");
		openMItem.addActionListener(this);
		openMItem.setMnemonic('O');
		openMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		fileMenu.add(openMItem);
		// ���j���[���ځuHTML�t�@�C���ɕۑ��v�̍쐬
		saveMItem = new JMenuItem("HTML�t�@�C���ɕۑ�(S)", new ImageIcon(getClass().getResource("image/save.gif")));
		saveMItem.setActionCommand("save");
		saveMItem.addActionListener(this);
		saveMItem.setMnemonic('S');
		saveMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		saveMItem.setEnabled(false);
		fileMenu.add(saveMItem);
		// ���j���[���ڊԂɋ��E��������
		fileMenu.addSeparator();
		// ���j���[���ځu�I���v�̍쐬
		exitMItem = new JMenuItem("�I��(X)", new ImageIcon(getClass().getResource("image/exit.gif")));
		exitMItem.setActionCommand("exit");
		exitMItem.addActionListener(this);
		exitMItem.setMnemonic('X');
		fileMenu.add(exitMItem);
		// ���j���[�u�w���v�v�̍쐬
		helpMenu = new JMenu("�w���v(H)");
		helpMenu.setMnemonic('H');
		menuBar.add(helpMenu);
		// ���j���[���ځu�o�[�W�������v�̍쐬
		infoMItem = new JMenuItem("�o�[�W�������(A)");
		infoMItem.setActionCommand("info");
		infoMItem.addActionListener(this);
		infoMItem.setMnemonic('A');
		helpMenu.add(infoMItem);
		
		fileLabel = new JLabel();
		fileLabel.setPreferredSize(new Dimension(250, 20));
		fileLabel.setFont(new Font("�l�r �S�V�b�N", Font.PLAIN, 12));
		fileLabel.setOpaque(true);
		fileLabel.setBackground(Color.white);
		fileLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		befLabel = new JLabel("�T�C�Y(�ϊ��O)�F");
		befLabel.setFont(new Font("�l�r �S�V�b�N", Font.PLAIN, 12));
		aftLabel = new JLabel("�T�C�Y(�ϊ���)�F");
		aftLabel.setFont(new Font("�l�r �S�V�b�N", Font.PLAIN, 12));
		// �t�@�C�����\���p�̃p�l��
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(3, 1, 2, 2));
		infoPanel.add(fileLabel);
		infoPanel.add(befLabel);
		infoPanel.add(aftLabel);
		
		// �ϊ����[�h�I��p�̃��W�I�{�^���̍쐬
		JPanel modePanel = new JPanel();
		modePanel.setLayout(new GridLayout(2, 1));
		ButtonGroup bGroup = new ButtonGroup();
		// �u���A�[�g�v���W�I�{�^���쐬
		squareRButton = new JRadioButton("���A�[�g", true);
		squareRButton.setFont(new Font("�l�r �S�V�b�N", Font.PLAIN, 13));
		squareRButton.setActionCommand("square");
		squareRButton.addActionListener(this);
		bGroup.add(squareRButton);
		modePanel.add(squareRButton);
		// �uTABLE�A�[�g�v���W�I�{�^���쐬
		tableRButton = new JRadioButton("TABLE�A�[�g", false);
		tableRButton.setFont(new Font("�l�r �S�V�b�N", Font.PLAIN, 13));
		tableRButton.setActionCommand("table");
		tableRButton.addActionListener(this);
		bGroup.add(tableRButton);
		modePanel.add(tableRButton);
		
		// �u�ϊ��v�{�^���̍쐬
		exeButton = new JButton("�ϊ�");
		exeButton.addActionListener(this);
		exeButton.setActionCommand("exe");
		exeButton.setPreferredSize(new Dimension(100, 30));
		exeButton.setFont(new Font("�l�r �S�V�b�N", Font.PLAIN, 13));
		
		// �㕔����̈�p�̃p�l��
		JPanel opPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 5));
		opPanel.add(modePanel);
		opPanel.add(exeButton);
		
		// �㕔�̈�p�̃p�l��
		JPanel hdrPanel = new JPanel(new BorderLayout());
		hdrPanel.add(infoPanel, BorderLayout.WEST);
		hdrPanel.add(opPanel, BorderLayout.EAST);
		
		// HTML�\�[�X�\���p�̃e�L�X�g�G���A�̍쐬
		tArea = new JTextArea();
		tArea.setLineWrap(true);
		tArea.setWrapStyleWord(true);
		tArea.setMargin(new Insets(1,5,1,5));
		tArea.setFont(new Font("�l�r �S�V�b�N", Font.PLAIN, 12));
		tArea.setEditable(false);
		// �X�N���[���y�C�����쐬���ăe�L�X�g�G���A���Z�b�g
		JScrollPane scrPane = new JScrollPane(tArea);
		scrPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		// �����󋵕\���p���x���̍쐬
		stateLabel = new JLabel(" ");
		stateLabel.setFont(new Font("�l�r �S�V�b�N", Font.PLAIN, 12));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(5, 5));
		panel.add(hdrPanel, BorderLayout.NORTH);
		panel.add(scrPane, BorderLayout.CENTER);
		panel.add(stateLabel, BorderLayout.SOUTH);
		panel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
		
		// �R���e�i�̍쐬
		Container cnt = this.getContentPane();
		cnt.setLayout(new BorderLayout());
		cnt.add(panel, BorderLayout.CENTER);
		
		// �~�{�^�����N���b�N�����Ƃ��I��
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){System.exit(0);}
		});
		
		// �t�@�C���̃h���b�O���h���b�v�ɑΉ�����
		new DropTarget(tArea, new DropTargetAdapter() {
			// �e�L�X�g�G���A�Ƀt�@�C�����h���b�v���ꂽ�Ƃ��̏���
			public void drop(DropTargetDropEvent e) {
				try {
					Transferable tr = e.getTransferable();
					if ( tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor) ){
						// �h���b�v���ꂽ�^�C�v���t�@�C���̃��X�g�Ȃ�h���b�v���󂯕t����
						e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
						// �h���b�v���ꂽ�t�@�C�����擾����
						java.util.List fileList = (java.util.List)(tr.getTransferData(DataFlavor.javaFileListFlavor));
						// �h���b�v���ꂽ�t�@�C����URL���X�g�ɒǉ����r���[���X�g���X�V����
						Iterator it = fileList.iterator();
						while (it.hasNext()){
						    imgFile = (File)it.next();
							// �����\�ȃt�@�C���̏ꍇ
							if(isExecutable(imgFile)){
							    // �摜�̓ǂݍ���
						        loadImage(imgFile);
						        // �摜���̕\��
						        setImageInfo();
						        // HTML�\���̈�̃N���A
						        tArea.setText("");
						        stateLabel.setText("�摜�t�@�C�����ǂݍ��܂�܂����D");
						        saveMItem.setEnabled(false);
							}
						}
					}
				} catch (Exception excp){
				    handleError(excp);
				}
			}
		});
		
		openFc = new JFileChooser(".");
		saveFc = new JFileChooser(".");
		
		// Look&Feel �̐ݒ�
		try{
			// Look&Feel���V�X�e���ŗL�̃X�^�C���ɕύX
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
			SwingUtilities.updateComponentTreeUI(openFc);
			SwingUtilities.updateComponentTreeUI(saveFc);
		} catch(Exception e){
		    handleError(e);
		}
		
		// �t�@�C���t�B���^�̐���
		DialogFileFilter openFilter[] = {	
			new DialogFileFilter(".jpg;.jpeg" , "JPEG �t�@�C�� (*.jpg;*.jpeg)"),
			new DialogFileFilter(".gif" , "GIF �t�@�C�� (*.gif)")
		};
		DialogFileFilter saveFilter[] = {
			new DialogFileFilter(".htm;.html" , "HTML �t�@�C�� (*.htm;*.html)"),
		};
		// �t�@�C���t�B���^�̒ǉ�
		for( int i = 0 ; i < openFilter.length ; i++ ){
			openFc.addChoosableFileFilter(openFilter[i]);
		}
		for( int i = 0 ; i < saveFilter.length ; i++ ){
		    saveFc.addChoosableFileFilter(saveFilter[i]);
		}
		    
		
		// �f�t�H���g�̕ϊ����[�h�̐ݒ�
		cnvMode = SQUARE_MODE;
		
		// �t���[���̕\��
		this.setVisible(true);
	}
	
	
	/**************************************/
	/* �@�@ �@  ���\�b�h��` �@�@    �@�@ */
	/**************************************/
	
	/**
	 * ���C���֐�
	 * @param args �R�}���h���C��������͂���������̃��X�g
	 */
	public static void main(String[] args){
		// �t���[���̐���
		ImageConverter art = new ImageConverter();
	}
	
	
	/**
	 * ActionEvent�������Ɋe�폈�����s���܂��D
	 * @param e �C�x���g
	 */
	public void actionPerformed(ActionEvent e){
		String action = e.getActionCommand();
		
		// ���j���[���ځu�J���v�������ꂽ�Ƃ��̏���
		if( action.equals("open") ){
			// �t�@�C���I�[�v���_�C�A���O��\�����āC�I�����ꂽFile�I�u�W�F�N�g���擾����
			imgFile = selectOpenFile();
			// �����\�ȃt�@�C���̏ꍇ
			if(isExecutable(imgFile)){
			    // �摜�̓ǂݍ���
			    loadImage(imgFile);
			    // �摜���̕\��
			    setImageInfo();
			    // HTML�\���̈�̃N���A
			    tArea.setText("");
			    stateLabel.setText("�摜�t�@�C�����ǂݍ��܂�܂����D");
			    saveMItem.setEnabled(false);
		    }
		}
		
		// ���j���[���ځuHTML�t�@�C���ɕۑ��v�������ꂽ�Ƃ��̏���
		if( action.equals("save") ){
		    // �t�@�C���I�[�v���_�C�A���O��\�����āC�I�����ꂽFile�I�u�W�F�N�g���擾����
			File saveFile = selectSaveFile();
			
			if(saveFile != null){
			    // �����̃t�@�C�������ɑ��݂��邩�m�F
			    if(saveFile.exists()){
			        // �㏑���m�F
			        if(isOverwrite(saveFile)){
			            // HTML�\�[�X���t�@�C���ɕۑ�
			            saveToFile(saveFile);
			            stateLabel.setText("HTML�t�@�C���ɕۑ�����܂����D");
			        }
			        else{
			            // �ۑ����Ȃ�
			        }
			    }
			    else{
			        // HTML�\�[�X���t�@�C���ɕۑ�
			        saveToFile(saveFile);
			        stateLabel.setText("HTML�t�@�C���ɕۑ�����܂����D");
			    }
			}
		}
		
		// ���j���[���ځu�I���v�������ꂽ�Ƃ��̏���
		else if( action.equals("exit") ){
			System.exit(0);
		}
		
		// ���j���[���ځu�o�[�W�������v�������ꂽ�Ƃ��̏���
		else if( action.equals("info") ){
			openInfoDialog();
		}
		
		// ���W�I�{�^���u���A�[�g�v���I�����ꂽ�Ƃ��̏���
		else if( action.equals("square") ){
			cnvMode = SQUARE_MODE;
			stateLabel.setText(" ");
			if(sa != null){
			    aftLabel.setText("�T�C�Y(�ϊ���)�F" + ExtendedUtil.getSizeString(sa.getSize()));
			}
		}
		
		// ���W�I�{�^���uTABLE�A�[�g�v���I�����ꂽ�Ƃ��̏���
		else if( action.equals("table") ){
			cnvMode = TABLE_MODE;
			stateLabel.setText(" ");
			if(ta != null){
			    aftLabel.setText("�T�C�Y(�ϊ���)�F" + ExtendedUtil.getSizeString(ta.getSize()));
			}
		}
		
		// �u�ϊ��v�{�^���������ꂽ�Ƃ��̏���
		else if( action.equals("exe") ){
		    if (sa != null && ta != null){
		        // �摜�ϊ������p�̃X���b�h�𐶐����Ď��s����
			    Thread th = new Thread(this);
		        th.start();
		    }
		}
	}
	
	
	/**
	 * �摜�̕ϊ��������s���܂��D 
	 */
	public void run(){
	    this.setEnabled(false);
	    stateLabel.setText("�ϊ����������s���Ă��܂�...");
	    try{
		    // �X�N�E�F�A�A�[�g�̍쐬
		    if( cnvMode == SQUARE_MODE ){
		        tArea.setText(sa.convert(img));
		    }
		    // TABLE�A�[�g�̍쐬
		    else{
		        tArea.setText(ta.convert(img));
		    }
		}catch(Exception e){
		    handleError(e);
		}
		tArea.setCaretPosition(0);
		saveMItem.setEnabled(true);
	    stateLabel.setText("�ϊ��������I�����܂����D");
	    this.setEnabled(true);
	}
	
	
	/**
	 * �t�@�C���I�[�v���_�C�A���O��\�����đI�����ꂽFile�I�u�W�F�N�g��Ԃ��܂��D 
	 * @return �I�����ꂽFile�I�u�W�F�N�g��Ԃ��܂�.<BR>
	 * �t�@�C�����I������Ȃ������ꍇ�ɂ�null��Ԃ��܂�.
	 */
	private File selectOpenFile(){
		// �u�t�@�C�����J���v�_�C�A���O�{�b�N�X�̕\��
		int intRet = openFc.showOpenDialog(this);
		// �uOK�v�{�^���������ꂽ�ꍇ�C�I�����ꂽFILE�I�u�W�F�N�g��Ԃ�
		if(intRet == JFileChooser.APPROVE_OPTION){
			return openFc.getSelectedFile();
		}
		else {return null;}
	}
	
	
	/**
	 * �t�@�C���ۑ��_�C�A���O��\�����đI�����ꂽFile�I�u�W�F�N�g��Ԃ��܂��D
	 * @return �I�����ꂽFile�I�u�W�F�N�g��Ԃ��܂�.<BR>
	 * �t�@�C�����I������Ȃ������ꍇ�ɂ�null��Ԃ��܂�.
	 */
	private File selectSaveFile(){
	    // �f�t�H���g�ŕ\�������ۑ��t�@�C�����̐ݒ�
		saveFc.setSelectedFile(new File(getSaveFileName(imgFile.getPath())));
		// �u�t�@�C���̕ۑ��v�_�C�A���O�{�b�N�X�̕\��
		int intRet = saveFc.showSaveDialog(this);
		// �uOK�v�{�^���������ꂽ�ꍇ�C�I�����ꂽFile�I�u�W�F�N�g��Ԃ�
		if(intRet == JFileChooser.APPROVE_OPTION){
			return saveFc.getSelectedFile();
		}
		else {return null;}
	}
	
	
	/**
	 * �摜�t�@�C���̏���ݒ肵�܂��D
	 */
	private void setImageInfo(){
	    try{
	        // SquareArt�ETableArt�I�u�W�F�N�g�̍쐬
	        sa = new SquareArt(img, img.getWidth(this), img.getHeight(this), DEFAULT_SQUARE_SIZE);
	        ta = new TableArt(img, img.getWidth(this), img.getHeight(this));
		} catch (Exception e){
		    handleError(e);
		}
		// �摜�t�@�C�����̐ݒ�
	    fileLabel.setText(" " + imgFile.getName());
	    befLabel.setText("�T�C�Y(�ϊ��O)�F" + ExtendedUtil.getSizeString(imgFile.length())
	            + " (" + img.getWidth(this) + "�~" + img.getHeight(this) + " pixel)");
	    if( cnvMode == SQUARE_MODE ){
	        aftLabel.setText("�T�C�Y(�ϊ���)�F" + ExtendedUtil.getSizeString(sa.getSize()));
	    }
	    else{
	        aftLabel.setText("�T�C�Y(�ϊ���)�F" + ExtendedUtil.getSizeString(ta.getSize()));
	    }
	}
	
	
	/**
	 * �����\�Ȍ`���̃t�@�C�����ǂ����𔻒肵�܂��D
	 * @param file �ǂݍ���File�I�u�W�F�N�g
	 * @return �����\�Ȍ`���Ȃ��true���C�����łȂ����False��Ԃ��܂�
	 */
	private boolean isExecutable(File file){
	    // �I�u�W�F�N�g��null�Ȃ�Ώ������Ȃ�
	    if(file == null) return false;
	    // �t�@�C�������݂��Ȃ��Ȃ�Ώ������Ȃ�
	    if(file.exists() != true){
	        return false;
	    }
	    else{
	        String fileName = file.getName();
	        // �w�肳�ꂽ�g���q�����Ȃ�Ώ�������
	        if(fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".gif")){
	            return true;
	        }
	        // �w�肳�ꂽ�g���q�������Ȃ��Ȃ�Ώ������Ȃ�
	        else{
	            return false;
	        }
	    }
	}
	
	
	/**
	 * �摜��ǂݍ���Image�I�u�W�F�N�g�𐶐����܂��D
	 * @param file �ǂݍ��މ摜��File�I�u�W�F�N�g
	 */
	private void loadImage(File file){
		Toolkit tk = getToolkit();
		
		// �摜��ǂݍ���
		img = tk.getImage(file.getAbsolutePath());
		// �摜�̓ǂݍ��݊�����҂�
		MediaTracker mt = new MediaTracker(this);
        mt.addImage(img, 0);
        try{
            mt.waitForAll();
        }catch(Exception e){
            handleError(e);
        }
	}
	
	
	/**
	 * HTML�\�[�X�ۑ��p�̃t�@�C�������擾���܂��D
	 * @param fileName �ǂݍ��񂾉摜�t�@�C����
	 * @return HTML�\�[�X�ۑ��p�̃t�@�C����
	 */
	private String getSaveFileName(String fileName){
		StringTokenizer st = new StringTokenizer(fileName, ".");
		String str = st.nextToken();
		for(int i = 0; i < st.countTokens() - 1; i++){
		    str = str + "." + st.nextToken();
		}
		return str + ".html";
	}
	
	
	/**
	 * HTML�\�[�X���t�@�C���ɕۑ����܂��D
	 * @param file �ۑ�����File�I�u�W�F�N�g
	 */
	public void saveToFile(File file){
	    try{
	        FileWriter fw = new FileWriter(file.getPath());
	        PrintWriter out = new PrintWriter(fw);
	        
	        out.println("<HTML>");
	        out.println("<HEAD>");
	        out.println("<TITLE>" + file.getName() + "</TITLE>");
	        out.println("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=x-sjis\">");
	        out.println("</HEAD>");
	        out.println("<BODY>");
	        out.println(tArea.getText());
	        out.println("</BODY>");
	        out.println("</HTML>");
	        out.flush();
	        out.close();
	        fw.close();
	    }catch(Exception e){
	        handleError(e);
        }
	}
	
	
	/**
	 * �_�C�A���O��\�����ď㏑�����邩�ǂ������m�F���܂��D
	 * @param file �ۑ�����File�I�u�W�F�N�g
	 * @return �㏑��������Ȃ�true���C���Ȃ��Ȃ��false��Ԃ��܂��D
	 */
	private boolean isOverwrite(File file){
		// �_�C�A���O�̍쐬
	    JLabel msgLabel = new JLabel(file.getPath() + " �͊��ɑ��݂��܂��B\n�㏑�����܂����H");
	    msgLabel.setFont(new Font("�l�r �S�V�b�N", Font.PLAIN, 12));
		JOptionPane optPane = new JOptionPane(msgLabel, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION);
		JDialog dialog = optPane.createDialog(this.getContentPane(), TOOL_NAME);
		
		// Look&Feel���V�X�e���ŗL�̃X�^�C���ɕύX
		try{
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(dialog);
		} catch(Exception e){
		    handleError(e);
		}
		// �m�F�_�C�A���O��\��
		dialog.setVisible(true);
		// �I�������I�v�V�����̎擾
		int val;
		Object opt = optPane.getValue();
		if(opt instanceof Integer){
		    val = ((Integer)opt).intValue();
		}
		else{
		    val = JOptionPane.CLOSED_OPTION;
		}
		// �㏑�����邩�ǂ����̔���
		if(val == JOptionPane.YES_OPTION){
		    return true;
		}
		else{
		    return false;
		}
	}
	
	
	/**
	 * �o�[�W�������̃_�C�A���O��\�����܂��D
	 */
	private void openInfoDialog(){
	    // �_�C�A���O�̍쐬
	    String msg = "<html>" + TOOL_NAME + "  Version " + VERSION + "<BR><BR>"
	    			+ "Copyright (C) 2004 by korotan" + "<BR></html>";
	    JLabel msgLabel = new JLabel(msg);
	    msgLabel.setFont(new Font("�l�r �S�V�b�N", Font.PLAIN, 12));
	    Object[] options = {" OK "};
		JOptionPane optPane = new JOptionPane(msgLabel, JOptionPane.INFORMATION_MESSAGE, 
		        								JOptionPane.DEFAULT_OPTION, null, options, options[0]);
		JDialog dialog = optPane.createDialog(this.getContentPane(), TOOL_NAME);
		
		// Look&Feel���V�X�e���ŗL�̃X�^�C���ɕύX
		try{
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(dialog);
		} catch(Exception e){
		    handleError(e);
		}
		// �_�C�A���O��\��
		dialog.setVisible(true);
	}
	
	
	/**
	 * �G���[�������s���܂��D
	 * @param excp ��O�I�u�W�F�N�g
	 */
	public void handleError(Exception excp){
	    ErrorHandler eh = new ErrorHandler();
	    // �G���[�_�C�A���O�̕\��
	    eh.showErrorDialog(excp, this, ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
	    // �G���[�������O�t�@�C���ɏo��
	    eh.printErrorLog(excp, LOG_FILE_NAME);
	    
	    System.exit(1);
	}
	
}