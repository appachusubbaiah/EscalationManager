package com.ocwen.escalation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ocwen.escalation.repositories.ErmAgent;
import com.ocwen.escalation.repositories.ErmAgentRepository;
import com.ocwen.escalation.service.DefaultHelloService;
import com.ocwen.escalation.service.HelloService;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

@SpringBootApplication(scanBasePackages={
		"com.ocwen.escalation.repositories"})
public class ERMSpringBootStandaloneApplication implements CommandLineRunner, ActionListener ,ItemListener 
{

	@Autowired
	//@Qualifier("JdbcErmAgentRepository")
	private ErmAgentRepository agentRepos;
	JFrame f;
	JMenuBar menuBar;
	JMenu menuAgent,menuSup;
	JMenuItem itemMyTasks,itemExit,itemMmanageHC;
	List<ErmAgent> agentList;
	JScrollPane scrollPanel;
	JTable table;
	EditableTableModel model;
	List<Integer> changedRows = new ArrayList<Integer>();
	String oldValue;
	JComboBox<String> cboBox;
	JComboBox<String> cboBoxSups;
	public static void main(String[] args) {
		SpringApplication.run(ERMSpringBootStandaloneApplication.class, args);
	}
	@Bean
	public HelloService getHelloService(){
		return  new DefaultHelloService();
	}

	@Override
	public void run(String... args) throws Exception {
		/*getHelloService().hello();*/
		      System.setProperty("java.awt.headless", "false");
		      System.out.println(java.awt.GraphicsEnvironment.isHeadless());
		      //System.out.println(System.getProperty("user.name"));
		//System.out.println(agentRepos.count());
		      //String ntid=agentRepos.getNameById(System.getProperty("user.name"));
		      agentList=agentRepos.find(System.getProperty("user.name"));
		      //if(ntid != null)
		      if(agentList!=null){
		    	  //System.out.println("Welcome " + ntid);
		    	  System.out.println("Welcome " + agentList.get(0).getAgentName());
		    	  System.out.println("Role " + agentList.get(0).geteRole());
		      }
		      else
		    	  System.out.println(System.getProperty("user.name") + " not found");
		     
		f=new JFrame();//creating instance of JFrame  
        
		/*JButton b=new JButton("click");//creating instance of JButton  
		b.setBounds(130,100,100, 40);//x axis, y axis, width, height  
		          
		f.add(b);//adding button in JFrame  */
		          
		f.setSize(1000,800);//400 width and 500 height
		 f.setVisible(true);
	     f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      f.setAlwaysOnTop(true);
	      f.setResizable(false);
	      //f.setExtendedState(f.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	      menuBar=new JMenuBar();  
	      menuAgent=new JMenu("Agent");
	      menuSup=new JMenu("Supervisor");
	      menuBar.add(menuAgent);
	      itemMyTasks=new JMenuItem("My Tasks");
	      itemExit=new JMenuItem("Exit");
	      menuAgent.add(itemMyTasks);
	      menuAgent.addSeparator();
	      menuAgent.add(itemExit);
	      menuBar.add(menuSup);
	      itemMmanageHC=new JMenuItem("Manage Head Count");
	      menuSup.add(itemMmanageHC);
	      f.setJMenuBar(menuBar);
	      f.revalidate();
	      f.repaint();
	    
	      
	      itemExit.addActionListener(this); 
	      itemMmanageHC.addActionListener(this);
	      itemMyTasks.addActionListener(this);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("New")){
				model.addRow(new ErmAgent(" ", "", "", ""));
				agentRepos.save(new ErmAgent(" ", "", "", ""));
		}
		else if (e.getActionCommand().equals("My Tasks")){
			f.getContentPane().removeAll();
    	f.revalidate();
    	 f.repaint();
		}
		else if (e.getActionCommand().equals("Manage Head Count"))
		{
			changedRows.clear();
			oldValue=null;
	    	f.getContentPane().removeAll();
	    	f.revalidate();
	    	 f.repaint();
	    	String[] columnTitles = { "NtId", "Agent Name", "Supervisor", "Role"};
	    	List<String> supervisorList=agentRepos.getSupervisorList();
	    	String[] supArray = new String[supervisorList.size()];
	    	supArray = supervisorList.toArray(supArray);
	    	agentList=null;
	    	agentList=agentRepos.findAll();
	    	model = new EditableTableModel(agentList);
	    	 table = new JTable(model);
	    	 DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)table.getDefaultRenderer(Object.class);
	         renderer.setHorizontalAlignment(SwingConstants.CENTER );
	         String [] roles={"Agent","Supervisor"};
	         cboBox = new JComboBox<String>(roles);
	         table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(cboBox));
	         cboBoxSups = new JComboBox<String>(supArray);
	         table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(cboBoxSups));
	         cboBox.addItemListener(this);
	         //table.addCell
	         Action action = new AbstractAction()
	         {
	             /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e)
	             {
	                 TableCellListener tcl = (TableCellListener)e.getSource();
	                 System.out.println("Row   : " + tcl.getRow());
	                 System.out.println("Column: " + tcl.getColumn());
	                 System.out.println("Old   : " + tcl.getOldValue());
	                 System.out.println("New   : " + tcl.getNewValue());
	                 System.out.println("NtId   : " + model.getValueAt(tcl.getRow(), 0).toString());
	                 if(tcl.getColumn()==0)
	                	 oldValue=(String) tcl.getOldValue();
	                 else
	                	 oldValue=(oldValue==null)?model.getValueAt(tcl.getRow(), 0).toString():oldValue;
	                 changedRows.add(tcl.getRow());
	             }
	         };

	         TableCellListener tcl = new TableCellListener(table, action);
	         
	       scrollPanel=new JScrollPane(table);
	       JToolBar toolBar=new JToolBar();
	       String buttonLabels[]={"New","Save","Delete"};
	       String iconFiles[]={"src\\main\\resources\\new.gif","src\\main\\resources\\save.gif","src\\main\\resources\\delete.gif"};
	       //String iconFiles[]={"new.gif","save.gif","delete.gif"};
	       JButton[] buttons= new JButton[buttonLabels.length];
	       ImageIcon[] icons=new ImageIcon[iconFiles.length];
	       for(int i=0;i<buttonLabels.length;i++)
	    	{
	    	   icons[i]=new ImageIcon(iconFiles[i]);
	    	   buttons[i]=new JButton(icons[i]);
	    	   buttons[i].setToolTipText(buttonLabels[i]);
	    	   buttons[i].addActionListener(this);
	    	   toolBar.add(buttons[i]); 
	    	   buttons[i].setActionCommand(buttonLabels[i]);
	    	}
	       f.getContentPane().setLayout(new BorderLayout());
	        f.getContentPane().add(scrollPanel,BorderLayout.CENTER);
	        f.getContentPane().add(toolBar,BorderLayout.NORTH);
	       f.revalidate();
	       f.repaint();
	    } 
		else if(e.getActionCommand().equals("Delete")){
			String ntidDel=model.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString();
			agentRepos.delete(ntidDel);
			model.delRow(table.getSelectedRow());
		}
		else if(e.getActionCommand().equals("Save")){
			if (null!=changedRows){
				changedRows.stream().forEach(r -> {agentRepos.update(new ErmAgent(model.getValueAt(r, 0).toString(),
               		 model.getValueAt(r, 1).toString(),model.getValueAt(r, 2).toString(),
               		 model.getValueAt(r, 3).toString()),oldValue);});
			}
		}
		else
			System.exit(0);
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == cboBox) {
			if (e.getItem().equals("Supervisor")){
			model.setValueAt(model.getValueAt(table.getSelectedRow(), 1), table.getSelectedRow(), 0);
			this.cboBoxSups.addItem(model.getValueAt(table.getSelectedRow(), 1).toString());
			this.cboBoxSups.setSelectedIndex(cboBoxSups.getItemCount()-1);
			System.out.println("ItemCount :" + cboBoxSups.getItemCount());
			System.out.println(e.getItem() + " " + e.getStateChange() );
			}
		}
		
	}
}

 class EditableTableModel extends AbstractTableModel
{
    
	private static final long serialVersionUID = 1L;

	private final List<ErmAgent> employeeList;
     
    private final String[] columnNames = new String[] {
            "NtId", "Name", "Supervisor", "Role"
    };
    /*private final Class[] columnClass = new Class[] {
        Integer.class, String.class, Double.class, Boolean.class
    };*/
 
    private final Class[] columnClass = new Class[] {
    		String.class, String.class, String.class, String.class
        };
    public EditableTableModel(List<ErmAgent> employeeList)
    {
        this.employeeList = employeeList;
    }
     
    @Override
    public String getColumnName(int column)
    {
        return columnNames[column];
    }
 
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnClass[columnIndex];
    }
 
    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }
 
    @Override
    public int getRowCount()
    {
        return employeeList.size();
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
    	ErmAgent row = employeeList.get(rowIndex);
        if(0 == columnIndex) {
            return row.getNtId();
        }
        else if(1 == columnIndex) {
            return row.getAgentName();
        }
        else if(2 == columnIndex) {
            return row.getSupervisor();
        }
        else if(3 == columnIndex) {
            return row.geteRole();
        }
        return null;
    }
 
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }
 
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
    	ErmAgent row = employeeList.get(rowIndex);
        if(0 == columnIndex) {
            row.setNtId((String) aValue);
        }
        else if(1 == columnIndex) {
            row.setAgentName((String) aValue);
        }
        else if(2 == columnIndex) {
            row.setSupervisor((String) aValue);
        }
        else if(3 == columnIndex) {
            row.seteRole((String) aValue);
        }
    }
    public void addRow(ErmAgent row) {
        
    	employeeList.add(row);
        this.fireTableDataChanged();
        
        }
    
    public void delRow(int row) {
        
    	employeeList.remove(row);       
        this.fireTableDataChanged();
         
        }
}
 
 class TableCellListener implements PropertyChangeListener, Runnable
 {
 	private JTable table;
 	private Action action;

 	private int row;
 	private int column;
 	private Object oldValue;
 	private Object newValue;

 	/**
 	 *  Create a TableCellListener.
 	 *
 	 *  @param table   the table to be monitored for data changes
 	 *  @param action  the Action to invoke when cell data is changed
 	 */
 	public TableCellListener(JTable table, Action action)
 	{
 		this.table = table;
 		this.action = action;
 		this.table.addPropertyChangeListener( this );
 	}

 	/**
 	 *  Create a TableCellListener with a copy of all the data relevant to
 	 *  the change of data for a given cell.
 	 *
 	 *  @param row  the row of the changed cell
 	 *  @param column  the column of the changed cell
 	 *  @param oldValue  the old data of the changed cell
 	 *  @param newValue  the new data of the changed cell
 	 */
 	private TableCellListener(JTable table, int row, int column, Object oldValue, Object newValue)
 	{
 		this.table = table;
 		this.row = row;
 		this.column = column;
 		this.oldValue = oldValue;
 		this.newValue = newValue;
 	}

 	/**
 	 *  Get the column that was last edited
 	 *
 	 *  @return the column that was edited
 	 */
 	public int getColumn()
 	{
 		return column;
 	}

 	/**
 	 *  Get the new value in the cell
 	 *
 	 *  @return the new value in the cell
 	 */
 	public Object getNewValue()
 	{
 		return newValue;
 	}

 	/**
 	 *  Get the old value of the cell
 	 *
 	 *  @return the old value of the cell
 	 */
 	public Object getOldValue()
 	{
 		return oldValue;
 	}

 	/**
 	 *  Get the row that was last edited
 	 *
 	 *  @return the row that was edited
 	 */
 	public int getRow()
 	{
 		return row;
 	}

 	/**
 	 *  Get the table of the cell that was changed
 	 *
 	 *  @return the table of the cell that was changed
 	 */
 	public JTable getTable()
 	{
 		return table;
 	}
 //
 //  Implement the PropertyChangeListener interface
 //
 	@Override
 	public void propertyChange(PropertyChangeEvent e)
 	{
 		//  A cell has started/stopped editing

 		if ("tableCellEditor".equals(e.getPropertyName()))
 		{
 			if (table.isEditing())
 				processEditingStarted();
 			else
 				processEditingStopped();
 		}
 	}

 	/*
 	 *  Save information of the cell about to be edited
 	 */
 	private void processEditingStarted()
 	{
 		//  The invokeLater is necessary because the editing row and editing
 		//  column of the table have not been set when the "tableCellEditor"
 		//  PropertyChangeEvent is fired.
 		//  This results in the "run" method being invoked

 		SwingUtilities.invokeLater( this );
 	}
 	/*
 	 *  See above.
 	 */
 	@Override
 	public void run()
 	{
 		row = table.convertRowIndexToModel( table.getEditingRow() );
 		column = table.convertColumnIndexToModel( table.getEditingColumn() );
 		oldValue = table.getModel().getValueAt(row, column);
 		newValue = null;
 	}

 	/*
 	 *	Update the Cell history when necessary
 	 */
 	private void processEditingStopped()
 	{
 		newValue = table.getModel().getValueAt(row, column);

 		//  The data has changed, invoke the supplied Action

 		if (! newValue.equals(oldValue))
 		{
 			//  Make a copy of the data in case another cell starts editing
 			//  while processing this change

 			TableCellListener tcl = new TableCellListener(
 				getTable(), getRow(), getColumn(), getOldValue(), getNewValue());

 			ActionEvent event = new ActionEvent(
 				tcl,
 				ActionEvent.ACTION_PERFORMED,
 				"");
 			action.actionPerformed(event);
 		}
 	}
 }
 
 
    
 