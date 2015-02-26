package com.swimmingdale.dff.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.swimmingdale.dff.core.FilesFinder;
import com.swimmingdale.dff.core.FilesFinderFactory;

public class DuplicateFilesFinderGui {

	private JFrame mainFrame;
	private JPanel controlPanel;
	private JScrollPane resultsScrollPane;
	private JPanel resultsPanel;
	private JPanel actionsPanel;

	public DuplicateFilesFinderGui() {
		prepareGUI();
	}

	public static void main(String[] args) {
		DuplicateFilesFinderGui swingControlDemo = new DuplicateFilesFinderGui();
		swingControlDemo.showControlPanel();
	}

	private List<ArrayList<File>> findDuplicates(String rootPath) {
		FilesFinderFactory ffFactory = new FilesFinderFactory();
		FilesFinder ff = ffFactory.createFilesFinder(rootPath);
		List<ArrayList<File>> duplicateFiles = new ArrayList<ArrayList<File>>();
		if (ff != null) {
			Map<Long, List<File>> allFilesMap = ff.getFileSizeMap();
			for (Long key : allFilesMap.keySet()) {
				if (allFilesMap.get(key).size() > 1) {
					duplicateFiles.add((ArrayList<File>) allFilesMap.get(key));
				}
			}
		}
		return duplicateFiles;
	}

	private void prepareGUI() {
		mainFrame = new JFrame("Duplicate Files Finder");
		mainFrame.setSize(1000, 600);
		mainFrame.setLayout(new BorderLayout());

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());

		resultsPanel = new JPanel();
		resultsScrollPane = new JScrollPane(resultsPanel);

		actionsPanel = new JPanel();
	}

	private void showControlPanel() {
		JLabel headerLabel = new JLabel("Choose directory:", JLabel.LEFT);
		final JTextField dirFilePathField = new JTextField("", 50);
		final JFileChooser fileDialog = new JFileChooser();
		final JButton findButton = new JButton("Find Duplicates");
		JButton showFileDialogButton = new JButton("Browse");

		Path desktopPath = FileSystems.getDefault().getPath(System.getProperty("user.home"), "");

		fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// fileDialog.setCurrentDirectory(desktopPath.toFile());
		dirFilePathField.setText(desktopPath.toString());

		dirFilePathField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showFoundFiles(findDuplicates(dirFilePathField.getText()));
			}
			
		});
		
		showFileDialogButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileDialog.showOpenDialog(mainFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					java.io.File file = fileDialog.getSelectedFile();
					dirFilePathField.setText(file.getAbsolutePath());
				} else {
				}
			}
		});

		findButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showFoundFiles(findDuplicates(dirFilePathField.getText()));
			}
		});

		controlPanel.add(headerLabel);
		controlPanel.add(dirFilePathField);
		controlPanel.add(showFileDialogButton);
		controlPanel.add(findButton);
		mainFrame.add(controlPanel, BorderLayout.PAGE_START);
		mainFrame.setVisible(true);
	}

	private void showActionsPanel() {
		JButton deleteButton = new JButton("Delete files");

		actionsPanel.add(deleteButton);

		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("deleting");
			}
		});

		mainFrame.add(actionsPanel, BorderLayout.PAGE_END);
		mainFrame.setVisible(true);
	}

	private void showFoundFiles(List<ArrayList<File>> duplicates) {
		resultsPanel.setLayout(new GridLayout(0, 1));
		resultsPanel.removeAll();
		final JLabel optionsLabel;
		final List<JCheckBox> checkBoxesList = new ArrayList<JCheckBox>();
		resultsPanel.setAutoscrolls(true);

		optionsLabel = new JLabel("Select the files to delete: ");

		resultsPanel.add(optionsLabel);
		for (List<File> similar : duplicates) {
			boolean first = true;
			for (File file : similar) {
				JCheckBox checkBox = new JCheckBox(file.getAbsolutePath());
				if (!first) {
					checkBox.setSelected(true);
				}
				first = false;
				resultsPanel.add(checkBox);
				checkBoxesList.add(checkBox);
			}
			resultsPanel.add(new JSeparator());
		}
		mainFrame.add(resultsScrollPane);
		mainFrame.setVisible(true);
		if (duplicates.size() > 0) {
			showActionsPanel();
		}
	}
}