package pt.fcul.cm2021.grupo9.shotop.comparator;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.Set;

import pt.fcul.cm2021.grupo9.shotop.entidades.Spot;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ComparatorFragment{
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");

    public int compareTwoSpots(Spot ogPhoto, Spot newPhoto){

        int result = 0;


        return result + compareDateTime(ogPhoto,newPhoto)
                + compareCaracteristicas(ogPhoto,newPhoto)
                + compareMetadados(ogPhoto,newPhoto);
    }


    private int compareDateTime(Spot ogPhoto, Spot newPhoto){ //20

        LocalDateTime ogPhotoDateTime = LocalDateTime.parse(ogPhoto.getDateTime(),formatter);
        LocalDateTime newPhotoDateTime = LocalDateTime.parse(newPhoto.getDateTime(),formatter);
        int result = 0;

        if( newPhotoDateTime.getMonthValue() == ogPhotoDateTime.getMonthValue()){ //month, for seasons,etc
            result =+ 5;
        }

        if(newPhotoDateTime.getHour() == ogPhotoDateTime.getHour() ) { // mesma hora
            result += 15;
        }else if(newPhotoDateTime.isAfter(ogPhotoDateTime.minusHours(1))
                && newPhotoDateTime.isBefore(ogPhotoDateTime.plusHours(1))){ //1 hora de diferenca
            result += 14;
        } else{
            result  += 10;
        }

        return result;
    }

    private int compareCaracteristicas(Spot ogPhoto, Spot newPhoto) { //30
        ArrayList<String> ogPhotoCaracteristicas  = (ArrayList<String>) ogPhoto.getCaracteristicas();
        ArrayList<String> newPhotoCaracteristicas  = (ArrayList<String>) newPhoto.getCaracteristicas();

        Set<String> intersect = new HashSet<String>(ogPhotoCaracteristicas);
        intersect.retainAll(newPhotoCaracteristicas);
        return intersect.size() * 30 /ogPhotoCaracteristicas.size(); //assumindo ogPhoto.caracteristicas.size !=0
    }

    private int compareMetadados(Spot ogPhoto, Spot newPhoto){ //50
        int result = 0;


        if (ogPhoto.getApertureValue().matches("null") && newPhoto.getApertureValue().matches("null")){
            result +=1;
        }else if(!ogPhoto.getApertureValue().matches("null") && !newPhoto.getApertureValue().matches("null")){
            String[] ogAp = ogPhoto.getApertureValue().split("/");
            String[] newAp = newPhoto.getApertureValue().split("/");
            double ogApp = Double.parseDouble(ogAp[1].replace(',','.'));
            double newApp = Double.parseDouble(newAp[1].replace(',','.'));

            if(ogApp == newApp){
                result +=1;
            }
            //range...

        }

        if (ogPhoto.getBrightnessValue().matches("null") && newPhoto.getBrightnessValue().matches("null")){
            result +=1;
        }else if(!ogPhoto.getBrightnessValue().matches("null") && !newPhoto.getBrightnessValue().matches("null")){
            double ogBv = Double.parseDouble(ogPhoto.getBrightnessValue().replace(',','.'));
            double newBv = Double.parseDouble(ogPhoto.getBrightnessValue().replace(',','.'));

            if(ogBv == newBv){
                result +=1;
            }
            //range...

        }

        if(newPhoto.getContrast().matches(ogPhoto.getContrast())){
            result +=1;
        } //ver range ditsto

        if(newPhoto.getDigitalZoomRatio().matches(ogPhoto.getDigitalZoomRatio())){
            result +=1;
        } //ver range ditsto

        if (ogPhoto.getExposureBiasValue().matches("null") && newPhoto.getExposureBiasValue().matches("null")){
            result +=1;
        }else if(!ogPhoto.getExposureBiasValue().matches("null") && !newPhoto.getExposureBiasValue().matches("null")){
            String[] ogEx = ogPhoto.getExposureBiasValue().split(" ");
            String[] newEx = newPhoto.getExposureBiasValue().split(" ");
            int ogExx= Integer.parseInt(ogEx[0]);
            int newExx= Integer.parseInt(newEx[0]);

            if(ogExx == newExx ){
                result +=1;
            }
            //range...

        }

        if (ogPhoto.getExposureTime().matches("null") && newPhoto.getExposureTime().matches("null")){ //acho q nunca acontecerá mas..
            result +=1;
        }else if(!ogPhoto.getExposureTime().matches("null") && !newPhoto.getExposureTime().matches("null")){
            String[] ogEpt = ogPhoto.getExposureTime().split(" ");
            String[] newEpt = newPhoto.getExposureTime().split(" ");
            double ogEptt = Double.parseDouble(ogEpt[0]);
            double newEptt = Double.parseDouble(newEpt[0]);

            if(ogEptt == newEptt){
                result +=1;
            }
            //range...

        }

        if (ogPhoto.getfNumber().matches("null") && newPhoto.getfNumber().matches("null")){
            result +=1;
        }else if(!ogPhoto.getfNumber().matches("null") && !newPhoto.getfNumber().matches("null")){
            String[] ogF = ogPhoto.getfNumber().split("/");
            String[] newF = newPhoto.getfNumber().split("/");
            double ogFf = Double.parseDouble(ogF[1].replace(',','.'));
            double newFf = Double.parseDouble(newF[1].replace(',','.'));

            if(ogFf == newFf ){
                result +=1;
            }
            //range...

        }

        if(newPhoto.getFlash().matches(ogPhoto.getFlash())){
            result +=1;
        } //ver range ditsto

        if (ogPhoto.getFocalLength().matches("null") && newPhoto.getFocalLength().matches("null")){
            result +=1;
        }else if(!ogPhoto.getFocalLength().matches("null") && !newPhoto.getFocalLength().matches("null")){
            String[] ogFl = ogPhoto.getExposureBiasValue().split(" ");
            String[] newFl = newPhoto.getExposureBiasValue().split(" ");
            double ogFll= Double.parseDouble(ogFl[0].replace(',','.'));
            double newFll= Double.parseDouble(newFl[0].replace(',','.'));

            if(ogFll == newFll ){
                result +=1;
            }
            //range...

        }

        if (ogPhoto.getiSOSpeedRatings().matches("null") && newPhoto.getiSOSpeedRatings().matches("null")){
            result +=1;
        }else if(!ogPhoto.getiSOSpeedRatings().matches("null") && !newPhoto.getiSOSpeedRatings().matches("null")){
            int ogIso= Integer.parseInt(ogPhoto.getiSOSpeedRatings());
            int newIso= Integer.parseInt(ogPhoto.getiSOSpeedRatings());

            if(ogIso == newIso ){
                result +=1;
            }
            //range...

        }

        //height,widht?
        //if(ogPhoto.getLoc().) ?

        if (ogPhoto.getMaxApertureValue().matches("null") && newPhoto.getMaxApertureValue().matches("null")){
            result +=1;
        }else if(!ogPhoto.getMaxApertureValue().matches("null") && !newPhoto.getMaxApertureValue().matches("null")){
            String[] ogMa = ogPhoto.getMaxApertureValue().split("/");
            String[] newMa = newPhoto.getMaxApertureValue().split("/");
            double ogMaa = Double.parseDouble(ogMa[1].replace(',','.'));
            double newMaa = Double.parseDouble(newMa[1].replace(',','.'));

            if(ogMaa == newMaa){
                result +=1;
            }
            //range...

        }

        if(newPhoto.getOrientation().matches(ogPhoto.getOrientation())){
            result +=1;
        } //ver range ditsto

        if(newPhoto.getSaturation().matches(ogPhoto.getSaturation())){
            result +=1;
        } //ver range ditsto

        if(newPhoto.getSharpness().matches(ogPhoto.getSharpness())){
            result +=1;
        } //ver range ditsto

        if (ogPhoto.getShutterSpeedValue().matches("null") && newPhoto.getShutterSpeedValue().matches("null")){
            result +=1;
        }else if(!ogPhoto.getShutterSpeedValue().matches("null") && !newPhoto.getShutterSpeedValue().matches("null")){
            String[] ogS = ogPhoto.getfNumber().replace("/"," ").split(" ");
            String[] newS = newPhoto.getfNumber().replace("/"," ").split(" ");
            int ogSS = Integer.parseInt(ogS[1]);
            int newSS = Integer.parseInt(newS[1]);

            if(ogSS == newSS ){
                result +=1;
            }
            //range...

        }

        if(newPhoto.getWhiteBalanceMode().matches(ogPhoto.getWhiteBalanceMode())){
            result +=1;
        } //ver range ditsto



        return result;
    }
}
