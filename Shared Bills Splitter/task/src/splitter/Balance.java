package splitter;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Balance {

    List<Operation> operations = new ArrayList<>();
    DecimalFormat df2 = new DecimalFormat("#,##0.00");

    public void addOperation(String date, String pOne, String pTwo, Double sum) {
        Operation op = new Operation(date, pOne, pTwo, sum);
        operations.add(op);
    }

    public String calculateBalance(String input, boolean isClose) {
        String result = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate date = LocalDate.parse(input, formatter);
        if (!isClose) {
            date = date.minusMonths(1);
            date = date.withDayOfMonth(date.lengthOfMonth());
        }
        boolean isNoRepayments = true;
        List<String> persons = new ArrayList<>();
        for (Operation op : operations) {
            if (!persons.contains(op.personOne)) persons.add(op.personOne);
            if (!persons.contains(op.personTwo)) persons.add(op.personTwo);
        }
        Collections.sort(persons);
        for (String pOne : persons) {
            for (String pTwo : persons) {
                if (pTwo.equals(pOne)) continue;
                double balance = balance(date, pOne, pTwo);
                if (balance < 0) {
                    isNoRepayments = false;
                    result += pOne + " owes " + pTwo + " " + df2.format(Math.abs(balance)) + "\n";
                }
            }
        }
        if (isNoRepayments) result = "No repayments need" + "\n";
        return result;
    }

    public double balance(LocalDate date, String personOne, String personTwo) {
        List<Operation> ops = operationsToDate(date);
        double balance = 0D;
        for (Operation op : ops) {
            if (op.personOne.equals(personOne) && op.personTwo.equals(personTwo)) {
                balance += op.sum;
                balance = round(balance, 2);
            }
            if (op.personOne.equals(personTwo) && op.personTwo.equals(personOne)) {
                balance -= op.sum;
                balance = round(balance, 2);
            }
        }
        return balance;
    }

    public List<Operation> operationsToDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        List<Operation> result = new ArrayList<>();
        for (Operation op : operations) {
          //  System.out.println(op.date);
            LocalDate localDate = LocalDate.parse(op.date, formatter);
            if (!localDate.isAfter(date)) {
                result.add(op);
            }
        }
        return result;
    }

    public double round(double value, int places) {
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
