package com.swimmingdale.dff.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.swimmingdale.dff.core.FilesFinder;
import com.swimmingdale.dff.core.FilesFinderFactory;
import com.swimmingdale.dff.core.Utils;
import com.swimmingdale.dff.executor.Executor;

public class DuplicateFilesFinderGui {

	private JFrame mainFrame;
	private JPanel controlPanel;
	private JScrollPane resultsScrollPane;
	private JPanel resultsPanel;
	private JPanel actionsPanel;
	private boolean hasDeleteButton;
	private List<JCheckBox> checkboxes;

	public DuplicateFilesFinderGui() {
		prepareGUI();
		hasDeleteButton = false;
	}

	public static void main(String[] args) {
		DuplicateFilesFinderGui swingControlDemo = new DuplicateFilesFinderGui();
		swingControlDemo.showControlPanel();
	}

	private List<JCheckBox> getCheckboxes() {
		return checkboxes;
	}

	private void setCheckboxes(List<JCheckBox> checkboxes) {
		this.checkboxes = checkboxes;
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

		Path desktopPath = FileSystems.getDefault().getPath(
				System.getProperty("user.home"), "");

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

	private void buildActionsPanel(boolean hasRecords) {
		if (hasRecords) {
			JButton deleteButton = new JButton("Delete selected files");

			if (!hasDeleteButton) {
				actionsPanel.add(deleteButton);
				hasDeleteButton = true;
			}

			deleteButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					performDelete();
				}
			});

			mainFrame.add(actionsPanel, BorderLayout.PAGE_END);
			mainFrame.setVisible(true);
		} else {
			if (hasDeleteButton) {
				hasDeleteButton = false;
				actionsPanel.removeAll();
				actionsPanel.revalidate();
			}
		}
	}

	private void showFoundFiles(List<ArrayList<File>> duplicates) {
		final JLabel optionsLabel = new JLabel();
		resultsPanel.setLayout(new GridLayout(0, 1));
		resultsPanel.removeAll();

		if (duplicates.size() == 0) {
			optionsLabel.setText("There are no duplicate files found!");
			resultsPanel.add(optionsLabel);
			buildActionsPanel(false);
			setCheckboxes(null);
		} else {
			optionsLabel.setText("Select which files to be deleted:");
			resultsPanel.add(optionsLabel);
			final List<JCheckBox> checkBoxesList = new ArrayList<JCheckBox>();

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
			setCheckboxes(checkBoxesList);

			mainFrame.add(resultsScrollPane);
			mainFrame.setVisible(true);
			buildActionsPanel(true);
		}

		resultsPanel.setAutoscrolls(true);
	}

	private void performDelete() {
		List<JCheckBox> checkboxesList = getCheckboxes();
		List<File> filesForDeletion = new ArrayList<File>();
		long deletedFilesSizeBytes = 0l;
		for (JCheckBox checkBox : checkboxesList) {
			if (checkBox.isSelected()) {
				File file = new File(checkBox.getText());
				filesForDeletion.add(file);
				long fileSize = file.length();
				try {
					Executor.deleteFile(file);
					filesForDeletion.remove(file);
					deletedFilesSizeBytes += fileSize;
				} catch (IOException e) {

				}

			}
		}
		showDeletionResults(deletedFilesSizeBytes);
	}

	private void showDeletionResults(long deletedBytes) {
		String title = deletedBytes > 0 ? "Deletion complete": "No files were deleted";
		JOptionPane.showMessageDialog(mainFrame,
				Utils.getMessageDeletedSize(deletedBytes), title,
				JOptionPane.PLAIN_MESSAGE);
	}
}