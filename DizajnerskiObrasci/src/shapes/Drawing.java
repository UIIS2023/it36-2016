//Kod za objektno, izbrisi sve
//package shapes;
//
//import java.awt.BorderLayout;
//import java.awt.EventQueue;
//
//import javax.swing.ButtonGroup;
//import javax.swing.JFrame;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.border.EmptyBorder;
//import javax.swing.JButton;
//import javax.swing.JToggleButton;
//import java.awt.GridBagLayout;
//import java.awt.GridBagConstraints;
//import java.awt.Insets;
//import javax.swing.GroupLayout;
//import javax.swing.GroupLayout.Alignment;
//import javax.swing.LayoutStyle.ComponentPlacement;
//import javax.swing.JToolBar;
//import java.awt.Font;
//import java.awt.Dimension;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.util.ArrayList;
//import java.util.Iterator;
//
//public class Drawing extends JFrame {
//
//	private JPanel contentPane;
//	JToggleButton tglbtnTacka;
//	JToggleButton tglbtnLinija;
//	JToggleButton tglbtnPravougaonik;
//	JToggleButton tglbtnKrug;
//	JToggleButton tglbtnKrugSaRupom;
//	JToggleButton tglbtnSelektuj;
//	JToggleButton tglbtnModifikuj;
//	JToggleButton tglbtnObrisi;
//	
//	
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Drawing frame = new Drawing();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
//	
//
//	/**
//	 * Create the frame.
//	 */
//	public Drawing() {
//		PnlDrawing panel= new PnlDrawing(this); 
//		setExtendedState(MAXIMIZED_BOTH);
//		setTitle("IT61-2017, Pavle Supeta");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(100, 100, 837, 534);
//		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		setContentPane(contentPane);
//		ButtonGroup grupa=new ButtonGroup();
//		
//		JToolBar toolBar = new JToolBar();
//		
//		tglbtnTacka = new JToggleButton("Ta\u010Dka");
//		tglbtnTacka.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//				panel.setStartPoint(null);
//			}
//		});
//		tglbtnTacka.setFont(new Font("Tahoma", Font.PLAIN, 20));
//		toolBar.add(tglbtnTacka);
//		
//		grupa.add(tglbtnTacka);
//	    tglbtnLinija = new JToggleButton("Linija");
//		tglbtnLinija.setFont(new Font("Tahoma", Font.PLAIN, 20));
//		toolBar.add(tglbtnLinija);
//		grupa.add(tglbtnLinija);
//		
//		tglbtnPravougaonik = new JToggleButton("Pravougaonik");
//		tglbtnPravougaonik.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				panel.setStartPoint(null);
//			}
//		});
//		tglbtnPravougaonik.setFont(new Font("Tahoma", Font.PLAIN, 20));
//		toolBar.add(tglbtnPravougaonik);
//		grupa.add(tglbtnPravougaonik);
//		
//		tglbtnKrug = new JToggleButton("Krug");
//		tglbtnKrug.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				panel.setStartPoint(null);
//			}
//		});
//		tglbtnKrug.setFont(new Font("Tahoma", Font.PLAIN, 20));
//		toolBar.add(tglbtnKrug);
//		grupa.add(tglbtnKrug);
//		
//		tglbtnKrugSaRupom = new JToggleButton("Krug sa rupom");
//		tglbtnKrugSaRupom.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				panel.setStartPoint(null);
//			}
//		});
//		tglbtnKrugSaRupom.setFont(new Font("Tahoma", Font.PLAIN, 20));
//		grupa.add(tglbtnKrugSaRupom);
//		toolBar.add(tglbtnKrugSaRupom);
//		
//		tglbtnSelektuj = new JToggleButton("Selektuj");
//		tglbtnSelektuj.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				panel.setStartPoint(null);
//			}
//		});
//		grupa.add(tglbtnSelektuj);
//		tglbtnSelektuj.setFont(new Font("Tahoma", Font.PLAIN, 20));
//		toolBar.add(tglbtnSelektuj);
//		
//		tglbtnModifikuj = new JToggleButton("Modifikuj");
//		tglbtnModifikuj.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//				if(panel.getTestOblik()!=null)
//				{
//					Shape pomOblik=panel.getTestOblik();
//					ArrayList<Shape> lista=panel.getOblik();
//					int index=lista.indexOf(pomOblik);
//				if(panel.getTestOblik() instanceof Point)
//				{
//		
//					DialogPoint mt=new DialogPoint();
//					mt.setTbX(Integer.toString(((Point) pomOblik).getX()));
//					mt.setTxtY(Integer.toString(((Point) pomOblik).getY()));
//					mt.setBoja(((Point)pomOblik).getCol());
//					mt.setVisible(true);
//					try {
//					if(mt.isOk())
//					{
//					((Point) pomOblik).setX(Integer.parseInt(mt.getTbX()));
//					((Point) pomOblik).setY(Integer.parseInt(mt.getTxtY()));
//					((Point) pomOblik).setCol(mt.getBoja());
//					lista.set(index, pomOblik);
//					panel.setOblik(lista);
//					panel.setTestOblik(pomOblik);
//					panel.repaint();
//					}
//					}
//					catch(Exception e)
//					{
//						JOptionPane.showMessageDialog(new JFrame(), "Neispravan unos podataka.Proverite da li su sva polja popunjena brojnim vrednostima!", "Greška", JOptionPane.WARNING_MESSAGE);
//					}
//				}
//				else if(panel.getTestOblik() instanceof Line)
//				{
//					DialogLine ml=new DialogLine();
//					ml.setTxtPocKoordX(Integer.toString(((Line) pomOblik).getStartPoint().getX()));
//					ml.setTxtPocKoordY(Integer.toString(((Line) pomOblik).getStartPoint().getY()));
//					ml.setTxtKrKoordX(Integer.toString(((Line) pomOblik).getEndPoint().getX()));
//					ml.setTxtKrKoordY(Integer.toString(((Line) pomOblik).getEndPoint().getY()));
//					ml.setCol(((Line) pomOblik).getCol());
//					ml.setVisible(true);
//					try
//					{
//					if(ml.isOk())
//					{
//						((Line)pomOblik).setStartPoint(new Point((Integer.parseInt(ml.getTxtPocKoordX())),(Integer.parseInt(ml.getTxtPocKoordY()))));
//						((Line)pomOblik).setEndPoint(new Point((Integer.parseInt(ml.getTxtKrKoordX())),(Integer.parseInt(ml.getTxtKrKoordY()))));
//						((Line)pomOblik).setCol(ml.getCol());
//						lista.set(index,pomOblik);
//						panel.setOblik(lista);
//						panel.setTestOblik(pomOblik);
//						panel.repaint();
//					}
//					}
//					catch(Exception ex)
//					{
//						JOptionPane.showMessageDialog(new JFrame(), "Neispravan unos podataka.Proverite da li su sva polja popunjena brojnim vrednostima!", "Greška", JOptionPane.WARNING_MESSAGE);
//					}
//				}
//				else if(panel.getTestOblik() instanceof Rectangle)
//				{
//					DialogRectangle dp=new DialogRectangle();
//					dp.setTxtXKoordinata(Integer.toString(((Rectangle)pomOblik).getUpperLeftPoint().getX()));
//					dp.setTxtYKoordinata(Integer.toString(((Rectangle)pomOblik).getUpperLeftPoint().getY()));
//					dp.setTxtSirina(Integer.toString(((Rectangle)pomOblik).getWidth()));
//					dp.setTxtVisina(Integer.toString(((Rectangle)pomOblik).getHeight()));
//					dp.setBojaUnut(((Rectangle)pomOblik).getBojaUnutrasnjosti());
//					dp.setBojaIvice(((Rectangle)pomOblik).getBojaIvice());
//					dp.setVisible(true);
//					try
//					{
//					if(dp.isOk())
//					{
//						((Rectangle)pomOblik).setUpperLeftPoint(new Point(Integer.parseInt(dp.getTxtXKoordinata()),Integer.parseInt(dp.getTxtYKoordinata())));
//						((Rectangle)pomOblik).setHeight(Integer.parseInt(dp.getTxtVisina()));
//						((Rectangle)pomOblik).setWidth(Integer.parseInt(dp.getTxtSirina()));
//						((Rectangle)pomOblik).setBojaIvice(dp.getBojaIvice());
//						((Rectangle)pomOblik).setBojaUnutrasnjosti(dp.getBojaUnut());
//						lista.set(index,pomOblik);
//						panel.setOblik(lista);
//						panel.setTestOblik(pomOblik);
//						panel.repaint();
//					}
//					}
//					catch(NumberFormatException ex)
//					{
//						JOptionPane.showMessageDialog(new JFrame(), "Neispravan unos podataka.Proverite da li su sva polja popunjena brojnim vrednostima!", "Greška", JOptionPane.WARNING_MESSAGE);
//					}
//					catch(Exception e)
//					{
//						JOptionPane.showMessageDialog(new JFrame(), "Visina i sirina moraju da budu pozitivni brojevi!", "Greška", JOptionPane.WARNING_MESSAGE);
//					}
//					
//				}
//				else if(panel.getTestOblik() instanceof Donut)
//				{
//					DialogDonut dk=new DialogDonut();
//					dk.setTxtKoordX(Integer.toString(((Donut)pomOblik).getCenter().getX()));
//					dk.setTxtKoordY(Integer.toString(((Donut)pomOblik).getCenter().getY()));
//					dk.setTxtUnut(Integer.toString(((Donut)pomOblik).getInnerRadius()));
//					dk.setTxtSpolj(Integer.toString(((Donut)pomOblik).getRadius()));
//					dk.setBojaIvice((((Donut)pomOblik).getBojaIvice()));
//					dk.setBojaUnut((((Donut)pomOblik).getBojaUnut()));
//					dk.setVisible(true);
//					try {
//					if(dk.isOk())
//					{
//						((Donut)pomOblik).setCenter(new Point(Integer.parseInt(dk.getTxtKoordX()),Integer.parseInt(dk.getTxtKoordY())));
//						((Donut)pomOblik).setInnerRadius(Integer.parseInt(dk.getTxtUnut()));
//						((Donut)pomOblik).setRadius(Integer.parseInt(dk.getTxtSpolj()));
//						((Donut)pomOblik).setBojaIvice(dk.getBojaIvice());
//						((Donut)pomOblik).setBojaManjeIvice(dk.getBojaIvice());
//						((Donut)pomOblik).setBojaUnut(dk.getBojaUnut());
//						lista.set(index,pomOblik);
//						panel.setOblik(lista);
//						panel.setTestOblik(pomOblik);
//						panel.repaint();
//					}
//					}
//					catch(NumberFormatException e)
//					{
//						JOptionPane.showMessageDialog(new JFrame(), "Neispravan unos podataka.Proverite da li su sva polja popunjena brojnim vrednostima!", "Greška", JOptionPane.WARNING_MESSAGE);
//					}
//					catch(Exception ex)
//					{
//						JOptionPane.showMessageDialog(new JFrame(), "Poluprecnici moraju da budu veci od nule i poluprecnik unutrasnjeg kruga mora da bude manji od poluprecnika velikog kruga!", "Greška", JOptionPane.WARNING_MESSAGE);
//					}
//					
//				}else if(panel.getTestOblik() instanceof Circle)
//				{
//					DialogCircle dk=new DialogCircle();
//					dk.setTxtKoordX(Integer.toString(((Circle)pomOblik).getCenter().getX()));
//					dk.setTxtKoordY(Integer.toString(((Circle)pomOblik).getCenter().getY()));
//					dk.setPoluprecnik(Integer.toString(((Circle)pomOblik).getRadius()));
//					dk.setBojaUnut(((Circle)pomOblik).getBojaUnut());
//					dk.setBojaIvice(((Circle)pomOblik).getBojaIvice());
//					dk.setVisible(true);
//					try
//					{
//					if(dk.isOk())
//					{
//						((Circle)pomOblik).setCenter(new Point(Integer.parseInt(dk.getTxtKoordX()),Integer.parseInt(dk.getTxtKoordY())));
//						((Circle)pomOblik).setRadius(Integer.parseInt(dk.getTextPoluprecnik()));
//						((Circle)pomOblik).setBojaIvice(dk.getBojaIvice());
//						((Circle)pomOblik).setBojaUnut(dk.getBojaUnut());
//						lista.set(index,pomOblik);
//						panel.setOblik(lista);
//						panel.setTestOblik(pomOblik);
//						panel.repaint();
//						
//					}
//					}
//					catch(NumberFormatException ex)
//					{
//						JOptionPane.showMessageDialog(new JFrame(), "Neispravan unos podataka.Proverite da li su sva polja popunjena brojnim vrednostima!", "Greška", JOptionPane.WARNING_MESSAGE);
//					}
//					catch(Exception e)
//					{
//						JOptionPane.showMessageDialog(new JFrame(), "Vrednost poluprecnika mora da bude pozitivna!", "Greška", JOptionPane.WARNING_MESSAGE);
//					}
//				}
//				
//				}
//				else
//				{
//					panel.setStartPoint(null);
//					JOptionPane.showMessageDialog(new JFrame(), "Nijedan oblik nije selektovan.", "Greska!", JOptionPane.WARNING_MESSAGE);
//				}
//			}
//			});
//		grupa.add(tglbtnModifikuj);
//		tglbtnModifikuj.setFont(new Font("Tahoma", Font.PLAIN, 20));
//		toolBar.add(tglbtnModifikuj);
//		tglbtnObrisi = new JToggleButton("Obri\u0161i");
//		tglbtnObrisi.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//				if(panel.getTestOblik()!=null)
//				{
//					Shape pomOblik=panel.getTestOblik();
//					ArrayList<Shape> lista=panel.getOblik();
//					int index=lista.indexOf(pomOblik);
//					if(JOptionPane.showConfirmDialog(new JFrame(), "Da li ste sigurni da želite da obrišete selektovani oblik?","Potvrda",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
//					{
//						lista.remove(index);
//						panel.setOblik(lista);
//						panel.setTestOblik(null); 
//						panel.repaint();
//					}
//				}
//				else
//				{
//					panel.setStartPoint(null);
//					JOptionPane.showMessageDialog(new JFrame(), "Nijedan oblik nije selektovan.", "Greska!", JOptionPane.WARNING_MESSAGE);
//				}
//	
//			}
//		});
//		grupa.add(tglbtnObrisi);
//		tglbtnObrisi.setFont(new Font("Tahoma", Font.PLAIN, 20));
//		toolBar.add(tglbtnObrisi);
//		
//		
//		panel.setSize(new Dimension(20, 40));
//		panel.setPreferredSize(new Dimension(200, 400));
//		contentPane.add(panel);
//		GroupLayout gl_contentPane = new GroupLayout(contentPane);
//		gl_contentPane.setHorizontalGroup(
//			gl_contentPane.createParallelGroup(Alignment.LEADING)
//				.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 805, Short.MAX_VALUE)
//				.addGroup(gl_contentPane.createSequentialGroup()
//					.addContainerGap()
//					.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//					.addContainerGap(129, Short.MAX_VALUE))
//		);
//		gl_contentPane.setVerticalGroup(
//			gl_contentPane.createParallelGroup(Alignment.LEADING)
//				.addGroup(gl_contentPane.createSequentialGroup()
//					.addContainerGap()
//					.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
//					.addPreferredGap(ComponentPlacement.RELATED)
//					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE))
//		);
//		contentPane.setLayout(gl_contentPane);
//		
//		
//		
//		
//	}
//	
//	public boolean getTglbtnTacka() {
//		return tglbtnTacka.isSelected();
//	}
//
//	public boolean getTglbtnLinija() {
//		return tglbtnLinija.isSelected();
//	}
//
//	public boolean getTglbtnPravougaonik() {
//		return tglbtnPravougaonik.isSelected();
//	}
//
//	public boolean getTglbtnKrug() {
//		return tglbtnKrug.isSelected();
//	}
//
//	public boolean getTglbtnKrugSaRupom() {
//		return tglbtnKrugSaRupom.isSelected();
//	}
//
//	public boolean getTglbtnSelektuj() {
//		return tglbtnSelektuj.isSelected();
//	}
//
//	public boolean getTglbtnModifikuj() {
//		return tglbtnModifikuj.isSelected();
//	}
//
//	public boolean getTglbtnObrisi() {
//		return tglbtnObrisi.isSelected();
//	}
//	
//}
