package splitter;
import javax.persistence.*;

@Entity(name = "account")
@SequenceGenerator(name="seq", initialValue=1, allocationSize=1000)
public class Account {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
    @Column
    private long id;

    @Column
    public String personOne;

    @Column
    public String personTwo;

    @Column
    public String date;

    @Column
    double sum;

    public String getPersonTwo() {
        return personTwo;
    }

    public String getPersonOne() {
        return personOne;
    }

    public String getDate() {
        return date;
    }

    public long getId() {
        return id;
    }

    public double getSum() {
        return sum;
    }

    public void setPersonTwo(String personTwo) {
        this.personTwo = personTwo;
    }

    public void setPersonOne(String personOne) {
        this.personOne = personOne;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
