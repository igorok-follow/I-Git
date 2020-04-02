package front.components;

import front.MainWindow;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import java.awt.*;
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

    private void createNewRepository() {
        if (repositoryNameField.getText() != null && localPathField.getText() != null) {
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
                    if (licensesList.getSelectedItem() != "none") {
                        String licenseName = Objects.
                                requireNonNull(licensesList.getSelectedItem()).toString().
                                replace(".", "").
                                replace("\"", "");

                        FileWriter fileWriter1;
                        File license = new File("src/main/resources/licenses/" + licenseName);
                        if (licenseName.contains("Apache") || licenseName.contains("BSD") ||
                                licenseName.contains("Eclipse") || licenseName.contains("MIT")) {

                            Properties p = new Properties();
                            FileInputStream fis = new FileInputStream("src/main/resources/user_info.properties");
                            p.load(fis);
                            String user_name = p.getProperty("name");
                            fis.close();

                            Scanner sc = new Scanner(license);
                            String licenseContent = "";
                            while (sc.hasNextLine()) {
                                licenseContent += sc.nextLine() + "\n";
                            }
                            licenseContent = licenseContent.replace("&yy&",
                                    String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
                            licenseContent = licenseContent.replace("`holders`", user_name);
                            fileWriter1 = new FileWriter(way + "/" + "LICENSE");
                            fileWriter1.write(licenseContent);
                            fileWriter1.flush();
                            fileWriter1.close();
                            JOptionPane.showMessageDialog(
                                    this, "Repository created!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
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
    }

    private void setActionsOnButtons() {
        cancel.addActionListener(e -> dispose());
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