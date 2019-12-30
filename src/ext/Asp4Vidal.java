package ext;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.WindowConstants;

public class Asp4Vidal extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static int A = 0, frameheight = 500, framewidth = 500;
	protected static Matcher m = null;
	protected static Pattern p = null;
	protected static String url_vidal;
	protected static URL url;
	protected static Pattern regex = Pattern.compile("[0-9].htm\">(.*)</a>");
	protected static JPanel pan = new JPanel();
	protected static JLabel UrlLabel = new JLabel();
	protected static JLabel UrlLabel1 = new JLabel();
	protected static JLabel choixLabel = new JLabel("De: ");
	protected static JLabel choixLabel1 = new JLabel("jusqu'a: ");
	protected static JLabel vitLabel = new JLabel(" Vitesse du transfert");
	protected static String lien;
	protected static char start, finich, first, last;
	protected static long time;
	protected static PrintWriter ecrire = null;
	protected static PrintWriter dico = null;

	public static void Asp(JMenuItem menuAspItem) throws FileNotFoundException, UnsupportedEncodingException {

		// ouverture des fichier

		// creation du frame

		// clicker dans le boutton aspiration

							JSlider vitSlide = new JSlider(0, 20, 0);
							JComboBox<Character> choix1 = new JComboBox<Character>();
							JComboBox<Character> choix2 = new JComboBox<Character>();
							Affiche2 frame = new Affiche2("Aspiration...", 500, 500);
							lien = JOptionPane.showInputDialog(frame, "Tapez l'URL:");
							if(lien==null){ }
							else{
							while(!lien.matches(".+(\\.htm|\\.html)")){
							lien = JOptionPane.showInputDialog(frame, "Veuillez entrer un URL valide!:");
							}
							JButton buttonAsp = new JButton("Start Aspiration...");			
							JProgressBar barre = new JProgressBar(0, 100);
							JProgressBar barre1 = new JProgressBar(0, 100);
							try {
								ecrire = new PrintWriter("Fichier_sortie.txt", "UTF-16LE");
								dico = new PrintWriter("Subst.dic", "UTF-16LE");
							} catch (FileNotFoundException | UnsupportedEncodingException e2) {
								e2.printStackTrace();
							}
							dico.write("\uFEFF");
							ecrire.write("\uFEFF");
							frame.setLocation(450, 300);
							
							// creation des items
							barre.setValue(0);
							barre1.setValue(0);
							choix1.setBounds(100, 15, 75, 30);
							choix2.setBounds(325, 15, 75, 30);
							choixLabel.setBounds(75, 15, 25, 30);
							choixLabel1.setBounds(270, 15, 55, 30);
							UrlLabel.setBounds(10, 50, 90, 50);
							barre.setBounds(100, 50, 300, 50);
							UrlLabel1.setBounds(10, 100, 90, 50);
							barre1.setBounds(100, 100, 300, 50);
							buttonAsp.setBounds(175, 200, 150, 50);
							vitLabel.setBounds(177, 300, 150, 50);
							vitSlide.setBounds(175, 270, 150, 50);
							JLabel oh = new JLabel("Catch me!");
							oh.setBounds(325, 285, 100, 70);
							
							// Ajout des items dans le paneau
							pan.setLayout(null);
							pan.add(choix1);
							pan.add(choix2);
							pan.add(vitLabel);
							pan.add(vitSlide);
							pan.add(choixLabel);
							pan.add(choixLabel1);
							pan.add(UrlLabel1);
							pan.add(barre1);
							pan.add(UrlLabel);
							pan.add(barre);
							pan.add(buttonAsp);
							barre.setStringPainted(true);
							barre1.setStringPainted(true);

							frame.setSize(framewidth, frameheight);
							frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
							frame.setContentPane(pan);
							for (char s = 'A'; s <= 'Z'; s++)
								choix1.addItem(s);
							choix1.setSelectedIndex(-1);
							choix2.setSelectedIndex(-1);

							ActionListener action = new ActionListener() {
								// init des Comboboxes
								public void actionPerformed(ActionEvent arg0) {

									choix2.removeAllItems();
									char j = (char) choix1.getSelectedItem();

									for (char i = (char) j; i <= 'Z'; i++) {
										choix2.addItem(i);
									}
								}
							}; ////// fin init combobox 2
							frame.setVisible(true);
							choix1.setSelectedIndex(-1);
							choix2.setSelectedIndex(-1);
							choix1.addActionListener(action);

							// clicker sur le bouton asspirer
							buttonAsp.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg) {
									if (vitSlide.getValue() == 20){pan.add(oh); frame.setContentPane(pan); pan.setVisible(true);}
									else{pan.remove(oh); frame.setContentPane(pan);  pan.setVisible(true);}
									Thread t = new Thread(new Runnable() {
										public void run() {
											buttonAsp.setEnabled(false);
											choix1.setEnabled(false);
											choix2.setEnabled(false);
											p = Pattern.compile("[A-Z]\\b");
											m = p.matcher(lien);
											time = System.currentTimeMillis();
											if (choix1.getSelectedIndex() == -1) {
												choix1.setSelectedItem('A');
												choix2.setSelectedItem('Z');
											}

											start = (char) choix1.getSelectedItem();
											finich = (char) choix2.getSelectedItem();

											for (A = start; A <= finich; A++) {

												url_vidal = m.replaceAll((char) A + "");

												try {
													url = new URL(url_vidal);
												} catch (MalformedURLException e1) {
														e1.printStackTrace();
												}

												// System.out.println("URL à
												// aspirer
												// :" + url);
												BufferedReader lire = null;

												// ouverutre du lien vidal
												try {
													lire = new BufferedReader(new InputStreamReader(url.openStream()));
												} catch (FileNotFoundException e) {

													continue; // au lieu de tout
													// lire = solution(lire);
												} catch (IOException e) {
													e.printStackTrace();
												}

												String line = null;
												try {
													line = lire.readLine();

													int readLength = 1;

													readLength = 0;

													// compt le nombre de ligne
													// dans
													// un pour chaque alphabet
													// du
													// fichier
													while ((line = lire.readLine()) != null)

													{
														readLength++;
																		
													}

													lire = new BufferedReader(new InputStreamReader(url.openStream()));

													UrlLabel.setText("\n\nTotal ==> ");
													UrlLabel1.setText("URL ==> " + (char) A);
													int len = 0;

													// commence le chargement
													// "avec
													// les bar de progression
													while ((line = lire.readLine()) != null) {

														len++; // len
																// +=line.length();
														int val = (int) (100 * len / readLength);
														barre.setBorderPainted(false);
														barre1.setBorderPainted(false);

														barre1.setValue(val);
														barre.setValue(
																(int) ((100 * (A - 65) + val) / (1 + finich - start)));

														// barre.setBorderPainted(true);
														// barre1.setBorderPainted(true);

														// System.out.println(line);
														// EXTRACTION DES
														// SUBSTANCES
														Matcher mm = regex.matcher(line);
														if (mm.find()) {
															dico.write(mm.group(1) + ",.N\n");
															// System.out.println(mm.group(1));
															// nbrcourant++;
														}
														ecrire.write(line + "\n");

														// recupere la valeur
														// pour
														// du slide "afin de
														// regler
														// la vitesse"
														int vit = 20 - vitSlide.getValue();

														try {

															java.lang.Thread.sleep(vit);

														} catch (InterruptedException e) {
															e.printStackTrace();
														}

													}

												} catch (IOException e) {
													e.printStackTrace();
												}

											}

											/*
											 * long elapsed =
											 * System.currentTimeMillis()-time;
											 * 
											 * JLabel timeLabel = new JLabel();
											 * timeLabel.
											 * setText("\nDurée Totale: "+
											 * (float)elapsed/1000+" sec.");
											 * timeLabel.setBounds(200,400 ,200
											 * ,50 );
											 * timeLabel.setVisible(true);
											 * System.out.println("Durée: "
											 * +elapsed+" Millis.");
											 * pan.add(timeLabel);
											 * frame.setContentPane(pan);
											 * pan.setVisible(true);
											 */
											// fermer la fenetre
											frame.dispose();
											frame.removeAll();
											pan.removeAll();
											ecrire.close();
											dico.close();
											frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
											File file = new File("Fichier_sortie.txt");
									//		File file1 = new File("Subst.dic");
											File file1 = new File("Google Chrome.app");

											// first check if Desktop is
											// supported by Platform or not
											if (!Desktop.isDesktopSupported()) {
												System.out.println("Desktop is not supported");
												return;
											}

											Desktop desktop = Desktop.getDesktop();
											if (file.exists()&&file1.exists())
												try {
													desktop.open(file);
													desktop.open(file1);
												} catch (IOException e) {
													e.printStackTrace();
												}

										}
									});// of thread t (first)
										t.start();
								}
							}); // action asp button

					}
	}

}
