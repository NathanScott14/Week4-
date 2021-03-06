import net.jini.core.transaction.*;
import net.jini.core.transaction.server.*;
import net.jini.space.*;
import net.jini.core.lease.*;
import java.awt.*;
import javax.swing.*;

// Test

public class PrintJobAdder extends JFrame {

	private JavaSpace space;
	private TransactionManager mgr;

	private JTextField jobNameIn, jobNumberOut;
	private JTextField printerNameln;


	public PrintJobAdder() {
		space = SpaceUtils.getSpace();
		if (space == null){
			System.err.println("Failed to find the javaspace - VM TEST EDIT");
			System.exit(1);
		}
		
		mgr = SpaceUtils.getManager();
		if(mgr == null) {
		    System.err.println("Failed to find the transaction manager");
		    System.exit(1);
        }

		initComponents ();
		pack ();
	}

	private void initComponents () {
		setTitle ("Print Job Adder");
		addWindowListener (new java.awt.event.WindowAdapter () {
			public void windowClosing (java.awt.event.WindowEvent evt) {
				System.exit (0);
			}
		}   );

		Container cp = getContentPane();
		cp.setLayout (new BorderLayout ());

		JPanel jPanel1 = new JPanel();
		jPanel1.setLayout (new FlowLayout ());

		JLabel jobLabel = new JLabel();
		jobLabel.setText ("Name of file to print ");
		jPanel1.add (jobLabel);

		jobNameIn = new JTextField (12);
		jobNameIn.setText ("");
		jPanel1.add (jobNameIn);

		JLabel jobNumberLabel = new JLabel();
		jobNumberLabel.setText ("Print job number ");
		jPanel1.add (jobNumberLabel);

		jobNumberOut = new JTextField (6);
		jobNumberOut.setText ("");
		jobNumberOut.setEditable(false);
		jPanel1.add (jobNumberOut);


		JPanel jPanel2 = new JPanel();
		jPanel2.setLayout (new FlowLayout ());

		JPanel jPanel3 = new JPanel();
		jPanel3.setLayout( new FlowLayout());

		JLabel printerNameLabel = new JLabel();
		printerNameLabel.setText("Name of Printer ");
		jPanel3.add(printerNameLabel);

		printerNameln = new JTextField(12);
		printerNameln.setText("");
		jPanel3.add(printerNameln);


		JButton addJobButton = new JButton();
		addJobButton.setText("Add Print Job");
		addJobButton.addActionListener (new java.awt.event.ActionListener () {
			public void actionPerformed (java.awt.event.ActionEvent evt) {
				addJob (evt);
			}
		}  );

		jPanel2.add(addJobButton);

        cp.add (jPanel1, "North");
        cp.add (jPanel2, "South");
        cp.add(jPanel3, "Center");
	}

	private void addJob(java.awt.event.ActionEvent evt)
	{
        Transaction.Created trc = null;
        
        try
        {
            trc = TransactionFactory.create(mgr, 3000);
        }
        catch (Exception e)
        {
            System.out.println("Could not create transaction " + e);;
        }
        
        Transaction txn = trc.transaction;
        
		try
		{
            QueueStatus qsTemplate = new QueueStatus();
            
            try
            {
                QueueStatus qStatus = (QueueStatus) space.read(qsTemplate, txn, Long.MAX_VALUE);
    
                int jobNumber = qStatus.nextJob;
                String jobName = jobNameIn.getText();
                String printerName = printerNameln.getText();
    
                NamedPrinterQuqueItem newJob = new NamedPrinterQuqueItem(jobNumber, jobName, printerName);
                //QueueItem newJob = new QueueItem(jobNumber, jobName); //Part of old system.
                space.write(newJob, txn, Lease.FOREVER);
                jobNumberOut.setText("" + jobNumber);
    
                qStatus.addJob();
    
                space.write(qStatus, txn, Lease.FOREVER);
            }
            catch ( Exception e)
            {
                System.out.println("Failed to read or write to space " + e);
                txn.abort();
    
                System.exit(1);
            }
            
            txn.commit();
		}
		catch ( Exception e)
		{
            System.out.print("Transaction failed " + e);
			e.printStackTrace();
		}
    }
	
	public static void main(String[] args) {
		new PrintJobAdder().setVisible(true);
	}
}
