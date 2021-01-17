package splitter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {
    String date;
    String personOne;
    String personTwo;
    String operations;
    String error;
    String balance = "close";
    String groupOperations;
    String groupName;
    List<String> members;
    double sum;
    boolean isOk;
    Map<String, List<String>> groups;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getError() {
        return error;
    }

    public void setPersonOne(String personOne) {
        this.personOne = personOne;
    }

    public String getPersonOne() {
        return personOne;
    }

    public void setPersonTwo(String personTwo) {
        this.personTwo = personTwo;
    }

    public String getPersonTwo() {
        return personTwo;
    }

    public void setOperations(String operations) {
        this.operations = operations;
    }

    public String getOperations() {
        return operations;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getSum() {
        return sum;
    }

    public void checkCommand(String command, Map<String, List<String>> groups) throws commandException {
        this.groups = groups;
        if (command.matches("((19|20)\\d\\d[- \\/.](0[1-9]|1[012])[- \\/.](0[1-9]|[12][0-9]|3[01]))* *" +
                "(?i)(borrow|repay|balance|group|purchase|secretSanta).*")) {

            checkCommandArgs(command);
        } else if (command.matches("(help|exit)")) {
            this.operations = command;
            isOk = true;
        } else {
                isOk = false;
                error = "unknown command.";
                throw new commandException("unknown command.");
        }
    }

    public void checkCommandArgs(String command) throws commandException {
        if (command.matches("((19|20)\\d\\d[- \\/.](0[1-9]|1[012])[- \\/.](0[1-9]|[12][0-9]|3[01]))* *(?i)(borrow|repay) [\\w]+ [\\w]+ [0-9.]+")) {
            parseCommand(command);
            isOk = true;
        } else if (command.matches("((19|20)\\d\\d[- \\/.](0[1-9]|1[012])[- \\/.](0[1-9]|[12][0-9]|3[01]))* *(?i)(balance) *(?i)(open|close)*")) {

            parseBalance(command);
            isOk = true;
        } else if (command.matches("group (create|show|add|remove) [A-Z]+ *(\\([\\w,+\\-\\s]+\\))*")) {
            parseGroup(command);
            isOk = true;
        } else if (command.matches("secretSanta *(\\([\\w,+\\-\\s]+\\))*")) {
            secretSanta(command);
            isOk = true;
        } else if (command.matches("((19|20)\\d\\d[- \\/.](0[1-9]|1[012])[- \\/.](0[1-9]|[12][0-9]|3[01]))* *purchase [\\w]+ [\\w]+ [0-9.]+ \\([\\w,+\\-\\s]+\\)")) {
            parsePurchase(command);
            isOk = true;
        } else {
            isOk = false;
            error = "Invalid command argument";
            throw new commandException("Illegal command arguments");
        }
    }

    public void secretSanta(String command) {
        this.operations = command.split(" ")[0].toLowerCase();
        this.groupName = command.split(" ")[1];
    }

    public List<String> parseTeam(String input) {
        List<String> res = new ArrayList<>();
        List<String> toAdd = new ArrayList<>();
        List<String> toRemove = new ArrayList<>();
        input = input.replace("(", "").replace(")", "");
        String[] array = input.split(", ");
        for (String s : array) {
            if (s.matches("[A-Z]+")) {
                for (String s2 : groups.get(s)) {
                    toAdd.add(s2);
                }
            } else if (s.matches("-[A-Z]+")) {
                for (String s3 : groups.get(s.replace("-", ""))) {
                    toRemove.add(s3);
                }
            } else if (s.startsWith("-")) {
                toRemove.add(s.replace("-", ""));
            } else {
                toAdd.add(s.replace("+", ""));
            }
        }
        for (String add : toAdd) {
            if (!res.contains(add)) res.add(add);
        }
        for (String rem : toRemove) {
            if (res.contains(rem)) res.remove(rem);
        }
        return res;
    }

    public void parsePurchase(String command) {
        Matcher dateMatcher = Pattern.compile("(19|20)\\d\\d[- \\/.](0[1-9]|1[012])[- \\/.](0[1-9]|[12][0-9]|3[01])").matcher(command);
        if (dateMatcher.find()) {
            this.date = command.split(" ")[0];
            this.operations = command.split(" ")[1].toLowerCase();
            this.personOne = command.split(" ")[2];
            this.personTwo = command.split(" ")[5].replace("(","").replace(")","");
            Matcher groupMatcher = Pattern.compile("\\([\\w+\\-,\\s]+\\)").matcher(command);
            if (groupMatcher.find()) {
                members = new ArrayList<>();
                String temp = groupMatcher.group();
                temp = temp.substring(1, temp.length() - 1);
                //String[] array = temp.split(", ");
                //members.addAll(Arrays.asList(array));
                members = parseTeam(temp);
                Collections.sort(members);
            }
            this.sum = Double.parseDouble(command.split(" ")[4]);
        } else {
            this.date = currentDate();
            this.operations = command.split(" ")[0].toLowerCase();
            this.personOne = command.split(" ")[1];
            this.personTwo = command.split(" ")[4].replace("(","").replace(")","");
            Matcher groupMatcher = Pattern.compile("\\([\\w+\\-,\\s]+\\)").matcher(command);
            if (groupMatcher.find()) {
                members = new ArrayList<>();
                String temp = groupMatcher.group();
                temp = temp.substring(1, temp.length() - 1);
                //String[] array = temp.split(", ");
                //members.addAll(Arrays.asList(array));
                members = parseTeam(temp);
                Collections.sort(members);
            }
            this.sum = Double.parseDouble(command.split(" ")[3]);
        }
    }

    public void parseGroup(String command) {
        this.operations = command.split(" ")[0].toLowerCase();
        this.groupOperations = command.split(" ")[1].toLowerCase();
        this.groupName = command.split(" ")[2];
        Matcher groupMatcher = Pattern.compile("\\([\\w+\\-,\\s]+\\)").matcher(command);
        if (groupMatcher.find()) {
            members = new ArrayList<>();
            String temp = groupMatcher.group();
            temp = temp.substring(1, temp.length() - 1);
            //String[] array = temp.split(", ");
            //members.addAll(Arrays.asList(array));
            members = parseTeam(temp);
            Collections.sort(members);
        }
    }

    public void parseCommand(String command) {
        Matcher dateMatcher = Pattern.compile("(19|20)\\d\\d[- \\/.](0[1-9]|1[012])[- \\/.](0[1-9]|[12][0-9]|3[01])").matcher(command);
        if (dateMatcher.find()) {
            this.date = command.split(" ")[0];
            this.operations = command.split(" ")[1].toLowerCase();
            this.personOne = command.split(" ")[2];
            this.personTwo = command.split(" ")[3];
            this.sum = Double.parseDouble(command.split(" ")[4]);
        } else {
            this.date = currentDate();
            this.operations = command.split(" ")[0].toLowerCase();
            this.personOne = command.split(" ")[1];
            this.personTwo = command.split(" ")[2];
            this.sum = Double.parseDouble(command.split(" ")[3]);
        }
    }

    public void parseBalance(String command) {
        Matcher dateMatcher = Pattern.compile("(19|20)\\d\\d[- \\/.](0[1-9]|1[012])[- \\/.](0[1-9]|[12][0-9]|3[01])").matcher(command);
        if (dateMatcher.find()) {
            this.date = command.split(" ")[0];
            this.operations = command.split(" ")[1].toLowerCase();
            if (command.split(" ").length > 2)
            this.balance = command.split(" ")[2];
        } else {
            this.date = currentDate();
            this.operations = command.split(" ")[0].toLowerCase();
            if (command.split(" ").length > 2)
                this.balance = command.split(" ")[2];
        }
    }

    public String currentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

}

class commandException extends Exception {
    public commandException(String errorMessage) {
        super(errorMessage);
    }
}
