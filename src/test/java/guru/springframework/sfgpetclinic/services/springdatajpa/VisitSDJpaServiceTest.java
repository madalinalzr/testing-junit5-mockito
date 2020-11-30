package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @InjectMocks
    VisitSDJpaService service;

    @Mock
    VisitRepository repository;

    @Test
    void findAll() {
        Set<Visit> visits = new HashSet<>();
        when(repository.findAll()).thenReturn(visits);
        Set<Visit> foundVisits = service.findAll();
        assertEquals(foundVisits, visits);
        verify(repository).findAll();
    }

    @Test
    void findById() {
        Visit visit = new Visit();
        when(repository.findById(1L)).thenReturn(Optional.of(visit));
        Visit foundVisit = service.findById(1L);
        assertEquals(foundVisit, visit);
        verify(repository).findById(anyLong());
    }

    @Test
    void findById_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Visit foundVisit = service.findById(1L);
        assertEquals(null, foundVisit);
        verify(repository).findById(anyLong());
    }

    @Test
    void save() {
        Visit visit = new Visit();
        when(repository.save(visit)).thenReturn(visit);
        Visit savedVisit = service.save(visit);
        assertEquals(savedVisit, visit);
        verify(repository).save(any(Visit.class));
    }

    @Test
    void delete() {
        Visit visit = new Visit();
        service.delete(visit);
        verify(repository).delete(visit);
    }

    @Test
    void deleteById() {
        service.deleteById(1L);
        verify(repository).deleteById(1L);
    }
}