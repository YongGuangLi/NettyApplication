package up.common.entity;

public class CertificateEntityWithBLOBs extends CertificateEntity {
    private String content;

    private String originalcontent;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getOriginalcontent() {
        return originalcontent;
    }

    public void setOriginalcontent(String originalcontent) {
        this.originalcontent = originalcontent == null ? null : originalcontent.trim();
    }
}