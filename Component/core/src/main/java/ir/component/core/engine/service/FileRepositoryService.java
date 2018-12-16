package ir.component.core.engine.service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;

/**
 * @author Mohammad Yasin Kaji
 */
@Service("repositoryService")
public class FileRepositoryService implements RepositoryService {
    final Logger logger = LoggerFactory.getLogger(FileRepositoryService.class);

    @Value("${sata.repos.home}")
    String reposHome;

//    @Resource
//    MediaService mediaService;

    private File home;

    @PostConstruct
    public void init() {
        home = new File(reposHome);
        logger.info("Init repository service: " + home);
        home.mkdirs();
    }

    @Override
    public File store(String name, InputStream ins) throws FileNotFoundException, IOException {
        Pair<File, FileOutputStream> pair = internalOpenFileStream(name);
        FileOutputStream outFile = pair.getValue();
        File destFile = pair.getKey();

        IOUtils.copy(ins, outFile);
        IOUtils.closeQuietly(outFile);

        logger.info("File: {}, size: {} stored.", destFile, FileUtils.byteCountToDisplaySize(destFile.length()));

        return destFile;
    }

    @Override
    public boolean move(File fromFile, String toName) {
        File toDir = new File(home, getDirName(toName));
        File toFile = new File(toDir, getFileName(toName));
        toDir.mkdirs();
        boolean result = fromFile.renameTo(toFile);

        logger.info("File: {}, move to: {}{}", fromFile, toFile, result ? " successful" : ", FAILED");

        return result;
    }

    Pair<File, FileOutputStream> internalOpenFileStream(String name) throws FileNotFoundException, IOException {
        logger.info("Store new file '{}' into repository.", name);
        File dir = new File(home, getDirName(name));

        if (!dir.exists()) {
            dir.mkdirs();
        } else if (!dir.isDirectory()) {
            throw new RuntimeException("Path is not a valid directory: " + dir.getAbsolutePath());
        }

        File destFile = new File(dir, getFileName(name));

        FileOutputStream outFile = new FileOutputStream(destFile);
        return Pair.of(destFile, outFile);
    }

    public String filePath(String name) {
        logger.info("Store new file '{}' into repository.", name);
        File dir = new File(home, getDirName(name));

        if (!dir.exists()) {
            dir.mkdirs();
        } else if (!dir.isDirectory()) {
            throw new RuntimeException("Path is not a valid directory: " + dir.getAbsolutePath());
        }

        File destFile = new File(dir, getFileName(name));

        return destFile.getAbsolutePath();
    }
    @Override
    public FileOutputStream openFileStream(String name) throws FileNotFoundException, IOException {
        return internalOpenFileStream(name).getValue();
    }

    /*private String getDirName(String name) {
        int resid = Math.abs(name.hashCode()) % (36 * 36);
        return String.format("%s/%s", Integer.toString(resid / 36, 36), Integer.toString(resid % 36, 36));
    }*/

    /**
     * @see FilenameUtils#getPathNoEndSeparator(String)
     * @param name
     * @return
     */
    private String getDirName(String name) {
        return FilenameUtils.getPathNoEndSeparator(name);
    }

    private String getFileName(String name) {
        return FilenameUtils.getName(name);
    }

    @Override
    public File get(String name) {
        if (name == null) {
            return null;
        }
        File dir = new File(home, getDirName(name));
        return new File(dir, getFileName(name));
        // File dir = new File(home, getDirName(name));
        // return new File(dir, mediaService.getThumb(name, thumbIndex));
    }

    @Override
    public String getHome() {
        return reposHome;
    }
}
