package pe.gob.sblm.api.alfresco.manager.defaults;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.DocumentType;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

import pe.gob.sblm.api.alfresco.manager.CMISManager;
import pe.gob.sblm.api.alfresco.manager.FileProperties;
import pe.gob.sblm.api.alfresco.session.SessionPJCM;

/**
 * Created by CSERRANOCA on 26/05/2015.
 */
public class CMISManagerImpl implements CMISManager {




    /**
     *
     * @param path Root path where create the folder
     * @param newFolderName Folder name of new folder
     * @return
     */
    public String cmisCreateFolder(String path, String newFolderName){
        Session session = null;
        Folder rootOperationFolder = null;
        CmisObject object = null;
        Folder newFolder = null;

        try{
            session = SessionPJCM.openSession();
            object = session.getObjectByPath(path);

            if (object!=null){
                rootOperationFolder = (Folder) object;
            }
            //crear carpeta
            System.out.println("Creating '"+newFolderName+"' in the root folder");
            Map<String, String> newFolderProps = new HashMap<String, String>();
            newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
            newFolderProps.put(PropertyIds.NAME, newFolderName);

            newFolder = rootOperationFolder.createFolder(newFolderProps);

        }finally {
            if(null != session){session.clear();}
            SessionPJCM.finalizeSession();
        }

        return newFolder.getId();
    }

    public String cmisCreateFolder( String newFolderName){
        Session session = null;
        Folder rootOperationFolder = null;
        Folder newFolder = null;

        try{
            session = SessionPJCM.openSession();
            rootOperationFolder = getRootFolder(session);

            //crear carpeta
            System.out.println("Creating '"+newFolderName+"' in the root folder");
            Map<String, String> newFolderProps = new HashMap<String, String>();
            newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
            newFolderProps.put(PropertyIds.NAME, newFolderName);

            newFolder = rootOperationFolder.createFolder(newFolderProps);

        }finally {
            if(null != session){session.clear();}
            SessionPJCM.finalizeSession();
        }

        return newFolder.getId();
    }

    private Folder getRootFolder(Session session) {
        //CmisObject object = session.getObjectByPath("/EJD");
        CmisObject object = session.getObjectByPath("/SINOE");
        Folder rootOperationFolder  = (Folder) object;
        return rootOperationFolder;
    }
    private Folder getFolder(String path, Session session) {
        CmisObject object = session.getObjectByPath(path);
        Folder rootOperationFolder  = (Folder) object;
        return rootOperationFolder;
    }

    /**
     * Comprueba la existencia de una carpeta
     * @param path
     * @return
     */
    public Boolean cmisExistsFolder(String path) {
        Session session = null;
        Boolean exist = new Boolean(false);
        try{
            session = SessionPJCM.openSession();
            CmisObject object = session.getObjectByPath(path);
            if (object!=null && ((Folder) object).getPath().equals(path)){
                exist = true;
            }
        }catch(CmisObjectNotFoundException e ){
            System.out.println(e.getMessage());
        }finally {
            if(null != session){session.clear();}
            SessionPJCM.finalizeSession();
        }

        return exist;
    }




    /***
     *
     * @param fp propiedades del documento como FileProperties
     * @param sourceFile Contenido del archivo como ByteArrayInputStream
     * @param fileName Nombre de archivo que tendra en Alfresco
     * @param destPath ruta de la carpeta donde se grabara el documento en el alfresco
     * @param mimetype MimeType del archivo
     * @param fileSize tama�o del archivo
     * @return retorna el uuid del documento almacenado en forma de String
     */
    public String  cmisUploadFile( FileProperties fp, ByteArrayInputStream sourceFile, String fileName, String destPath, String mimetype, long fileSize){

        Session session = null;
        Document doc = null;
        Folder folder;
        ContentStream contentStream;

        try {
            session = SessionPJCM.openSession();
            contentStream = session.getObjectFactory().createContentStream(fileName, fileSize, mimetype, sourceFile);
            folder = this.getFolder(destPath, session);
            doc = folder.createDocument(fp.getProperties(), contentStream, VersioningState.MAJOR);
        }finally {
            if(null != session){session.clear();}
            SessionPJCM.finalizeSession();
        }

        return doc.getId();
    }

    /***
     *
     * @param fp propiedades del documento como FileProperties
     * @param sourceFile Contenido del archivo como byte array (hacer un getBytes(Charset) .getBytes("UTF-8")
     * @param fileName Nombre de archivo que tendra en Alfresco
     * @param destPath ruta de la carpeta donde se grabara el documento en el alfresco
     * @param mimetype MimeType del archivo
     * @return retorna el uuid del documento almacenado en forma de String
     */
    public String cmisUploadFile(FileProperties fp, byte[] sourceFile, String fileName, String destPath, String mimetype){
    	

        ByteArrayInputStream input = new ByteArrayInputStream(sourceFile);
        return cmisUploadFile(fp, input, fileName, destPath, mimetype, sourceFile.length);
    }



    /***
     *
     * @param fp propiedades del documento como FileProperties
     * @param sourceFile Archivo a subir, ruta completa con nombre de archivo
     * @param fileName Nombre de archivo que tendra en Alfresco
     * @param destPath ruta de la carpeta donde se grabara el documento en el alfresco
     * @param mimetype MimeType del archivo
     * @return retorna el uuid del documento almacenado en forma de String
     * @throws FileNotFoundException
     * @throws IOException
     */
    public String  cmisUploadFile(FileProperties fp, String sourceFile, String fileName, String destPath, String mimetype) throws FileNotFoundException, IOException {

        File file = new File(sourceFile);
        byte[] fileData = new byte[(int) file.length()];
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        dis.readFully(fileData);
        dis.close();

        return cmisUploadFile(fp, fileData, fileName, destPath, mimetype);

    }


    /***
     *
     * @param fileName es el nombre del archivo
     * @param fileDest es la ruta donde
     * @return
     * @throws IOException
     */
    public String cmisGetFile(String fileName, String fileDest) throws IOException{

        Session session = null;

        try{
            session = SessionPJCM.openSession();
            //leer el doc por su path
            //String path = "/EJD" + "/" + fileName;
            String path = fileName;
            System.out.println("Getting object by path " + path);

            Document doc = (Document) session.getObjectByPath(path);
            ContentStream contentStream = doc.getContentStream();
            if (contentStream != null) {

                //(contentStream.getStream());
                // write the inputStream to a FileOutputStream
                InputStream inputStream = contentStream.getStream();
                OutputStream outputStream =
                        new FileOutputStream(new File(fileDest));

                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                outputStream.close();
            } else {
                System.out.println("No content.");
            }

        }finally {
            if(null != session){session.clear();}
            SessionPJCM.finalizeSession();
        }

        return fileDest;
    }

    /***
     *
     * @param fileName
     * @param fileDest
     * @param exist
     * @param exist1
     * @return
     * @throws IOException
     */
    public String cmisGetFile(String fileName, String fileDest,boolean exist,boolean exist1) throws IOException{

        Session session = null;

        try{
            session = SessionPJCM.openSession();
            //leer el doc por su path
            //String path = CMISConfiguracion.SITE_ROOT_FOLDER_PATH_PLANTILLAS + "/" + fileName;
            System.out.println("Getting object by path " + fileName);

            Document doc = (Document) session.getObjectByPath(fileName);
            ContentStream contentStream = doc.getContentStream();
            if (contentStream != null) {

                //(contentStream.getStream());
                // write the inputStream to a FileOutputStream
                InputStream inputStream = contentStream.getStream();
                OutputStream outputStream =
                        new FileOutputStream(new File(fileDest));

                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                inputStream.close();
                outputStream.close();
            } else {
                System.out.println("No content.");
            }

        }finally {
            if (null != session){session.clear();}
            SessionPJCM.finalizeSession();
        }

        return fileDest;
    }

    public ArrayList<String> cmisGetFolderContents(String subFolder){
        ArrayList<String> listado = new ArrayList<String>();
        Session session = null;

        try{
            session = SessionPJCM.openSession();
            Folder folder  = (Folder) getFolder("/EJD" + "/" + subFolder, session);
            ItemIterable<CmisObject> children = folder.getChildren();

            for (CmisObject o : children) {
                listado.add(o.getName());
            }
        }finally {
            if (null != session){session.clear();}
            SessionPJCM.finalizeSession();
        }
        return listado;
    }

    @Override
    public Boolean cmisDeleteFile(String destPath) {
        Session session = null;
        Boolean delete = false;
        try {

            session = SessionPJCM.openSession();
            CmisObject object = session.getObjectByPath(destPath);
            if (object != null) {
                session.delete(object);
                delete = true;
            }
        } catch (CmisObjectNotFoundException e) {
            System.out.println(e.getMessage());
        }finally {
            if(null != session){session.clear();}
            SessionPJCM.finalizeSession();
        }

        return delete;
    }

    public Boolean cmisDeleteFile(String destPath, String name) {
        Session session = null;
        Boolean delete = false;
        try {

            session = SessionPJCM.openSession();
            CmisObject object = session.getObjectByPath(destPath + "/" + name);
            if (object != null) {
                session.delete(object);
                delete = true;
            }
        } catch (CmisObjectNotFoundException e) {
            System.out.println(e.getMessage());
        }finally {
            if(null != session){session.clear();}
            SessionPJCM.finalizeSession();
        }

        return delete;
    }

    public Boolean cmisExistFile(String destPath, String name) {
        Session session = null;
        Boolean exist = false;
        try {
            session = SessionPJCM.openSession();
            CmisObject object = session.getObjectByPath(destPath + "/" + name);
            if (object != null) {
                exist = true;
            }

        } catch (CmisObjectNotFoundException e) {
            System.out.println(e.getMessage());

        }finally {
            if(null != session){session.clear();}
            SessionPJCM.finalizeSession();
        }

        return exist;
    }

    public Object cmisFindDocument(String destPath, String name){
        Session session = null;
        CmisObject object = null;

        try {
            session = SessionPJCM.openSession();
            object = session.getObjectByPath(destPath + "/" + name);
            if (object != null) {
                return object;
            }
        } catch (CmisObjectNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (null != session ){session.clear();}
            SessionPJCM.finalizeSession();
        }
        return object;
    }

    public String cmisUpdateDocument(FileProperties fp, String sourceFile, String fileName, String destPath, String mimetype) throws FileNotFoundException, IOException{

        File file = new File(sourceFile);
        byte[] fileData = new byte[(int) file.length()];
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        dis.readFully(fileData);
        dis.close();
        return cmisUpdateDocument(fp, fileData, fileName, destPath, mimetype);

    }

    public String cmisUpdateDocument(FileProperties fp, byte[] sourceFile, String fileName, String destPath, String mimetype){

        ByteArrayInputStream input = new ByteArrayInputStream(sourceFile);
        return cmisUpdateDocument(fp, input, fileName, destPath, mimetype, sourceFile.length);

    }

    private String cmisUpdateDocument(FileProperties fp, ByteArrayInputStream sourceFile, String fileName, String destPath, String mimetype, long fileSize){
        Session session = null;
        CmisObject doc = null;

        try {
            session = SessionPJCM.openSession();
            //obtenermos el documento en alfresco
            doc = session.getObjectByPath(destPath + "/" + fileName);


            if(doc == null){
                throw new Exception(" No existe documento ");
            }
            
            System.out.println("if");
            
            
            DocumentType documentType = (DocumentType) session.getTypeDefinition("D:sgicm:inmuebleDocumento");
            boolean isVersionable = Boolean.TRUE.equals(documentType.isVersionable());

            if (isVersionable) {
                System.out.println("if");
            	doc.refresh();
                Document newObject = (Document) doc;
                ObjectId  idOfCheckedOutDocument = newObject.checkOut();
                Document pwc = (Document) session.getObject(idOfCheckedOutDocument);

                ContentStream contentStream = session.getObjectFactory().createContentStream(fileName, fileSize, mimetype, sourceFile);
               
                System.out.println("try");
                try {
                    ObjectId objectId = pwc.checkIn(false, null, contentStream," Mayor version ");
                    doc = (Document) session.getObject(objectId);
                } catch (CmisBaseException e) {
                    e.printStackTrace();
                    pwc.cancelCheckOut();
                }	
                List<Document> versions = newObject.getAllVersions();

                for (Document version : versions) {
                    System.out.println("\tname: " + version.getName());
                    System.out.println("\tversion label: " + version.getVersionLabel());
                    System.out.println("\tversion series id: " + version.getVersionSeriesId());
                    System.out.println("\tchecked out by: "
                            + version.getVersionSeriesCheckedOutBy());
                    System.out.println("\tchecked out id: "
                            + version.getVersionSeriesCheckedOutId());
                    System.out.println("\tmajor version: " + version.isMajorVersion());
                    System.out.println("\tlatest version: " + version.isLatestVersion());
                    System.out.println("\tlatest major version: " + version.isLatestMajorVersion());
                    System.out.println("\tcheckin comment: " + version.getCheckinComment());
                    System.out.println("\tcontent length: " + version.getContentStreamLength()
                            + "\n");
                }
            }

        } catch (CmisObjectNotFoundException e) {
            e.printStackTrace();
        }  catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null != session){session.clear();}
            SessionPJCM.finalizeSession();
        }

        return doc.getId();
    }
    public String getFilePath(String docId) {
        Session session = null;

        try {
            session = SessionPJCM.openSession();
            CmisObject object = session.getObject(docId);
            if (object == null) {
                return "";
            }
            return ((Document)object).getPaths().get(0);
        } catch (CmisObjectNotFoundException e) {
            System.out.println(e.getMessage());
            return "";
        }finally {
            if (null != session){session.clear();}
            SessionPJCM.finalizeSession();
        }

    }


    public byte[] getFileByUuid(String docId) {

        System.out.println("*** getFileByUuid ***");

        Session session = null;
        byte[] respuesta=null;
        try {
            session = SessionPJCM.openSession();
            CmisObject object = session.getObject(docId);
            if (object == null) {
                return respuesta;
            }

            Document doc = (Document) session.getObject(docId);

            System.out.println("doc.getName() = " + doc.getName());



            ContentStream contentStream = doc.getContentStream();
            if (contentStream != null) {

                respuesta=inputStreamToBytes(contentStream.getStream());

            } else {
                System.out.println("No content.");
            }


        } catch (CmisObjectNotFoundException e) {
            System.out.println(e.getMessage());

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            if (null != session){session.clear();}
            SessionPJCM.finalizeSession();
        }
        return respuesta;

    }

    public static byte[] inputStreamToBytes(InputStream in) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        byte[] bytes = new byte[512];
        int readBytes;
        while ((readBytes = in.read(bytes)) > 0) {
            outputStream.write(bytes, 0, readBytes);
        }
        byte[] byteData = outputStream.toByteArray();
        outputStream.close();
        return byteData;
    }


    public Map executeQuery(String query){
        Map<String, Object> mapProperties = new HashMap();
        Session session = SessionPJCM.openSession();
        ItemIterable<QueryResult> results = session.query(query, false);

        for(QueryResult hit: results) {
            for(PropertyData<?> property: hit.getProperties()){
                String queryName = property.getQueryName();
                Object value = property.getFirstValue();
                if(null != value){
                    mapProperties.put(queryName, value);
                }
            }
        }

        if(mapProperties.isEmpty()){
            mapProperties = null;
        }

        return mapProperties;
    }

    public void createFolferIfNotExist(String path, String newNameFolder){
    	
    	
        String fullPath;
        if("/".equals(path)){
            fullPath = path + newNameFolder;

        }else{
            fullPath = path + "/" + newNameFolder;
        }

        Boolean existFolder =  cmisExistsFolder(fullPath);

        if(existFolder.equals(false)){
            /*** Creamos el folder ***/
            cmisCreateFolder(path,newNameFolder);
            System.out.println("Folder "+ fullPath +" creado con exito.");
        }
    }

}
