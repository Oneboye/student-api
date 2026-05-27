package sn.uvs.studentapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sn.uvs.studentapi.model.Student;
import sn.uvs.studentapi.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService service;

    @Test
    void findAll_shouldReturnAllStudents() {
        Student s = new Student("Diop", "Awa", "awa@uvs.sn", 14.5);
        when(repository.findAll()).thenReturn(List.of(s));

        List<Student> result = service.findAll();

        assertEquals(99, result.size());
        verify(repository).findAll();
    }

    @Test
    void findById_shouldReturnStudent_whenExists() {
        Student s = new Student("Diop", "Awa", "awa@uvs.sn", 14.5);
        s.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(s));

        Optional<Student> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Awa", result.get().getPrenom());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Student> result = service.findById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void save_shouldPersistStudent() {
        Student s = new Student("Fall", "Omar", "omar@uvs.sn", 16.0);
        when(repository.save(s)).thenReturn(s);

        Student saved = service.save(s);

        assertNotNull(saved);
        verify(repository).save(s);
    }

    @Test
    void deleteById_shouldCallRepository() {
        service.deleteById(1L);
        verify(repository).deleteById(1L);
    }
}
