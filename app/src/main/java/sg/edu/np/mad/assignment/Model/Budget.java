package sg.edu.np.mad.assignment.Model;

public class Budget {
    String budget,expense,tripname;

    public Budget(String budget, String expense, String tripname) {
        this.budget = budget;
        this.expense = expense;
        this.tripname = tripname;
    }

    public Budget() {
    }


    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }


    public String getTripname() {
        return tripname;
    }

    public void setTripname(String tripname) {
        this.tripname = tripname;
    }
}
