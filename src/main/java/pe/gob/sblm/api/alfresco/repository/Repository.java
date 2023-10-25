package pe.gob.sblm.api.alfresco.repository;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by pjudicial on 07/05/2015.
 */
@XmlRootElement(name = "repository")
@XmlType(propOrder = { "user", "password", "host", "port" })
public class Repository implements Serializable{

    private static final long serialVersionUID = -603240173773840971L;
    public static final String  ATOMPUB_URL_DEFAULT = "alfresco/api/-default-/public/cmis/versions/1.0/atom";


    private String user;
    private String password;
    private String host;
    private String port;
    private String atompub_url;

    @XmlElement(name = "user")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @XmlElement(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlElement(name = "host")
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @XmlElement(name = "port")
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @XmlAttribute(name = "atompub-url")
    public String getAtompub_url() {
        return atompub_url;
    }

    public void setAtompub_url(String atompub_url) {
        this.atompub_url = atompub_url;
    }


    @Override
    public String toString() {
        return "Repository{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", atompub_url='" + atompub_url + '\'' +
                '}';
    }
}
