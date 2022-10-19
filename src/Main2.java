import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.FileNameMap;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.swing.Timer;

import static java.nio.file.StandardCopyOption.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.DefaultComboBoxModel;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Window.Type;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JProgressBar;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JScrollBar;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.SwingConstants;

public class Main2 extends JFrame {
	private static JPanel contentPane;
	private static JButton startBtn;
	private static JLabel adeLoaded;
	private static JLabel sydLoaded;
	private static JLabel scLoaded;
	private static JRadioButton sydBtn;
	private static JRadioButton adeBtn;
	private static JRadioButton scBtn;
	private static double percent;
	//private static Main2 frame;
	private static JComboBox driveBox;
	private static Thread thread;
	public static boolean isMinimised;
	public static TrayIcon icon;

	static long startTime;
	static long elapsedTime;
	static long elapsedSeconds;
	static long secondsDisplay;
	static long elapsedMinutes;
	static long elapsedHours;
	private static String percentStr;

	private static int SYDCount;
	private static int ADECount;
	private static int SCCount;
	private static int totalCount;
	//private static int totalCount = SYDCount * ADECount;

	private static int failed = 0;
	private static String missing = "";
	private static boolean success = true;
	private static JProgressBar progressBar;

	private static boolean checkSYD = false;
	private static boolean SYDLoaded = false;
	private static boolean isReplaced = false;
	private static boolean isProductsLoaded = false;

	static int progressCount = 0;
	static int scanCheck = 0;

	static String log = "";

	static Records[] recordsSYD = new Records[99999];
	static Records[] recordsADE = new Records[99999];
	static Records[] recordsSC = new Records[99999];

	static String SYD = ":\\Shared drives\\Information Technology\\SYDNEY\\Website\\SYD Images\\";
	static String ADE = ":\\Shared drives\\Information Technology\\ADELAIDE\\Website\\ADE Images\\";
	static String SC = ":\\Shared drives\\Information Technology\\SOUTH COAST\\Website\\SC Images\\";
	private static final String TRAY_LOGO = "res//trayIcon3.png"; 


	static String sydCompareTxt = "smb://acrserver/Docs/SYDCompare.txt";
	static String adeCompareTxt = "smb://10.25.45.1/Docs/ADECompare.txt";
	static String scCompareTxt = "smb://10.22.5.1/2_docs/SCCompare.txt";
	private static JLabel lblNewLabel;


	public static void getDriveLetter(String letter)
	{
		SYD = letter + ":\\Shared drives\\Information Technology\\SYDNEY\\Website\\SYD Images\\";
		ADE = letter + ":\\Shared drives\\Information Technology\\ADELAIDE\\Website\\ADE Images\\";
		SC = letter + ":\\Shared drives\\Information Technology\\SOUTH COAST\\Website\\SC Images\\";
	}

	public static Date getTodaysDate() throws ParseException
	{
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
		String newLastModifiedString = dateFormat.format(date);

		Date newLastModifiedDate = dateFormat.parse(newLastModifiedString);


		return newLastModifiedDate;
	}

	public static void getPercent(int n, int t, JFrame frame)
	{
		float proportion = ((float) n) / ((float) t);
		float total = proportion * 100;

		percentStr = String.format("%.1f", total) + "%";
		frame.setTitle("Image Transfer - " + String.format("%.1f", total) + "%");

	}

	public static void matchBarcodes(boolean success, String sourceLocation, String sourceID, String 
			sourceBarcode, String destLocation, String destID, String destBarcode1, String destBarcode2, String destBarcode3, String destProd, JFrame frame) throws IOException, ParseException
	{

		if(sourceBarcode.equalsIgnoreCase("-"))
		{

		}
		else
		{
			if(sourceBarcode.equalsIgnoreCase(destBarcode1) ||  sourceBarcode.equalsIgnoreCase(destBarcode2)
					|| sourceBarcode.equalsIgnoreCase(destBarcode3))
			{
				File source = new File(sourceLocation + sourceID + ".jpg");
				File dest = new File(destLocation + destID + ".jpg");

				Path source1 = Paths.get(sourceLocation + sourceID + ".jpg");
				Path dest1 = Paths.get(destLocation + destID + ".jpg");

				try
				{
					if(dest.exists() && source.exists())
					{
						//Deletes item if it exists already. Will be overwritten.
						dest.delete();

						//System.out.println("Inner Deleted - " + destProd);

						Files.copy(source1, dest1, StandardCopyOption.REPLACE_EXISTING);

						if(!isMinimised)
						{
							progressBar.setValue(progressCount);
							progressBar.update(progressBar.getGraphics());

						}

						getPercent(scanCheck, totalCount, frame);

						//System.out.println("Replacing " + destID + " - " + destProd);
						isReplaced = true;


					}
					else if(source.exists())
					{
						Files.copy(source1, dest1, StandardCopyOption.REPLACE_EXISTING);

						progressBar.setValue(progressCount);
						progressBar.update(progressBar.getGraphics());

						getPercent(scanCheck, totalCount, frame);


						//System.out.println("Adding " + destID + " - " + destProd);
						isReplaced = true;
					}
					else
					{

					}

				}
				catch (NoSuchFileException e)
				{
					success = false;
					failed++;


				}

			}
		}

	}

	public static void copyFiles(JFrame frame) throws IOException, ParseException 
	{
		if(sydBtn.isSelected() && SYDLoaded == true)
		{
			//totalCount = SYDCount * ADECount;

			for(int s = 0; s < SYDCount; s++)
			{
				for(int a = 0; a < ADECount; a++)
				{

					if(isReplaced == false)
					{

						matchBarcodes(true, SYD, recordsSYD[s].getID(), recordsSYD[s].getInnerBarcode(), ADE, recordsADE[a].getID(), recordsADE[a].getInnerBarcode(),
								recordsADE[a].getOuterBarcode(), recordsADE[a].getShipperBarcode(), recordsADE[a].getProdDesc(), frame);
					}

					if(isReplaced == false)
					{

						matchBarcodes(true, SYD, recordsSYD[s].getID(), recordsSYD[s].getOuterBarcode(), ADE, recordsADE[a].getID(), recordsADE[a].getInnerBarcode(),
								recordsADE[a].getOuterBarcode(), recordsADE[a].getShipperBarcode(), recordsADE[a].getProdDesc(), frame);
					}

					if(isReplaced == false)
					{

						matchBarcodes(true, SYD, recordsSYD[s].getID(), recordsSYD[s].getShipperBarcode(), ADE, recordsADE[a].getID(), recordsADE[a].getInnerBarcode(),
								recordsADE[a].getOuterBarcode(), recordsADE[a].getShipperBarcode(), recordsADE[a].getProdDesc(), frame);
					}


					scanCheck++;
					progressCount++;						


					if(success == false)
					{
						System.out.println("File not successfully copied! - " + recordsSYD[s].getID());
					}
				}

				isReplaced = false;

			}


			progressBar.setValue(progressCount);


			elapsedTime = System.currentTimeMillis() - startTime;
			elapsedSeconds = elapsedTime / 1000;
			secondsDisplay = elapsedSeconds % 60;
			elapsedMinutes = elapsedSeconds / 60;
			elapsedHours = elapsedMinutes / 60;


			System.out.println("Completed..");
			icon.displayMessage("Image Transfer", "Transfer Complete - 100%", TrayIcon.MessageType.INFO);

			System.out.println("Files Scanned: " + scanCheck);

			scanCheck = 0;
			progressCount = 0;


			System.out.println("Time Taken: " + elapsedMinutes + "mins " + secondsDisplay + "secs");

			adeBtn.setEnabled(true);

			checkSYD = true;
		}

		if((adeBtn.isSelected()) && (checkSYD == true))
		{
			//totalCount = SYDCount * ADECount;

			for(int s = 0; s < SYDCount; s++)
			{
				for(int a = 0; a < ADECount; a++)
				{

					if(isReplaced == false)
					{
						matchBarcodes(true, ADE, recordsADE[a].getID(), recordsADE[a].getInnerBarcode(), SYD, recordsSYD[s].getID(), recordsSYD[s].getInnerBarcode(),
								recordsSYD[s].getOuterBarcode(), recordsSYD[s].getShipperBarcode(), recordsSYD[s].getProdDesc(), frame);
					}

					if(isReplaced == false)
					{
						matchBarcodes(true, ADE, recordsADE[a].getID(), recordsADE[a].getOuterBarcode(), SYD, recordsSYD[s].getID(), recordsSYD[s].getInnerBarcode(),
								recordsSYD[s].getOuterBarcode(), recordsSYD[s].getShipperBarcode(), recordsSYD[s].getProdDesc(), frame);
					}

					if(isReplaced == false)
					{
						matchBarcodes(true, ADE, recordsADE[a].getID(), recordsADE[a].getShipperBarcode(), SYD, recordsSYD[s].getID(), recordsSYD[s].getInnerBarcode(),
								recordsSYD[s].getOuterBarcode(), recordsSYD[s].getShipperBarcode(), recordsSYD[s].getProdDesc(), frame);
					}


					scanCheck++;
					progressCount++;



					if(success == false)
					{
						System.out.println("File not successfully copied! - " + recordsSYD[s].getID());
					}

				}

				isReplaced = false;

			}


			progressBar.setValue(progressCount);

			elapsedTime = System.currentTimeMillis() - startTime;
			elapsedSeconds = elapsedTime / 1000;
			secondsDisplay = elapsedSeconds % 60;
			elapsedMinutes = elapsedSeconds / 60;
			elapsedHours = elapsedMinutes / 60;



			System.out.println("Completed..");
			icon.displayMessage("Image Transfer", "Transfer Complete - 100%", TrayIcon.MessageType.INFO);

			System.out.println("Files Scanned: " + scanCheck);

			scanCheck = 0;
			progressCount = 0;



			System.out.println("Time Taken: " + elapsedMinutes + "mins " + secondsDisplay + "secs");

			adeBtn.setEnabled(true);			
		}

		if((scBtn.isSelected()) && (SYDLoaded == true))
		{
			//totalCount = SYDCount * SCCount;

			for(int s = 0; s < SYDCount; s++)
			{
				for(int a = 0; a < SCCount; a++)
				{

					if(isReplaced == false)
					{

						matchBarcodes(true, SYD, recordsSYD[s].getID(), recordsSYD[s].getInnerBarcode(), SC, recordsSC[a].getID(), recordsSC[a].getInnerBarcode(),
								recordsSC[a].getOuterBarcode(), recordsSC[a].getShipperBarcode(), recordsSC[a].getProdDesc(), frame);
					}

					if(isReplaced == false)
					{

						matchBarcodes(true, SYD, recordsSYD[s].getID(), recordsSYD[s].getOuterBarcode(), SC, recordsSC[a].getID(), recordsSC[a].getInnerBarcode(),
								recordsSC[a].getOuterBarcode(), recordsSC[a].getShipperBarcode(), recordsSC[a].getProdDesc(), frame);
					}

					if(isReplaced == false)
					{

						matchBarcodes(true, SYD, recordsSYD[s].getID(), recordsSYD[s].getShipperBarcode(), SC, recordsSC[a].getID(), recordsSC[a].getInnerBarcode(),
								recordsSC[a].getOuterBarcode(), recordsSC[a].getShipperBarcode(), recordsSC[a].getProdDesc(), frame);
					}

					scanCheck++;
					progressCount++;



					if(success == false)
					{
						System.out.println("File not successfully copied! - " + recordsSYD[s].getID());
					}

				}

				isReplaced = false;

			}


			progressBar.setValue(progressCount);

			elapsedTime = System.currentTimeMillis() - startTime;
			elapsedSeconds = elapsedTime / 1000;
			secondsDisplay = elapsedSeconds % 60;
			elapsedMinutes = elapsedSeconds / 60;
			elapsedHours = elapsedMinutes / 60;


			System.out.println("Completed..");
			icon.displayMessage("Image Transfer", "Transfer Complete - 100%", TrayIcon.MessageType.INFO);

			System.out.println("Files Scanned: " + scanCheck);

			scanCheck = 0;
			progressCount = 0;

			System.out.println("Time Taken: " + elapsedMinutes + "mins " + secondsDisplay + "secs");

		}


	}

	public static int loadFile(int count, String compareTxtLocation, Records[] records, String siteName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, SmbException, MalformedURLException, UnknownHostException
	{
		count = 0;

		if(siteName.equalsIgnoreCase("Sydney"))
		{
			SYDLoaded = true;
		}

		String user = "administrator";
		String pass ="368M00reb";

		//a smb package was needed to be installed for this to work - jcifs 1.3.19.jar
		String sharedFolder="shared";
		String path = compareTxtLocation;
		//NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("",user, pass);
		SmbFile smbFile = new SmbFile(path);
		SmbFileInputStream smbfos = new SmbFileInputStream(smbFile);

		Scanner load = new Scanner(smbfos);

		String ID, prodDesc, innerBarcode, outerBarcode, shipperBarcode;

		while(load.hasNextLine())
		{
			String str = load.nextLine();
			StringTokenizer reader = new StringTokenizer(str, "\t");

			ID = reader.nextToken();
			prodDesc = reader.nextToken();
			innerBarcode = reader.hasMoreTokens() ? reader.nextToken() : "-";
			outerBarcode = reader.hasMoreTokens() ? reader.nextToken() : "-";
			shipperBarcode = reader.hasMoreTokens() ? reader.nextToken() : "-";


			records[count] = new Records(ID, prodDesc, innerBarcode, outerBarcode, shipperBarcode);		

			count++;
		}

		load.close();


		System.out.println("Loaded " + count + " " + siteName + " records...");
		return count;


	}

	public static String searchBarcode(String input)
	{
		String sydney = "NOT FOUND", adelaide = "NOT FOUND", southCoast = "NOT FOUND";

		for(int i = 0; i < SYDCount; i++)
		{
			if(recordsSYD[i].getInnerBarcode().equals(input) || recordsSYD[i].getOuterBarcode().equals(input) || recordsSYD[i].getShipperBarcode().equals(input))
			{
				sydney = recordsSYD[i].getProdDesc();
			}

		}

		for(int i = 0; i < ADECount; i++)
		{
			if(recordsADE[i].getInnerBarcode().equals(input) || recordsADE[i].getOuterBarcode().equals(input) || recordsADE[i].getShipperBarcode().equals(input))
			{
				adelaide = recordsADE[i].getProdDesc();
			}

		}

		for(int i = 0; i < SCCount; i++)
		{
			if(recordsSC[i].getInnerBarcode().equals(input) || recordsSC[i].getOuterBarcode().equals(input) || recordsSC[i].getShipperBarcode().equals(input))
			{
				southCoast = recordsSC[i].getProdDesc();
			}

		}

		return "Sydney: " + sydney + " matches Adelaide: " + adelaide + " & South Coast: " + southCoast; 
	}



	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main2 frame = new Main2();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public Main2() throws IOException {
		setTitle("Image Transfer");
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setBounds(100, 100, 623, 314);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		

		if (!SystemTray.isSupported()) {
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			return;
		}

		final SystemTray tray = SystemTray.getSystemTray();
		
		BufferedImage img = null; img = ImageIO.read(new File(TRAY_LOGO));
		
		final PopupMenu popup = new PopupMenu();
		icon = new TrayIcon(img, "Image Transfer", popup);
		
				
		icon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				isMinimised = false;
				setVisible(true);
				tray.remove(icon);				
			}	
		});
		
		icon.addMouseMotionListener(new MouseMotionAdapter()
		{
		    @Override
		    public void mouseMoved(MouseEvent e)
		    {
		    	icon.setToolTip(percentStr);
		    }
		});

		Handler h = new Handler(tray, icon);
		addWindowStateListener(h);
		addWindowListener(h);

		MenuItem item1 = new MenuItem("Open");
		item1.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				isMinimised = false;
				setVisible(true);
				tray.remove(icon);
			}
		});

		MenuItem item2 = new MenuItem("Close");
		item2.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				tray.remove(icon);
				setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				dispose();
				System.exit(0);
			}
		});

		popup.add(item1);
		popup.add(item2);

		
		
		JButton btnLoad = new JButton("LOAD");
		btnLoad.setForeground(Color.BLACK);
		btnLoad.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {	

				thread = new Thread() {
					public void run() {
						try {

							getDriveLetter(driveBox.getSelectedItem().toString());
							System.out.println(SYD);

							try {						
								SYDCount = loadFile(SYDCount, sydCompareTxt, recordsSYD, "Sydney");
								sydLoaded.setText("Loaded " + SYDCount + " SYD records...");

								ADECount = loadFile(ADECount, adeCompareTxt, recordsADE, "Adelaide");
								adeLoaded.setText("Loaded " + ADECount + " ADE records...");

								SCCount = loadFile(SCCount, scCompareTxt, recordsSC, "South Coast");
								scLoaded.setText("Loaded " + SCCount + " SC records...");

								isProductsLoaded = true;

								//totalCount = SYDCount * ADECount;

								setTitle("Image Transfer - Loaded");

							} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SmbException
									| MalformedURLException | UnknownHostException | UnsupportedLookAndFeelException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


							thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}  
				};

				thread.start();

			}
		});

		btnLoad.setBounds(369, 11, 112, 144);
		contentPane.add(btnLoad);

		startBtn = new JButton("START");
		startBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {				

				thread = new Thread() {
					public void run() {
						try {

							getDriveLetter(driveBox.getSelectedItem().toString());
							System.out.println(SYD);

							try {

								if(sydBtn.isSelected())
								{
									totalCount = SYDCount * ADECount;
								}
								else if(adeBtn.isSelected())
								{
									totalCount = SYDCount * ADECount;
								}
								else if(scBtn.isSelected())
								{
									totalCount = SYDCount * SCCount;
								}


								startTime = System.currentTimeMillis();
								setTitle("Image Transfer - Processing");

								progressBar = new JProgressBar(progressCount, totalCount);
								progressBar.setValue(progressCount);
								progressBar.setBounds(10, 11, 346, 143);
								contentPane.add(progressBar);

								contentPane.repaint();

								getPercent(scanCheck, totalCount, Main2.this);

								System.out.println(checkSYD);

								copyFiles(Main2.this);

								System.out.println("Time Taken: " + elapsedMinutes + "mins " + secondsDisplay + "secs");
								setTitle("Image Transfer - Completed in " + elapsedMinutes + "mins " + secondsDisplay + "secs");


							} catch (IOException e1) {
								// TODO Auto-generated catch block
								System.out.println("Failed");
								e1.printStackTrace();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


							thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}  
				};

				thread.start();





			}
		});
		
		
		
		startBtn.setBounds(485, 11, 112, 144);
		contentPane.add(startBtn);

		sydBtn = new JRadioButton("SYD to ADE");
		sydBtn.setSelected(true);
		sydBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				adeBtn.setSelected(false);
				scBtn.setSelected(false);

			}
		});
		sydBtn.setBounds(6, 168, 109, 23);
		contentPane.add(sydBtn);

		adeBtn = new JRadioButton("ADE to SYD");
		adeBtn.setEnabled(false);
		adeBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sydBtn.setSelected(false);
				scBtn.setSelected(false);

			}
		});
		adeBtn.setBounds(130, 168, 109, 23);
		contentPane.add(adeBtn);

		sydLoaded = new JLabel("");
		sydLoaded.setFont(new Font("Tahoma", Font.PLAIN, 12));
		sydLoaded.setBounds(422, 200, 175, 14);
		contentPane.add(sydLoaded);

		adeLoaded = new JLabel("");
		adeLoaded.setFont(new Font("Tahoma", Font.PLAIN, 12));
		adeLoaded.setBounds(422, 225, 175, 14);
		contentPane.add(adeLoaded);

		JButton btnNewButton = new JButton("FIND PRODUCT");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {

				if(isProductsLoaded)
				{
					String s = JOptionPane.showInputDialog("Enter Barcode:");
					JOptionPane.showMessageDialog(null, searchBarcode(s));
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Products not loaded..");
				}

			}
		});
		btnNewButton.setBounds(369, 162, 150, 29);
		contentPane.add(btnNewButton);
		
		JButton closeBtn = new JButton("Close");
		closeBtn.setForeground(Color.BLACK);
		closeBtn.setBounds(524, 162, 74, 29);
		contentPane.add(closeBtn);
		
		closeBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				System.exit(0);
			}
		});

		scBtn = new JRadioButton("SYD to SC");
		scBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				sydBtn.setSelected(false);
				adeBtn.setSelected(false);

			}
		});
		scBtn.setBounds(254, 168, 109, 23);
		contentPane.add(scBtn);

		scLoaded = new JLabel("");
		scLoaded.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scLoaded.setBounds(422, 249, 175, 14);
		contentPane.add(scLoaded);

		driveBox = new JComboBox();
		driveBox.setModel(new DefaultComboBoxModel(new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}));
		driveBox.setSelectedIndex(7);
		driveBox.setBounds(10, 217, 51, 22);
		contentPane.add(driveBox);

		lblNewLabel = new JLabel("Change Drive");
		lblNewLabel.setBounds(73, 221, 91, 14);
		contentPane.add(lblNewLabel);

	}
}

class Handler extends WindowAdapter {
	private final SystemTray tray;
	private final TrayIcon icon;

	public Handler(SystemTray tray, TrayIcon icon) {
		super();
		this.tray = tray;
		this.icon = icon;
	}

	private void addTrayIconDisposeFrame(JFrame frame) {
		try {
			tray.add(icon);
			frame.dispose();
			//frame.setVisible(false);
		} catch (AWTException ex) {
			ex.printStackTrace();
		}
	}

	@Override public void windowStateChanged(WindowEvent e) {
		System.out.println("ICONIFIED");
		if (e.getNewState() == Frame.ICONIFIED) {
			addTrayIconDisposeFrame((JFrame) e.getSource());
		}
	}
	@Override public void windowClosing(WindowEvent e) {
		System.out.println("windowClosing");
		Main2.isMinimised = true;
		addTrayIconDisposeFrame((JFrame) e.getSource());
	}
}


