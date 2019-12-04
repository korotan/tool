import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

/**
 * エラー処理を行うクラスです．
 * @author korotan
 * 作成日:2004.11.23
 */
public class ErrorHandler{
    
    /**
	 * エラーダイアログを表示します．
	 * @param excp 発生した例外オブジェクト
	 * @param frame 呼び出し元のJFrameオブジェクト
	 * @param title ダイアログのタイトル
	 * @param msg 出力メッセージ
	 */
	public void showErrorDialog(Exception excp, JFrame frame, String title, String msg){
		try{
		    // ダイアログの作成
		    JLabel msgLabel = new JLabel(msg);
		    msgLabel.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 12));
			JOptionPane optPane = new JOptionPane(msgLabel, JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optPane.createDialog(frame.getContentPane(), title);
			// Look&Feelをシステム固有のスタイルに変更
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(dialog);
			// ダイアログを表示
			dialog.setVisible(true);
		} catch(Exception e){
			System.exit(1);
		}
	}
	
	
	/**
	 * エラー情報をログファイルに出力します．
	 * @param excp 発生した例外オブジェクト
	 * @param fileName ログファイル名
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
