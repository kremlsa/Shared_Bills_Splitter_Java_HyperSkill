package splitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Menu implements CommandLineRunner {
    Balance bal = new Balance();
    Scanner sc = new Scanner(System.in);
    Map<String, List<String>> groups = new LinkedHashMap<>();

    @Autowired
    OpRepository opr;

    @Override
    public void run(String... args) throws Exception{
        boolean isRun = true;
        //Command command = new Command();

        while (isRun) {
            Command command = new Command();
            String input = sc.nextLine();
            input = input.trim();
            try {
                command.checkCommand(input, groups);
            } catch (commandException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            if (command.isOk) {
                switch (command.operations) {
                    case "help":
                        printHelp();
                       // isRun = false;
                        break;
                    case "exit":
                        isRun = false;
                        break;
                    case "borrow":
                        borrow(command);
                        break;
                    case "repay":
                        repay(command);
                        break;
                    case "group":
                        group(command);
                        break;
                    case "purchase":
                        purchase(command);
                        break;
                    case "balance":
                        System.out.print(balance(command));
                        break;
                }
            }
        }
    }

    public void group(Command command) {
        if (command.groupOperations.equals("show")) {
            if (groups.containsKey(command.groupName)) {
                groups.get(command.groupName).forEach(System.out::println);
            } else {
                System.out.println("Unknown group");
            }
        }
        if (command.groupOperations.equals("create")) {
            groups.put(command.groupName, command.members);
        }
        if (command.groupOperations.equals("add")) {
            List<String> temp = groups.get(command.groupName);
            for (String s : command.members) {
                if (!temp.contains(s)) temp.add(s);
            }
            Collections.sort(temp);
            groups.replace(command.groupName, temp);
        }

        if (command.groupOperations.equals("remove")) {
            List<String> temp = groups.get(command.groupName);
            for (String s : command.members) {
                if (temp.contains(s)) temp.remove(s);
            }
            Collections.sort(temp);
            groups.replace(command.groupName, temp);
        }



    }

    public void purchase(Command command) {
        int tempSum = (int) (command.sum * 100D);
        //int membersSize = groups.get(command.personTwo).size();
        int membersSize = command.members.size();
        int ints = tempSum / membersSize;
        int remainder = tempSum - membersSize * ints;

        //for (String person : groups.get(command.personTwo)) {
        for (String person : command.members) {
            int sum = ints + (remainder > 0 ? 1 : 0);
            remainder--;
            double formatSum = (double) sum;
            formatSum = formatSum / 100D;
            bal.addOperation(command.getDate(), command.personOne, person, formatSum);
        }
    }

    public void borrow(Command command) {
        bal.addOperation(command.getDate(), command.getPersonTwo(), command.getPersonOne(), command.getSum());
        Account acc = new Account();
        acc.setDate(command.getDate());
        acc.setPersonOne(command.getPersonOne());
        acc.setPersonTwo(command.getPersonTwo());
        acc.setSum(command.getSum());
        opr.save(acc);
    }

    public void repay(Command command) {
        bal.addOperation(command.getDate(), command.getPersonOne(), command.getPersonTwo(), command.getSum());
    }

    public String balance(Command command) {
        boolean isClose = true;
        if (command.balance.equals("open")) isClose = false;
        return bal.calculateBalance(command.getDate(), isClose);

    }

    public void printHelp() {
        System.out.print("balance\n" +
                "borrow\n" +
                "exit\n" +
                "group\n" +
                "help\n" +
                "purchase\n" +
                "repay");
    }

}
