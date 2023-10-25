package pe.gob.sblm.api.alfresco.manager;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by pjudicial on 08/05/2015.
 */
public interface CMISManager {

    /**
     *
     * @param path Root path where create the folder
     * @param newFolderName Folder name of new folder
     * @return
     */
    public String cmisCreateFolder(String path, String newFolderName);

    public String cmisCreateFolder( String newFolderName);

    /**
     * Comprueba la existencia de una carpeta
     * @param path
     * @return
     */
    public Boolean cmisExistsFolder(String path);

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
    public String  cmisUploadFile( FileProperties fp, ByteArrayInputStream sourceFile, String fileName, String destPath, String mimetype, long fileSize);

    /***
     *
     * @param fp propiedades del documento como FileProperties
     * @param sourceFile Contenido del archivo como byte array (hacer un getBytes(Charset) .getBytes("UTF-8")
     * @param fileName Nombre de archivo que tendra en Alfresco
     * @param destPath ruta de la carpeta donde se grabara el documento en el alfresco
     * @param mimetype MimeType del archivo
     * @return retorna el uuid del documento almacenado en forma de String
     */
    public String cmisUploadFile(FileProperties fp, byte[] sourceFile, String fileName, String destPath, String mimetype);

    
    public String cmisUpdateDocument(FileProperties fp, byte[] sourceFile, String fileName, String destPath, String mimetype);
    

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
    public String  cmisUploadFile(FileProperties fp, String sourceFile, String fileName, String destPath, String mimetype) throws FileNotFoundException, IOException;

    /***
     *
     * @param fileName es el nombre del archivo
     * @param fileDest es la ruta donde
     * @return
     * @throws IOException
     */
    public String cmisGetFile(String fileName, String fileDest) throws IOException;

    /***
     *
     * @param fileName
     * @param fileDest
     * @param exist
     * @param exist1
     * @return
     * @throws IOException
     */
    public String cmisGetFile(String fileName, String fileDest,boolean exist,boolean exist1) throws IOException;

    public ArrayList<String> cmisGetFolderContents(String subFolder);

    public Boolean cmisDeleteFile(String destPath);
    public Boolean cmisDeleteFile(String destPath, String name);

    public Boolean cmisExistFile(String destPath, String name);

    public Object cmisFindDocument(String destPath, String name);

   

    public String getFilePath(String docId);

    public byte[] getFileByUuid(String docId);

    public Map executeQuery(String query);

    public void createFolferIfNotExist(String path, String newNameFolder);

}
