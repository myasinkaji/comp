package ir.component.web.controller;

import ir.magfa.sdk.model.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Mohammad Yasin Kaji
 */
@Service
@Scope("session")
public class SessionBean {


    private static Map<String, String> themes = Collections.emptyMap();
    private User user = new User();



    static {
        themes = new TreeMap<String, String>();
        themes.put("Omega", "omega");
        themes.put("Afterdark", "afterdark");
        themes.put("Afternoon", "afternoon");
        themes.put("Afterwork", "afterwork");
        themes.put("Aristo", "aristo");
        themes.put("Black-Tie", "black-tie");
        themes.put("Blitzer", "blitzer");
        themes.put("Bluesky", "bluesky");
        themes.put("Bootstrap", "bootstrap");
        themes.put("Casablanca", "casablanca");
        themes.put("Cruze", "cruze");
        themes.put("Cupertino", "cupertino");
        themes.put("Dark-Hive", "dark-hive");
        themes.put("Delta", "delta");
        themes.put("Dot-Luv", "dot-luv");
        themes.put("Eggplant", "eggplant");
        themes.put("Excite-Bike", "excite-bike");
        themes.put("Flick", "flick");
        themes.put("Glass-X", "glass-x");
        themes.put("Home", "home");
        themes.put("Hot-Sneaks", "hot-sneaks");
        themes.put("Humanity", "humanity");
        themes.put("Le-Frog", "le-frog");
        themes.put("Midnight", "midnight");
        themes.put("Mint-Choc", "mint-choc");
        themes.put("Overcast", "overcast");
        themes.put("Pepper-Grinder", "pepper-grinder");
        themes.put("Redmond", "redmond");
        themes.put("Rocket", "rocket");
        themes.put("Sam", "sam");
        themes.put("Smoothness", "smoothness");
        themes.put("South-Street", "south-street");
        themes.put("Start", "start");
        themes.put("Sunny", "sunny");
        themes.put("Swanky-Purse", "swanky-purse");
        themes.put("Trontastic", "trontastic");
        themes.put("UI-Darkness", "ui-darkness");
        themes.put("UI-Lightness", "ui-lightness");
        themes.put("Vader", "vader");

        themes = Collections.unmodifiableMap(themes);
    }


    private String theme = "omega";

    public String login() {
        return "/page/cartable.xhtml?faces-redirect=true";
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Map<String, String> getThemes() {
        return themes;
    }

    public void setThemes(Map<String, String> themes) {
        themes = themes;
    }

    public User getUser() {
        return user;
    }
}
