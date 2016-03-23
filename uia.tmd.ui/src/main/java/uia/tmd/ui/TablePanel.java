package uia.tmd.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import uia.tmd.model.xml.DbServerType;
import uia.tmd.model.xml.TableType;

public class TablePanel extends JPanel {

    private static final long serialVersionUID = 2616675894691845512L;

    private static final int WIDTH = 300;

    private MainFrame frame;

    private JButton addButton;

    private JComboBox dbServverBox;

    private JTable listTable;

    private JTable pkeyTable;

    private List<TableType> data;

    private TableType selectedItem;

    public TablePanel() {
        this.data = new ArrayList<TableType>();

        setLayout(new BorderLayout(0, 0));
        setPreferredSize(new Dimension(WIDTH, 400));

        JPanel toolbarPanel = new JPanel();
        toolbarPanel.setLayout(null);
        toolbarPanel.setPreferredSize(new Dimension(WIDTH, 30));
        add(toolbarPanel, BorderLayout.NORTH);

        this.addButton = new JButton();
        this.addButton.setIcon(new ImageIcon(NaviPanel.class.getResource("/resources/images/add-small.png")));
        this.addButton.setToolTipText("Quick add one task");
        this.addButton.setBounds(0, 3, 24, 24);
        this.addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                TablePanel.this.frame.createTask(TablePanel.this.selectedItem);
            }

        });
        toolbarPanel.add(this.addButton);

        this.dbServverBox = new JComboBox();
        this.dbServverBox.setBounds(40, 3, 195, 24);
        toolbarPanel.add(this.dbServverBox);

        JScrollPane listScroll = new JScrollPane();
        listScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(listScroll, BorderLayout.CENTER);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        this.listTable = new JTable();
        this.listTable.setRowHeight(25);
        this.listTable.setModel(new TableDefModel());
        this.listTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        this.listTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        this.listTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        this.listTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.listTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent event) {
                int row = TablePanel.this.listTable.getSelectedRow();

                TablePanel.this.selectedItem = row < 0 || row >= TablePanel.this.data.size() ? null : TablePanel.this.data.get(row);
                TablePanel.this.pkeyTable.updateUI();
            }
        });
        listScroll.setViewportView(this.listTable);

        JScrollPane pkeyScroll = new JScrollPane();
        pkeyScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        pkeyScroll.setPreferredSize(new Dimension(WIDTH, 169));
        add(pkeyScroll, BorderLayout.SOUTH);

        this.pkeyTable = new JTable();
        this.pkeyTable.setRowHeight(25);
        this.pkeyTable.setModel(new TablePKeyModel());
        pkeyScroll.setViewportView(this.pkeyTable);
    }

    public MainFrame getFrame() {
        return this.frame;
    }

    public void setFrame(MainFrame frame) {
        this.frame = frame;
    }

    public void load() {
        this.data = new ArrayList<TableType>(this.frame.getTaskFactory().getTables().values());
        this.listTable.updateUI();
        this.listTable.getSelectionModel().setSelectionInterval(0, 0);

        List<DbServerType> dbs = this.frame.getTaskFactory().getTmd().getDatabaseSpace().getDbServer();
        for (DbServerType db : dbs) {
            this.dbServverBox.addItem(db.getId());
        }
        this.dbServverBox.setSelectedIndex(0);

        /**
        try {
            DataAccessor da = DataAccessor.create(
                    this.frame.getTaskFactory().getDbServer((String) this.dbServverBox.getSelectedItem()),
                    new TreeMap<String, TableType>());
        
            List<String> tables = da.listTables();
        
        }
        catch (Exception e) {
        
        }
         */
    }

    class TableDefModel extends AbstractTableModel {

        private static final long serialVersionUID = 5618580924166019218L;

        @Override
        public String getColumnName(int columnIndex) {
            return columnIndex == 0 ? "" : "Table Name";
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public int getRowCount() {
            return TablePanel.this.data.size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex < 0 || TablePanel.this.data.size() == 0) {
                return null;
            }

            switch (columnIndex) {
                case 0:
                    return "D";
                case 1:
                    return TablePanel.this.data.get(rowIndex).getName();
                default:
                    return null;
            }
        }

    }

    class TablePKeyModel extends AbstractTableModel {

        private static final long serialVersionUID = -3553978563489256767L;

        @Override
        public String getColumnName(int columnIndex) {
            return "Primary Key";
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public int getRowCount() {
            return TablePanel.this.selectedItem == null ? 0 : TablePanel.this.selectedItem.getPk().getName().size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return TablePanel.this.selectedItem == null || rowIndex < 0 ? null : TablePanel.this.selectedItem.getPk().getName().get(rowIndex);
        }
    }
}
