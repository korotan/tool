/**********************************************************************************************************************
 * ���i�[ �R���o�[�^�i2ch�當���ϊ��c�[���j
 *
 * Version History
 * Date			Version		Programmer		Fixed
 * --------------------------------------------------------------------------------------------------------------------
 * 2003/11/20	1.0.0		korotan
 * 2003/11/21	1.0.1		korotan			�e�L�X�g�G���A���X�V����Ă����ԂŃt�@�C����D&D������t���[�Y����o�O���C���D
 * 											�i�j���m�F�_�C�A���O��\�������Ȃ��悤�ɏC���j
 * 2003/11/22	1.0.2		korotan			�當���ϊ��e�[�u���̖�������}�b�`���O�E�ϊ����s���悤�ɏC���D
 *											���[�U��`�̊當���ϊ��f�[�^�t�@�C����1�s�ڂ��R�����g�Ƃ��ēǂݔ�΂��悤�ɏC���D
 * 2003/12/30	1.0.3		korotan			�V�X�e����`�̊當���ϊ��f�[�^�t�@�C����jar�t�@�C���̊O�ɏo�����D
 *                                          �󕶎��ւ̕ϊ����������D
 *                                          �ۑ��t�@�C�����Ɍ��݊J���Ă���t�@�C�������\�������悤�ɂ����D
 *                                          �ۑ����Ɋg���q���w�肵�Ȃ������ꍇ�A�g���q�������I�ɕ⊮����悤�ɂ����D
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
 * ���ʂ̊當����2ch�當���ɕϊ�����c�[���D
 *
 * @author korotan
 * @version 1.0.3
 */
public class MonaConverter extends JFrame implements ActionListener {
	
	/****************************************/
	/* �@�@ �@  �t�B�[���h��` �@�@    �@�@ */
	/****************************************/
	
	/** �E�C���h�E�̃^�C�g���o�[�ɕ\�������c�[���� */
	static final String TITLE_NAME = "���i�[ �R���o�[�^ �i�L�́M �j";
	/** �X�V���ɃE�C���h�E�̃^�C�g���o�[�ɕ\�������c�[���� */
	static final String UPDATED_TITLE_NAME = "���i�[ �R���o�[�^ �i�L�́M*�j";
	/** �t�@�C�������I������Ă��Ȃ��Ƃ��Ƀ^�C�g���o�[�ɕ\������镶�� */
	static final String NON_FILE_NAME ="����";
	/** �j���m�F���b�Z�[�W */
	static final String DISPOSE_CONFIRM_MESSAGE = "�ւ̕ύX��j�����܂����H";
	/** �ۑ��m�F���b�Z�[�W */
	static final String SAVE_CONFIRM_MESSAGE = "�ւ̕ύX��ۑ����܂����H";
	/** �G���[�_�C�A���O�^�C�g�� */
	static final String ERROR_DIALOG_TITLE = "�A�v���P�[�V�����G���[";
	/** �G���[�_�C�A���O���b�Z�[�W */
	static final String ERROR_DIALOG_MESSAGE = "�A�v���P�[�V�����̎��s���ɃG���[���������܂����D\n�������I�����܂��D";
	
	/** �t���[���̕� */
	static final int FRAME_WIDTH = 500;
	/** �t���[���̍��� */
	static final int FRAME_HEIGHT = 400;
	/** �t���[���̕\���ʒu�i�t���[���̍���[��X,Y���W�j */
	static final int FRAME_POSITION = 10;
	
	/** ���j���[�e�L�X�g�̃t�H���g�� */
	static final String MENU_FONT_NAME = "�l�r �S�V�b�N";
	/** ���j���[�e�L�X�g�̃t�H���g�T�C�Y */
	static final int MENU_FONT_SIZE = 13;
	
	/** �e�L�X�g�G���A���̃e�L�X�g�̃t�H���g�� */
	static final String TAREA_FONT_NAME = "�l�r �o�S�V�b�N";
	/** �e�L�X�g�G���A���̃e�L�X�g�̃t�H���g�T�C�Y */
	static final int TAREA_FONT_SIZE = 12;
	
	/** �e�L�X�g�{�^���̕� */
	static final int BUTTON_WIDTH = 100;
	/** �e�L�X�g�{�^���̍��� */
	static final int BUTTON_HEIGHT = 30;
	/** �e�L�X�g�{�^���̃t�H���g�� */
	static final String BUTTON_FONT_NAME = "�l�r �S�V�b�N";
	/** �e�L�X�g�{�^���̃t�H���g�T�C�Y */
	static final int BUTTON_FONT_SIZE = 13;
	
	/** �當���ϊ��f�[�^�̃V�X�e����`�t�@�C���� */
	static final String SYS_DEF_FILE_NAME = "sys_cnv_def.dat";
	/** �當���ϊ��f�[�^�̃��[�U��`�t�@�C���� */
	static final String USR_DEF_FILE_NAME = "usr_cnv_def.dat";
	/** �當���ϊ���`�f�[�^�t�@�C���ɂ������؂蕶�� */
	static final String DEF_FILE_SEGMENT = ",";
	
	/** �當���ϊ��e�[�u�� */
	private ConvertTable convTable;
	
	/** ���j���[�o�[ */
	private JMenuBar menuBar;
	/** ���j���[ */
	private JMenu fileMenu;
	private JMenu editMenu;
	private JMenu setupMenu;
	/** ���j���[�A�C�e�� */
	private JMenuItem newMItem;
	private JMenuItem openMItem;
	private JMenuItem saveMItem;
	private JMenuItem exitMItem;
	private JMenuItem undoMItem;
	private JMenuItem redoMItem;
	private JMenuItem cutMItem;
	private JMenuItem copyMItem;
	private JMenuItem pasteMItem;
	private JMenuItem selectAllMItem;
	private JMenuItem appendPatternMItem;
	
	/** �c�[���o�[ */
	private JToolBar toolBar;
	/** �{�^���i�A�C�R���j */
	private JButton newButton;
	private JButton openButton;
	private JButton saveButton;
	private JButton exitButton;
	private JButton cutButton;
	private JButton copyButton;
	private JButton pasteButton;
	private JButton undoButton;
	private JButton redoButton;
	/** �{�^���i�e�L�X�g�j */
	private JButton exeButton;
	
	/** �e�L�X�g�G���A */
	private JTextArea tArea;
	/** �X�N���[���y�C�� */
	private JScrollPane scrPane;
	
	/** FileChooser */
	private JFileChooser fileChooser;
	
	/** �e�L�X�g�G���A���̕����񂪍X�V���ꂽ���ǂ�����\���t���O */
	private boolean isUpdate;
	/** �ǂݍ���ł���t�@�C���̃p�X�� */
	private String pathName;
	/** �ϊ��O�̃e�L�X�g�G���A���̕����� */
	private String befStr;
	/** �ϊ���̃e�L�X�g�G���A���̕����� */
	private String aftStr;
	
	
	/**************************************/
	/* �@�@ �@  ���\�b�h��` �@�@    �@�@ */
	/**************************************/
	
	/**
	 * ���C���֐�
	 * @param args[] �R�}���h���C������̕�����
	 */
	public static void main(String args[]){
		// �t���[���̐���
		MonaConverter converter = new MonaConverter(TITLE_NAME + " - " + NON_FILE_NAME);
	}
	
	
	/****************************************/
	/* �@�@ �@  �R���X�g���N�^ �@�@    �@�@ */
	/****************************************/
	
	/**
	 * �t���[������ъ當���ϊ��e�[�u���𐶐����܂��D
	 * @param title �^�C�g���o�[�ɕ\�����镶����
	 */
	MonaConverter(String title){
		// �^�C�g���̐ݒ�
		super(title);
		// �^�C�g���o�[�ɕ\������A�C�R���̐ݒ�
		try{
			Toolkit tk = getToolkit();
			URL url = getClass().getResource("icon/logo.gif");
        	Image titleIcon = tk.createImage((ImageProducer)url.getContent());
        	this.setIconImage(titleIcon);
        } catch (Exception e) {
			// �G���[���b�Z�[�W�_�C�A���O��\������
			showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
			System.exit(1);			// �v���O�������I������
		}
		
		// �t���[���T�C�Y�̐ݒ�
		this.setBounds(FRAME_POSITION, FRAME_POSITION, FRAME_WIDTH, FRAME_HEIGHT);
		
		// ���j���[�o�[�̍쐬
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		// ���j���[�uFile�v�̍쐬
		fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');			// �j�[���j�b�N�L�[�̐ݒ�
		menuBar.add(fileMenu);				// ���j���[�o�[�ɒǉ�
		
		// ���j���[���ځuNew�v�̍쐬
		newMItem = new JMenuItem("New", new ImageIcon(getClass().getResource("icon/new.gif")));
		newMItem.setActionCommand("new");	// �A�N�V�����R�}���h�̐ݒ�
		newMItem.addActionListener(this);	// �C�x���g���X�i��o�^
		newMItem.setMnemonic('N');			// �j�[���j�b�N�L�[�̐ݒ�
		fileMenu.add(newMItem);				// ���j���[�ɒǉ�
		// ���j���[���ځuOpen�v�̍쐬
		openMItem = new JMenuItem("Open", new ImageIcon(getClass().getResource("icon/open.gif")));
		openMItem.setActionCommand("open");
		openMItem.addActionListener(this);
		openMItem.setMnemonic('O');
		openMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));		// �V���[�g�J�b�g�L�[�̐ݒ�
		fileMenu.add(openMItem);
		// ���j���[���ځuSave�v�̍쐬
		saveMItem = new JMenuItem("Save", new ImageIcon(getClass().getResource("icon/save.gif")));
		saveMItem.setActionCommand("save");
		saveMItem.addActionListener(this);
		saveMItem.setMnemonic('S');
		saveMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		fileMenu.add(saveMItem);
		
		// ���j���[���ڊԂɋ��E��������
		fileMenu.addSeparator();
		
		// ���j���[���ځuExit�v�̍쐬
		exitMItem = new JMenuItem("Exit", new ImageIcon(getClass().getResource("icon/exit.gif")));
		exitMItem.setActionCommand("exit");
		exitMItem.addActionListener(this);
		exitMItem.setMnemonic('X');
		fileMenu.add(exitMItem);
		
		// ���j���[�uEdit�v�̍쐬
		editMenu = new JMenu("Edit");
		editMenu.setMnemonic('E');
		menuBar.add(editMenu);
		
		// ���j���[���ځuUndo�v�̍쐬
		undoMItem = new JMenuItem("Undo", new ImageIcon(getClass().getResource("icon/undo.gif")));
		undoMItem.setActionCommand("undo");
		undoMItem.addActionListener(this);
		undoMItem.setMnemonic('U');
		undoMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
		undoMItem.setEnabled(false);	// ���j���[���ڂ𖳌��i�I��s�\�j�ɂ���
		editMenu.add(undoMItem);
		// ���j���[���ځuRedo�v�̍쐬
		redoMItem = new JMenuItem("Redo", new ImageIcon(getClass().getResource("icon/redo.gif")));
		redoMItem.setActionCommand("redo");
		redoMItem.addActionListener(this);
		redoMItem.setMnemonic('R');
		redoMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl Y"));
		redoMItem.setEnabled(false);
		editMenu.add(redoMItem);
		
		// ���j���[���ڊԂɋ��E��������
		editMenu.addSeparator();
		
		// ���j���[���ځuCut�v�̍쐬
		cutMItem = new JMenuItem("Cut", new ImageIcon(getClass().getResource("icon/cut.gif")));
		cutMItem.setActionCommand("cut");
		cutMItem.addActionListener(this);
		cutMItem.setMnemonic('F');
		cutMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
		editMenu.add(cutMItem);
		// ���j���[���ځuCopy�v�̍쐬
		copyMItem = new JMenuItem("Copy", new ImageIcon(getClass().getResource("icon/copy.gif")));
		copyMItem.setActionCommand("copy");
		copyMItem.addActionListener(this);
		copyMItem.setMnemonic('C');
		copyMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
		editMenu.add(copyMItem);
		// ���j���[���ځuPaste�v�̍쐬
		pasteMItem = new JMenuItem("Paste", new ImageIcon(getClass().getResource("icon/paste.gif")));
		pasteMItem.setActionCommand("paste");
		pasteMItem.addActionListener(this);
		pasteMItem.setMnemonic('V');
		pasteMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));
		editMenu.add(pasteMItem);
		
		// ���j���[���ڊԂɋ��E��������
		editMenu.addSeparator();
		
		// ���j���[���ځuSelect All�v�̍쐬
		selectAllMItem = new JMenuItem("Select All", new ImageIcon("icon/no_icon.gif"));
		selectAllMItem.setActionCommand("selectAll");
		selectAllMItem.addActionListener(this);
		selectAllMItem.setMnemonic('A');
		selectAllMItem.setAccelerator(KeyStroke.getKeyStroke("ctrl A"));
		editMenu.add(selectAllMItem);
		
		// ���j���[�uSetup�v�̍쐬
		setupMenu = new JMenu("Setup");
		setupMenu.setMnemonic('S');
		menuBar.add(setupMenu);
		
		// ���j���[���ځu�ϊ��f�[�^�̓o�^�v�̍쐬
		appendPatternMItem = new JMenuItem("�ϊ��f�[�^�̓o�^");
		appendPatternMItem.setActionCommand("appendPattern");
		appendPatternMItem.addActionListener(this);
		appendPatternMItem.setMnemonic('A');
		appendPatternMItem.setFont(new Font(MENU_FONT_NAME, Font.PLAIN, MENU_FONT_SIZE));	// �t�H���g�̐ݒ�
		setupMenu.add(appendPatternMItem);
		
		
		// �c�[���o�[�̍쐬
		toolBar = new JToolBar();
		//toolBar.setRollover(true);
		// �A�C�R���{�^���uNew�v�̍쐬
		newButton = new JButton(new ImageIcon(getClass().getResource("icon/new.gif")));
		newButton.setActionCommand("new");			// �A�N�V�����R�}���h�̐ݒ�
		newButton.addActionListener(this);			// �C�x���g���X�i��o�^
		newButton.setMargin(new Insets(0,0,0,0));	// �A�C�R���摜�ƃ{�^���̃}�[�W�����Ȃ���
		toolBar.add(newButton);
		// �A�C�R���{�^���uOpen�v�̍쐬
		openButton = new JButton(new ImageIcon(getClass().getResource("icon/open.gif")));
		openButton.setActionCommand("open");
		openButton.addActionListener(this);
		openButton.setMargin(new Insets(0,0,0,0));
		toolBar.add(openButton);
		// �A�C�R���{�^���uSave�v�̍쐬
		saveButton = new JButton(new ImageIcon(getClass().getResource("icon/save.gif")));
		saveButton.setActionCommand("save");
		saveButton.addActionListener(this);
		saveButton.setMargin(new Insets(0,0,0,0));
		toolBar.add(saveButton);
		// �A�C�R���{�^���uExit�v�̍쐬
		exitButton = new JButton(new ImageIcon(getClass().getResource("icon/exit.gif")));
		exitButton.setActionCommand("exit");
		exitButton.addActionListener(this);
		exitButton.setMargin(new Insets(0,0,0,0));
		toolBar.add(exitButton);
		
		// �A�C�R���{�^���Ԃɋ��E��������
		toolBar.addSeparator();
		
		// �A�C�R���{�^���uCut�v�̍쐬
		cutButton = new JButton(new ImageIcon(getClass().getResource("icon/cut.gif")));
		cutButton.setActionCommand("cut");
		cutButton.addActionListener(this);
		cutButton.setMargin(new Insets(0,0,0,0));
		toolBar.add(cutButton);
		// �A�C�R���{�^���uCopy�v�̍쐬
		copyButton = new JButton(new ImageIcon(getClass().getResource("icon/copy.gif")));
		copyButton.setActionCommand("copy");
		copyButton.addActionListener(this);
		copyButton.setMargin(new Insets(0,0,0,0));
		toolBar.add(copyButton);
		// �A�C�R���{�^���uPaste�v�̍쐬
		pasteButton = new JButton(new ImageIcon(getClass().getResource("icon/paste.gif")));
		pasteButton.setActionCommand("paste");
		pasteButton.addActionListener(this);
		pasteButton.setMargin(new Insets(0,0,0,0));
		toolBar.add(pasteButton);
		
		// �A�C�R���{�^���Ԃɋ��E��������
		toolBar.addSeparator();
		
		// �A�C�R���{�^���uUndo�v�̍쐬
		undoButton = new JButton(new ImageIcon(getClass().getResource("icon/undo.gif")));
		undoButton.setActionCommand("undo");
		undoButton.addActionListener(this);
		undoButton.setMargin(new Insets(0,0,0,0));
		toolBar.add(undoButton);
		// �A�C�R���{�^���uRedo�v�̍쐬
		redoButton = new JButton(new ImageIcon(getClass().getResource("icon/redo.gif")));
		redoButton.setActionCommand("redo");
		redoButton.addActionListener(this);
		redoButton.setMargin(new Insets(0,0,0,0));
		toolBar.add(redoButton);
		
		// �e�L�X�g�G���A�̍쐬
		tArea = new JTextArea();
		tArea.setLineWrap(true);				// �܂�Ԃ����T�|�[�g
		tArea.setWrapStyleWord(true);			// ���[�h���E�Ő܂�Ԃ��悤�ɐݒ�
		tArea.setMargin(new Insets(1,5,1,5));	// �e�L�X�g�G���A�̃}�[�W����ݒ�
		tArea.setFont(new Font(TAREA_FONT_NAME, Font.PLAIN, TAREA_FONT_SIZE));		// �t�H���g�̐ݒ�
		
		// �X�N���[���y�C�����쐬���ăe�L�X�g�G���A���Z�b�g
		scrPane = new JScrollPane(tArea);
		
		// �u�ϊ��J�n�v�{�^���̍쐬
		exeButton = new JButton("�ϊ��J�n", new ImageIcon(getClass().getResource("icon/exe.gif")));
		exeButton.addActionListener(this);
		exeButton.setActionCommand("exe");
		exeButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		exeButton.setFont(new Font(BUTTON_FONT_NAME, Font.PLAIN, BUTTON_FONT_SIZE));
		exeButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.lightGray, Color.gray));
		
		// �R���e�i�̍쐬
		Container content = this.getContentPane();
		content.setLayout(new BorderLayout());
		content.add(toolBar, BorderLayout.NORTH);
		content.add(scrPane, BorderLayout.CENTER);
		content.add(exeButton, BorderLayout.SOUTH);
		
		
		// �t���[���́~�{�^�����N���b�N�����Ƃ��Ƀt���[������Ȃ��悤�ɐݒ肷��
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		// �t���[���́~�{�^�����N���b�N�����Ƃ��̏���
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				// �X�V�t���O��ON�̂Ƃ��A�t�@�C���ۑ��m�F�_�C�A���O��\������
				if( isUpdate == true ){
					int rc = showSaveConfirmDialog();
					// �u�͂��v��I�������Ƃ��A�t�@�C���ۑ��_�C�A���O��\�����ăv���O�������I������
					if( rc == JOptionPane.YES_OPTION ){
						showFileSaveDialog();
						System.exit(0);
					}
					// �u�������v��I�������Ƃ��A�v���O�������I������
					else if( rc == JOptionPane.NO_OPTION ) System.exit(0);
				}
				// �X�V�t���O��OFF�̂Ƃ��A�v���O�������I������
				else System.exit(0);
				// �t�H�[�J�X���e�L�X�g�G���A�Ɉړ�����
				tArea.requestFocus();
			}
		});
		
		
		// �e�L�X�g�G���A�ɕ������^�C�v���ꂽ�Ƃ��̏���
		tArea.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e){
				updateTitle();
			}
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
						File file = (File)fileList.get(0);
						
						// �h���b�v���ꂽ�t�@�C�����e�L�X�g�G���A�ɕ\������
						// ���ݍX�V���̃e�L�X�g�G���A���̕����͋����I�ɃN���A�����
						pathName = file.toString();
						readFile(pathName);
						
						/*
						// �e�L�X�g�G���A�����X�V����Ă����Ԃ�D&D������ƃt���[�Y����o�O������
						// �X�V�t���O��ON�̂Ƃ��A�t�@�C���j���m�F�_�C�A���O��\������
						if( isUpdate == true ){
							int rc = showDisposeConfirmDialog();
							// �u�͂��v��I�������Ƃ��A�t�@�C����ǂݍ���
							if( rc == JOptionPane.YES_OPTION ){
								pathName = file.toString();
								readFile(pathName);
							}
						}
						// �X�V�t���O��OFF�̂Ƃ��A�t�@�C����ǂݍ���
						else{
							pathName = file.toString();
							readFile(pathName);
						}
						*/
						
						tArea.requestFocus();
					}
				} catch (Exception ex) {
					// �G���[���b�Z�[�W�_�C�A���O��\������
					showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
					System.exit(1);			// �v���O�������I������
				}
			}
		});
		
		// FileChooser�̐ݒ�
		fileChooser = new JFileChooser(".");
		DialogFileFilter filter[] = {			// �t�@�C���t�B���^�̐���
			new DialogFileFilter(".htm;.html" , "HTML �t�@�C�� (*.htm;*.html)"),
			new DialogFileFilter(".txt" , "�e�L�X�g �t�@�C�� (*.txt)")
		};
		for( int i = 0 ; i < filter.length ; i++ )
			fileChooser.addChoosableFileFilter(filter[i]);		// �t�@�C���t�B���^�̒ǉ�
		
		// Look&Feel �̐ݒ�
		try{
			// Look&Feel���V�X�e���ŗL�̃X�^�C���ɕύX
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(this);
			SwingUtilities.updateComponentTreeUI(fileChooser);
		} catch(Exception e){
		    System.exit(1);
		}
		
		// �t�B�[���h�ϐ��̏�����
		isUpdate = false;
		pathName = NON_FILE_NAME;
		befStr = "";
		aftStr = "";
		
		// �當���ϊ��e�[�u���̍쐬
		convTable = makeConvertTable(SYS_DEF_FILE_NAME, USR_DEF_FILE_NAME);
		
		// �t���[���̕\��
		this.setVisible(true);
		// �e�L�X�g�G���A�Ƀt�H�[�J�X���ړ�������
		tArea.requestFocus();
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
		
		// ���j���[���ځuNew�v���I�����ꂽ�Ƃ��̏���
		if( action.equals("new") ){
			// �X�V�t���O��ON�̂Ƃ��A�t�@�C���j���m�F�_�C�A���O��\������
			if( isUpdate == true ){
				int rc = showDisposeConfirmDialog();
				// �u�͂��v��I�������Ƃ��A�e�L�X�g�G���A���N���A����
				if( rc == JOptionPane.YES_OPTION ) clearTextArea();
			}
			// �X�V�t���O��OFF�̂Ƃ��A�e�L�X�g�G���A���N���A����
			else clearTextArea();
			tArea.requestFocus();
		}
		
		
		// ���j���[���ځuOpen�v���I�����ꂽ�Ƃ��̏���
		else if( action.equals("open") ){
			// �X�V�t���O��ON�̂Ƃ��A�t�@�C���j���m�F�_�C�A���O��\������
			if( isUpdate == true ){
				int rc = showDisposeConfirmDialog();
				// �u�͂��v��I�������Ƃ��A�t�@�C���I�[�v���_�C�A���O��\������
				if( rc == JOptionPane.YES_OPTION ) showFileOpenDialog();
			}
			// �X�V�t���O��OFF�̂Ƃ��A�t�@�C���I�[�v���_�C�A���O��\������
			else showFileOpenDialog();
			tArea.requestFocus();
		}
		
		// ���j���[���ځuSave�v���I�����ꂽ�Ƃ��̏���
		else if( action.equals("save") ){
			showFileSaveDialog();		// �t�@�C���ۑ��_�C�A���O��\������
			tArea.requestFocus();
		}
		
		
		// ���j���[���ځuExit�v���I�����ꂽ�Ƃ��̏���
		else if( action.equals("exit") ) {
			// �X�V�t���O��ON�̂Ƃ��A�t�@�C���ۑ��m�F�_�C�A���O��\������
			if( isUpdate == true ){
				int rc = showSaveConfirmDialog();
				// �u�͂��v��I�������Ƃ��A�t�@�C���ۑ��_�C�A���O��\������
				if( rc == JOptionPane.YES_OPTION ) showFileSaveDialog();
				// �u�������v��I�������Ƃ��A�v���O�������I������
				else if( rc == JOptionPane.NO_OPTION ) System.exit(0);
			}
			// �X�V�t���O��OFF�̂Ƃ��A�v���O�������I������
			else System.exit(0);
			// �t�H�[�J�X���e�L�X�g�G���A�Ɉړ�����
			tArea.requestFocus();
		}
		
		// ���j���[���ځuUndo�v���I�����ꂽ�Ƃ��̏���
		else if( action.equals("undo") ) {
			tArea.setText(befStr);
			tArea.requestFocus();
		}
		
		// ���j���[���ځuRedo�v���I�����ꂽ�Ƃ��̏���
		else if( action.equals("redo") ) {
			tArea.setText(aftStr);
			tArea.requestFocus();
		}
		
		// ���j���[���ځuCut�v���I�����ꂽ�Ƃ��̏���
		else if( action.equals("cut") ) {
			tArea.cut();	// �e�L�X�g�G���A���őI�����ꂽ�������؂����ăN���b�v�{�[�h�ɓ]������
			updateTitle();
			tArea.requestFocus();
		}
		
		// ���j���[���ځuCopy�v���I�����ꂽ�Ƃ��̏���
		else if( action.equals("copy") ) {
			tArea.copy();	// �e�L�X�g�G���A���őI�����ꂽ��������N���b�v�{�[�h�ɓ]������
			updateTitle();
			tArea.requestFocus();
		}
		
		// ���j���[���ځuPaste�v���I�����ꂽ�Ƃ��̏���
		else if( action.equals("paste") ) {
			tArea.paste();	// �N���b�v�{�[�h�ɃR�s�[����Ă��镶������e�L�X�g�G���A�ɓ\��t����
			updateTitle();
			tArea.requestFocus();
		}
		
		// ���j���[���ځuSelectAll�v���I�����ꂽ�Ƃ��̏���
		else if( action.equals("selectAll") ) {
			tArea.selectAll();	// �e�L�X�g�G���A���̑S�Ă̕�����I������
		}
		
		// ���j���[���ځu�ϊ��f�[�^�̓o�^�v���I�����ꂽ�Ƃ��̏���
		else if( action.equals("appendPattern") ) {
			// �當���ϊ��f�[�^�o�^�̃_�C�A���O�̐���
			DataAppendManager manager = new DataAppendManager(this, convTable);
			tArea.requestFocus();
		}
		
		// �{�^���u�ϊ��J�n�v�������ꂽ�Ƃ��̏���
		else if( action.equals("exe") ) {
			convertTextArea();		// �e�L�X�g�G���A���̕�����̕ϊ��������s��
			updateTitle();
			tArea.requestFocus();
		}
	}
	
	
	/**
	 * �當���ϊ���`�f�[�^�t�@�C����ǂݍ���Ŋ當���ϊ��e�[�u�����쐬���܂��D
	 * @param sysDefFileName �V�X�e����`�̊當���ϊ��f�[�^�t�@�C����
	 * @param usrDefFileName ���[�U��`�̊當���ϊ��f�[�^�t�@�C����
	 * @return �쐬�����當���ϊ��e�[�u��
	 */
	private ConvertTable makeConvertTable(String sysDefFileName, String usrDefFileName) {
		ConvertTable convTable = new ConvertTable();	// �當���ϊ��e�[�u��
		
		// �V�X�e����`�t�@�C�������݂��Ȃ��ꍇ�͏I��
		if ( (new File(sysDefFileName)).exists() != true ){
			// �G���[���b�Z�[�W�_�C�A���O��\������
			showErrorDialog(ERROR_DIALOG_TITLE, "�t�@�C�� " + sysDefFileName + " ��������܂���D");
			System.exit(1);		// �v���O�������I������
		}
		// �V�X�e����`�̊當���ϊ��f�[�^�t�@�C����ǂݍ���
		convTable = readDefFile(convTable, sysDefFileName, DEF_FILE_SEGMENT);
		// ���[�U��`�t�@�C�������݂��Ȃ��ꍇ�͓ǂݍ��ݏ������I��
		if ( (new File(usrDefFileName)).exists() != true ) return convTable;
		// ���[�U��`�̊當���ϊ��f�[�^�t�@�C����ǂݍ���
		convTable = readDefFile(convTable, usrDefFileName, DEF_FILE_SEGMENT);
		
		return convTable;
	}
	
	
	/**
	 * �當���ϊ��f�[�^�t�@�C����ǂݍ���ŕϊ��e�[�u���ɒǉ����܂��D
	 * @param convTable �ϊ��e�[�u��
	 * @param fileName �當���ϊ��f�[�^�t�@�C����
	 * @param segment �ϊ��f�[�^�t�@�C���ɂ������؂蕶����
	 * @return �ǂݍ��ݏ�����̕ϊ��e�[�u��
	 */
	private ConvertTable readDefFile(ConvertTable convTable, String fileName, String segment){
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader in = new BufferedReader(fr);
			while(true){
				String str = in.readLine();
				if ( str == null ) break;
				// �ǂݍ��񂾊當���̃y�A���當���ϊ��e�[�u���ɒǉ�
				StringTokenizer st = new StringTokenizer(str, segment);
				if( st.countTokens() == 1 ){
					String befStr = st.nextToken();
					String aftStr = "";
					convTable.setElement(befStr, aftStr);
				}
				else if( st.countTokens() >= 2 ){
					String befStr = st.nextToken();
					String aftStr = st.nextToken();
					convTable.setElement(befStr, aftStr);
				}
			}
			in.close();
			fr.close();
		} catch (Exception e) {
			// �G���[���b�Z�[�W�_�C�A���O��\������
			showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
			System.exit(1);			// �v���O�������I������
		}
		return convTable;
	}
	
	
	/**
	 * �t�@�C���I�[�v���_�C�A���O��\�����܂��D
	 */
	private void showFileOpenDialog(){
		int intRet = fileChooser.showOpenDialog(this);		// �u�t�@�C�����J���v�_�C�A���O�{�b�N�X�̕\��
		// �uOK�v�{�^���������ꂽ�Ƃ��̏���
		if( intRet == JFileChooser.APPROVE_OPTION ){
			File file = fileChooser.getSelectedFile();
			pathName = file.getAbsolutePath();		// �I�����ꂽ�t�@�C���̐�΃p�X���擾����
			readFile(pathName);		// �t�@�C����ǂݍ���Ńe�L�X�g�G���A�ɕ\������
		}
	}
	
	
	/**
	 * �t�@�C���ۑ��_�C�A���O��\�����܂��D
	 */
	private void showFileSaveDialog(){
		fileChooser.setSelectedFile(new File((new File(pathName)).getName()));
		int intRet = fileChooser.showSaveDialog(this);		// �u�t�@�C���̕ۑ��v�_�C�A���O�{�b�N�X�̕\��
		// �uOK�v�{�^���������ꂽ�Ƃ��̏���
		if( intRet == JFileChooser.APPROVE_OPTION ){
			File file = fileChooser.getSelectedFile();		// �I�����ꂽ�t�@�C��
			DialogFileFilter filter = (DialogFileFilter)fileChooser.getFileFilter();	// �I�����ꂽ�t�@�C���t�B���^
			// �I�����ꂽ�t�@�C���̊g���q���t�@�C���t�B���^�ɓo�^����Ă���g���q�ƈႤ�ꍇ
			if( filter.accept(file) != true ){
				// �g���q��ǉ�
				pathName = file.getAbsolutePath() + (String)filter.getExtList().get(0);
			}
			else pathName = file.getAbsolutePath();
			saveFile(pathName);		// �e�L�X�g�G���A���̕�������t�@�C���ɕۑ�����
		}
	}
	
	
	/**
	 * �t�@�C����ǂݍ���Ńe�L�X�g�G���A�ɕ\�����܂��D
	 * @param path �ǂݍ��ރt�@�C���̃p�X
	 */
	private void readFile(String path){
		try{
			String str;
			FileReader fr = new FileReader(path);
			BufferedReader in = new BufferedReader(fr);
			
			this.setTitle( TITLE_NAME + " - " +  path );	// �^�C�g���o�[�̐ݒ�
			isUpdate = false;							// �X�V�t���O��OFF�ɂ���
			tArea.setText(in.readLine());				// 1�s�ڂ̕�������e�L�X�g�G���A�ɏ�������
			while( (str = in.readLine()) != null ){		// 2�s�ڈȍ~�̕�����͉��s�R�[�h������Œǉ�
				tArea.append("\n" + str);
			}
			in.close();
			fr.close();
		}catch(Exception ex){
			// �G���[���b�Z�[�W�_�C�A���O��\������
			showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
			System.exit(1);			// �v���O�������I������
		}
	}
	
	
	/**
	 * �e�L�X�g�G���A���̕�������t�@�C���ɕۑ����܂��D
	 * @param path �ۑ�����t�@�C���̃p�X
	 */
	private void saveFile(String path){
		try{
			FileWriter fw = new FileWriter(path);
			PrintWriter out = new PrintWriter(fw);
			
			this.setTitle( TITLE_NAME + " - " + pathName );		// �^�C�g���o�[�̐ݒ�
			isUpdate = false;				// �X�V�t���O��OFF�ɂ���
			out.write(tArea.getText());		// �e�L�X�g�G���A���̕�������t�@�C���ɏ����o��
			out.close();
			fw.close();
		} catch(Exception e){
			// �G���[���b�Z�[�W�_�C�A���O��\������
			showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
			System.exit(1);			// �v���O�������I������
		}
	}
	
	
	/**
	 * �e�L�X�g�G���A���̕����񂪍X�V���ꂽ�Ƃ��Ƀ^�C�g���̕\����ύX���܂��D
	 */
	private void updateTitle(){
		if( isUpdate == false ){
			isUpdate = true;
			this.setTitle(UPDATED_TITLE_NAME + " - " + pathName);	// �^�C�g������ύX����
		}
	}
	
	
	/**
	 * �e�L�X�g�G���A���̕�������N���A���܂��D
	 */
	private void clearTextArea(){
		pathName = NON_FILE_NAME;		// �p�X���̐ݒ�
		this.setTitle( TITLE_NAME + " - " + pathName );		// �^�C�g���o�[�̐ݒ�
		isUpdate = false;				// �X�V�t���O��OFF�ɂ���
		tArea.setText("");				// �e�L�X�g�G���A���̕��������ׂď�������
		undoMItem.setEnabled(false);	// ���j���[���ځuUndo�v�𖳌��ɂ���
		redoMItem.setEnabled(false);	// ���j���[���ځuRedo�v�𖳌��ɂ���
	}
	
	
	/**
	 * �當���ϊ��������s���܂��D
	 */
	private void convertTextArea(){
		befStr = tArea.getText();				// �e�L�X�g�G���A���̕�����̎擾
		aftStr = convTable.convert(befStr);		// �當���ϊ�����
		tArea.setText(aftStr);					// �ϊ���̕�������e�L�X�g�G���A�ɕ\��
		undoMItem.setEnabled(true);				// ���j���[���ځuUndo�v��L���ɂ���
		redoMItem.setEnabled(true);				// ���j���[���ځuRedo�v��L���ɂ���
	}
	
	
	/**
	 * �t�@�C���̔j���m�F�_�C�A���O��\�����܂��D
	 * @return ���[�U���I�������I�v�V����������int�l
	 */
	private int showDisposeConfirmDialog(){
		// �_�C�A���O�̍쐬
		JOptionPane optPane = new JOptionPane(pathName + DISPOSE_CONFIRM_MESSAGE, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
		JDialog dialog = optPane.createDialog(this.getContentPane(), TITLE_NAME);
		
		// �_�C�A���O��Look&Feel��Windows�X�^�C���ɕύX
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(dialog);
		} catch(Exception e){
			// �G���[���b�Z�[�W�_�C�A���O��\������
			showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
			System.exit(1);			// �v���O�������I������
		}
		
		// �_�C�A���O��\��
		dialog.setVisible(true);
		
		// �I�������I�v�V�����̎擾
		Object opt = optPane.getValue();
		if( opt instanceof Integer ) return ((Integer)opt).intValue();
		else return JOptionPane.CLOSED_OPTION;
	}
	
	
	/**
	 * �t�@�C���̕ۑ��m�F�_�C�A���O��\�����܂��D
	 * @return ���[�U���I�������I�v�V����������int�l
	 */
	private int showSaveConfirmDialog(){
		// �_�C�A���O�̍쐬
		JOptionPane optPane = new JOptionPane(pathName + SAVE_CONFIRM_MESSAGE, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION);
		JDialog dialog = optPane.createDialog(this.getContentPane(), TITLE_NAME);
		
		// �_�C�A���O��Look&Feel��Windows�X�^�C���ɕύX
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(dialog);
		} catch(Exception e){
			// �G���[���b�Z�[�W�_�C�A���O��\������
			showErrorDialog(ERROR_DIALOG_TITLE, ERROR_DIALOG_MESSAGE);
			System.exit(1);			// �v���O�������I������
		}
		
		// �_�C�A���O��\��
		dialog.setVisible(true);
		
		// �I�������I�v�V�����̎擾
		Object opt = optPane.getValue();
		if( opt instanceof Integer ) return ((Integer)opt).intValue();
		else return JOptionPane.CLOSED_OPTION;
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