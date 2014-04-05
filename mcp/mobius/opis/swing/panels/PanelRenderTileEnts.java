package mcp.mobius.opis.swing.panels;

import java.util.ArrayList;

import javax.swing.JPanel;

import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.data.holders.newtypes.DataTileEntityRender;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.swing.actions.ActionRunOpisClient;
import mcp.mobius.opis.swing.widgets.JTableStats;
import net.miginfocom.swing.MigLayout;
import net.minecraft.item.ItemStack;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;

public class PanelRenderTileEnts extends JPanel implements ITabPanel{
	private JTableStats table;
	private JButton btnRunRender;
	private JLabel lblTotal;

	/**
	 * Create the panel.
	 */
	public PanelRenderTileEnts() {
		setLayout(new MigLayout("", "[grow][grow][]", "[][grow][]"));
		
		btnRunRender = new JButton("Run Render");
		add(btnRunRender, "cell 2 0");
		btnRunRender.addActionListener(new ActionRunOpisClient());
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1 3 1,grow");
		
		table = new JTableStats();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null},
			},
			new String[] {
				"Name", "Coordinates", "Timing"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, Object.class, DataTiming.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.setAutoCreateRowSorter(true);			
		scrollPane.setViewportView(table);
		
		lblTotal = new JLabel("Total : 0 µs");
		add(lblTotal, "cell 0 2 3 1,alignx center");

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( SwingConstants.CENTER );		
		
		for (int i = 0; i < table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);		
				
		
	}

	@Override
	public String getTabTitle() {
		return "TileEntity Render";
	}

	@Override
	public String getTabRefName() {
		return "opis.client.terender";
	}	
	
	public void setTable(ArrayList<DataTileEntityRender> data){
		
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		int               row   = this.updateData(table, model, DataTileEntityRender.class);		
		
		for (DataTileEntityRender o : data){
			
			ItemStack is;
			String name  = String.format("te.%d.%d", o.id, o.meta);
			
			try{
				is = new ItemStack(o.id, 1, o.meta);
				name  = is.getDisplayName();
			}  catch (Exception e) {	}			
			
			model.addRow(new Object[]  {
					 name,
					 String.format("[ %4d %4d %4d ]", o.pos.x, o.pos.y, o.pos.z),
					 o.update
					});
		}
		
		this.dataUpdated(table, model, row);			
	}
	
	public JTableStats getTable() {
		return table;
	}
	
	public <U> int updateData(JTable table, DefaultTableModel model, Class<U> datatype){
		int row = table.getSelectedRow();
		
		if (model.getRowCount() > 0)
			for (int i = model.getRowCount() - 1; i >= 0; i--)
				model.removeRow(i);		
		
		return row;
	}
	
	public void dataUpdated(JTable table, DefaultTableModel model, int row){
		model.fireTableDataChanged();
		
		try{
			table.setRowSelectionInterval(row, row);
		} catch (IllegalArgumentException e ){
			
		}		
	}	
	public JButton getBtnRunRender() {
		return btnRunRender;
	}
	public JLabel getLblTotal() {
		return lblTotal;
	}
}