package org.employee.controller;


import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.employee.model.Employee;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.*;


@RestController
public class EmployeeController {
    private Object response = null;
    private MessageChannel messageChannel ;
    public EmployeeController(@Qualifier("publish") MessageChannel messageChannel) {
        this.messageChannel = messageChannel;
    }
    @RequestMapping (method = RequestMethod.POST ,  value = "/employees/")
    public void add(@RequestBody Employee employee) {
        publish("POST","/employees/",employee);
    }
    @RequestMapping(method = RequestMethod.GET , value="/employees/{id}")
    public Object get(@PathVariable int id) {
        Employee employee = new Employee(id,"",-1);
        publish("GET","/employees/{id}",employee);
        return consume();
    }
    @RequestMapping(method = RequestMethod.PUT , value = "/employees/")
    public void update (@RequestBody Employee employee) {
        publish("PUT","/employees/",employee);
    }
    @RequestMapping(method = RequestMethod.DELETE, value = "/employees/{id}")
    public void delete (@PathVariable int id) {
        Employee employee = new Employee(id,"",-1);
        publish("DELETE","/employees/{id}",employee);
    }
    @RequestMapping(method = RequestMethod.GET , value = "/employees/")
    public Object findAll() {
        publish("GET","/employees/",new Employee());
        return consume();
    }

    @RequestMapping(method = RequestMethod.GET , value= "/")
    public String homePage () {
        String message  = "Hey welcome!! This page contains information regarding the employees' data handling apis\n1. For adding employee ==> POST /employees/ and provide the details in the body of the message in JSON.\n2. For getting employee details by id ==> GET /employees/{employeeId}\n3. For updating employee details ==> PUT /employees/ and provide the updated details in the body of the message in JSON.\n4. To delete employee ==> DELETE /employees/{employeeId}.\n5. To list all employee ==> GET /employees/";
        return message;
    }

    private void publish (String method , String url , Employee employee) {
        Message<Employee> employeeMesssage = MessageBuilder.withPayload(employee)
                .setHeader("method",method)
                .setHeader("url",url)
                .build();
        messageChannel.send(employeeMesssage);
    }

    public Object consume () {
        Object message = null;
        ConnectionFactory connectionFactory = new ConnectionFactory();
        try (Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            BlockingQueue<Object> response = new ArrayBlockingQueue<>(1);
            channel.basicConsume("rabbitMQEmployeeOutput",true,(consumerTag , delivery)->{

                Object res = delivery.getBody();
                response.offer(res);

            } , (CancelCallback) null);
            message = response.poll(5, TimeUnit.SECONDS);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return message;
        }
     }

}
