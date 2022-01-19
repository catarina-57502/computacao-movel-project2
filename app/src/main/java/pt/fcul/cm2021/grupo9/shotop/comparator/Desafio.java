package pt.fcul.cm2021.grupo9.shotop.comparator;

public class Desafio {
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

    public String getId() {
        return id;
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

    public void setScore(String score) {
        this.score = score;
    }
}


