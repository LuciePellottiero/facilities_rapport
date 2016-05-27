package utilities;

import java.awt.Component;
import java.awt.Container;
import java.math.BigDecimal;

public abstract class OperationUtilities {

	public static boolean isNumeric(final String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	public static BigDecimal truncateDecimal(final double x,final int numberofDecimals)
	{
	    if ( x > 0) {
	        return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
	    } 
	    else {
	        return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
	    }
	}
	
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
	
	public static <T> int findIndex(T[] array, T value) {
		for (int i = 0; i < array.length; ++i) {
			
			if (array[i].equals(value)) {
				return i;
			}
		}
		
		return -1;
	}
}
