package ext;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Affiche2 extends Affiche1 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Affiche2(String name, int a, int b) {
		super(name, a, b);
	}

	// creation de la premiere fenetre "extraction d'information"
	protected final static JFrame f = new Affiche2("Extraction d'information", 500, 500);
	protected static Matcher m = null;
	protected static Pattern p = null;

	protected static JMenuBar menubar = new JMenuBar();
	protected static JMenu menu = new JMenu("Dictionnaire");
	protected static JMenu menuCorp = new JMenu("Corpus");
	protected static JMenuItem menuAspItem = new JMenuItem("Aspiration");
	protected static JMenuItem menuInfItem = new JMenuItem("Infos");
	protected static JMenuItem menuRichItem = new JMenuItem("Enrichir");
	protected static JMenuItem menuQuitItem = new JMenuItem("Quitter");
	protected static JMenuItem menuExtrItem = new JMenuItem("Extraction");
	protected static JMenuItem menuCoocItem = new JMenuItem("Calcul de Cooccurence");
	protected static JMenuItem menuViderItem = new JMenuItem("Vider BDD");

	public static void F() {

		// initialisation du main frame
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(500, 500);
		f.setJMenuBar(menubar);

		// ajout des item dans le menu bar
		menubar.add(menu);
		menubar.add(menuCorp);
		menuCorp.add(menuExtrItem);
		menuCorp.add(menuViderItem);
		menuCorp.add(menuCoocItem);

		menu.add(menuAspItem);
		menu.add(menuInfItem);
		menu.add(menuRichItem);
		menu.add(menuQuitItem);

		menu.setMnemonic('D');
		menuCorp.setMnemonic('C');
		menuAspItem.setMnemonic('A');
		menuInfItem.setMnemonic('I');
		menuRichItem.setMnemonic('E');
		menuQuitItem.setMnemonic('Q');
		menuExtrItem.setMnemonic('E');
		menuCoocItem.setMnemonic('C');

		f.setVisible(true);

		menuAspItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ar) {

				Thread asp = new Thread(new Runnable() {
					public void run() {

						try {
							Asp4Vidal.Asp(menuAspItem);
						} catch (FileNotFoundException | UnsupportedEncodingException e) {
							e.printStackTrace();
						}
					}
				});

				if (SwingUtilities.isEventDispatchThread())
					asp.run();

			}
		});
	}

	public static void ViderBDD() {
		menuViderItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg) {

				Thread ex = new Thread(new Runnable() {

					public void run() {

						try {
							MySQL.ViderAllBDD();
							JOptionPane.showMessageDialog(new JFrame(), "La Base de Données a été vidée avec succès!");
						} catch (SQLException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, "Echec de connection au serveur!");
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});

				ex.run();

			}
		});
	}

	public static void Info() {

		menuInfItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg) {

				Thread inf = new Thread(new Runnable() {
					public void run() {

						try {

							BufferedReader lire = new BufferedReader(
									new InputStreamReader(new FileInputStream("Subst.dic"), "UTF-16LE"));
							String line = null;

							PrintWriter ecrireinfo = new PrintWriter("info.txt", "UTF-16LE");
							ecrireinfo.write("\uFEFF");
							int nbrmedictotal = 0;
							int nbrcourant = 0;

							line = lire.readLine();
							if (line != null) {
								line = line.replaceAll("\uFEFF", "");
								char firstletter = line.charAt(0);
								while (line != null) {
									nbrmedictotal++;
									line = Normalizer.normalize(line, Normalizer.Form.NFD);
									line = line.replaceAll("[^\\p{ASCII}]", "");

									if (Character.toLowerCase(firstletter) == line.charAt(0)) {
										nbrcourant++;
										// ecriture quand on termine avec une
										// lettre
									} else {
										ecrireinfo.write("Nombre de Médicaments de la page: " + firstletter + " est:"
												+ nbrcourant + "\n");
										firstletter = line.charAt(0);
										nbrcourant = 1;
									}

									line = lire.readLine();

								}
								// regexp a-z
								ecrireinfo.write("Nombre de Médicaments de la page: " + firstletter + " est:"
										+ nbrcourant + "\n"); // le
																// dernier
								ecrireinfo.write("Nombre de Médicaments total est:" + nbrmedictotal + "\n");
								// System.out.println("Nombre de Médicaments
								// total
								// est:" + nbrmedictotal + "\n");

								// ecriretemp.close();
								// tmp.deleteOnExit();
								ecrireinfo.close();
								lire.close();
							} else {

							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						File file = new File("info.txt");

						// first check if Desktop is supported by Platform or
						// not
						if (!Desktop.isDesktopSupported()) {
							System.out.println("Desktop is not supported");
							return;
						}

						Desktop desktop = Desktop.getDesktop();
						if (file.exists()) {
							if (file.length() > 0)
								try {
									desktop.open(file);
								} catch (IOException e) {
									e.printStackTrace();
								}
							else
								JOptionPane.showMessageDialog(new JFrame(), "Le fichier info.txt est vide!");
						} else {
							JOptionPane.showMessageDialog(new JFrame(), "Le fichier info.txt n'a pas été crée!");
						}
						//////////////////// --------------------------------
						//////////////////// AVOIR NOMBRE TOTAL DE MEDIC

					}
				});

				inf.start();

				// JOptionPane.showMessageDialog(null, "Fichier Info Crée!");

			}
		});

	}

	public static void Extraction() {

		menuExtrItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg) {

				JFileChooser fileChoisi = new JFileChooser();

				Thread ex = new Thread(new Runnable() {
					public void run() {

						fileChoisi.setSelectedFile(null);
						fileChoisi.setFileFilter(new FileNameExtensionFilter("Fichiers  texte", "txt"));
						fileChoisi.setCurrentDirectory(new File(System.getProperty("user.dir")));
					//	fileChoisi.
						fileChoisi.setDialogTitle("fichier source de l'extraction");
						fileChoisi.showOpenDialog(null);
						fileChoisi.approveSelection();
						if (!fileChoisi.accept(fileChoisi.getSelectedFile())) {
							fileChoisi.setSelectedFile(new File("corpus.txt"));
							JOptionPane.showMessageDialog(new JFrame(),
									"\tAucun fichier choisi!\ncorpus.txt a été choisi par défaut");
						}
					if (	!(fileChoisi.getSelectedFile().getName().endsWith("dic")))
					{
						
					}
						File Extcorp = fileChoisi.getSelectedFile();
						fileChoisi.setFileFilter(new FileNameExtensionFilter("Fichiers dictionnaires", "dic"));
						fileChoisi.setSelectedFile(null);
						fileChoisi.setDialogTitle("Dictionnaire de référence");
						fileChoisi.showOpenDialog(null);
						fileChoisi.approveSelection();
						if (fileChoisi.getSelectedFile() == null) {
							fileChoisi.setSelectedFile(new File("Subst.dic"));
							JOptionPane.showMessageDialog(new JFrame(),
									"\tAucun fichier choisi!\nSubst.dic a été choisi par défaut");
						}
						File Ext = fileChoisi.getSelectedFile();

						JButton start = new JButton("Commencer!");

						JSlider ExtSlide = new JSlider(0, 20, 0);
						JFrame w = new JFrame("Extraction");
						JPanel pa = new JPanel();
						pa.setLayout(null);
						w.setContentPane(pa);
						JTextArea a = new JTextArea();
						JScrollPane b = new JScrollPane(a);
						JLabel lab = new JLabel("Choisissez la vitesse");
						b.setBounds(10, 110, 280, 240);
						a.setBounds(0, 0, 280, 100);
						ExtSlide.setBounds(75, 25, 150, 30);
						start.setBounds(100, 60, 100, 40);
						lab.setBounds(80, 5, 150, 20);
						pa.add(ExtSlide);
						pa.add(start);
						pa.add(lab);
						pa.add(b);
						b.setAutoscrolls(true);
						w.setSize(300, 400);
						w.setVisible(true);

						start.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg) {

								Thread but = new Thread(new Runnable() {
									public void run() {

										try {
											MySQL.ViderAllBDD();
											
											java.sql.Connection c = null;
											c = DriverManager.getConnection("jdbc:mysql://localhost:3306/Extraction", "root", "");
											String req = "INSERT INTO Substances VALUES(2,'first', 0 );";
											java.sql.Statement s = c.createStatement();
											
											s.executeUpdate(req);
											BufferedReader lireExt = new BufferedReader(
													new InputStreamReader(new FileInputStream(Ext), "UTF-16LE"));

											BufferedReader lirecorp = new BufferedReader(
													new InputStreamReader(new FileInputStream(Extcorp), "UTF-16LE"));
											String ligne = null;
											String linesub = null;
											PrintWriter htmlwrite = null;

											htmlwrite = new PrintWriter("Substances-de-Corpus.html", "UTF-16LE");
											htmlwrite.write("\uFEFF");
											// Cours 10 // Pattern p =
											// Pattern.compile("((et)\\b|(par)\\b|(avec)\\b|(sur)\\b|(de)\\b|(les)\\b|x\\b)|([a-zA-Zéèêüûëäïÿöô-]+\\b)(
											// (à |augmenté à |)[,.0-9]+[
											// ]?((mg|mmol/l|µmol/l|mL)|([mM]?[gG])|[gG][rR]|(cuillère)|([mM][lL])))+");
											// // Pattern p // THIS IS IT !!!!
											// 51 !!

											htmlwrite.write(
													"<!DOCTYPE html>\n<html lang=\"fr\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-16LE\" >\n<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n");
											int nl = 0;
											int x = 0;
											boolean b = false;
											linesub = lireExt.readLine();
											linesub = linesub.replaceAll("\uFEFF", "");

											while ((linesub) != null) {

												linesub = linesub.replaceAll(",.N", "");

												Pattern pmed = Pattern.compile("\\b" + linesub + "\\b");
												// Pattern pmed =
												// Pattern.compile("\\b"+linesub+"\\b|[^a-zéèâà\\-]"+linesub+"[^a-zéèâà\\-]|
												// "+linesub+" ");
												lirecorp = new BufferedReader(new InputStreamReader(
														new FileInputStream(Extcorp), "UTF-16LE"));
												while ((ligne = lirecorp.readLine()) != null) {
													Matcher ms = pmed.matcher(ligne.toLowerCase());

													nl++;
													while (ms.find()) {
														x++;
														a.append(x + "- ligne:" + nl + " " + ms.group() + "\n");
														// w.setVisible(true);

														// System.out.println(nl
														// + " " +
														// ms.group());
														htmlwrite.write("<span style = \"color:red\">" + ms.group()
																+ "</span><p>" + ligne + "</p><p>numéro: " + nl
																+ "</p>\n");
														try {
															MySQL.BDD(ms.group(), nl);
														} catch (SQLException e) {
															if (!b)
																JOptionPane.showMessageDialog(new JFrame(),
																		"Echec de connection avec La Base de Données!");
															b = true;
															continue;
														}
													}
												}
												nl = 0;

												int vit = 20 - ExtSlide.getValue();

												if (vit != 0)
													java.lang.Thread.sleep(vit);
												linesub = lireExt.readLine();
											}
											lireExt.close();
											lirecorp.close();
											htmlwrite.close();
											File file = new File("Substances-de-Corpus.html");

											if (!Desktop.isDesktopSupported()) {
												System.out.println("Desktop is not supported");
												return;
											}

											if (file.exists())
												Desktop.getDesktop().open(file);

										} catch (IOException e) {
											e.printStackTrace();
										} catch (Exception e) {
											e.printStackTrace();
										}

									}

								});

								but.start();

							}
						});
					}

				});

				ex.run();

			}
		});

	}

	public static void Quit() {

		menuQuitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg) {

				Thread ex = new Thread(new Runnable() {
					public void run() {
						System.exit(1);
					}
				});

				ex.start();

			}
		});

	}

	public static void Enrichir() {

		menuRichItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg) {

				Thread ex = new Thread(new Runnable() {
					public void run() {
						JFrame w = new JFrame("Enrichissement");
						JPanel pa = new JPanel();
						pa.setLayout(null);
						w.setContentPane(pa);
						JTextArea a = new JTextArea();
						JScrollPane b = new JScrollPane(a);
						b.setBounds(10, 10, 280, 240);
						a.setBounds(0, 0, 380, 200);
						pa.add(b);
						w.setSize(300, 300);
						w.setVisible(true);
						try {

							BufferedReader lirecorp = new BufferedReader(
									new InputStreamReader(new FileInputStream("corpus.txt"), "UTF-16LE"));

							String ligne = null;
							String linesub = null;
							String linecorp = null;

							boolean found = false;

							Pattern p = Pattern.compile(
									"((avait)\\b|(normale)\\b|(reste)\\b|(et)\\b|(par)\\b|(avec)\\b|(sur)\\b|(de)\\b|(les)\\b|x\\b)|([a-zA-Zéèêüûëäïÿöô\\-]+\\b)"
											+ "( ([lL][pP] |)[,.0-9]+[ ]?" + "([mM]?[gG])|[gG][rR]|(cuillère))+");

							// Pattern.compile("([a-zA-Z]+) [,.0-9]+(
							// |)(mg|mmol/l|µmol/l|mL|[.0-9]+g)");
							String[] tabcorp = new String[200];
							boolean trouve = false;
							int x = 0;
							int r = 0;
							while ((ligne = lirecorp.readLine()) != null) {
								Matcher mm = p.matcher(ligne);
								while (mm.find()) {
									if (mm.group(11) != null) {
										linecorp = mm.group(11).toLowerCase();
										for (r = 0; r < x; r++) {
											if (tabcorp[r].equals(linecorp)) {
												trouve = true;
											}
										}
										if (!trouve) {
											tabcorp[x] = linecorp;
											x++;
										}
										trouve = false;
									}
								}
							}

							lirecorp.close();

							r = 0;
							while (tabcorp[r] != null) {

								BufferedReader lireExt = new BufferedReader(
										new InputStreamReader(new FileInputStream("Subst.dic"), "UTF-16LE"));
								linesub = lireExt.readLine();
								String[] bx = linesub.split("\uFEFF");
								linesub = bx[1];

								while (linesub != null) {
									String[] ax = linesub.split(",");
									linesub = ax[0];

									if ((linesub.equals(tabcorp[r]))) {
										found = true;
										break;
									}

									linesub = lireExt.readLine();
								}

								if (!found) {
									FileOutputStream out = new FileOutputStream("Subst.dic", true);
									out.write((tabcorp[r] + ",.N\n").getBytes("UTF-16LE"));
									a.append(tabcorp[r] + "\n");
									found = true;
									out.close();
									lireExt.close();
								}

								r++;
								found = false;

							}

							ArrayList<String> tabsub = new ArrayList<String>();
							int tt = 0;
							BufferedReader lireExt = new BufferedReader(
									new InputStreamReader(new FileInputStream("Subst.dic"), "UTF-16LE"));
							linesub = lireExt.readLine();
							linesub = linesub.replaceAll("\uFEFF", "");
							while (linesub != null) {
								tabsub.add(linesub);
								tt++;
								linesub = lireExt.readLine();

							}
							Collections.sort(tabsub, new Comparator<String>() {
								@Override
								public int compare(String o1, String o2) {
									o1 = Normalizer.normalize(o1, Normalizer.Form.NFD);
									o2 = Normalizer.normalize(o2, Normalizer.Form.NFD);
									return o1.compareTo(o2);
								}
							});
							// on peut utiliser ta

							/*
							 * tabsub.sort(new Comparator<String>() {
							 * 
							 * @Override public int compare(String o1, String
							 * o2) { o1 = Normalizer.normalize(o1,
							 * Normalizer.Form.NFD); o2 =
							 * Normalizer.normalize(o2, Normalizer.Form.NFD);
							 * return o1.compareTo(o2); } });
							 * 
							 */
							tt = 0;
							lireExt.close();
							PrintWriter sub = new PrintWriter("Subst.dic", "UTF-16LE");
							sub.write("\uFEFF");
							while (tt < tabsub.size()) {
								sub.write(tabsub.get(tt++) + "\n");
							}
							sub.close();

							File file = new File("Subst.dic");

							if (!Desktop.isDesktopSupported()) {
								System.out.println("Desktop is not supported");
								return;
							}

							Desktop desktop = Desktop.getDesktop();
							if (file.exists())
								desktop.open(file);

						} catch (IOException e) {
							e.printStackTrace();
						}

					}

				});

				ex.start();

			}
		});

	}

	public static void Cooccurence() {
		menuCoocItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg) {

				Thread rch = new Thread(new Runnable() {
					public void run() {

						JFrame w = new JFrame();
						JPanel pa = new JPanel();
						pa.setLayout(null);
						w.setContentPane(pa);
						JLabel tokLab = new JLabel("Token");
						JLabel freqLab = new JLabel("Frequence");
						JComboBox<Integer> combtok = new JComboBox<Integer>();
						JButton start = new JButton("Version\nSimplifié");
						JButton start1 = new JButton("Version\nComplete");
						JTextArea a = new JTextArea();
						JScrollPane b = new JScrollPane(a);
						new JTextArea();
						JSlider freqSlide = new JSlider(0, 20, 0);
						tokLab.setBounds(25, 30, 100, 30);
						combtok.setBounds(105, 30, 80, 30);
						freqLab.setBounds(25, 85, 100, 30);
						freqSlide.setBounds(105, 80, 370, 50);
						freqSlide.setMajorTickSpacing(1);
						freqSlide.setMinimum(1);
						freqSlide.setPaintTicks(true);
						freqSlide.setPaintLabels(true);

						start.setBounds(210, 25, 120, 50);
						start1.setBounds(345, 25, 130, 50);
						b.setBounds(20, 160, 460, 300);
						pa.add(b);
						pa.add(freqSlide);
						pa.add(tokLab);
						pa.add(combtok);
						pa.add(freqLab);
						pa.add(start);
						pa.add(start1);
						b.setAutoscrolls(true);
						w.setSize(500, 500);
						w.setVisible(true);

						combtok.addItem(2);
						combtok.addItem(3);
						combtok.addItem(4);
						combtok.addItem(5);

						start1.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg) {
								int token = (int) combtok.getSelectedItem();
								int freq = freqSlide.getValue();
								a.setText("");
								RealCooccurence(a, token, freq);
							}
						});

						start.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg) {

								Thread but = new Thread(new Runnable() {
									public void run() {

										try {
											BufferedReader buff = new BufferedReader(new InputStreamReader(
													new FileInputStream("corpus.txt"), "UTF-16LE"));
											String line = null;

											// MOT DE LONGEUR token QUI SE
											// REPETE freq FOIS
											int token = (int) combtok.getSelectedItem();
											int freq = freqSlide.getValue();
											int i = 0;

											String mot = "([a-zA-Zéèêüûëäïÿöô]+( |’|'| - )([a-zA-Zéèêüûëäïÿöô0-9/]+( |’|' | - ))"
													+ "{" + (token - 2) + "}" + "[a-zA-Zéèêüûëäïÿöô]+)";

											Pattern p1 = Pattern.compile(mot);
											// line = buff.readLine();

											String[] tab = new String[10000];
											String[] tabx = new String[10000];

											int cpt = 0;
											Matcher matcher = null;

											while ((line = buff.readLine()) != null) {
												matcher = p1.matcher(line);

												while (matcher.find()) {
													tab[cpt] = matcher.group();
													cpt++;
												}
											}

											buff.close();
											PrintWriter ecrire = new PrintWriter("Cooccurence.txt", "UTF-16LE");
											ecrire.write("\uFEFF");
											int j = 0, k = 0;
											int compteur = 0;
											int compteurx = 0;
											boolean trouv = false;

											for (i = 0; i < cpt; i++) {

												for (j = 0; j < cpt; j++) {
													if ((tab[i].toLowerCase()).equals(tab[j].toLowerCase())) {
														compteur++;
													}

												}
												if (compteur == freq) {
													trouv = false;
													for (k = 0; k < compteurx; k++) {
														if ((tab[i].toLowerCase()).equals(tabx[k].toLowerCase())) {
															trouv = true;

														}
													}

													if (!trouv) {
														tabx[compteurx] = tab[i];
														compteurx++;
													}
												}

												compteur = 0;
											}

											int r = 0;
											a.setText("");
											while (tabx[r] != null) {
												a.append(tabx[r] + "\n");
												ecrire.write(tabx[r] + "\n");
												r++;
											}

											ecrire.close();
										} catch (Exception e1) {
											e1.printStackTrace();
										}

									}
								});

								but.start();

							}
						});

					}
				});

				rch.start();

			}
		});
	}

	public static void RealCooccurence(JTextArea txt, int token, int frequence) {
		Thread t = new Thread(new Runnable() {
			public void run() {

				BufferedReader lireCorpus = null;
				try {
					lireCorpus = new BufferedReader(
							new InputStreamReader(new FileInputStream("corpus.txt"), "UTF-16LE"));
					String line_c;
					String chaine = "";
					StringTokenizer st;
					ArrayList<String> list = new ArrayList<String>();

					while ((line_c = lireCorpus.readLine()) != null) {
						chaine = chaine + line_c;
					}
					// division du fichier en plusieurs token
					st = new StringTokenizer(chaine, ".,-²_://\\;–+()|\\?!=%?°'`\" \t *?@" + "");
					while (st.hasMoreTokens())
						list.add(st.nextToken().toLowerCase()); // mettre les
																// tokens
					ArrayList<String> tab;
					int n = token;
					Matcher matcher;

					ArrayList<String> tabbuf = new ArrayList<String>();
					Pattern pattern = Pattern.compile("\\w+(\\d+)|(\\d+)\\w+|\\d+");
					PrintWriter ecrire = new PrintWriter("Cooccurence.txt", "UTF-16LE");

					ecrire.write("\uFEFF");
					txt.append("");
					for (int i = 0; i < list.size(); i++) {
						int nbCoo = 1;
						int j = i;
						tab = new ArrayList<String>();
						matcher = pattern.matcher(list.get(i));

						if (!(matcher.find()) && i < list.size() - n) {
							String buf = "";
							buf = buf + " " + list.get(j);
							tab.add(list.get(i));
							j++;

							for (int k = 1; k < n - 1; k++) {
								buf = buf + " " + list.get(j);
								tab.add(list.get(j));
								j++;

							}

							matcher = pattern.matcher(list.get(j));

							if (!(matcher.find())) {

								tab.add(list.get(j));

								buf = buf + " " + list.get(j);

								if (verif(tabbuf, buf) == true) {

									tabbuf.add(buf);

									for (int k = i + 1; k < list.size() - n; k++) {

										int c = 0;

										j = k;

										if (list.get(j).equals(tab.get(c)))

											while (c < n && list.get(j).equals(tab.get(c))) {
												j++;
												c++;
											}
										if (c == n) {
											nbCoo++;
										}
									}
									if (nbCoo == frequence)
										txt.append(buf + "\n");
									ecrire.write(buf + "\n");
								}

							}
						}
					}

					ecrire.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		t.start();
	}

	static Boolean verif(ArrayList<String> tabbuf, String buf) {
		for (int i = 0; i < tabbuf.size(); i++) {
			if (buf.equals(tabbuf.get(i)))
				return false;
		}
		return true;
	}

}
