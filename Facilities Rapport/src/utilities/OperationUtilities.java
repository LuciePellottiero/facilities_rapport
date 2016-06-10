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
	 * Renvoie si l'argument est numerique ou non (avec '.' et un '-' optionnel pris en compte)
	 * @param str la String a tester
	 * @return true si c'est un nombre, false sinon
	 */
	public static boolean isNumeric(final String str)
	{
	  // Correspond a n'importe quel nombre avec signe negatif et decimal
	  return str.matches("-?\\d+(\\.\\d+)?");
	}
	
	/**
	 * Obtient l'index d'un Component dans la hierarchie de son parent
	 * @param component Le Component dont on veut l'index
	 * @return L'index du Component, -1 si le Component ou son parent est null
	 */
	public static int getComponentIndex(final Component component) {
	    
		// Si le Component et son parent ne sont pas null
		if (component != null && component.getParent() != null) {
			// Obtient le parent
			Container container = component.getParent();
		    
			// Itere sur tous les Component du parent
			for (int i = 0; i < container.getComponentCount(); i++) {
		    	
				// Une fois que l'on a retrouvé celui que l'on cherche, on renvoie son index
				if (container.getComponent(i) == component) {
		    		return i;
		    	}
		    }
	    }
	
		// Si le Component ou son parent est null, on renvoie -1
	    return -1;
	}
}
