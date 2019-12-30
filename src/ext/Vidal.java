package ext;

public class Vidal {

	public static void main(String[] args) throws Exception {
		Affiche2.F(); 				//
		Affiche2.Extraction();		//
		Affiche2.Info();			//
		Affiche2.Quit();			//
		Affiche2.Enrichir();		//
		Affiche2.ViderBDD();
		Affiche2.Cooccurence();		//

	//	file:///Users/mac/Desktop//vidal/vidal-Sommaires-Substances-A.htm
		
	}

	/*
	 * public static BufferedReader solution(BufferedReader lire) throws
	 * IOException{ frame.setSize(framewidth, Vidal.frameheight+=20); JLabel lab
	 * = new JLabel("\n\""+url_vidal+"\" n'a pas l'air d'exister!");
	 * frame.add(lab); A++; url_vidal =m.replaceAll((char)A+""); url = new
	 * URL(url_vidal); try{ lire = new BufferedReader(new
	 * InputStreamReader(url.openStream())); }catch(FileNotFoundException e){
	 * lire = solution(lire); } return lire; }
	 * 
	 * 
	 * public static void Asp(JMenuItem menuAspItem)throws Exception{
	 * menuAspItem.addActionListener(new ActionListener(){ public void
	 * actionPerformed(ActionEvent e){ } }); }
	 */

}

// Pattern p = Pattern.compile(" (.+?)
// [0-9/]+[,.]*[/0-9][/mMlLgG]+( :|:)?");
// Pattern p =
// Pattern.compile("([0-9a-zA-Zéèêüûëäïÿöô-]+ ^(et
// |par |avec |sur |de |les |x ))([,.0-9]+[
// ]?(([mM]?[gGlL])|[gG][rR]|(cuillère)|([mM][lL])))+");
// Pattern p =
// Pattern.compile("((et)\\b|(par)\\b|(avec)\\b|(sur)\\b|(de)\\b|(les)\\b|x\\b)|([a-zA-Zéèêüûëäïÿöô-]+\\b)(
// (à |augmenté à |)[,.0-9]+[
// ]?((mg|mmol/l|µmol/l|mL)|([mM]?[gG])|[gG][rR]|(cuillère)|([mM][lL])))+");
// Pattern p = Pattern.compile("([a-zA-Z]+)
// [,.0-9]+( |)(mg|mmol/l|µmol/l|mL|[.0-9]+g)");
