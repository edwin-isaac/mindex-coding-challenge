package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;


    /**
     * For manipulating Compensation table.
     */
    @Autowired
    private CompensationRepository compensationRepository;
    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }
    /**
    @param employeeId Employee ID for search
    Task 1 Service.
    Here we take first layer of supporting members and then I call getReporters() which returns
    a set of Members who reports directly and transitively to the given EmployeeID. I used set
    so that it won't have any duplications. The size of this set will be the employees reporting to the given employeeID
     */
    @Override
    public ReportingStructure getRs(String employeeId) {
        ReportingStructure reportingStructure = new ReportingStructure();
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        Set<String> reportingEmployeesId  = getReporters(employee.getDirectReports());

        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(reportingEmployeesId.size());
        return reportingStructure;
    }

    /**
    @param EmployeeId  Employee ID received,
    @param salary: Salary to set,
    @param effectiveDate: Date to set.
     @return Compensation of the employee or on failure, throws an Exception
     */
    @Override
    public Compensation setCompensation(String EmployeeId, double salary, String effectiveDate) {
        Compensation newCompensation = new Compensation();
        Employee employee = employeeRepository.findByEmployeeId(EmployeeId);

        if(employee!=null)
        {
            newCompensation.setEmployee(employee);
            newCompensation.setSalary(salary);
            try {
                newCompensation.setEffectiveDate( LocalDate.parse(effectiveDate));
            } catch (Exception e) {
                throw new RuntimeException("Invalid Date Format" + effectiveDate);
            }
            compensationRepository.save(newCompensation);
            return newCompensation;
        }
        throw  new RuntimeException(" Invalid EmployeeId "+ EmployeeId);

    }

    /**
    Returns the compensation for an EmployeeID, if it was created.
     */
    @Override
    public Compensation getCompensation(String EmployeeId) {
        Compensation compensation = compensationRepository.findCompensationByEmployee_EmployeeId(EmployeeId);
        if(compensation!=null)
        return compensation;
        else throw new RuntimeException("Employee Does not have Compensation Field Yet.");
    }

    /**
    Helper function for Task 1
    @param reportingEmployees: List of employees reporting to the parent employee.
    @return : Set of employeeIDs which report to the employees in the parameter
    This calls recursively passing the list of employees who report to the current employee.
    It returns a set and this set is union with all the other employees direct reports and then it is returned,
    giving each employee number of their reports and by recursion it gets unionized with previous set.
     */
    private Set<String> getReporters(List<Employee> reportingEmployees){
        Set<String> reportingEmployeesId = new HashSet<>(2);
       // List<Employee> directReports = employee.getDirectReports();
        if (reportingEmployees!=null)
        {for (Employee directReport: reportingEmployees) {
            Employee employee = employeeRepository.findByEmployeeId(directReport.getEmployeeId());
            reportingEmployeesId.add(directReport.getEmployeeId());
            reportingEmployeesId.addAll(getReporters(employee.getDirectReports()));
        }}
        return  reportingEmployeesId;

    }
}
