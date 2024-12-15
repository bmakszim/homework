package jobagency.entities;

public class JobOffer {
    private String position;

    public JobOffer(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}