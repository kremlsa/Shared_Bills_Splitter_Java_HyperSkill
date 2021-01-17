package splitter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.System.exit;

@SpringBootApplication
public class Main{

    public static void main (String[] args) {
        SpringApplication.run(Main.class, args);
    }

/*    @Override
    public void run(String... args) throws Exception {
        Menu menu = new Menu();
        try {
            menu.menu();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getClass().getName().equals("splitter.commandException"))
                System.out.println(e.getMessage());
        }
        exit(0);
    }*/
}

   /* public static void main(String[] args) {

        Menu menu = new Menu();
        try {
            menu.menu();
        } catch (Exception e) {
            if (e.getClass().getName().equals("splitter.commandException"))
                System.out.println(e.getMessage());
        }
    }
}*/
