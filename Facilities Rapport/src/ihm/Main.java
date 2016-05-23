package ihm;

import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
	
	public static void main(String[] args) throws IOException{ 
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                try {
					new Formulaire();
				} 
                catch (IOException e) {
					e.printStackTrace();
				}
            }
        });	
	}
	
}
