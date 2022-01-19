package pt.fcul.cm2021.grupo9.shotop.comparator;


import java.util.Comparator;

public class Desafio  {
    private String id;
    private String fotoParticipacao;
    private String idSpot;
    private String idUserOriginal;
    private String idUserParticipante;
    private String score;

    public Desafio ( String fotoParticipacao, String idSpot, String idUserOriginal, String idUserParticipante, String score){
        
        this.fotoParticipacao = fotoParticipacao;
        this.idSpot = idSpot; //original
        this.idUserOriginal = idUserOriginal;
        this.idUserParticipante = idUserParticipante;
        this.score = score;
    }



    public void setId(String id) {
        this.id = id;
    }

    public String getFotoParticipacao() {
        return fotoParticipacao;
    }

    public void setFotoParticipacao(String fotoParticipacao) {
        this.fotoParticipacao = fotoParticipacao;
    }

    public String getIdSpot() {
        return idSpot;
    }

    public void setIdSpot(String idSpot) {
        this.idSpot = idSpot;
    }

    public String getIdUserOriginal() {
        return idUserOriginal;
    }

    public void setIdUserOriginal(String idUserOriginal) {
        this.idUserOriginal = idUserOriginal;
    }

    public String getIdUserParticipante() {
        return idUserParticipante;
    }

    public void setIdUserParticipante(String idUserParticipante) {
        this.idUserParticipante = idUserParticipante;
    }

    public String getScore() {
        return score;
    }

    public int getScoreInt() {
        try{
            int score = Integer.parseInt(getScore());
            return score;
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
        return 0;
    }

    public void setScore(String score) {
        this.score = score;
    }





}


