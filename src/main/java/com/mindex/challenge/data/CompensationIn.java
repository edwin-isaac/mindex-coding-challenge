package com.mindex.challenge.data;

/**
 * A Entity class to receive Compensation Creation.
 */
public class CompensationIn {
    String employeeID;
    double salary;
    String edate;

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }
}
