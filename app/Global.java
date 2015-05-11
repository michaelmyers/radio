
import play.Application;
import play.GlobalSettings;
import play.Play;
import utils.DevDataUtil;

public class Global extends GlobalSettings {

    public static String APP_NAME = "Radio";

    @Override
    public void onStart(Application application) {

        DevDataUtil.createSuperUser();

        if (Play.application().isDev()) {
            DevDataUtil.loadDevData();
        }
    }
}

