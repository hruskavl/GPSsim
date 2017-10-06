import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class test
{

    public static ArrayList<Double> vypocet(double lat, double lon, double speed, double timeInHrs, double timeInMin, double timeInSec, double bearing, double klesani, double angle) throws IOException {


        double distance;    // m
        double dist;

        double R = 6371000;    // Earth radius v metrech


        double speedPerMinute;  // rychlost v metrech za minutu
        double speedPerSecond;  // rychlost v metrech za sekundu
        double time;   // sec

        double vzdalenostPoKlesani;

        String latStr;
        String lonStr;


        time = timeInSec + (timeInMin * 60) + (timeInHrs * 3600);
        speedPerMinute = speed * 1000 / 60;
        speedPerSecond = speedPerMinute / 60;
        distance = speedPerSecond * time;    // ujeta vzdalenost v m/s

        // System.out.println(time + " cas v sec");
        // System.out.println(speedPerMinute + " rychlost m/min");
        // System.out.println(speedPerSecond + " rychlost m/s");
        // System.out.println(distance + " vzdalenost v metrech");



        double distancePerMinute = distance / time * 60; // vzdalenost za minutu v metrech
        dist = distancePerMinute / R;



        int pocetSkoku = (int) (distance / distancePerMinute);  // celkovy pocet reportovanych souradnic na trase



        // System.out.println(distancePerMinute + " m/min");
        // System.out.println(pocetSkoku + " skoku");

        lat = Math.toRadians(lat);
        lon = Math.toRadians(lon);

        double anglePart;
        double angleTrue;

        anglePart = angle / pocetSkoku;
        angleTrue = bearing + anglePart;

        bearing = Math.toRadians(bearing);


        FileWriter fileWriter = new FileWriter("test.csv",true);    // zacne zapisovat na konec existujiciho souboru





        if (klesani == 0)
        {


            for (int a = 0; a < pocetSkoku; a++)
            {

                if (angle != 0)
                {



                    angleTrue = Math.toRadians(angleTrue);

                    lat = Math.asin(Math.sin(lat) * Math.cos(dist) + Math.cos(lat) * Math.sin(dist) * Math.cos(angleTrue));
                    lon = lon + Math.atan2(Math.sin(angleTrue) * Math.sin(dist) * Math.cos(lat), Math.cos(dist) - Math.sin(lat) * Math.sin(lat));

                    System.out.println(Math.toDegrees(lat) + ", " + Math.toDegrees(lon));

                    latStr = Double.toString(Math.toDegrees(lat));
                    lonStr = Double.toString(Math.toDegrees(lon));

                    fileWriter.write("\r\n" + latStr + " , " + lonStr);


                    angleTrue = Math.toDegrees(angleTrue);
                    //System.out.println(angleTrue);

                    angleTrue = angleTrue + anglePart;


                }

                else
                {

                    lat = Math.asin(Math.sin(lat) * Math.cos(dist) + Math.cos(lat) * Math.sin(dist) * Math.cos(bearing));
                    lon = lon + Math.atan2(Math.sin(bearing) * Math.sin(dist) * Math.cos(lat), Math.cos(dist) - Math.sin(lat) * Math.sin(lat));

                    System.out.println(Math.toDegrees(lat) + ", " + Math.toDegrees(lon));

                    latStr = Double.toString(Math.toDegrees(lat));
                    lonStr = Double.toString(Math.toDegrees(lon));

                    fileWriter.write("\r\n" + latStr + " , " + lonStr);

                }


            }

        }

        else    // kdyz klesa
        {
            vzdalenostPoKlesani = Math.sqrt(Math.pow(distance, 2) - Math.pow(klesani, 2)); // vypocet vzdalenosti pri klesani
            System.out.println(vzdalenostPoKlesani + " m");

            double absolutniRychlost = vzdalenostPoKlesani / time;  // rychlost vstazena k realne urazene vzdalenosti k povrchu zeme
            System.out.println(absolutniRychlost + " m/s");

            double distancePerMinuteBehemKlesani = vzdalenostPoKlesani / time * 60; // vzdalenost za minutu v metrech
            int pocetSkokuBehemKlesani = (int) (vzdalenostPoKlesani / distancePerMinuteBehemKlesani);
            System.out.println(pocetSkokuBehemKlesani + " skoku");

            latStr = Double.toString(Math.toDegrees(lat));
            lonStr = Double.toString(Math.toDegrees(lon));

            fileWriter.write("\r\n" + latStr + " , " + lonStr);


            for (int a = 0; a < pocetSkokuBehemKlesani; a++)
            {
                lat = Math.asin(Math.sin(lat) * Math.cos(dist) + Math.cos(lat) * Math.sin(dist) * Math.cos(bearing));
                lon = lon + Math.atan2(Math.sin(bearing) * Math.sin(dist) * Math.cos(lat), Math.cos(dist) - Math.sin(lat) * Math.sin(lat));


                // koncove souradnice bez klesani


                System.out.println(Math.toDegrees(lat) + ", " + Math.toDegrees(lon));

                latStr = Double.toString(Math.toDegrees(lat));
                lonStr = Double.toString(Math.toDegrees(lon));

                fileWriter.write("\r\n" + latStr + " , " + lonStr);

            }
        }

        fileWriter.close();

        lat = Math.toDegrees(lat);
        lon = Math.toDegrees(lon);


        ArrayList<Double> returnLatLon = new ArrayList<Double>();
        returnLatLon.add(lat);
        returnLatLon.add(lon);
        return returnLatLon;





    }



    public static void main (String [] args) throws IOException
    {

        ArrayList<Double> actualLatLon = new ArrayList<Double>();
        actualLatLon.add(48.8468611);
        actualLatLon.add(14.4754297);







        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.txt");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            //System.out.println(prop.getProperty("speed"));

            int a = 1;


            for (int x =0; x<5; x++ ) {



                Double rych = Double.parseDouble(prop.getProperty("rychlost" + a));   // nacte radku speed a prevede na double
               // System.out.println(d);
                Double hod = Double.parseDouble(prop.getProperty("hodin" + a));
                Double min = Double.parseDouble(prop.getProperty("minut" + a));
                Double sec = Double.parseDouble(prop.getProperty("vterin" + a));
                Double bea = Double.parseDouble(prop.getProperty("bearing" + a));
                Double kle = Double.parseDouble(prop.getProperty("klesani" + a));
                Double ang = Double.parseDouble(prop.getProperty("angle" + a));

                a = a + 1;


                actualLatLon = vypocet(actualLatLon.get(0),actualLatLon.get(1),rych, hod, min, sec, bea, kle, ang);


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






                //System.out.println(actualLatLon.get(0));
                // System.out.println(actualLatLon.get(1));














    }


}



