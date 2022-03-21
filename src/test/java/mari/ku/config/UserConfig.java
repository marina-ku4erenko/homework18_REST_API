package mari.ku.config;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:config/user.properties"})
public interface UserConfig extends Config {

    String userFirstName();

    String userLastName();

    String userLogin();

    String userPassword();
}
