package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.CompensationIn;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    /**
     *
     * @param compensationIn A Pojo input of the values we have to insert.
     * @return on Success the updated Compensation object
     */
    @PostMapping("/compensation")
    public Compensation createCompensation(@RequestBody CompensationIn compensationIn) {
        LOG.debug("Received Compensation create request for [{}]", compensationIn.getEmployeeID());

        return employeeService.setCompensation(compensationIn.getEmployeeID(),compensationIn.getSalary(),compensationIn.getEdate());
    }


    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);

        return employeeService.read(id);
    }

    /**
     *
     * @param id EmployeeID to search for Compensation
     * @return if Present in database it returns Compensation object for the class.
     */
    @GetMapping("/getcompensation/{id}")
    public Compensation getCompensation(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);

        return employeeService.getCompensation(id);
    }

    /**
     *
     * @param id EmployeeID for whom Reporting Structure has to be made
     * @return Reporting structure object.
     */
    @GetMapping("/reporting/{id}")
    public ReportingStructure getReportingStructure(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);

        return employeeService.getRs(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }
}
