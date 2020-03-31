package front;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;

import front.components.Button;
import front.components.ChangedFilesViewerPanel;
import front.components.HistoryFilesViewerPanel;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

public class MainWindow extends JFrame {

    private String repositoryName = "";
    private String branchName = "";
    private String linkToCloneRepository;

    boolean checkFetchOrigin = false;

    private JMenuBar menuBar;
    private Button currentRepositoryButton, currentBranchButton, fetchOriginButton, pushButton;
    private JPanel mainPanel, commitInfoPanel, editPanel, topButtonsPanel, codeEditorPanel, informationCenterPanel;

    private JTabbedPane changesPane;
    private JPanel historyPanel, changesPanel, commitCommentsPanel;
    private JButton commitToMaster;
    private JTextField commitText;
    private JTextArea commitDescription;

    private JLabel noLocalChanges, viewRepositoryOnGitHub, openRepositoryInExplorer;

    private Font headerFont, preHeaderFont;

    private final String repositoryPath = "C:/I-Git-Repositories/";
    private final String pathToTempDirectory = "src/main/java/temp/openedRepository";

    public MainWindow() {
        getOpenedRepository();
        initFonts();
        initFrame();
        setButtonsText();
    }

    private void createNewEmptyRepository(String name) {
        try {
            String way = "C:/I-Git-Repositories/" + name;
            Git.init().setDirectory(new File(way)).call();
            FileWriter fileWriter = new FileWriter(way + "/" + ".gitattributes");
            fileWriter.write("* text=auto");
            fileWriter.flush();
            fileWriter.close();
            repositoryName = name;
            setOpenedRepository();
            setButtonsText();
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setButtonsText() {
        currentRepositoryButton.setText("<html>Current repository:<br />" + repositoryName + "</html>");
        currentBranchButton.setText("<html>Current branch:<br />" + branchName + "</html>");
        repaint();
    }

    private void setOpenedRepository() {
        try {
            FileWriter fileWriter = new FileWriter(pathToTempDirectory);
            fileWriter.write(repositoryName + "/" + branchName);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getOpenedRepository() {
        File file = new File(pathToTempDirectory);
        Scanner sc;
        try {
            sc = new Scanner(file);
            String fileContent = sc.nextLine();
            repositoryName = fileContent.split("/", 2)[0];
            branchName = fileContent.split("/", 2)[1];
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(
                    this, "Error 002: App-file (openedRepository.file) not found", "Error", JOptionPane.ERROR_MESSAGE);
            repositoryName = "none";
            branchName = "none";
        }
    }

    private void setRepositoryUri() {
        linkToCloneRepository = JOptionPane.showInputDialog(
                this,
                "Enter clone-repository link:",
                "Repository Cloning",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void getRepositoryName() {
        try {
            repositoryName = linkToCloneRepository.replace("https://github.com/", "")
                    .replace(".git", "")
                    .split("/", 2)[1];
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error 003: Incorrect link", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gitCloneRepository() throws IOException, GitAPIException {
        File directory = new File(repositoryPath + repositoryName);
        Git git = Git.cloneRepository().setURI(linkToCloneRepository).setDirectory(directory).call();
        Repository repository = git.getRepository();
        branchName = repository.getBranch();
    }

    private void cloneRepository() {
        try {
            setRepositoryUri();
            getRepositoryName();
            gitCloneRepository();
            setOpenedRepository();
            setButtonsText();
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    private void initFonts() {
        try {
            String[] fontPath = new String[] {
                    "Baloo2/Baloo2-ExtraBold.ttf", "Baloo2/Baloo2-Bold.ttf", "Baloo2/Baloo2-Regular.ttf"};

            for (String s : fontPath) {
                File file = new File(Objects.requireNonNull(
                        getClass().getClassLoader().getResource(s)).getFile());
                GraphicsEnvironment ge =
                        GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, file));
            }

            headerFont = new Font("Baloo 2 ExtraBold", Font.PLAIN, 30);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setPanels() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        commitInfoPanel = new JPanel();
        commitInfoPanel.setLayout(new BorderLayout());
        commitInfoPanel.setPreferredSize(new Dimension(301, 600));

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

        //create if on program start, which check code updates
        codeEditorPanel = new JPanel();
        codeEditorPanel.setBackground(Color.gray);
//        mainPanel.add(codeEditorPanel, BorderLayout.CENTER);

        informationCenterPanel = new JPanel();
        informationCenterPanel.setBackground(Color.CYAN);
        informationCenterPanel.setLayout(null);
        mainPanel.add(informationCenterPanel, BorderLayout.CENTER);
    }

    private void setSecondComponents() {
        noLocalChanges = new JLabel("<html>No local changes</html>");
        viewRepositoryOnGitHub = new JLabel(
                "<html>Open page of your repository in browser:<br />Tap the button or press BUTTON_NAMES</html>");
        openRepositoryInExplorer = new JLabel(
                "<html>View the files of your repository in explorer:<br />Tap the button or press BUTTONS_NAMES</html>");

        noLocalChanges.setFont(headerFont);
        noLocalChanges.setBounds(70, 70, 600, 100);
        openRepositoryInExplorer.setBounds(160, 260, 530, 50);
        viewRepositoryOnGitHub.setBounds(160, 310, 530, 50);

        informationCenterPanel.add(noLocalChanges);
        informationCenterPanel.add(viewRepositoryOnGitHub);
        informationCenterPanel.add(openRepositoryInExplorer);
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
        currentRepositoryButton = new Button("<html>Current repository<br /></html>");
        currentRepositoryButton.setBounds(1, 0, 300, 50);
        topButtonsPanel.add(currentRepositoryButton);

        currentBranchButton = new Button("<html>Current branch<br /></html>");
        currentBranchButton.setBounds(302, 0, 200, 50);
        topButtonsPanel.add(currentBranchButton);

        fetchOriginButton = new Button("<html>Fetch origin<br /></html>");
        fetchOriginButton.setBounds(503, 0, 200, 50);
        topButtonsPanel.add(fetchOriginButton);

        pushButton = new Button("Pull origin");
        pushButton.setBounds(704, 0, 200, 50);
        topButtonsPanel.add(pushButton);
    }

    private void setComponentsOnChangesPanel() {
        changesPane = new JTabbedPane();
        changesPane.setTabPlacement(JTabbedPane.TOP);
        changesPane.setPreferredSize(new Dimension(301, 400));
        changesPane.addChangeListener(e -> {
            codeEditorPanel.removeAll();
            int width = codeEditorPanel.getWidth();
            int height = codeEditorPanel.getHeight();
            if (changesPane.getSelectedComponent() == changesPanel) {
                codeEditorPanel.add(new ChangedFilesViewerPanel(width, height));
                repaint();
            } else {
                codeEditorPanel.add(new HistoryFilesViewerPanel(width, height));
                repaint();
            }
        });

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
        newRepository.addActionListener(e -> {
            createNewEmptyRepository(JOptionPane.showInputDialog(this, "Enter the repository name:"));
            setButtonsText();
        });
        JMenuItem cloneRepository = new JMenuItem("Clone repository...");
        cloneRepository.addActionListener(e -> {
            try {
                cloneRepository();
                JOptionPane.showMessageDialog(
                        this, "Cloning success", "Success", JOptionPane.INFORMATION_MESSAGE);
                repaint();
            } catch (JGitInternalException ex) {
                JOptionPane.showMessageDialog(
                        this, "Error 001: Directory exist", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
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
