package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controller.LatexEditorController;
import model.VersionsManager;
import model.strategies.VersionsStrategy;
import model.strategies.VolatileVersionsStrategy;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OpeningWindow {

	private JFrame frame;
	public static LatexEditorController controller;
	public static VersionsManager versionsManager;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OpeningWindow window = new OpeningWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public OpeningWindow() {
		VersionsStrategy versionsStrategy = new VolatileVersionsStrategy();

		versionsManager = VersionsManager.getInstance();
		versionsManager.setStrategy(versionsStrategy);
		
		controller = LatexEditorController.getInstance();

		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnCreateNewDocument = new JButton("Create New Document");
		btnCreateNewDocument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChooseTemplate("opening");
				frame.dispose();
			}
		});
		btnCreateNewDocument.setBounds(89, 26, 278, 36);
		frame.getContentPane().add(btnCreateNewDocument);
		
		JButton btnOpenExistingDocument = new JButton("Open Existing Document");
		btnOpenExistingDocument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				
				if (chooser.getSelectedFile() != null) {
					Object response = null;
					controller.setFilename(chooser.getSelectedFile().getAbsolutePath());
					if (controller.getFilename().endsWith(".html")) {
						
						response = JOptionPane.showInputDialog(null,
						            "Choose the file type", "",
						            JOptionPane.QUESTION_MESSAGE, null, new String[] { "Book", "Report", "Article", "Letter", "Empty" },
						            "B");
						
						if (response != null) {
							controller.setType(response.toString().toLowerCase() + "Template");					
							controller.enact("loadHTML");
						}
					}	
					else {
						controller.enact("load");
					}
					
					if (response != null) {
						new MainWindow();
						frame.dispose();
					}
				}
			}
		});
		btnOpenExistingDocument.setBounds(89, 92, 278, 36);
		frame.getContentPane().add(btnOpenExistingDocument);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(99, 169, 268, 25);
		frame.getContentPane().add(btnExit);
	}
}
