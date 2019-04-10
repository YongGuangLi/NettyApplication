package up.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetsDeviceEntity implements Serializable {
    private Integer id;

    private String ip;

    private String name;

    private String mac;

    private String serialnumber;

    private String factoryname;

    private String factorydec;

    private String iptwo;

    private String mactwo;

    private String devicetype;

    private String publicname;

    private String systemVersion;

    private Byte hold;

    private Byte snmpVersion;

    private String snmpv3Username;

    private Byte snmpv3AuthAlgo;

    private Byte snmpv3EncryptAlgo;

    private String snmpRead;

    private String snmpWrite;

    private Integer isonline;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac == null ? null : mac.trim();
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber == null ? null : serialnumber.trim();
    }

    public String getFactoryname() {
        return factoryname;
    }

    public void setFactoryname(String factoryname) {
        this.factoryname = factoryname == null ? null : factoryname.trim();
    }

    public String getFactorydec() {
        return factorydec;
    }

    public void setFactorydec(String factorydec) {
        this.factorydec = factorydec == null ? null : factorydec.trim();
    }

    public String getIptwo() {
        return iptwo;
    }

    public void setIptwo(String iptwo) {
        this.iptwo = iptwo == null ? null : iptwo.trim();
    }

    public String getMactwo() {
        return mactwo;
    }

    public void setMactwo(String mactwo) {
        this.mactwo = mactwo == null ? null : mactwo.trim();
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype == null ? null : devicetype.trim();
    }

    public String getPublicname() {
        return publicname;
    }

    public void setPublicname(String publicname) {
        this.publicname = publicname == null ? null : publicname.trim();
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion == null ? null : systemVersion.trim();
    }

    public Byte getHold() {
        return hold;
    }

    public void setHold(Byte hold) {
        this.hold = hold;
    }

    public Byte getSnmpVersion() {
        return snmpVersion;
    }

    public void setSnmpVersion(Byte snmpVersion) {
        this.snmpVersion = snmpVersion;
    }

    public String getSnmpv3Username() {
        return snmpv3Username;
    }

    public void setSnmpv3Username(String snmpv3Username) {
        this.snmpv3Username = snmpv3Username == null ? null : snmpv3Username.trim();
    }

    public Byte getSnmpv3AuthAlgo() {
        return snmpv3AuthAlgo;
    }

    public void setSnmpv3AuthAlgo(Byte snmpv3AuthAlgo) {
        this.snmpv3AuthAlgo = snmpv3AuthAlgo;
    }

    public Byte getSnmpv3EncryptAlgo() {
        return snmpv3EncryptAlgo;
    }

    public void setSnmpv3EncryptAlgo(Byte snmpv3EncryptAlgo) {
        this.snmpv3EncryptAlgo = snmpv3EncryptAlgo;
    }

    public String getSnmpRead() {
        return snmpRead;
    }

    public void setSnmpRead(String snmpRead) {
        this.snmpRead = snmpRead == null ? null : snmpRead.trim();
    }

    public String getSnmpWrite() {
        return snmpWrite;
    }

    public void setSnmpWrite(String snmpWrite) {
        this.snmpWrite = snmpWrite == null ? null : snmpWrite.trim();
    }

    public Integer getIsonline() {
        return isonline;
    }

    public void setIsonline(Integer isonline) {
        this.isonline = isonline;
    }

    @Override
    public String toString() {
        return "AssetsDeviceEntity{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", name='" + name + '\'' +
                ", mac='" + mac + '\'' +
                ", serialnumber='" + serialnumber + '\'' +
                ", factoryname='" + factoryname + '\'' +
                ", factorydec='" + factorydec + '\'' +
                ", iptwo='" + iptwo + '\'' +
                ", mactwo='" + mactwo + '\'' +
                ", devicetype='" + devicetype + '\'' +
                ", publicname='" + publicname + '\'' +
                ", systemVersion='" + systemVersion + '\'' +
                ", hold=" + hold +
                ", snmpVersion=" + snmpVersion +
                ", snmpv3Username='" + snmpv3Username + '\'' +
                ", snmpv3AuthAlgo=" + snmpv3AuthAlgo +
                ", snmpv3EncryptAlgo=" + snmpv3EncryptAlgo +
                ", snmpRead='" + snmpRead + '\'' +
                ", snmpWrite='" + snmpWrite + '\'' +
                ", isonline=" + isonline +
                '}';
    }
}