package splitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpService {

    @Autowired
    private OpRepository opRepository;

    public void saveAccount(String date, String pOne, String pTwo, Double sum) {
        Account acc = new Account();
        acc.setSum(sum);
        acc.setPersonTwo(pTwo);
        acc.setPersonOne(pOne);
        acc.setSum(sum);
        System.out.println(date + " " + pOne + " " + pTwo + " " + sum);
        System.out.println("start");
        try {
            opRepository.save(acc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //opRepository.save(acc);
        System.out.println("finish");
    }

    public Iterable<Account> getAll() {
        System.out.println(opRepository);
        return opRepository.findAll();
    }

}