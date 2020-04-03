package front.components;

import front.MainWindow;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;

public class CreateNewRepositoryWindow extends JFrame {

    private final int FIELD_WIDTH = 352, FIELD_HEIGHT = 24;

    private MainWindow owner;
    private int ownerWidth, ownerHeight;
    private int ownerX, ownerY;

    private int childX, childY;
    private final int childWidth = 400, childHeight = 457;

    private JPanel contentPanel;

    private Label name, description, localPath, licence, initRepoWithReadMe;
    private JTextField repositoryNameField, repositoryDescriptionField, localPathField;
    private JCheckBox initReadMeChecker;
    private JComboBox<String> licensesList;
    private ArrayList<String> licenses = new ArrayList<>();
    private Button accept, cancel, choosePath;

    private Font labelsFont = new Font("Arial", Font.PLAIN, 11);
    private Font headerFont;

    private File licensesNamesFile = new File("src/main/resources/licenses/namesOfLicenses");

    public CreateNewRepositoryWindow(MainWindow owner,
                                     int ownerX, int ownerY, int ownerWidth, int ownerHeight, Font headerFont) {
        this.headerFont = headerFont;
        this.ownerX = ownerX;
        this.ownerY = ownerY;
        this.ownerWidth = ownerWidth;
        this.ownerHeight = ownerHeight;
        this.owner = owner;

        loadLicenses();
        initChildCoordinates();
        initWindow();
        setPanel();
        addRepositoryInfoFields();
        setLabels();
        setInitReadMeChecker();
        setLicensesList();
        setButtons();
        setActionsOnButtons();
        loadWay();
    }

    private void loadLicenses() {
        try {
            Scanner sc = new Scanner(licensesNamesFile);
            while (sc.hasNextLine()) {
                licenses.add(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadWay() {
        File way = new File("src/main/java/temp/wayToRepositories");
        Scanner scanner = null;
        try {
            scanner = new Scanner(way);
            localPathField.setText(scanner.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createNewRepository() {
        if (repositoryNameField.getText() != null &&
                localPathField.getText() != null && localPathField.getText() != null) {
            String name = repositoryNameField.getText();
            String way = localPathField.getText() + name;
            try {
                File repository = new File(way);
                if (!repository.exists()) {
                    Git.init().setDirectory(new File(way)).call();
                    FileWriter fileWriter = new FileWriter(way + "/" + ".gitattributes");
                    fileWriter.write("* text=auto");
                    fileWriter.flush();
                    fileWriter.close();
                    owner.repositoryName = name;
                    owner.setOpenedRepository();
                    owner.setButtonsText();
                    if (repositoryDescriptionField.getText() != null) {
                        createDescription(repositoryDescriptionField.getText(), way);
                    }
                    if (licensesList.getSelectedIndex() != 0) {
                        createLicense(way);
                    } else {
                        JOptionPane.showMessageDialog(
                                this, "Repository created!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            this, "Error 001", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (GitAPIException | IOException ex) {
                ex.printStackTrace();
            }
        }
        owner.setEnabled(true);
        dispose();
    }

    private void createDescription(String description, String way) {
        try {
            FileWriter fileWriter = new FileWriter(way + "/.git" + "/" + "description");
            fileWriter.write(description);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createLicense(String way) throws IOException {
        String licenseName = Objects.
                requireNonNull(licensesList.getSelectedItem()).toString().
                replace(".", "").
                replace("\"", "");

        FileWriter fileWriter;
        File license = new File("src/main/resources/licenses/" + licenseName);

        Scanner sc = new Scanner(license);
        String licenseContent = "";
        while (sc.hasNextLine()) {
            licenseContent += sc.nextLine() + "\n";
        }

        if (licenseName.contains("Apache") || licenseName.contains("BSD") ||
                licenseName.contains("Eclipse") || licenseName.contains("MIT")) {
            Properties p = new Properties();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream("src/main/resources/user_info.properties");
                p.load(fis);
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String user_name = p.getProperty("name");

            licenseContent = licenseContent.replace("&yy&",
                    String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
            licenseContent = licenseContent.replace("`holders`", user_name);

            fileWriter = new FileWriter(way + "/" + "LICENSE");
            fileWriter.write(licenseContent);
            fileWriter.flush();
            fileWriter.close();
            JOptionPane.showMessageDialog(
                    this, "Repository created!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            fileWriter = new FileWriter(way + "/" + "LICENCE");
            fileWriter.write(licenseContent);
        }
    }

    private void setActionsOnButtons() {
        cancel.addActionListener(e -> {
            owner.setEnabled(true);
            dispose();
        });
        accept.addActionListener(e -> {
            createNewRepository();
        });
    }

    private void setButtons() {
        choosePath = new Button(309, 205, 69, 24, "Choose", labelsFont);
        contentPanel.add(choosePath);
        accept = new Button(134, 412, 119, 25, "Create repository", labelsFont);
        contentPanel.add(accept);
        cancel = new Button(259, 412, 119, 25, "Cancel", labelsFont);
        contentPanel.add(cancel);
    }

    private void setLicensesList() {
        licensesList = new JComboBox<>();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addAll(licenses);
        model.setSelectedItem(licenses.get(0));
        licensesList.setModel(model);

        licensesList.setBounds(20, 289, FIELD_WIDTH, FIELD_HEIGHT);
        contentPanel.add(licensesList);
    }

    private void setInitReadMeChecker() {
        initReadMeChecker = new JCheckBox();
        initReadMeChecker.setBounds(20, 243, 20, 13);
        contentPanel.add(initReadMeChecker);
    }

    private void setLabels() {
        name = new Label(20, 75, FIELD_WIDTH, 12, "Name:", labelsFont);
        contentPanel.add(name);

        description = new Label(20, 133, FIELD_WIDTH, 12, "Description:", labelsFont);
        contentPanel.add(description);

        localPath = new Label(20, 189, FIELD_WIDTH, 12, "Local path:", labelsFont);
        contentPanel.add(localPath);

        licence = new Label(20, 273, FIELD_WIDTH, 12, "License:", labelsFont);
        contentPanel.add(licence);

        initRepoWithReadMe = new Label(40, 245, FIELD_WIDTH - 20, 12,
                "Initialize this repository with a README", labelsFont);
        contentPanel.add(initRepoWithReadMe);
    }

    private void addRepositoryInfoFields() {
        repositoryNameField = new JTextField();
        repositoryNameField.setBounds(20, 92, FIELD_WIDTH, FIELD_HEIGHT);
        contentPanel.add(repositoryNameField);

        repositoryDescriptionField = new JTextField();
        repositoryDescriptionField.setBounds(20, 148, FIELD_WIDTH, FIELD_HEIGHT);
        contentPanel.add(repositoryDescriptionField);

        localPathField = new JTextField();
        localPathField.setBounds(20, 204, 278, FIELD_HEIGHT);
        contentPanel.add(localPathField);
    }

    private void setPanel() {
        contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setFont(headerFont);
                g.drawString("Create a new repository:", 20, 32);
                g.drawLine(0, 50, 400, 50);
                g.drawLine(0, 391, 400, 391);
            }
        };
        contentPanel.setLayout(null);
        getContentPane().add(contentPanel);
    }

    private void initChildCoordinates() {
        childX = ownerX + (ownerWidth / 2) - (childWidth / 2);
        childY = ownerY + (ownerHeight / 2) - (childHeight / 2);
    }

    private void initWindow() {
        setUndecorated(true);
        setLocationRelativeTo(owner);
        setSize(childWidth, childHeight);
        setLocation(childX, childY);
        setVisible(true);
    }

}