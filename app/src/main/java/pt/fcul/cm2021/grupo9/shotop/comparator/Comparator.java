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
public class Comparator {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");

    public double compareTwoSpots(Spot ogPhoto, Spot newPhoto){

        double result = 0;
        if(ogPhoto == null || newPhoto == null){
            return 0;
        }

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

    private double compareCaracteristicas(Spot ogPhoto, Spot newPhoto) { //50
        ArrayList<String> ogPhotoCaracteristicas  = (ArrayList<String>) ogPhoto.getCaracteristicas();
        ArrayList<String> newPhotoCaracteristicas  = (ArrayList<String>) newPhoto.getCaracteristicas();

        Set<String> intersect = new HashSet<String>(ogPhotoCaracteristicas);
        intersect.retainAll(newPhotoCaracteristicas);
        return intersect.size() * 50.0 /ogPhotoCaracteristicas.size(); //assumindo ogPhoto.caracteristicas.size !=0
    }

    private double compareMetadados(Spot ogPhoto, Spot newPhoto){ //30 /14 params
        double maxScore = 30.00;
        double result = 0;
        double onePerfectScore = maxScore/14.00;
        double oneHalfScore = 0.5 * maxScore/14.00;


        if (ogPhoto.getApertureValue() == null && newPhoto.getApertureValue()== null){
            result += onePerfectScore;
        }else if(ogPhoto.getApertureValue()!=null && newPhoto.getApertureValue()!=null){
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

        if (ogPhoto.getBrightnessValue()==null && newPhoto.getBrightnessValue()==null){
            result +=onePerfectScore;
        }else if(ogPhoto.getBrightnessValue()!=null && newPhoto.getBrightnessValue()!=null){
            double ogBv = Double.parseDouble(ogPhoto.getBrightnessValue().replace(',','.'));
            double newBv = Double.parseDouble(ogPhoto.getBrightnessValue().replace(',','.'));

            if(ogBv == newBv){
                result +=onePerfectScore;
            }else if(newBv<=ogBv+0.3 && newBv>=ogBv-0.3){
                result += oneHalfScore;
            }
        }

        if((ogPhoto.getContrast()== null && newPhoto.getContrast()== null )){
            result += onePerfectScore;
        }else if(ogPhoto.getContrast()!= null && newPhoto.getContrast()!= null &&
                (newPhoto.getContrast().compareTo(ogPhoto.getContrast())==0)) {
            result += onePerfectScore;
        }

        if((ogPhoto.getDigitalZoomRatio()== null && newPhoto.getDigitalZoomRatio()== null ) ){
            result +=onePerfectScore;
        }else if(ogPhoto.getDigitalZoomRatio()!= null && newPhoto.getDigitalZoomRatio()!= null &&
                (newPhoto.getDigitalZoomRatio().compareTo(ogPhoto.getDigitalZoomRatio()) ==0)) {
            result += onePerfectScore;
        }


        if (ogPhoto.getExposureBiasValue()== null && newPhoto.getExposureBiasValue()==null){
            result +=onePerfectScore;
        }else if(ogPhoto.getExposureBiasValue()!=null && newPhoto.getExposureBiasValue()!=null){
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

        if (ogPhoto.getExposureTime()==null && newPhoto.getExposureTime()==null){
            result +=onePerfectScore;
        }else if(ogPhoto.getExposureTime()!=null && newPhoto.getExposureTime()!=null){
            String[] ogEpt;
            String[] newEpt;
            if(ogPhoto.getExposureTime().contains("/")){
                ogEpt = ogPhoto.getExposureTime().split("/");
            }else{
                ogEpt = ogPhoto.getExposureTime().split(" ");
            }
            if(newPhoto.getExposureTime().contains("/")){
                newEpt = ogPhoto.getExposureTime().split("/");
            }else{
                newEpt = ogPhoto.getExposureTime().split(" ");
            }

            double ogEptt = Double.parseDouble(ogEpt[0]);
            double newEptt = Double.parseDouble(newEpt[0]);

            if(ogEptt == newEptt){
                result +=onePerfectScore;
            }else if(newEptt<=ogEptt+0.05 && newEptt>=ogEptt-0.05){
                result += oneHalfScore;
            }
        }

        if (ogPhoto.getfNumber()==null && newPhoto.getfNumber()==null){
            result +=onePerfectScore;
        }else if(ogPhoto.getfNumber()!=null && newPhoto.getfNumber()!=null){
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

        if((ogPhoto.getFlash()== null && newPhoto.getFlash()== null )){
            result += onePerfectScore;
        }else if(ogPhoto.getFlash()!= null && newPhoto.getFlash()!= null &&
                (newPhoto.getFlash().compareTo(ogPhoto.getFlash())==0)) {
            result += onePerfectScore;
        }


        if (ogPhoto.getFocalLength()==null && newPhoto.getFocalLength()==null){
            result +=onePerfectScore;
        }else if(ogPhoto.getFocalLength()!=null && newPhoto.getFocalLength()!=null){
            String[] ogFl = ogPhoto.getFocalLength().split(" ");
            String[] newFl = newPhoto.getFocalLength().split(" ");
            double ogFll= Double.parseDouble(ogFl[0].replace(',','.'));
            double newFll= Double.parseDouble(newFl[0].replace(',','.'));

            if(ogFll == newFll ){
                result +=onePerfectScore;
            }else if(newFll<=ogFll+6 && newFll>=ogFll-6){
                result += oneHalfScore;
            }
        }

        if (ogPhoto.getiSOSpeedRatings()==null && newPhoto.getiSOSpeedRatings()==null){
            result +=onePerfectScore;
        }else if(ogPhoto.getiSOSpeedRatings()!=null && newPhoto.getiSOSpeedRatings()!=null){
            int ogIso= Integer.parseInt(ogPhoto.getiSOSpeedRatings()); 
            int newIso= Integer.parseInt(ogPhoto.getiSOSpeedRatings());

            if(ogIso == newIso ){
                result +=onePerfectScore;
            }else if(newIso<=ogIso+100 && newIso>=ogIso-100){
                result += oneHalfScore;
            }
        }

        if((ogPhoto.getSaturation()== null && newPhoto.getSaturation()== null ) ){
            result +=onePerfectScore;
        }else if(ogPhoto.getSaturation()!= null && newPhoto.getSaturation()!= null &&
                (newPhoto.getSaturation().compareTo(ogPhoto.getSaturation()) == 0)) {
            result += onePerfectScore;
        }


        if((ogPhoto.getSharpness()== null && newPhoto.getSharpness()== null ) ){
            result +=onePerfectScore;
        }else if(ogPhoto.getSharpness()!= null && newPhoto.getSharpness()!= null &&
                (newPhoto.getSharpness().compareTo(ogPhoto.getSharpness()) == 0)) {
            result += onePerfectScore;
        }

        if (ogPhoto.getShutterSpeedValue()==null && newPhoto.getShutterSpeedValue()==null){
            result +=onePerfectScore;
        }else if(ogPhoto.getShutterSpeedValue()!=null && newPhoto.getShutterSpeedValue()!=null){
            String[] ogS = ogPhoto.getShutterSpeedValue().replace("/"," ").split(" ");
            String[] newS = newPhoto.getShutterSpeedValue().replace("/"," ").split(" ");
            int ogSs = Integer.parseInt(ogS[1]);
            int newSs = Integer.parseInt(newS[1]);

            if(ogSs == newSs ){
                result +=onePerfectScore;
            }else if(newSs<=ogSs+100 && newSs>=ogSs-100){
                result += oneHalfScore;
            }
        }

        if((ogPhoto.getWhiteBalanceMode()== null && newPhoto.getWhiteBalanceMode()== null ) ){
            result +=onePerfectScore;
        }else if(ogPhoto.getWhiteBalanceMode()!= null && newPhoto.getWhiteBalanceMode()!= null &&
                (newPhoto.getWhiteBalanceMode().compareTo(ogPhoto.getWhiteBalanceMode()) == 0)) {
            result += onePerfectScore;
        }

        return result;
    }
}
