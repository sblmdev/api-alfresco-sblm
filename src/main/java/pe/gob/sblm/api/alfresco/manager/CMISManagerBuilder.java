package pe.gob.sblm.api.alfresco.manager;

import pe.gob.sblm.api.alfresco.manager.defaults.CMISManagerImpl;
import pe.gob.sblm.api.alfresco.repository.Repository;
import pe.gob.sblm.api.alfresco.session.SessionPJCM;

/**
 * Created by pjudicial on 08/05/2015.
 */
public class CMISManagerBuilder {

    public CMISManager build(Repository repository){
        SessionPJCM.setting(repository);
        CMISManager cmisManager = new CMISManagerImpl();
        return cmisManager;
    }

}
