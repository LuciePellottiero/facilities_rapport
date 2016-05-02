package documentHandler.writeStrategieFactory;

import documentHandler.writeStrategies.DefaultWriteStrategie;
import documentHandler.writeStrategies.IWriteStrategie;

/**
 * Cette classe permet d'obtenir la strategie d'edition de document que l'on souhaite
 * @author Lucie PELLOTIERRO
 *
 */
public class WriteStrategieFactory {

	/**
	 * Constante correspondant a la strategie d'edition par defaut
	 */
	final public static int DEFAULT_STRATEGIE = 1;
	
	/**
	 * Permet d'obtenir la strategie souhaitee
	 * @param strategie Chiffre permettant de choisir la strategie souhaitee. Voir avec les constantes de cette classe
	 * @return la Strategie d'edition souhaitee
	 * @throws Exception Si un mauvais nombre est entre, alors on lance une exception
	 */
	public static IWriteStrategie getStrategie (int strategie) throws Exception{
		switch (strategie) {
			// Strategie par defaut
			case 1: 
				return new DefaultWriteStrategie();
			// Si un mauvais nombre est rentre, alors on lance une exception
			default:
				throw new Exception("Invalid strategie. Use final attributes from StrategieFactory class to choose youre strategie.");
		}
		
	}
}
