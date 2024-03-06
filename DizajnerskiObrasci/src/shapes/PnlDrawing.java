//package shapes;
//KOD iz objektnog obrisi sve
//
//import java.awt.Graphics;
//import java.awt.RadialGradientPaint;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.util.ArrayList;
//import java.util.Iterator;
//
//import javax.management.modelmbean.ModelMBeanInfoSupport;
//import javax.swing.JFrame;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//
//public class PnlDrawing extends JPanel {
//
//	private Drawing frejm;
//	private ArrayList<Shape> oblik=new ArrayList<Shape>();
//	private Point startPoint;
//	private Shape testOblik;
//	
//	
//	/**
//	 * Create the panel.
//	 */
//	
//	public PnlDrawing() {
//		addMouseListener(new MouseAdapter(){
//			public void mouseClicked(MouseEvent e) {
//				thisMouseClicked(e);
//			}
//		});
//	
//	}
//	public PnlDrawing(Drawing frame) {
//		this.frejm=frame;
//		addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//			thisMouseClicked(e);
//			}
//		});
//	}
//	
//	
//	
//	protected void thisMouseClicked(MouseEvent e) {
//		if(frejm.getTglbtnSelektuj())
//		{
//			
//			testOblik=null;
//			Point p=new Point(e.getX(),e.getY());
//			Iterator<Shape> it=oblik.iterator();
//			while(it.hasNext())
//			{
//				Shape geo=it.next();
//				geo.setSelected(false);
//				if((geo.contains(p)) && geo.isSelected()==false)
//				{
//					testOblik=geo;
//				}
//				
//			}
//			
//		}
//		if(frejm.getTglbtnTacka()) {
//			Point p=new Point(e.getX(),e.getY());
//			DialogPoint dp=new DialogPoint();
//			dp.setTbXEdt(false);
//			dp.setTxtYEdt(false);
//			dp.setTbX(Integer.toString(p.getX()));
//			dp.setTxtY(Integer.toString(p.getY()));
//			dp.setVisible(true);
//			p.setCol(dp.getBoja());
//			oblik.add(p);
//		}
//		else if(frejm.getTglbtnLinija())
//		{
//			if(startPoint==null)
//			startPoint=new Point(e.getX(),e.getY());
//			else {
//			Line l=new Line(startPoint,new Point(e.getX(),e.getY()));
//			DialogLine dl=new DialogLine();
//			dl.setTxtKrKoordXEdt(false);
//			dl.setTxtKrKoordYEdt(false);
//			dl.setTxtPocKoordXEdt(false);
//			dl.setTxtPocKoordYEdt(false);
//			dl.setTxtPocKoordX(Integer.toString(l.getStartPoint().getX()));
//			dl.setTxtPocKoordY(Integer.toString(l.getStartPoint().getY()));
//			dl.setTxtKrKoordX(Integer.toString(l.getEndPoint().getX()));
//			dl.setTxtKrKoordY(Integer.toString(l.getEndPoint().getY()));
//			dl.setVisible(true);
//			l.setCol(dl.getCol());
//			oblik.add(l);
//			startPoint=null;
//			}
//		}
//		else if(frejm.getTglbtnPravougaonik())
//		{
//			Point p=new Point(e.getX(),e.getY());
//			DialogRectangle dija=new DialogRectangle();
//			dija.setTxtXKoordinata(Integer.toString(p.getX()));
//			dija.setTxtYKoordinata(Integer.toString(p.getY()));
//			dija.setTxtXKoordinataEnabled(false);
//			dija.setTxtYKoordinataEnabled(false);
//			dija.setVisible(true);
//			if(dija.isOk()) {
//			try {
//			int sirina=Integer.parseInt(dija.getTxtSirina());
//			int visina=Integer.parseInt(dija.getTxtVisina());
//			Rectangle rct=new Rectangle(p,sirina,visina);
//			rct.setBojaIvice(dija.getBojaIvice());
//			rct.setBojaUnutrasnjosti(dija.getBojaUnut());
//			oblik.add(rct);
//			}
//			catch(NumberFormatException ex)
//			{
//				JOptionPane.showMessageDialog(new JFrame(), "Neispravan unos podataka.Proverite da li su sva polja popunjena brojnim vrednostima!", "Greška", JOptionPane.WARNING_MESSAGE);
//			}
//			catch(Exception ex)
//			{
//				JOptionPane.showMessageDialog(new JFrame(), "Visina i sirina moraju da budu pozitivne!", "Greška", JOptionPane.WARNING_MESSAGE);
//			}
//			}
//		}
//		else if(frejm.getTglbtnKrug())
//		{
//			Point centar=new Point(e.getX(),e.getY());
//			DialogCircle dija=new DialogCircle();
//			dija.setTxtKoordXEdt(false);
//			dija.setTxtKoordYEdt(false);
//			dija.setTxtKoordX(Integer.toString(centar.getX()));
//			dija.setTxtKoordY(Integer.toString(centar.getY()));
//			dija.setVisible(true);
//			try
//			{
//			if(dija.isOk())
//			{
//				int radius=Integer.parseInt(dija.getTextPoluprecnik());
//				Circle krug=new Circle(centar,radius);
//				krug.setBojaIvice(dija.getBojaIvice());
//				krug.setBojaUnut(dija.getBojaUnut());
//				oblik.add(krug);
//			
//			}
//			}
//			catch(NumberFormatException ex)
//			{
//				JOptionPane.showMessageDialog(new JFrame(), "Neispravan unos podataka.Proverite da li su sva polja popunjena brojnim vrednostima!!", "Greška", JOptionPane.WARNING_MESSAGE);
//			}
//			catch(Exception ex)
//			{
//				JOptionPane.showMessageDialog(new JFrame(), "Vrednost poluprecnika mora da bude pozitivna!", "Greška", JOptionPane.WARNING_MESSAGE);	
//			}
//		}
//		else if(frejm.getTglbtnKrugSaRupom())
//		{
//			Point centar=new Point(e.getX(),e.getY());
//			DialogDonut dija=new DialogDonut();
//			dija.setTxtKoordX(Integer.toString(centar.getX()));
//			dija.setTxtKoordY(Integer.toString(centar.getY()));
//			dija.setTxtKoordXEditable(false);
//			dija.setTxtKoordYEditable(false);			dija.setVisible(true);
//			try
//			{
//			if(dija.isOk())
//			{
//				int innerRadius=Integer.parseInt(dija.getTxtUnut());
//				int outerRadius=Integer.parseInt(dija.getTxtSpolj());
//				Donut krofna=new Donut(centar,outerRadius,innerRadius);
//				krofna.setBojaIvice(dija.getBojaIvice());
//				krofna.setBojaManjeIvice(dija.getBojaIvice());
//				krofna.setBojaUnut(dija.getBojaUnut());
//				oblik.add(krofna);
//			}
//			}
//			catch(NumberFormatException ex)
//			{
//				JOptionPane.showMessageDialog(new JFrame(), "Neispravan unos podataka.Proverite da li su sva polja popunjena brojnim vrednostima!", "Greška", JOptionPane.WARNING_MESSAGE);
//			}
//			catch(Exception ex)
//			{
//				JOptionPane.showMessageDialog(new JFrame(), "Poluprecnici moraju da budu veci od nule i poluprecnik unutrasnjeg kruga mora da bude manji od poluprecnika velikog kruga!", "Greška", JOptionPane.WARNING_MESSAGE);
//			}
//			
//		}
//		else if(testOblik!=null)
//		{
//			testOblik.setSelected(true);
//		}
//		if(oblik!=null) 
//			repaint();
//	}
//	
//	public void paint(Graphics g) {
//		super.paint(g);
//		Iterator it=oblik.iterator();
//		while(it.hasNext())
//			((Shape) it.next()).draw(g);
//        }
//	
//	public ArrayList<Shape> getOblik() {
//		return oblik;
//	}
//
//	public void setOblik(ArrayList<Shape> oblik) {
//		this.oblik = oblik;
//	}
//
//	public Shape getTestOblik() {
//		return testOblik;
//	}
//
//	public void setTestOblik(Shape testOblik) {
//		this.testOblik = testOblik;
//	}
//	
//	public void setStartPoint(Point p)
//	{
//		this.startPoint=p;
//	}
//
//}
