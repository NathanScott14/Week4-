import net.jini.core.entry.*;

public class QueueItem implements Entry{
    // Variables
    public Integer jobNumber;
    public String filename;

    // No arg contructor
    public QueueItem (){
    }

    // Arg constructor
    public QueueItem (int job, String fn){
        jobNumber = job;
        filename = fn;
    }
}
