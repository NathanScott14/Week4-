import net.jini.core.entry.*;

public class NamedPrinterQuqueItem implements Entry
{
    // Variables.
    public Integer jobNumber;
    public String filename;
    public String printerName;

    // No arg constructor
    public NamedPrinterQuqueItem()
    {

    }

    // Arg constructor
    public NamedPrinterQuqueItem(int job, String fn, String pn)
    {
        jobNumber = job;
        filename = fn;
        printerName = pn;
    }
}
