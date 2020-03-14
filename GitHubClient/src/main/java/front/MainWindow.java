package front;

import javax.swing.*;
import java.awt.*;

import front.components.Button;

public class MainWindow extends JFrame {

    private String repositoryName = "rep-name";
    private String branchName = "branch-name";

    boolean checkFetchOrigin = false;

    private JMenuBar menuBar;
    private Button currentRepositoryButton, currentBranchButton, fetchOriginButton, pushButton;
    private JPanel mainPanel, commitInfoPanel, editPanel, topButtonsPanel, codeEditorPanel;

    private JTabbedPane changesPane;
    private JPanel historyPanel, changesPanel, commitCommentsPanel;
    private JButton commitToMaster;
    private JTextField commitText;
    private JTextArea commitDescription;

    private JLabel noLocalChanges;

    //private JButton changesBtn, historyButton;

    public MainWindow() {
        initFrame();
    }

    private void setPanels() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        commitInfoPanel = new JPanel();
        commitInfoPanel.setLayout(new BorderLayout());
        commitInfoPanel.setPreferredSize(new Dimension(200, 600));

        mainPanel.add(commitInfoPanel, BorderLayout.WEST);

        editPanel = new JPanel();
        editPanel.setLayout(null);
        mainPanel.add(editPanel, BorderLayout.CENTER);

        topButtonsPanel = new JPanel();
        topButtonsPanel.setPreferredSize(new Dimension(900, 50));
        topButtonsPanel.setLayout(null);
        mainPanel.add(topButtonsPanel, BorderLayout.NORTH);

        //panels in tabbed pane
        changesPanel = new JPanel();
        changesPanel.setLayout(new BorderLayout());
        changesPanel.setBackground(Color.gray);

        historyPanel = new JPanel();
        historyPanel.setLayout(new BorderLayout());

        commitCommentsPanel = new JPanel();
        commitCommentsPanel.setLayout(null);
        commitCommentsPanel.setPreferredSize(new Dimension(200, 190));
        commitCommentsPanel.setBackground(Color.BLACK);
        changesPanel.add(commitCommentsPanel, BorderLayout.SOUTH);

        codeEditorPanel = new JPanel();
        mainPanel.add(codeEditorPanel, BorderLayout.CENTER);
    }

    private void setSecondComponents() {
        noLocalChanges = new JLabel("No local changes");
        codeEditorPanel.add(noLocalChanges);
    }

    private void setComponentsOnCommitCommentPanel() {
        commitText = new JTextField();
        commitText.setBounds(40, 10, 150, 25);
        commitCommentsPanel.add(commitText);

        commitDescription = new JTextArea();
        commitDescription.setBounds(5, 40, 185, 115);
        commitCommentsPanel.add(commitDescription);

        commitToMaster = new JButton("Commit to master");
        commitToMaster.setBounds(5, 160, 185, 25);
        commitCommentsPanel.add(commitToMaster);
    }

    private void setButtonsOnTopPanel() {
        currentRepositoryButton = new Button("<html>Current repository<br />" + repositoryName + "</html>");
        currentRepositoryButton.setBounds(1, 0, 200, 50);
        topButtonsPanel.add(currentRepositoryButton);

        currentBranchButton = new Button("<html>Current branch<br />" + branchName + "</html>");
        currentBranchButton.setBounds(202, 0, 200, 50);
        topButtonsPanel.add(currentBranchButton);

        fetchOriginButton = new Button("<html>Fetch origin<br />" + checkFetchOrigin + "</html>");
        fetchOriginButton.setBounds(403, 0, 200, 50);
        topButtonsPanel.add(fetchOriginButton);

        pushButton = new Button("Pull origin");
        pushButton.setBounds(604, 0, 200, 50);
        topButtonsPanel.add(pushButton);
    }

    private void setComponentsOnChangesPanel() {
        changesPane = new JTabbedPane();
        changesPane.setTabPlacement(JTabbedPane.TOP);
        changesPane.setPreferredSize(new Dimension(200, 400));

        changesPane.addTab("Changes", changesPanel);
        changesPane.addTab("History", historyPanel);
        commitInfoPanel.add(changesPane, BorderLayout.CENTER);
    }

    private void setMenuBar() {
        menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        menuBar.add(file);

        JMenu repository = new JMenu("Repository");
        menuBar.add(repository);

        JMenuItem newRepository = new JMenuItem("Create new repository...");
        JMenuItem cloneRepository = new JMenuItem("Clone repository...");
        JMenuItem options = new JMenuItem("Options...");
        JMenuItem exit = new JMenuItem("Exit");

        file.add(newRepository);
        file.add(cloneRepository);
        file.add(options);
        file.add(exit);

        JMenuItem push = new JMenuItem("Push");
        JMenuItem pull = new JMenuItem("Pull");

        repository.add(push);
        repository.add(pull);

        setJMenuBar(menuBar);
    }

    private void tuneFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));
        setTitle("IGit");
        setVisible(true);
    }

    private void initFrame() {
        tuneFrame();
        setMenuBar();
        setPanels();
        setButtonsOnTopPanel();
        setComponentsOnChangesPanel();
        setSecondComponents();
        setComponentsOnCommitCommentPanel();
    }

}
