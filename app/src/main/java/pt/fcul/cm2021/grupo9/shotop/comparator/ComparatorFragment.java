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

    public double compareTwoSpots(Spot ogPhoto, Spot newPhoto){

        double result = 0;


        return result + compareDateTime(ogPhoto,newPhoto)
                + compareCaracteristicas(ogPhoto,newPhoto)
                + compareMetadados(ogPhoto,newPhoto);
    }


    private double compareDateTime(Spot ogPhoto, Spot newPhoto){ //20

        LocalDateTime ogPhotoDateTime = LocalDateTime.parse(ogPhoto.getDateTime(),formatter);
        LocalDateTime newPhotoDateTime = LocalDateTime.parse(newPhoto.getDateTime(),formatter);
        double result = 0;

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

    private double compareCaracteristicas(Spot ogPhoto, Spot newPhoto) { //30
        ArrayList<String> ogPhotoCaracteristicas  = (ArrayList<String>) ogPhoto.getCaracteristicas();
        ArrayList<String> newPhotoCaracteristicas  = (ArrayList<String>) newPhoto.getCaracteristicas();

        Set<String> intersect = new HashSet<String>(ogPhotoCaracteristicas);
        intersect.retainAll(newPhotoCaracteristicas);
        return intersect.size() * 30.0 /ogPhotoCaracteristicas.size(); //assumindo ogPhoto.caracteristicas.size !=0
    }

    private double compareMetadados(Spot ogPhoto, Spot newPhoto){ //50
        double maxScore = 50.0;
        double result = 0;
        double onePerfectScore = maxScore/16.0;
        double oneHalfScore = 0.5 * maxScore/16.0;


        if (ogPhoto.getApertureValue().matches("null") && newPhoto.getApertureValue().matches("null")){
            result += onePerfectScore;
        }else if(!ogPhoto.getApertureValue().matches("null") && !newPhoto.getApertureValue().matches("null")){
            String[] ogAp = ogPhoto.getApertureValue().split("/");
            String[] newAp = newPhoto.getApertureValue().split("/");
            double ogApp = Double.parseDouble(ogAp[1].replace(',','.'));
            double newApp = Double.parseDouble(newAp[1].replace(',','.'));

            if(ogApp == newApp){
                result += onePerfectScore;
            }else if(newApp<=ogApp+5 && newApp>=ogApp-5){
                result += oneHalfScore;
            }
        }

        if (ogPhoto.getBrightnessValue().matches("null") && newPhoto.getBrightnessValue().matches("null")){
            result +=onePerfectScore;
        }else if(!ogPhoto.getBrightnessValue().matches("null") && !newPhoto.getBrightnessValue().matches("null")){
            double ogBv = Double.parseDouble(ogPhoto.getBrightnessValue().replace(',','.'));
            double newBv = Double.parseDouble(ogPhoto.getBrightnessValue().replace(',','.'));

            if(ogBv == newBv){
                result +=onePerfectScore;
            }else if(newBv<=ogBv+0.3 && newBv>=ogBv-0.3){
                result += oneHalfScore;
            }
        }

        if(newPhoto.getContrast().matches(ogPhoto.getContrast())){
            result +=onePerfectScore;
        }

        if(newPhoto.getDigitalZoomRatio().matches(ogPhoto.getDigitalZoomRatio())){
            result +=onePerfectScore;
        }

        if (ogPhoto.getExposureBiasValue().matches("null") && newPhoto.getExposureBiasValue().matches("null")){
            result +=onePerfectScore;
        }else if(!ogPhoto.getExposureBiasValue().matches("null") && !newPhoto.getExposureBiasValue().matches("null")){
            String[] ogEx = ogPhoto.getExposureBiasValue().split(" ");
            String[] newEx = newPhoto.getExposureBiasValue().split(" ");
            double ogExx= Double.parseDouble(ogEx[0]);
            double newExx= Double.parseDouble(newEx[0]);

            if(ogExx == newExx ){
                result +=onePerfectScore;
            }else if(newExx<=ogExx+3 && newExx>=ogExx-3){
                result += oneHalfScore;
            }
        }

        if (ogPhoto.getExposureTime().matches("null") && newPhoto.getExposureTime().matches("null")){ //acho q nunca acontecerá mas..
            result +=onePerfectScore;
        }else if(!ogPhoto.getExposureTime().matches("null") && !newPhoto.getExposureTime().matches("null")){
            String[] ogEpt = ogPhoto.getExposureTime().split(" ");
            String[] newEpt = newPhoto.getExposureTime().split(" ");
            double ogEptt = Double.parseDouble(ogEpt[0]);
            double newEptt = Double.parseDouble(newEpt[0]);

            if(ogEptt == newEptt){
                result +=onePerfectScore;
            }else if(newEptt<=ogEptt+0.05 && newEptt>=ogEptt-0.05){
                result += oneHalfScore;
            }
        }

        if (ogPhoto.getfNumber().matches("null") && newPhoto.getfNumber().matches("null")){
            result +=onePerfectScore;
        }else if(!ogPhoto.getfNumber().matches("null") && !newPhoto.getfNumber().matches("null")){
            String[] ogF = ogPhoto.getfNumber().split("/");
            String[] newF = newPhoto.getfNumber().split("/");
            double ogFf = Double.parseDouble(ogF[1].replace(',','.'));
            double newFf = Double.parseDouble(newF[1].replace(',','.'));

            if(ogFf == newFf ){
                result +=onePerfectScore;
            }else if(newFf<=ogFf+5 && newFf>=ogFf-5){
                result += oneHalfScore;
            }
        }

        if(newPhoto.getFlash().matches(ogPhoto.getFlash())){
            result +=onePerfectScore;
        }

        if (ogPhoto.getFocalLength().matches("null") && newPhoto.getFocalLength().matches("null")){
            result +=onePerfectScore;
        }else if(!ogPhoto.getFocalLength().matches("null") && !newPhoto.getFocalLength().matches("null")){
            String[] ogFl = ogPhoto.getExposureBiasValue().split(" ");
            String[] newFl = newPhoto.getExposureBiasValue().split(" ");
            double ogFll= Double.parseDouble(ogFl[0].replace(',','.'));
            double newFll= Double.parseDouble(newFl[0].replace(',','.'));

            if(ogFll == newFll ){
                result +=onePerfectScore;
            }else if(newFll<=ogFll+6 && newFll>=ogFll-6){
                result += oneHalfScore;
            }
        }

        if (ogPhoto.getiSOSpeedRatings().matches("null") && newPhoto.getiSOSpeedRatings().matches("null")){
            result +=onePerfectScore;
        }else if(!ogPhoto.getiSOSpeedRatings().matches("null") && !newPhoto.getiSOSpeedRatings().matches("null")){
            int ogIso= Integer.parseInt(ogPhoto.getiSOSpeedRatings()); //tbm funcionaria int mas só para manter coerencia
            int newIso= Integer.parseInt(ogPhoto.getiSOSpeedRatings());

            if(ogIso == newIso ){
                result +=onePerfectScore;
            }else if(newIso<=ogIso+100 && newIso>=ogIso-100){
                result += oneHalfScore;
            }
        }

        if(newPhoto.getOrientation().matches(ogPhoto.getOrientation())){
            result +=onePerfectScore;
        }

        if(newPhoto.getSaturation().matches(ogPhoto.getSaturation())){
            result +=onePerfectScore;
        }

        if(newPhoto.getSharpness().matches(ogPhoto.getSharpness())){
            result +=onePerfectScore;
        }

        if (ogPhoto.getShutterSpeedValue().matches("null") && newPhoto.getShutterSpeedValue().matches("null")){
            result +=onePerfectScore;
        }else if(!ogPhoto.getShutterSpeedValue().matches("null") && !newPhoto.getShutterSpeedValue().matches("null")){
            String[] ogS = ogPhoto.getfNumber().replace("/"," ").split(" ");
            String[] newS = newPhoto.getfNumber().replace("/"," ").split(" ");
            int ogSs = Integer.parseInt(ogS[1]);
            int newSs = Integer.parseInt(newS[1]);

            if(ogSs == newSs ){
                result +=onePerfectScore;
            }else if(newSs<=ogSs+100 && newSs>=ogSs-100){
                result += oneHalfScore;
            }
        }

        if(newPhoto.getWhiteBalanceMode().matches(ogPhoto.getWhiteBalanceMode())){
            result +=onePerfectScore;
        }

        return result;
    }
}
