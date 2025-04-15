package org.example.springbootgraphqlbatchloaderdemo.config;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootgraphqlbatchloaderdemo.model.Category;
import org.example.springbootgraphqlbatchloaderdemo.model.Department;
import org.example.springbootgraphqlbatchloaderdemo.model.Employee;
import org.example.springbootgraphqlbatchloaderdemo.model.Task;
import org.example.springbootgraphqlbatchloaderdemo.repository.CategoryRepository;
import org.example.springbootgraphqlbatchloaderdemo.repository.DepartmentRepository;
import org.example.springbootgraphqlbatchloaderdemo.repository.EmployeeRepository;
import org.example.springbootgraphqlbatchloaderdemo.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@Slf4j
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initData(
            DepartmentRepository departmentRepository,
            EmployeeRepository employeeRepository,
            CategoryRepository categoryRepository,
            TaskRepository taskRepository) {
        
        return args -> {
            log.info("Initializing sample data...");
            
            // Create departments
            Department engineering = departmentRepository.save(Department.builder()
                    .name("Engineering")
                    .location("Building A")
                    .build());
            
            Department marketing = departmentRepository.save(Department.builder()
                    .name("Marketing")
                    .location("Building B")
                    .build());
            
            Department hr = departmentRepository.save(Department.builder()
                    .name("Human Resources")
                    .location("Building C")
                    .build());
            
            // Create employees
            Employee johnDoe = employeeRepository.save(Employee.builder()
                    .name("John Doe")
                    .email("john.doe@example.com")
                    .position("Software Engineer")
                    .department(engineering)
                    .build());
            
            Employee janeSmith = employeeRepository.save(Employee.builder()
                    .name("Jane Smith")
                    .email("jane.smith@example.com")
                    .position("Senior Developer")
                    .department(engineering)
                    .build());
            
            Employee bobJohnson = employeeRepository.save(Employee.builder()
                    .name("Bob Johnson")
                    .email("bob.johnson@example.com")
                    .position("Marketing Specialist")
                    .department(marketing)
                    .build());
            
            Employee aliceBrown = employeeRepository.save(Employee.builder()
                    .name("Alice Brown")
                    .email("alice.brown@example.com")
                    .position("HR Manager")
                    .department(hr)
                    .build());
            
            Employee charlieWilson = employeeRepository.save(Employee.builder()
                    .name("Charlie Wilson")
                    .email("charlie.wilson@example.com")
                    .position("DevOps Engineer")
                    .department(engineering)
                    .build());
            
            // Create categories
            Category development = categoryRepository.save(Category.builder()
                    .name("Development")
                    .description("Software development tasks")
                    .build());
            
            Category bugFix = categoryRepository.save(Category.builder()
                    .name("Bug Fix")
                    .description("Bug fixing tasks")
                    .build());
            
            Category documentation = categoryRepository.save(Category.builder()
                    .name("Documentation")
                    .description("Documentation tasks")
                    .build());
            
            Category devOps = categoryRepository.save(Category.builder()
                    .name("DevOps")
                    .description("DevOps and infrastructure tasks")
                    .build());
            
            Category hrCategory = categoryRepository.save(Category.builder()
                    .name("HR")
                    .description("Human resources tasks")
                    .build());
            
            Category marketingCategory = categoryRepository.save(Category.builder()
                    .name("Marketing")
                    .description("Marketing tasks")
                    .build());
            
            // Create tasks
            taskRepository.save(Task.builder()
                    .title("Implement API")
                    .description("Implement REST API endpoints")
                    .status("IN_PROGRESS")
                    .dueDate(LocalDate.now().plusDays(5))
                    .employee(johnDoe)
                    .category(development)
                    .build());
            
            taskRepository.save(Task.builder()
                    .title("Fix bug #123")
                    .description("Fix critical bug in login flow")
                    .status("TODO")
                    .dueDate(LocalDate.now().plusDays(2))
                    .employee(johnDoe)
                    .category(bugFix)
                    .build());
            
            taskRepository.save(Task.builder()
                    .title("Code review")
                    .description("Review PR #456")
                    .status("DONE")
                    .dueDate(LocalDate.now().minusDays(1))
                    .employee(janeSmith)
                    .category(development)
                    .build());
            
            taskRepository.save(Task.builder()
                    .title("Create marketing campaign")
                    .description("Q3 product launch campaign")
                    .status("IN_PROGRESS")
                    .dueDate(LocalDate.now().plusDays(10))
                    .employee(bobJohnson)
                    .category(marketingCategory)
                    .build());
            
            taskRepository.save(Task.builder()
                    .title("Onboard new hire")
                    .description("Complete onboarding for new team member")
                    .status("TODO")
                    .dueDate(LocalDate.now().plusDays(3))
                    .employee(aliceBrown)
                    .category(hrCategory)
                    .build());
            
            taskRepository.save(Task.builder()
                    .title("Deploy to production")
                    .description("Deploy new version to production")
                    .status("TODO")
                    .dueDate(LocalDate.now().plusDays(1))
                    .employee(charlieWilson)
                    .category(devOps)
                    .build());
            
            taskRepository.save(Task.builder()
                    .title("Update documentation")
                    .description("Update API documentation")
                    .status("IN_PROGRESS")
                    .dueDate(LocalDate.now().plusDays(4))
                    .employee(janeSmith)
                    .category(documentation)
                    .build());
            
            taskRepository.save(Task.builder()
                    .title("Security audit")
                    .description("Perform security audit of the system")
                    .status("TODO")
                    .dueDate(LocalDate.now().plusDays(7))
                    .employee(charlieWilson)
                    .category(devOps)
                    .build());
            
            log.info("Sample data initialization complete!");
        };
    }
}
