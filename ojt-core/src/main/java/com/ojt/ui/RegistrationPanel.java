/**
 * 
 */
package com.ojt.ui;

import org.apache.log4j.Logger;

import com.ojt.AddCompetitorListener;
import com.ojt.Club;
import com.ojt.CompetitionDescriptor;
import com.ojt.Competitor;
import com.ojt.CompetitorList;
import com.ojt.Pair;
import com.ojt.balance.BalanceListener;
import com.ojt.dao.CompetitorsDao;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public final class RegistrationPanel extends JPanel implements BalanceListener {

    // -------------------------------------------------------------------------
    // Constantes
    // -------------------------------------------------------------------------

    private final static String ADD_COMPETITOR_BUTTON = "Nouveau compétiteur";

    // -------------------------------------------------------------------------
    // Propriétés de l'objet
    // -------------------------------------------------------------------------

    private JTextField licenseOrNameFieldSearch;

    private JTable competitorTable;

    private CompetitorList registeredCompetitorsList = new CompetitorList();

    private CompetitorList competitorsList = new CompetitorList();

    private CompetitorsDao competitorsDao = null;

    private CompetitorsDao registeredCompetitorsDao;

    private final Object monitor;

    private final Logger logger = Logger.getLogger(getClass());

    private JButton addButton;

    private AddCompetitorDialog addCompetitorFrame;

    private JPopupMenu optionPopupMenu;

    private JMenuItem deleteMenuItem;

    private Map<String, Club> clubList;

    private CompetitionDescriptor competitionDescriptor;

    private final JLabel weightCompetitorsNumberLabel = new JLabel();

    public RegistrationPanel() {
        monitor = new Object();
        clubList = new HashMap<String, Club>();
        buildGui();
    }

    // -------------------------------------------------------------------------
    // Accesseurs
    // -------------------------------------------------------------------------

    public void setCompetitionDescriptor(final CompetitionDescriptor competitionDescriptor) {
        this.competitionDescriptor = competitionDescriptor;
    }

    public void setRegisteredCompetitorList(final CompetitorList competitorList) {
        this.registeredCompetitorsList = competitorList;
    }

    public CompetitorList getRegisteredCompetitorList() {
        return registeredCompetitorsList;
    }

    public void setCompetitorsList(final CompetitorList competitorsList) {
        this.competitorsList = competitorsList;
        searchCompetitorsByNameOrLicense();
    }

    public CompetitorList getCompetitorList() {
        return competitorsList;
    }

    public void setRegisteredCompetitorsDao(final CompetitorsDao registeredCompetitorsDao) {
        this.registeredCompetitorsDao = registeredCompetitorsDao;
    }

    public CompetitorsDao getRegisteredCompetitorsDao() {
        return registeredCompetitorsDao;
    }

    /**
     * Permet d'ajouter une liste de compétiteurs avec une recherche s'il
     * n'existe pas déjà. S'il existe déja, on garde selui qui a un poids et pas
     * l'autre. S'ils ont tous les deux un poids, on est en conflit.
     * @param competitors liste à ajouter
     * @param replaceIfExist remplacer si le compétiteur existe déjà
     * @return renvoie la liste des compétiteurs en conflits pour traitements
     */
    public List<Pair<Competitor, Competitor>> appendCompetitors(final List<Competitor> competitors,
            final boolean replaceIfExist) {
        final List<Pair<Competitor, Competitor>> conflicts = new LinkedList<Pair<Competitor, Competitor>>();
        for (final Competitor competitor : competitors) {
            final Pair<Competitor, Competitor> conflict = appendCompetitor(competitor, replaceIfExist);
            if (conflict != null) {
                conflicts.add(conflict);
            }
        }
        return conflicts;
    }

    /**
     * Permet d'ajouter un compétiteur avec une recherche s'il n'existe pas
     * déjà. S'il existe déja, on garde selui qui a un poids et pas l'autre.
     * S'ils ont tous les deux un poids, on est en conflit.
     * @param competitor compétiteur à ajouter
     * @param replaceIfExist remplacer si le compétiteur existe déjà
     * @return renvoie la les compétiteurs en conflits pour traitements
     */
    public Pair<Competitor, Competitor> appendCompetitor(final Competitor competitor,
            final boolean replaceIfExist) {
        if (competitor == null) {
            return null;
        }
        if (competitorsList.contains(competitor)) {
            // il existe, on compare
            final Competitor comp = competitorsList.get(competitorsList.indexOf(competitor));
            if (replaceIfExist) {
                // on le remplace
                // 30.10.2009 (FMo) : utilisation des méthodes qui mettent à
                // jour le DAO
                // competitorList.remove(comp);
                // competitorList.add(competitor);
                deleteCompetitor(comp);
                addCompetitor(competitor);
            } else {
                // on regarde en fonction du poids
                if (comp.getWeightAsFloat() <= 0 && competitor.getWeightAsFloat() > 0) {
                    // le nouveau a un poids et pas l'ancien, on le prends
                    // 30.10.2009 (FMo) : utilisation des méthodes qui mettent à
                    // jour le DAO
                    // competitorList.remove(comp);
                    // competitorList.add(competitor);
                    deleteCompetitor(comp);
                    addCompetitor(competitor);
                } else if (comp.getWeightAsFloat() == competitor.getWeightAsFloat()) {
                    // les poids sont egaut, c'est pareil, on fait rien, on
                    // garde celui qui existe
                } else if (comp.getWeightAsFloat() > 0 && competitor.getWeightAsFloat() > 0) {
                    // les 2 ont un poids, il y a un problème, on renvoie la
                    // liste et on ne fait rien de plus
                    return new Pair(comp, competitor);
                }
            }
        } else {
            // Il existe pas, on l'ajoute si le poids est >0
            if (competitor.getWeightAsFloat() > 0) {
                // 30.10.2009 (FMo) : utilisation des méthodes qui mettent à
                // jour le DAO
                // competitorList.add(competitor);
                addCompetitor(competitor);
            }
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // Méthodes publiques
    // -------------------------------------------------------------------------

    /**
     * @param competitorsDao the competitorsDao to set
     */
    public void setCompetitorsDao(final CompetitorsDao competitorsDao) {
        this.competitorsDao = competitorsDao;
    }

    /**
     * Rempli la table avec la liste de compétiteurs venant du fichier excel
     */
    public void fillTable() {
        searchCompetitorsByNameOrLicense();
        updateNumberOfRegisteredCompetitors();
    }

    /**
     * Donne le focus par défaut au champ de recherche
     */
    public void setFocusOnFieldSearch() {
        licenseOrNameFieldSearch.requestFocusInWindow();
    }

    public void fillClubList() {
        clubList = new TreeMap<String, Club>(new Comparator<String>() {
            @Override
            public int compare(final String o1, final String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        for (final Competitor competitor : getCompetitorList()) {
            final Club competitorClub = competitor.getClub();
            clubList.put(competitorClub.getClubName() + " - " + competitorClub.getDepartment() + " ("
                    + competitorClub.getClubCode() + ")", competitorClub);
        }
    }

    public CompetitorList getValidCompetitors() {
        final CompetitorList validCompetitors = new CompetitorList();
        for (final Competitor competitor : registeredCompetitorsList) {
            if (competitor.getWeight() != null && competitor.getWeight().intValue() > 0) {
                validCompetitors.add(competitor);
            }
        }
        return validCompetitors;
    }

    /**
     * @return La {@link CompetitorList} des {@link Competitor} invalides (pas
     *         de poids saisi).
     */
    public CompetitorList getUnvalidCompetitors() {
        final CompetitorList unvalidCompetitors = new CompetitorList();
        for (final Competitor competitor : registeredCompetitorsList) {
            if (competitor.getWeight() == null || competitor.getWeight().intValue() <= 0) {
                unvalidCompetitors.add(competitor);
            }
        }
        return unvalidCompetitors;
    }

    // ---------------------------------------------------------
    // Package
    // ---------------------------------------------------------
    @Override
    public void weightReceived(final float weight) {
        logger.info("Réception d'un poids : " + weight);
        logger.info("ligne = " + competitorTable.getEditingRow() + " - colonne = "
                + competitorTable.getEditingColumn());
        if (competitorTable.getEditingRow() != -1
                && competitorTable.getEditingColumn() == CompetitorTableModel.WEIGHT_INDEX) {
            if (competitorTable.getCellEditor() != null) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        final int row = competitorTable.getEditingRow();
                        competitorTable.getCellEditor().stopCellEditing();
                        competitorTable.setValueAt(weight, row, CompetitorTableModel.WEIGHT_INDEX);
                        updateCompetitorsTable((CompetitorTableModel) competitorTable.getModel());
                        setFocusOnFieldSearch();
                    }
                });
            }
        }
    }

    // -------------------------------------------------------------------------
    // Méthodes privées
    // -------------------------------------------------------------------------

    private void buildGui() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(800, 450));

        createOptionMenu();

        final JLabel labelLicense = new JLabel("Nom ou licence : ");
        licenseOrNameFieldSearch = new JTextField();
        licenseOrNameFieldSearch.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(final KeyEvent event) {
                synchronized (monitor) {
                    logger.debug("Valeur saisie : " + event.getKeyChar() + " : 0x"
                            + Integer.toHexString(event.getKeyChar()));
                    if (event.getKeyChar() == 0x0a) {
                        licenseOrNameFieldSearch.transferFocus();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                competitorTable.editCellAt(0, CompetitorTableModel.WEIGHT_INDEX);
                            }
                        });
                    } else {
                        searchCompetitorsByNameOrLicense();
                    }
                }
            }
        });
        licenseOrNameFieldSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(final FocusEvent event) {
                licenseOrNameFieldSearch.selectAll();
            }
        });
        licenseOrNameFieldSearch.setPreferredSize(new Dimension(150, 20));

        addButton = new OjtButton(ADD_COMPETITOR_BUTTON);
        addButton.setPreferredSize(new Dimension(150, 21));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                addCompetitorFrame = new AddCompetitorDialog(
                        (JFrame) SwingUtilities.windowForComponent(RegistrationPanel.this));
                addCompetitorFrame.addListener(new AddCompetitorListener() {
                    @Override
                    public void addNewCompetitor(final Competitor competitor) {
                        licenseOrNameFieldSearch.setText(competitor.getLicenseCode());
                        addCompetitor(competitor);
                    }

                });
                addCompetitorFrame.setClubList(clubList);
                addCompetitorFrame.initFrame();
            }
        });

        competitorTable = new JTable();
        competitorTable.setPreferredScrollableViewportSize(new Dimension(800, 300));
        competitorTable.setFillsViewportHeight(true);
        competitorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    final int selectedRow = competitorTable.rowAtPoint(e.getPoint());
                    competitorTable.getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
                    optionPopupMenu.show(competitorTable, e.getX(), e.getY());
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    final int selectedRow = competitorTable.rowAtPoint(e.getPoint());
                    final int selectedCol = competitorTable.columnAtPoint(e.getPoint());
                    if (selectedCol == CompetitorTableModel.GRADE_INDEX) {
                        final Point pt = new Point(e.getLocationOnScreen());
                        SwingUtilities.convertPointFromScreen(pt, RegistrationPanel.this);
                        GradeBeltEditor.editGradeBelt(competitorTable, selectedRow, selectedCol, pt);
                    }
                }

            }
        });
        final JScrollPane scrollPane = new JScrollPane(competitorTable);
        scrollPane.setPreferredSize(new Dimension(700, 250));
        scrollPane.setSize(new Dimension(700, 250));

        add(labelLicense, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(20, 10, 0, 0), 0, 0));
        add(licenseOrNameFieldSearch, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(20, 10, 0, 0), 0, 0));

        add(addButton, new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(20, 10, 0, 0), 0, 0));
        add(scrollPane, new GridBagConstraints(0, 1, 4, 1, 1, 1, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(20, 10, 0, 0), 0, 0));
        add(new JLabel(), new GridBagConstraints(0, 1, 4, 1, 1, 1, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        add(weightCompetitorsNumberLabel, new GridBagConstraints(0, 2, 4, 1, 1, 1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(20, 10, 0, 0), 0, 0));
        setVisible(true);
    }

    private void createOptionMenu() {
        optionPopupMenu = new JPopupMenu();
        deleteMenuItem = new JMenuItem("Supprimer");
        optionPopupMenu.add(deleteMenuItem);
        deleteMenuItem.addActionListener(new MenuChoosedActionLister());
    }

    private void deleteCompetitor(final Competitor competitor) {
        competitorsList.remove(competitor);
        competitorsDao.deleteCompetitor(competitor);
        fillTable();
    }

    private void addCompetitor(final Competitor competitor) {
        getCompetitorList().add(competitor);
        competitorsDao.createCompetitor(competitor);
        fillTable();
    }

    /**
     * Recherche par licence
     */
    private void searchCompetitorsByNameOrLicense() {
        updateCompetitorsTable(new RegistrationCompetitorTableModel(competitionDescriptor,
                licenseOrNameFieldSearch.getText().equals("") ? getCompetitorList()
                        : retrieveCompetitorsByLicense(licenseOrNameFieldSearch.getText()), competitorsDao,
                new CompetitorWeightListener() {
                    @Override
                    public void newWeightEntered() {
                        updateNumberOfRegisteredCompetitors();
                    }
                }, registeredCompetitorsList, registeredCompetitorsDao));
    }

    private void updateNumberOfRegisteredCompetitors() {
        weightCompetitorsNumberLabel.setText(String.valueOf(registeredCompetitorsList.size())
                + " compétiteur(s) inscrits");
    }

    private void updateCompetitorsTable(final CompetitorTableModel dataModel) {
        competitorTable.setModel(dataModel);
        competitorTable.setDefaultRenderer(Object.class, new RegistrationCompetitorTableRenderer(
                registeredCompetitorsList));
        competitorTable.getColumnModel().getColumn(CompetitorTableModel.WEIGHT_INDEX).setCellEditor(
                new WeightCellEditor(new JTextField()));
        competitorTable.getColumnModel().getColumn(CompetitorTableModel.WEIGHT_INDEX).setPreferredWidth(60);
        competitorTable.getColumnModel().getColumn(CompetitorTableModel.GRADE_INDEX).setPreferredWidth(45);
    }

    private CompetitorList retrieveCompetitorsByLicense(final String requestText) {
        final CompetitorList competitorListResultSet = new CompetitorList();

        for (final Competitor competitor : getCompetitorList()) {
            if (cleanString(competitor.getLicenseCode()).indexOf(cleanString(requestText)) != -1
                    || cleanString(competitor.getDisplayName()).indexOf(cleanString(requestText)) != -1) {
                competitorListResultSet.add(competitor);
            }
        }

        return competitorListResultSet;

    }

    private String cleanString(final String name) {
        String temp = name.toLowerCase();
        temp = temp.replace('ý', 'y');
        temp = temp.replaceAll("[ùúûü]", "u");
        temp = temp.replaceAll("òóôõö]", "o");
        temp = temp.replaceAll("[ìíîï]", "i");
        temp = temp.replaceAll("[èéêë]", "e");
        temp = temp.replace('ç', 'c');
        temp = temp.replaceAll("[àáâãäåæ]", "a");
        return temp;
    }

    // -------------------------------------------------------------------------
    //
    // -------------------------------------------------------------------------
    class WeightCellEditor extends DefaultCellEditor {

        JTextField textField = new JTextField();

        public WeightCellEditor(final JTextField textField0) {
            super(textField0);
            textField = textField0;
        }

        @Override
        public boolean stopCellEditing() {
            fireEditingStopped();
            setFocusOnFieldSearch();
            return true;
        }

        @Override
        public Component getTableCellEditorComponent(final JTable table, final Object value,
                final boolean isSelected, final int row, final int column) {
            final Competitor competitor = (Competitor) value;
            textField.setText(competitor.getWeight() == null ? "" : String.valueOf(competitor.getWeight()));
            setFocusOnFieldSearch();
            return textField;
        }
    }

    class MenuChoosedActionLister implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (e.getSource().equals(deleteMenuItem)) {
                final Competitor selectedCompetitor = (Competitor) competitorTable.getValueAt(
                        competitorTable.getSelectedRow(), 0);
                if (JOptionPane.showConfirmDialog(
                        RegistrationPanel.this,
                        "Etes vous sur de vouloir supprimer le compétiteur : \n "
                                + selectedCompetitor.getDisplayName()) == JOptionPane.OK_OPTION) {
                    deleteCompetitor(selectedCompetitor);
                }
            }

        }

    }
}
