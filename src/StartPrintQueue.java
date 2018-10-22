import net.jini.core.lease.Lease;
import net.jini.space.JavaSpace;

public class StartPrintQueue{
    private static final long ONESECOND = 1000;  // one thousand milliseconds

    public static void main(String args[]){

        JavaSpace space = SpaceUtils.getSpace();

        if (space == null){
            System.err.println("Failed to find the javaspace - NATHAN EDIT");
            System.exit(1);
        }

        QueueStatus template = new QueueStatus();
        try {
            QueueStatus returnedObject = (QueueStatus)space.readIfExists(template,null, ONESECOND);
            if (returnedObject == null) {
                // there is no object in the space, so create one
                try {
                    QueueStatus qs = new QueueStatus(0);
                    space.write(qs, null, Lease.FOREVER);
                    System.out.println("QueueStatus object added to space");
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // there is already an object available, so don't create one
                System.out.println("QueueStatus object is already in the space");
                System.exit(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
