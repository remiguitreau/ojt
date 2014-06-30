package com.ojt.ui;

import org.jdesktop.swingx.JXDatePicker;

import com.ojt.AddCompetitorListener;
import com.ojt.Club;
import com.ojt.Competitor;
import com.ojt.OJTUtils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Formulaire de saisie d'un nouveau compétiteur
 * @author CDa
 * @since 9 août 2009 (CDa) : Création
 */
public class AddCompetitorDialog extends JDialog {

    // -------------------------------------------------------------------------
    // Constantes
    // -------------------------------------------------------------------------

    private static final String CODE_CLUB = "Code";

    private static final String CLUB_NAME = "Nom";

    private static final String NEW_CLUB = "Nouveau club";

    private final static String COMPETITOR_NAME = CLUB_NAME;

    private final static String COMPETITOR_FIRST_NAME = "Prénom";

    private final static String COMPETITOR_LICENCE = "Licence";

    private final static String COMPETITOR_CLUB_NAME = "Club";

    private static final String OK_BUTTON = "Ajouter";

    private static final String CANCEL_BUTTON = "Annuler";

    // -------------------------------------------------------------------------
    // Propriétés de l'objet
    // -------------------------------------------------------------------------

    private JPanel formPanel, buttonPanel;

    private JLabel competitorNameLabel, competitorFirstNameLabel, competitorLicenceLabel,
            competitorClubNameLabel, competitorClubCodeLabel, competitorClubNewNameLabel,
            competitorBirthDateLabel;

    private JTextField competitorNameTextField, competitorFirstNameTextField, competitorLicenceTextField,
            competitorClubCodeTextField, competitorClubNewNameTextField;

    private JXDatePicker competitorBirthDatePicker;

    private JComboBox competitorClubNameComboBox;

    private JButton buttonOk, buttonCancel;

    private List<AddCompetitorListener> listeners;

    private Map<String, Club> clubList;

    // -------------------------------------------------------------------------
    // Constructeurs
    // -------------------------------------------------------------------------

    /**
     * Constructeur par défaut
     */
    public AddCompetitorDialog(final JFrame parent) {
        super(parent, "Saisie d'un nouveau compétiteur", true);
    }

    // -------------------------------------------------------------------------
    // Accesseurs
    // -------------------------------------------------------------------------

    public Map<String, Club> getClubList() {
        return clubList;
    }

    public void setClubList(final Map<String, Club> clubList) {
        this.clubList = clubList;
    }

    // -------------------------------------------------------------------------
    // Méthodes publiques
    // -------------------------------------------------------------------------

    /**
     * Méthode d'initialisation de l'affichage du formulaire
     */
    public void initFrame() {
        setLayout(new BorderLayout());
        setResizable(false);
        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildButtonPanel(), BorderLayout.SOUTH);
        setSize(300, 270);

        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        setVisible(true);
    }

    private JPanel buildFormPanel() {
        formPanel = new JPanel(new GridBagLayout());
        initComponents();
        int numRow = 0;
        formPanel.add(competitorNameLabel, new GridBagConstraints(0, numRow, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(10, 5, 5, 5), 0, 0));
        formPanel.add(competitorNameTextField, new GridBagConstraints(1, numRow++, 1, 1, 1, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(10, 5, 5, 5), 0, 0));
        final Insets insets = new Insets(0, 5, 5, 5);
        formPanel.add(competitorFirstNameLabel, new GridBagConstraints(0, numRow, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0));
        formPanel.add(competitorFirstNameTextField, new GridBagConstraints(1, numRow++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0));

        formPanel.add(competitorLicenceLabel, new GridBagConstraints(0, numRow, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0));
        formPanel.add(competitorLicenceTextField, new GridBagConstraints(1, numRow++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0));

        formPanel.add(competitorBirthDateLabel, new GridBagConstraints(0, numRow, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0));
        formPanel.add(competitorBirthDatePicker, new GridBagConstraints(1, numRow++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0));

        formPanel.add(competitorClubNameLabel, new GridBagConstraints(0, numRow, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0));
        formPanel.add(competitorClubNameComboBox, new GridBagConstraints(1, numRow++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0));

        formPanel.add(competitorClubNewNameLabel, new GridBagConstraints(0, numRow, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0));
        formPanel.add(competitorClubNewNameTextField, new GridBagConstraints(1, numRow++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0));

        formPanel.add(competitorClubCodeLabel, new GridBagConstraints(0, numRow, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0));
        formPanel.add(competitorClubCodeTextField, new GridBagConstraints(1, numRow++, 1, 1, 0, 0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, insets, 0, 0));
        return formPanel;
    }

    /**
     * Ajoute un écouteur
     * @param addCompetitorListener L'écouteur à ajouter
     */
    public void addListener(final AddCompetitorListener addCompetitorListener) {
        if (listeners == null) {
            listeners = new ArrayList<AddCompetitorListener>();
        }
        listeners.add(addCompetitorListener);
    }

    /**
     * Supprime un écouteur
     * @param addCompetitorListener L'écouteur à supprimer
     */
    public void removeListener(final AddCompetitorListener addCompetitorListener) {
        if (listeners == null) {
            return;
        }
        listeners.remove(addCompetitorListener);
    }

    // -------------------------------------------------------------------------
    // Méthode privées
    // -------------------------------------------------------------------------

    private void initComponents() {
        competitorNameLabel = new JLabel(COMPETITOR_NAME);
        competitorNameLabel.setPreferredSize(new Dimension(80, 20));
        competitorFirstNameLabel = new JLabel(COMPETITOR_FIRST_NAME);
        competitorLicenceLabel = new JLabel(COMPETITOR_LICENCE);
        competitorClubNameLabel = new JLabel(COMPETITOR_CLUB_NAME);
        competitorBirthDateLabel = new JLabel("Date de naissance");
        initComponentsForAddingNewClub();

        competitorNameTextField = new JTextField();
        competitorFirstNameTextField = new JTextField();
        competitorLicenceTextField = new JTextField();
        competitorBirthDatePicker = new JXDatePicker();
        final Set<String> clubNames = fillComboBoxWithClub();
        competitorClubNameComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (e.getItem().equals(NEW_CLUB)) {
                    setVisibilityOfNewClubFields(true);
                } else {
                    setVisibilityOfNewClubFields(false);
                }
            }
        });
        competitorClubNameComboBox.setSelectedIndex(clubNames.size() > 0 ? 1 : 0);
        if (clubNames.size() == 0) {
            setVisibilityOfNewClubFields(true);
        }
    }

    private Set<String> fillComboBoxWithClub() {
        competitorClubNameComboBox = new JComboBox(new String[] { NEW_CLUB });
        final Set<String> clubNames = getClubList().keySet();
        for (final String clubName : clubNames) {
            competitorClubNameComboBox.addItem(clubName);
        }
        return clubNames;
    }

    private void initComponentsForAddingNewClub() {
        competitorClubNewNameLabel = new JLabel(CLUB_NAME);
        competitorClubNewNameLabel.setVisible(false);
        competitorClubCodeLabel = new JLabel(CODE_CLUB);
        competitorClubCodeLabel.setVisible(false);

        competitorClubCodeTextField = new JTextField();
        competitorClubCodeTextField.setVisible(false);
        competitorClubNewNameTextField = new JTextField();
        competitorClubNewNameTextField.setVisible(false);
    }

    private void setVisibilityOfNewClubFields(final boolean visible) {
        competitorClubNewNameLabel.setVisible(visible);
        competitorClubCodeLabel.setVisible(visible);

        competitorClubCodeTextField.setVisible(visible);
        competitorClubNewNameTextField.setVisible(visible);
    }

    private JPanel buildButtonPanel() {
        buttonPanel = new JPanel(new GridLayout());
        buttonCancel = new JButton(CANCEL_BUTTON);
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                AddCompetitorDialog.this.dispose();
            }
        });
        buttonOk = new JButton(OK_BUTTON);
        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (isFormCorrectlyFilledIn()) {
                    notifyOnAddingCompetitor();
                    AddCompetitorDialog.this.dispose();
                } else {
                    JOptionPane.showMessageDialog(AddCompetitorDialog.this,
                            "Veuillez remplir tous les champs.");
                }
            }
        });
        buttonPanel.add(buttonOk);
        buttonPanel.add(buttonCancel);
        return buttonPanel;
    }

    /**
     * Notification des écouteurs en cas d'ajout d'un compétiteur
     */
    private void notifyOnAddingCompetitor() {
        for (final AddCompetitorListener addCompetitorListener : listeners) {
            final Competitor competitor = new Competitor();
            competitor.setName(competitorNameTextField.getText());
            competitor.setFirstName(competitorFirstNameTextField.getText());
            if (newClubIsSelectedInComboBox()) {
                final Club club = new Club(competitorClubCodeTextField.getText(),
                        OJTUtils.extractDepartmentFromClubId(competitorClubCodeTextField.getText()),
                        competitorClubNewNameTextField.getText());
                competitor.setClub(club);
                getClubList().put(club.toString2(), club);
                fillComboBoxWithClub();
                competitorClubNameComboBox.repaint();
            } else {
                competitor.setClub(getClubList().get(competitorClubNameComboBox.getSelectedItem()));
            }
            competitor.setLicenseCode(competitorLicenceTextField.getText());
            competitor.setBirthDate(competitorBirthDatePicker.getDate());
            OJTUtils.fillCompetitorWithCategory(competitor, new Date());
            addCompetitorListener.addNewCompetitor(competitor);
        }
    }

    private boolean newClubIsSelectedInComboBox() {
        return competitorClubNameComboBox.getSelectedItem().equals(NEW_CLUB);
    }

    /**
     * Vérifie la conformité du formulaire
     * @return vrai si le formulaire est correctement renseigné, faux sinon
     */
    private boolean isFormCorrectlyFilledIn() {
        boolean correctlyFilledIn = true;
        correctlyFilledIn &= competitorNameTextField.getText().equals("") ? false : true;
        correctlyFilledIn &= competitorFirstNameTextField.getText().equals("") ? false : true;
        correctlyFilledIn &= competitorLicenceTextField.getText().equals("") ? false : true;
        if (newClubIsSelectedInComboBox()) {
            correctlyFilledIn &= competitorClubNewNameTextField.getText().equals("") ? false : true;
            correctlyFilledIn &= competitorClubCodeTextField.getText().equals("") ? false : true;
            if (OJTUtils.extractDepartmentFromClubId(competitorClubCodeTextField.getText()).equals("")) {
                correctlyFilledIn &= false;
            }
        }
        return correctlyFilledIn;
    }

    // -------------------------------------------------------------------------
    // Méthode de tests
    // -------------------------------------------------------------------------

    /**
     * Programme de tests
     */
    public static void main(final String[] args) {
        final AddCompetitorDialog competitorFrame = new AddCompetitorDialog(null);
        final HashMap<String, Club> clubList2 = new HashMap<String, Club>();
        clubList2.put("club1", new Club());
        competitorFrame.setClubList(clubList2);
        competitorFrame.initFrame();
    }
}
