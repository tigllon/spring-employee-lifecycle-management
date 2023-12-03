package org.employee.repository;

import org.employee.model.Employee;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represent the utility class for handling the CRUD operations.
 * It uses the HashMap as a database for storing details of the Employees.
 * Before each CRUD operation , the hashMap is deserialized to get the latest data.
 * After each CRUD operation the hashMap is deserialized for persistent of the data.
 */

@Repository
public class EmployeeRepository {
    /**
     * Used a database for storing the Employees details.
     */
    HashMap<Integer , Employee> hashMap = new HashMap<>();
    private String databasePath="/home/prakhar/ukg_project/rabbitmq/repository/src/main/resources/data.db";

    /**
     * Checks whether the database is initially empty or not.
     * If it is empty , then it just serializes the empty hashMap into the database.
     * So that the header is inserted into the file and InputStream does not generate
     * exception saying that the INVALID HEADER or EOFException
     */
    public EmployeeRepository() {
//        this.databasePath= databasePath;
        try (BufferedReader br = new BufferedReader(new FileReader(databasePath));) {
            if (br.readLine()==null) {
                FileOutputStream file = new FileOutputStream(databasePath);
                ObjectOutputStream output = new ObjectOutputStream(file);
                output.writeObject(hashMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Utility method for serializing the object after each CRUD operation so that
     * we have persistent data.
     */
    public void  serialize () {
        try (
                FileOutputStream file = new FileOutputStream(databasePath);
                ObjectOutputStream output = new ObjectOutputStream(file);
        ) {
            output.writeObject(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Utility method for deserializing the object before each CRUD operation so that
     * we have the latest data that we have stored yet.
     */
    public boolean deserialize () {
        try (
                FileInputStream file = new FileInputStream(databasePath);
                ObjectInputStream input = new ObjectInputStream(file);
                ) {
            Object object=  input.readObject();
            if (object instanceof  HashMap) hashMap = (HashMap<Integer, Employee>) object;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * Add employee into the hashMap.
     * If employee already exists then reports it else add the data into the hashMap
     */
    public void add (Employee employee) {
        update (employee);
    }

    /**
     * Get employee from the hashMap.
     * If id exists then give the details of employee if employee does exist not then report the same.
     * @param id    ID of Employee
     */
    public Employee get (int id) {
        deserialize();
        Employee employee = null;
        if (hashMap.containsKey(id)) {
            employee = hashMap.get(id);
        }
//        if (employee==null) throw new EmployeeNotFoundException(id);
        return employee;
    }

    /**
     * Update the employee entry in the database.
     * If employee exists then update the entry if employee does not exist then add the employee.
     */
    public void update (Employee employee) {
        int id = employee.getId();
        String name = employee.getName();
        int salary = employee.getSalary();
        deserialize();
        if (hashMap.containsKey(id)) {
            hashMap.put(id,new Employee(id,name,salary));
        }
        else {
            hashMap.put(id, new Employee(id,name,salary));
        }
        serialize();
    }

    /**
     * Delete the employee entry from the database.
     * If employee exists then delete it's entry if employee does not exist then report the same.
     * @param id    ID of Employee
     */
    public void delete (int id) {
        deserialize();
        if (hashMap.containsKey(id)){
            hashMap.remove(id);
            serialize();
        }

    }

    /**
     * Lists all the employee that are present in the database
     */
    public List<Employee> listAllEmployee() {
        deserialize();
        List<Employee> employeeList = new ArrayList<>();
        if (hashMap instanceof HashMap) {
            for (Map.Entry<Integer, Employee> entry : hashMap.entrySet()) {
                employeeList.add(entry.getValue());
            }
        }
//        if (employeeList.size()==0) throw new NoEmployeeFoundException();
        return employeeList;
    }
}
