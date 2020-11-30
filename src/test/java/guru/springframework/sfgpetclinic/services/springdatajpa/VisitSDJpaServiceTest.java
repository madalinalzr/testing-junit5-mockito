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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
        //given
        Set<Visit> visits = new HashSet<>();
        given(repository.findAll()).willReturn(visits);
        //when
        Set<Visit> foundVisits = service.findAll();
        //then
        assertEquals(foundVisits, visits);
        then(repository).should().findAll();
    }

    @Test
    void findById() {
        //given
        Visit visit = new Visit();
        given(repository.findById(1L)).willReturn(Optional.of(visit));
        //when
        Visit foundVisit = service.findById(1L);
        //then
        assertEquals(foundVisit, visit);
        then(repository).should().findById(anyLong());
    }

    @Test
    void findById_notFound() {
        //given
        given(repository.findById(1L)).willReturn(Optional.empty());
        //when
        Visit foundVisit = service.findById(1L);
        //then
        assertEquals(null, foundVisit);
        then(repository).should().findById(anyLong());
    }

    @Test
    void save() {
        //given
        Visit visit = new Visit();
        given(repository.save(visit)).willReturn(visit);
        //when
        Visit savedVisit = service.save(visit);
        //then
        assertEquals(savedVisit, visit);
        then(repository).should().save(any(Visit.class));
    }

    @Test
    void delete() {
        //given
        Visit visit = new Visit();
        //when
        service.delete(visit);
        //then
        then(repository).should().delete(visit);
    }

    @Test
    void deleteById() {
        //given - none
        //when
        service.deleteById(1L);
        //then
        then(repository).should().deleteById(1L);
    }
}