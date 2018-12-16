package ir.component.core.engine.service;


import ir.component.core.dao.model.Media;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

/**
 * @author Mohammad Yasin Kaji
 */
public abstract class MediaConverterService {
    protected Map<String, String> envContext;

/*
    @Resource
    protected UiController uiController;
*/
    @Value("${media.env:}")
    String env;

    @PostConstruct
    public void init() {
        envContext = new HashMap<String, String>();

        String TMPDIR = System.getProperty("java.io.tmpdir");
        if (TMPDIR != null && !TMPDIR.isEmpty()) {
            envContext.put("TMP", TMPDIR);
            envContext.put("TEMP", TMPDIR);
        }

        if (StringUtils.isNotBlank(env)) {
            String[] split = StringUtils.split(env, ',');
            for (String s : split) {
                String[] envItem = StringUtils.split(s, '=');
                String key = envItem[0];
                String value = envItem.length > 1 ? envItem[1] : null;
                envContext.put(key, value);
            }
        }

        // since this is shared between all requests no one should change it
        envContext = Collections.unmodifiableMap(envContext);
    }


    /**
     * Everyone implement this behavior must provide a piece of code for setting flags!
     * <p/>
     * Flag structure: 0000 0000 0000 0000 0000 0000 0000 0000
     * <ul>
     * <li>2<sup>0</sup>: Dimension Detected</li>
     * <li>2<sup>1</sup>: Media Converted</li>
     * </ul>
     */
    abstract public void convert(Media media, String home, File inFile, String creator) throws Exception;


}