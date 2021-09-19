package pt.ist.socialsoftware.edition.user.utils;

import pt.ist.socialsoftware.edition.user.domain.Role;
import pt.ist.socialsoftware.edition.user.domain.User;
import pt.ist.socialsoftware.edition.user.domain.UserModule;

public class UserBootstrap {

    public static boolean initializeUserModule() {

        boolean userCreate = false;
        if (UserModule.getInstance() == null) {
            new UserModule();
            UserModule.getInstance().setAdmin(true);
            createUsersAndRoles();
            userCreate = true;
        }
        return userCreate;
    }

    private static void createUsersAndRoles() {
        UserModule userModule = UserModule.getInstance();

        Role user = Role.getRole(Role.RoleType.ROLE_USER);
        Role admin = Role.getRole(Role.RoleType.ROLE_ADMIN);

        // the bcrypt generator
        // https://www.dailycred.com/blog/12/bcrypt-calculator
        User ars = new User(userModule, User.USER_ARS, "$2a$11$Y0PQlyE20CXaI9RGhtjZJeTM/0.RUyp2kO/YAJI2P2FeINDEUxd2m",
                "António", "Rito Silva", "rito.silva@tecnico.ulisboa.pt");

        User twitter = new User(userModule, "Twitter", null, User.USER_TWITTER, "Social Media", "");

        // User diego = new User(ldod, "diego",
        // "$2a$11$b3rI6cl/GOzVqOKUOWSQQ.nTJFn.s8a/oALV.YOWoUZu6HZGvyCXu",
        // "Diego", "Giménez", "dgimenezdm@gmail.com");
        // User mp = new User(ldod, "mp",
        // "$2a$11$Nd6tuFTBZV3ej02xJcJhUOZtHKsc888UOBXFz9jDYDBs/EHQIIP26", "Manuel",
        // "Portela", "mportela@fl.uc.pt");
        // User tiago = new User(ldod, "tiago",
        // "$2a$11$GEa2gLrEweOV5b.fzTi5ueg.s9h2wP/SmRUt2mCvU.Ra7BxgkPVci",
        // "Tiago", "Santos", "tiago@tiagosantos.me");
        // User nuno = new User(ldod, "nuno",
        // "$2a$11$ICywhcOlcgbkWmi2zxYRi./AjLrz4Vieb25TBUeK3FsMwYmSPTcMu",
        // "Nuno", "Pinto", "nuno.mribeiro.pinto@gmail.com");
        // User luis = new User(ldod, "luis",
        // "$2a$11$c0Xrwz/gw0tBoMo3o1AG3.boCszoGOXyDWZ5z2vSY259/RDLK4ZDi",
        // "Luís", "Lucas Pereira", "lmlbpereira@gmail.com");
        // User andre = new User(ldod, "afs",
        // "$2a$11$na24dttCBjjT5uVT0mBCb.MlDdCGHwu3w6tRTqf5OD9QAsIPYJzfu",
        // "André", "Santos", "andrefilipebrazsantos@gmail.com");
        // User daniela = new User(ldod, "daniela",
        // "$2a$04$QiGbDnmoyrvyFnJdfsHhSeJoWJkjVkegrIpIADcIBVziVYWPHnPpC",
        // "Daniela", "Maduro", "cortesmaduro@hotmail.com");
        // User joana = new User(ldod, "joana",
        // "$2a$12$tdXO4XfyDP0BdrvGyScv9uRHjDPitbwKzpU1eepeJxgzZFnXZczLq",
        // "Joana", "Malta", "joanavmalta@gmail.com");
        // User bernardosoares = new User(ldod, "bernardosoares",
        // "$2a$04$2romaiXNBOFcVpDrcg0Miepy7AeeBGJq4jc4EdRA/EFekYxSFxTsC", "Bernardo",
        // "Soares",
        // "bernardosoares@pessoa.pt");
        // User rita = new User(ldod, "rita",
        // "$2a$12$6UbQBZNy0s2LQnQjaPe2au645FF.gEC7/RF5Xv9P8bdAhJo.fugoa",
        // "Rita", "Marrone", "bernardosoares@pessoa.pt");
        // User osvaldo = new User(ldod, "osvaldo",
        // "$2a$12$5WFTqOwTFfhPEeJ.L2Dbk.qvbCArQCSkcp7DdeUkrxj3dX2XT827e",
        // "Osvaldo", "Silvestre", "omsilvestre@gmail.com");
        // User jose = new User(ldod, "jose",
        // "$2a$12$gqbtKFUkIS8hqALVuc/h3eETWIWeQgxmPiWK9fm3joROsZRYHDkiW",
        // "José Maria", "Cunha", "z@josemariacunha.com");

        ars.setEnabled(true);
        ars.addRoles(user);
        ars.addRoles(admin);

        twitter.setActive(false);
        twitter.setEnabled(true);
        // diego.setEnabled(true);
        // diego.addRoles(user);
        // diego.addRoles(admin);
        //
        // mp.setEnabled(true);
        // mp.addRoles(user);
        // mp.addRoles(admin);
        //
        // tiago.setEnabled(true);
        // tiago.addRoles(user);
        // tiago.addRoles(admin);
        //
        // nuno.setEnabled(true);
        // nuno.addRoles(user);
        // nuno.addRoles(admin);
        //
        // luis.setEnabled(true);
        // luis.addRoles(user);
        // luis.addRoles(admin);
        //
        // andre.setEnabled(true);
        // andre.addRoles(user);
        // andre.addRoles(admin);
        //
        // daniela.setEnabled(true);
        // daniela.addRoles(user);
        // daniela.addRoles(admin);
        //
        // joana.setEnabled(true);
        // joana.addRoles(user);
        //
        // bernardosoares.setEnabled(true);
        // bernardosoares.addRoles(user);
        //
        // rita.setEnabled(true);
        // rita.addRoles(user);
        // rita.addRoles(admin);
        //
        // osvaldo.setEnabled(true);
        // osvaldo.addRoles(user);
        // osvaldo.addRoles(admin);
        //
        // jose.setEnabled(true);
        // jose.addRoles(user);
        // jose.addRoles(admin);
    }
}
