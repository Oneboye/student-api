package sn.uvs.studentapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sn.uvs.studentapi.model.Student;
import sn.uvs.studentapi.service.StudentService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_shouldReturnStudentList() throws Exception {
        Student s = new Student("Sall", "Fatou", "fatou@uvs.sn", 15.0);
        when(service.findAll()).thenReturn(List.of(s));

        mockMvc.perform(get("/api/students"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].prenom").value("Fatou"));
    }

    @Test
    void getById_shouldReturnStudent_whenExists() throws Exception {
        Student s = new Student("Sall", "Fatou", "fatou@uvs.sn", 15.0);
        s.setId(1L);
        when(service.findById(1L)).thenReturn(Optional.of(s));

        mockMvc.perform(get("/api/students/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nom").value("Sall"));
    }

    @Test
    void getById_shouldReturn404_whenNotExists() throws Exception {
        when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/students/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturnCreatedStudent() throws Exception {
        Student s = new Student("Ba", "Ibrahima", "ibra@uvs.sn", 13.0);
        when(service.save(any(Student.class))).thenReturn(s);

        mockMvc.perform(post("/api/students")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(s)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.prenom").value("Ibrahima"));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
               .andExpect(status().isNoContent());
    }
}
