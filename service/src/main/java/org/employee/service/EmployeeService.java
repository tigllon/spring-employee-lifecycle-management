package org.employee.service;

import org.employee.model.Employee;
import org.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

@Service
@Transactional
public class EmployeeService {
    private MessageChannel messageChannel  ;
    private EmployeeRepository employeeRepository ;
    public EmployeeService(EmployeeRepository employeeRepository, @Qualifier("publish") MessageChannel messageChannel) {
        this.employeeRepository = employeeRepository;
        this.messageChannel = messageChannel ;
    }

    public void add (Employee employee) {
        employeeRepository.add(employee);
    }
    public Employee get (int id) {
        return employeeRepository.get(id);
    }
    public void update (Employee employee) {
        employeeRepository.update(employee);
    }
    public void delete (int id) {
        employeeRepository.delete(id);
    }
    public List<Employee> findAll() {
        return employeeRepository.listAllEmployee();
    }

    @ServiceActivator(inputChannel = "queue")
    public void consume (@Header("method") String method , @Header("url") String url , @Payload Employee employee) {
        if (method.equals("DELETE") && url.equals("/employees/{id}")) {
            employeeRepository.delete(employee.getId());
        }
        else if ((method.equals("PUT") || method.equals("POST")) && url.equals("/employees/")) {
            employeeRepository.add(employee);
        }
        else if (method.equals("GET") && url.equals("/employees/{id}")) {
            Employee employee1 = employeeRepository.get(employee.getId());
            Message<Serializable> message = MessageBuilder.withPayload((employee1 !=null) ? employee1 : ("Employee with id " + employee.getId()+" is not found"))
                    .build();
            messageChannel.send(message);
        }
        else if (method.equals("GET") && url.equals("/employees/")) {
            ArrayList<Employee> employeeList = new ArrayList<>(employeeRepository.listAllEmployee());
            Message<Serializable> message = MessageBuilder.withPayload((employeeList!=null) ?employeeList:"The database is completely emppty")
                    .build();
            messageChannel.send(message);
        }
        else {
            Message<String> message = MessageBuilder.withPayload("Invalid operation !! go with / to see more detaiils").build();
            System.err.println(message);
        }

    }
}
