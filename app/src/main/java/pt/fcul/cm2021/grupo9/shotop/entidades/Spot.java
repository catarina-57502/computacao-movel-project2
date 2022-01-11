package pt.fcul.cm2021.grupo9.shotop.entidades;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Spot {

    String nome;
    GeoPoint loc;
    byte[] imagem;
    ArrayList<String> caracteristicas;
    String iso;

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

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public ArrayList<String> getCaracteristicas() {
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
