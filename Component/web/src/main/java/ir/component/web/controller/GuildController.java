package ir.component.web.controller;

import ir.component.core.dao.model.Guild;
import ir.component.core.engine.GuildDao;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @author Zahra Afsharinia
 */

@Controller
@Scope("view")
public class GuildController implements Serializable {

    private Guild guild = new Guild();

    @Resource
    private GuildDao guildDao;

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public void save() {
        guildDao.persist(guild);
    }

    public List<Guild> getAllGuilds() {
        return guildDao.allGuilds();
    }
}
