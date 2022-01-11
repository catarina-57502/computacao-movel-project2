package pt.fcul.cm2021.grupo9.shotop.entidades;

import android.annotation.SuppressLint;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Spot {

    String nome;
    GeoPoint loc;
    String imagem;
    List<String> caracteristicas;
    String iso;

    public Spot(String nome, GeoPoint loc){
        this.nome = nome;
        this.loc = loc;
    }

    public String getImagem() {
        return imagem;
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

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    @Override
    public String toString() {
        return "Spot{" +
                "nome='" + nome + '\'' +
                ", loc=" + loc +
                '}';
    }


}
