import net.jini.space.JavaSpace;
import javax.swing.*;
import java.awt.*;


public class PrintJobPrinter extends JFrame {

    private static final long TWO_SECONDS = 2 * 1000;  // two thousand milliseconds

    private JavaSpace space;
    private JTextArea jobList;
    private String printerName;

    public PrintJobPrinter() {
        space = SpaceUtils.getSpace();
        if (space == null){
            System.err.println("Failed to find the javaspace");
            System.exit(1);
        }

        initComponents ();
        pack ();
        setVisible(true);
        processPrintJobs();
    }

    private void initComponents () {

        printerName = JOptionPane.showInputDialog("Enter Name of Printer"); // Added to prompt the user to enter printer name.

        setTitle ("Printer Name: " + printerName);
        addWindowListener (new java.awt.event.WindowAdapter () {
            public void windowClosing (java.awt.event.WindowEvent evt) {
                System.exit(0);
            }
        }   );

        Container cp = getContentPane();
        cp.setLayout (new BorderLayout ());

        JPanel jPanel1 = new JPanel();
        jPanel1.setLayout(new FlowLayout());

        jobList = new JTextArea(30,30);
        jPanel1.add(jobList);

        cp.add(jPanel1,"Center");
    }

    public void processPrintJobs(){
        while(true){
            try {
                //QueueItem qiTemplate = new QueueItem();
                //QueueItem nextJob = (QueueItem)space.take(qiTemplate,null, TWO_SECONDS);

                NamedPrinterQuqueItem qiTemplate = new NamedPrinterQuqueItem();
                qiTemplate.printerName = printerName;
                NamedPrinterQuqueItem nextJob = (NamedPrinterQuqueItem)space.take(qiTemplate, null, TWO_SECONDS);
                if (nextJob == null) {
                    // no print job was found, so sleep for a couple of seconds and try again
                    Thread.sleep(TWO_SECONDS);
                } else {
                    // we have a job to process
                    int nextJobNumber = nextJob.jobNumber;
                    String nextJobName = nextJob.filename;
                    jobList.append("Job Number: " + nextJobNumber + " Filename: " + nextJobName + "\n");
                }
            }  catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {
        new PrintJobPrinter();
    }
}
