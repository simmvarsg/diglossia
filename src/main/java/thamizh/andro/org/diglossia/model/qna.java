package thamizh.andro.org.diglossia.model;

public class qna {
    int serialno;
    String question;
    String rightans;
    String wrongans;
    String language;
    boolean status;

    public qna() {
    }

    public qna(int serialno, String question, String rightans, String wrongans, String language) {
        this.serialno = serialno;
        this.question = question;
        this.rightans = rightans;
        this.wrongans = wrongans;
        this.language = language;
        this.status = false;
    }

    public int getSerialno() {
        return serialno;
    }

    public void setSerialno(int serialno) {
        this.serialno = serialno;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRightans() {
        return rightans;
    }

    public void setRightans(String rightans) {
        this.rightans = rightans;
    }

    public String getWrongans() {
        return wrongans;
    }

    public void setWrongans(String wrongans) {
        this.wrongans = wrongans;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "qna{" +
                "serialno=" + serialno +
                ", question='" + question + '\'' +
                ", rightans='" + rightans + '\'' +
                ", wrongans='" + wrongans + '\'' +
                ", language='" + language + '\'' +
                ", status=" + status +
                '}';
    }
}
