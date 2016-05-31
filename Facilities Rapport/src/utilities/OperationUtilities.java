package utilities;

import java.awt.Component;
import java.awt.Container;

/**
 * Fonctions utiles pour effectuer des operations
 * @author Lucie PELLOTTIERO
 *
 */
public abstract class OperationUtilities {

	/**
	 * Renvoie si l'argument est numerique ou non (avec '.' et un '-' pris en compte)
	 * @param str la String a tester
	 * @return true si c'est un nombre, false sinon
	 */
	public static boolean isNumeric(final String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	/**
	 * Obtient l'index d'un Component dans la hierarchie de son parent
	 * @param component Le Component dont on veut l'index
	 * @return L'index du Component, -1 si le Component ou son parent est null
	 */
	public static int getComponentIndex(final Component component) {
	    
		if (component != null && component.getParent() != null) {
			Container container = component.getParent();
		    
			for (int i = 0; i < container.getComponentCount(); i++) {
		    	
				if (container.getComponent(i) == component) {
		    		return i;
		    	}
		    }
	    }
	
	    return -1;
	}
}
