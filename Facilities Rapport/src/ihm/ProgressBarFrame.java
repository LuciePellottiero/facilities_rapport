package ihm;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBarFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JProgressBar pbar;
	
	public static final int MY_MINIMUM = 0;
	
	public static final int MY_MAXIMUM = 100;
	
	public ProgressBarFrame() {
		super ("Création du pdf");
		
		JFrame thisFrame = this;
		
		// initialize Progress Bar
		pbar = new JProgressBar();
		pbar.setMinimum(MY_MINIMUM);
		pbar.setMaximum(MY_MAXIMUM);
		pbar.setStringPainted(true);
		pbar.setValue(MY_MINIMUM);
		
		EventQueue.invokeLater(new Runnable() {
            
			@Override
            public void run() {
            	JPanel progressBarPanel = new JPanel();
        		
        		// add to JPanel
        		progressBarPanel.add(pbar);
        		
        		thisFrame.setContentPane(progressBarPanel);
        		thisFrame.pack();
        		thisFrame.setLocationRelativeTo(null); //position de la fenetre
        		thisFrame.setAutoRequestFocus(true);
        		thisFrame.setVisible(true);
        		thisFrame.toFront();
            }
        });

	}
	
	public void updateBar(final int newValue) {
		pbar.setValue(newValue);
		
		this.repaint();
		this.revalidate();
	}
	
	public int getProgress() {
		return  pbar.getValue();
	}
}