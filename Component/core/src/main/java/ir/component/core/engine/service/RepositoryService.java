package ir.component.core.engine.service;

import java.io.*;

/**
 * Repository service interface.
 * 
 * @author Mohammad Ysain Kaji
 */
public interface RepositoryService {

    /**
     * Stores input stream into repository.
     * 
     * @param name
     * @param ins
     * @return stored file.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public File store(String name, InputStream ins) throws FileNotFoundException, IOException;

    /**
     * @param from
     * @param toName
     * @return <code>true</code> if moved successfully
     * @throws FileNotFoundException
     * @throws IOException
     */
    public boolean move(File from, String toName);

    /**
     * Create a {@link FileOutputStream} with the given name and returns it. It's important to close the stream after your work is finished.
     * 
     * @param name
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public FileOutputStream openFileStream(String name) throws FileNotFoundException, IOException;

    /**
     * Retrieves a {@link File} pointer to the given file name. It doesn't check if file exists, so returned {@link File} should be checked
     * it it exists ({@link File#exists()}).
     * 
     * @param name
     * @return retrieved file.
     */
    public File get(String name);

    /**
     * Retrieves a {@link File} pointer to the file thumbnail with the given index. It doesn't check if file exists, so returned
     * {@link File} should be checked it it exists ({@link File#exists()}).
     * 
     * @param name
     * @param thumbIndex
     * @return retrieved file.
     */
    /*
    public File get(String name, int thumbIndex);*/

    /**
     * @return
     */
    public String getHome();
}
