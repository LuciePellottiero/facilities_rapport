package DocumentHandler;

/**
 * This class is about create the default templates
 * @author Lucie PELLOTIERRO
 *
 */
public class CreateDefaultPdfDocument {
	private static String defaultPdfTemplatePathName = "Facilities Rapport/Files/Templates/DefaultPdfTemplate.pdf";
	
	public static boolean createDefaultPdf () throws Exception{
		return false;
	}
	
	public static boolean createDefaultPdf (String filePathName) throws Exception{
		return false;		
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			try {
				if (createDefaultPdf()) {
					System.out.println("Template created");
				}
				else {
					System.out.println("An error occured");
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (args.length < 2){
			try {
				switch (args[1].toUpperCase()) {				
				case "PDF" :
					if(createDefaultPdf()) {
						System.out.println("Template created");
					}
					else {
						System.out.println("An error occured");
					}
					break;
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			try {
				switch (args[1].toUpperCase()) {				
				case "PDF" :
					if(createDefaultPdf(args[0])) {
						System.out.println("Template created");
					}
					else {
						System.out.println("An error occured");
					}
					break;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
