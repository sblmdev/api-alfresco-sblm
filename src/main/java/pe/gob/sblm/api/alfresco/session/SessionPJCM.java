package pe.gob.sblm.api.alfresco.session;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import pe.gob.sblm.api.alfresco.constant.Constant;
import pe.gob.sblm.api.alfresco.repository.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pjudicial on 07/05/2015.
 */
public abstract class SessionPJCM {

    private static Session session;
    private static Map<String, String> parameter;



    public static void setting(Repository repository){

        if(Constant.PHRASE_DEFAULT.equals(repository.getAtompub_url())){
            repository.setAtompub_url(Repository.ATOMPUB_URL_DEFAULT);
        }

        parameter = new HashMap<String, String>();
        parameter.put(SessionParameter.USER, repository.getUser());
        parameter.put(SessionParameter.PASSWORD, repository.getPassword());
        parameter.put(SessionParameter.ATOMPUB_URL, "http://"+ repository.getHost() +":"+ repository.getPort() +"/" + repository.getAtompub_url());
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        // Set the alfresco object manager
        // Used when using the CMIS extension for Alfresco for working with aspects
        parameter.put(SessionParameter.OBJECT_FACTORY_CLASS, "org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl");
    }

    public static Session openSession(){
        if(null == session){
            SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
            List<org.apache.chemistry.opencmis.client.api.Repository> repositories = sessionFactory.getRepositories(parameter);
            session = repositories.get(0).createSession();
        }
        return session;
    }

    public static void finalizeSession(){
        if(null != session){
            session.clear();
        }
    }

}
