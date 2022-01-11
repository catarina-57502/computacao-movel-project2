package pt.fcul.cm2021.grupo9.shotop.processoAdicionar;

public class VisionResponse {
    public String description;
    public String mid;
    public String score;
    public String topicality;

    public VisionResponse(String description, String mid, String score, String topicality) {
        this.description = description;
        this.mid = mid;
        this.score = score;
        this.topicality = topicality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTopicality() {
        return topicality;
    }

    public void setTopicality(String topicality) {
        this.topicality = topicality;
    }
}
