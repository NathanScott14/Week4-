import net.jini.space.*;
import net.jini.core.lease.*;
import java.awt.*;
import javax.swing.*;

public class PrintJobAdder extends JFrame {

	private JavaSpace space;

	private JTextField jobNameIn, jobNumberOut;


	public PrintJobAdder() {
		space = SpaceUtils.getSpace();
		if (space == null){
			System.err.println("Failed to find the javaspace");
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
	}


	private void addJob(java.awt.event.ActionEvent evt){
		try {
			QueueStatus qsTemplate = new QueueStatus();
			QueueStatus qStatus = (QueueStatus)space.take(qsTemplate,null,Long.MAX_VALUE);

			int jobNumber = qStatus.nextJob;
			String jobName = jobNameIn.getText();
			QueueItem newJob = new QueueItem(jobNumber, jobName);
			space.write( newJob, null, Lease.FOREVER);
			jobNumberOut.setText(""+jobNumber);

			qStatus.addJob();

			space.write( qStatus, null, Lease.FOREVER);
		}  catch ( Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new PrintJobAdder().setVisible(true);
	}
}
