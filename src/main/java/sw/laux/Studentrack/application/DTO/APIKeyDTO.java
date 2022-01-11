package sw.laux.Studentrack.application.DTO;

public class APIKeyDTO {
    private String mailAddress;
    private String key;

    public APIKeyDTO() {

    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
