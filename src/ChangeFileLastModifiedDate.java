import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
 
public class ChangeFileLastModifiedDate {
 
    public static final String filepath = "C:\\Users\\jason.parkin\\Desktop\\WEB CATEGORY LOOKUP CODE.txt";
 
    public static void main(String[] args) {
        try {
 
            File file = new File(filepath);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
 
            // print the original Last Modified date
            System.out.println("Original Last Modified Date : "
                    + dateFormat.format(file.lastModified()));
 
            // set this date
            String newLastModifiedString = "08/02/2019";
 
            // we have to convert the above date to milliseconds...
            Date newLastModifiedDate = dateFormat.parse(newLastModifiedString);
            file.setLastModified(newLastModifiedDate.getTime());
 
            // print the new Last Modified date
            System.out.println("Lastest Last Modified Date : "
                    + dateFormat.format(file.lastModified()));
 
        } catch (ParseException e) {
 
            e.printStackTrace();
 
        }
 
    }
 
}