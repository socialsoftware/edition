package pt.ist.socialsoftware.edition.game.utils;

import pt.ist.socialsoftware.edition.game.domain.ClassificationModule;

public class GameBootstrap {

    public static void initializeGameModule() {
        boolean classificationCreate = false;
        if (ClassificationModule.getInstance() == null) {
            new ClassificationModule();
            classificationCreate = true;
        }
    }
}
