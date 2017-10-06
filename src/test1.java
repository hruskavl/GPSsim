

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class test1 {
    public static void main(String[] args) {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.txt");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            //System.out.println(prop.getProperty("speed"));

            int a = 1;
            int b = 1;
            int c = 1;

            for (int x =0; x<6; x++ ) {



                Double d = Double.parseDouble(prop.getProperty("speed" + a));   // nacte radku speed a prevede na double
                System.out.println(d);

                System.out.println(prop.getProperty("angle" + b));
                System.out.println(prop.getProperty("hrs" + c));

                a = a + 1;
                b = b + 1;
                c = c + 1;
            }




        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}