package pt.fcul.cm2021.grupo9.shotop.entidades;

import java.util.ArrayList;
import java.util.List;

public class User {
    String id;
    String nome;
    String email;

    List<User> amigos;

    public User() {

    }

    public User(String id, String nome, String email, List<User> amigos) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.amigos = amigos;
    }

    public User(String id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.amigos = new ArrayList<>();
    }

    public User(String nome, String email) {
        this.nome = nome;
        this.email = email;
        this.amigos = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<User> getAmigos() {
        return amigos;
    }

    public String getIdNoBD(){
        return id;
    }

    public void setIdNoBD(String id){
       this.id = id;
    }

    public void setAmigos(List<User> amigos) {
        this.amigos = amigos;
    }

    @Override
    public String toString() {
        return "User{" +
                "nome='" + nome + '\'' +
                ", email=" + email + '\'' +
                '}';
    }
}