/**
 *-------------------------------------------------------------- 80 columns ---|
 * This is the main class for the application. It only has two methods: the
 * static main()  that kicks the whole thing off and a menu creation method.  
 * The given code instantiates the window and sets up its internals by creating 
 * and installing the drawing canvas, toolbar, and menus. All of the code works 
 * correctly as is and should require no changes.
 *  (minor edits by Nick Parlante 2/2002)
 *
 * @version      1.0 10/8/99
 * @author       Julie Zelenski
 */
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


public class JavaDraw 
{

    static public void main(String[] args) 
    {
	JFrame frame = new JFrame("JavaDraw!");
	Toolbar toolbar = new Toolbar();
	DrawingCanvas canvas = new DrawingCanvas(toolbar, 350, 350);
	frame.getContentPane().setLayout(new BorderLayout(4,4));
	frame.getContentPane().add(toolbar, BorderLayout.SOUTH);
	frame.getContentPane().add(canvas, BorderLayout.CENTER);
	frame.setJMenuBar(createMenus(canvas));

	frame.pack();	// resize window to fit components at preferred size
	frame.setVisible(true); // bring window on-screen
		
	frame.addWindowListener(new WindowAdapter() {public void windowClosing(WindowEvent e) {
	    System.exit(0);
	}
	    });
		
    }


    private static File get_file_for_save(){
	JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	jfc.setDialogTitle("Choose a directory to save your file: ");

	int returnValue = jfc.showSaveDialog(null);
	if (returnValue == JFileChooser.APPROVE_OPTION) {
	    File selectedFile = jfc.getSelectedFile();
	    return selectedFile;
	}
	return null;
    }

    private static File get_file_for_open(){
	JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	jfc.setDialogTitle("Choose a directory to save your file: ");

	int returnValue = jfc.showOpenDialog(null);
	if (returnValue == JFileChooser.APPROVE_OPTION) {
	    File selectedFile = jfc.getSelectedFile();
	    return selectedFile;
	}
	return null;
    }

    public static void export(java.awt.Component component, String type, File file){
	BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
	Graphics2D g = image.createGraphics();
	g.dispose();
	try {
	    ImageIO.write(image, type, file);
	} catch (IOException exp) {
	    exp.printStackTrace();
	}
    }
    

    static protected JMenuBar createMenus(final DrawingCanvas canvas)

    {
	JMenuBar mb = new JMenuBar();
	JMenuItem mi;
	int menuMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		
	JMenu m = new JMenu("File");
	m.add(mi = new JMenuItem("Clear all"));
	mi.addActionListener( new ActionListener() { 
		public void actionPerformed(ActionEvent e) { canvas.clearAll();}});
	mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, menuMask));
	m.add(mi = new JMenuItem("Load file"));
	mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, menuMask));
	mi.addActionListener( new ActionListener() { 
		public void actionPerformed(ActionEvent e) {
		    canvas.loadFile(get_file_for_open());
		}

	    });
	m.add(mi = new JMenuItem("Save to file"));
	mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, menuMask));
	mi.addActionListener( new ActionListener() { 
		public void actionPerformed(ActionEvent e) {
		    canvas.saveToFile(get_file_for_save());
		}});
	m.add(mi = new JMenuItem("Export to PNG file"));
	mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, menuMask));
	mi.addActionListener( new ActionListener() { 
		public void actionPerformed(ActionEvent e) {
		    File file = get_file_for_save();
		    export(canvas, "png", file);
		}});
	m.add(mi = new JMenuItem("Quit"));
	mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, menuMask));
	mi.addActionListener( new ActionListener() { 
		public void actionPerformed(ActionEvent e) { System.exit(0);}});
	mb.add(m);
		
	m = new JMenu("Edit");
	m.add(mi = new JMenuItem("Cut"));
	mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, menuMask));
	mi.addActionListener( new ActionListener() { 
		public void actionPerformed(ActionEvent e) { canvas.cut();}});
	m.add(mi = new JMenuItem("Copy"));
	mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, menuMask));
	mi.addActionListener( new ActionListener() { 
		public void actionPerformed(ActionEvent e) { canvas.copy();}});
	m.add(mi = new JMenuItem("Paste"));
	mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, menuMask));
	mi.addActionListener( new ActionListener() { 
		public void actionPerformed(ActionEvent e) { canvas.paste();}});
	m.add(mi = new JMenuItem("Delete"));
	mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0));
	mi.addActionListener( new ActionListener() { 
		public void actionPerformed(ActionEvent e) { canvas.delete();}});
	mb.add(m);
		
	m = new JMenu("Layering");
	m.add(mi = new JMenuItem("Bring to front"));
	mi.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) { canvas.bringToFront();}});
	m.add(mi = new JMenuItem("Send to back"));
	mi.addActionListener( new ActionListener() { 
		public void actionPerformed(ActionEvent e) { canvas.sendToBack();}});
	mb.add(m);
	return mb;
    }
}
