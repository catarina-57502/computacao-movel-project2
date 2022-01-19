package pt.fcul.cm2021.grupo9.shotop.entidades;

import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Spot {

    String id;
    String nome;
    GeoPoint loc;
    String imagem;
    List<String> caracteristicas;
    String idUser;
    boolean desafio;
    String imageHeight;
    String imageWidth;
    String model;
    String dateTime;
    String orientation;
    String fNumber;
    String exposureTime;
    String focalLength;
    String flash;
    String iSOSpeedRatings;
    String whiteBalanceMode;
    String apertureValue;
    String shutterSpeedValue;
    String detectedFileTypeName;
    String fileSize;
    String brightnessValue;
    String exposureBiasValue;
    String maxApertureValue;
    String digitalZoomRatio;
    String contrast;
    String saturation;
    String sharpness;

    public Spot() {

    }

    public Spot(String nome, String imagem) {
        this.nome = nome;
        this.imagem = imagem;
    }

    public Spot(String id, String nome, GeoPoint loc, String imagem, List<String> caracteristicas, String imageHeight, String imageWidth, String model, String dateTime, String orientation, String fNumber, String exposureTime, String focalLength, String flash, String iSOSpeedRatings, String whiteBalanceMode, String apertureValue, String shutterSpeedValue, String detectedFileTypeName, String fileSize, String brightnessValue, String exposureBiasValue, String maxApertureValue, String digitalZoomRatio, String contrast, String saturation, String sharpness) {
        this.id = id;
        this.nome = nome;
        this.loc = loc;
        this.imagem = imagem;
        this.caracteristicas = caracteristicas;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.model = model;
        this.dateTime = dateTime;
        this.orientation = orientation;
        this.fNumber = fNumber;
        this.exposureTime = exposureTime;
        this.focalLength = focalLength;
        this.flash = flash;
        this.iSOSpeedRatings = iSOSpeedRatings;
        this.whiteBalanceMode = whiteBalanceMode;
        this.apertureValue = apertureValue;
        this.shutterSpeedValue = shutterSpeedValue;
        this.detectedFileTypeName = detectedFileTypeName;
        this.fileSize = fileSize;
        this.brightnessValue = brightnessValue;
        this.exposureBiasValue = exposureBiasValue;
        this.maxApertureValue = maxApertureValue;
        this.digitalZoomRatio = digitalZoomRatio;
        this.contrast = contrast;
        this.saturation = saturation;
        this.sharpness = sharpness;
    }

    public Spot(String id, String nome, GeoPoint loc, String imagem, List<String> caracteristicas, String idUser, boolean desafio, String imageHeight, String imageWidth, String model, String dateTime, String orientation, String fNumber, String exposureTime, String focalLength, String flash, String iSOSpeedRatings, String whiteBalanceMode, String apertureValue, String shutterSpeedValue, String detectedFileTypeName, String fileSize, String brightnessValue, String exposureBiasValue, String maxApertureValue, String digitalZoomRatio, String contrast, String saturation, String sharpness) {
        this.id = id;
        this.nome = nome;
        this.loc = loc;
        this.imagem = imagem;
        this.caracteristicas = caracteristicas;
        this.idUser = idUser;
        this.desafio = desafio;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.model = model;
        this.dateTime = dateTime;
        this.orientation = orientation;
        this.fNumber = fNumber;
        this.exposureTime = exposureTime;
        this.focalLength = focalLength;
        this.flash = flash;
        this.iSOSpeedRatings = iSOSpeedRatings;
        this.whiteBalanceMode = whiteBalanceMode;
        this.apertureValue = apertureValue;
        this.shutterSpeedValue = shutterSpeedValue;
        this.detectedFileTypeName = detectedFileTypeName;
        this.fileSize = fileSize;
        this.brightnessValue = brightnessValue;
        this.exposureBiasValue = exposureBiasValue;
        this.maxApertureValue = maxApertureValue;
        this.digitalZoomRatio = digitalZoomRatio;
        this.contrast = contrast;
        this.saturation = saturation;
        this.sharpness = sharpness;
    }

    public boolean isDesafio() {
        return desafio;
    }

    public void setDesafio(boolean desafio) {
        this.desafio = desafio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getImagem() {
        return imagem;
    }

    public String getExposureBiasValue() {
        return exposureBiasValue;
    }

    public void setExposureBiasValue(String exposureBiasValue) {
        this.exposureBiasValue = exposureBiasValue;
    }

    public String getDigitalZoomRatio() {
        return digitalZoomRatio;
    }

    public void setDigitalZoomRatio(String digitalZoomRatio) {
        this.digitalZoomRatio = digitalZoomRatio;
    }

    public String getSharpness() {
        return sharpness;
    }

    public void setSharpness(String sharpness) {
        this.sharpness = sharpness;
    }



    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getfNumber() {
        return fNumber;
    }

    public void setfNumber(String fNumber) {
        this.fNumber = fNumber;
    }

    public String getExposureTime() {
        return exposureTime;
    }

    public void setExposureTime(String exposureTime) {
        this.exposureTime = exposureTime;
    }

    public String getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(String focalLength) {
        this.focalLength = focalLength;
    }

    public String getFlash() {
        return flash;
    }

    public void setFlash(String flash) {
        this.flash = flash;
    }

    public String getiSOSpeedRatings() {
        return iSOSpeedRatings;
    }

    public void setiSOSpeedRatings(String iSOSpeedRatings) {
        this.iSOSpeedRatings = iSOSpeedRatings;
    }

    public String getWhiteBalanceMode() {
        return whiteBalanceMode;
    }

    public void setWhiteBalanceMode(String whiteBalanceMode) {
        this.whiteBalanceMode = whiteBalanceMode;
    }

    public String getApertureValue() {
        return apertureValue;
    }

    public void setApertureValue(String apertureValue) {
        this.apertureValue = apertureValue;
    }

    public String getShutterSpeedValue() {
        return shutterSpeedValue;
    }

    public void setShutterSpeedValue(String shutterSpeedValue) {
        this.shutterSpeedValue = shutterSpeedValue;
    }

    public String getDetectedFileTypeName() {
        return detectedFileTypeName;
    }

    public void setDetectedFileTypeName(String detectedFileTypeName) {
        this.detectedFileTypeName = detectedFileTypeName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getBrightnessValue() {
        return brightnessValue;
    }

    public void setBrightnessValue(String brightnessValue) {
        this.brightnessValue = brightnessValue;
    }

    public String getMaxApertureValue() {
        return maxApertureValue;
    }

    public void setMaxApertureValue(String maxApertureValue) {
        this.maxApertureValue = maxApertureValue;
    }

    public String getContrast() {
        return contrast;
    }

    public void setContrast(String contrast) {
        this.contrast = contrast;
    }

    public String getSaturation() {
        return saturation;
    }

    public void setSaturation(String saturation) {
        this.saturation = saturation;
    }

    public Spot(String nome, GeoPoint loc){
        this.nome = nome;
        this.loc = loc;
    }



    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public GeoPoint getLoc() {
        return loc;
    }

    public void setLoc(GeoPoint loc) {
        this.loc = loc;
    }

    @SuppressLint("NewApi")
    public void setImagem(byte[] imagem) {
        String s = Base64.getEncoder().encodeToString(imagem);
        this.imagem = s;


    }

    public List<String> getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(ArrayList<String> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }


    @Override
    public String toString() {
        return "Spot{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", loc=" + loc +
                ", imagem='" + imagem + '\'' +
                ", caracteristicas=" + caracteristicas +
                ", idUser='" + idUser + '\'' +
                ", imageHeight='" + imageHeight + '\'' +
                ", imageWidth='" + imageWidth + '\'' +
                ", model='" + model + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", orientation='" + orientation + '\'' +
                ", fNumber='" + fNumber + '\'' +
                ", exposureTime='" + exposureTime + '\'' +
                ", focalLength='" + focalLength + '\'' +
                ", flash='" + flash + '\'' +
                ", iSOSpeedRatings='" + iSOSpeedRatings + '\'' +
                ", whiteBalanceMode='" + whiteBalanceMode + '\'' +
                ", apertureValue='" + apertureValue + '\'' +
                ", shutterSpeedValue='" + shutterSpeedValue + '\'' +
                ", detectedFileTypeName='" + detectedFileTypeName + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", brightnessValue='" + brightnessValue + '\'' +
                ", exposureBiasValue='" + exposureBiasValue + '\'' +
                ", maxApertureValue='" + maxApertureValue + '\'' +
                ", digitalZoomRatio='" + digitalZoomRatio + '\'' +
                ", contrast='" + contrast + '\'' +
                ", saturation='" + saturation + '\'' +
                ", sharpness='" + sharpness + '\'' +
                '}';
    }
}
