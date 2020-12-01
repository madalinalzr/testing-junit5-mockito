package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @InjectMocks
    SpecialitySDJpaService specialitySDJpaService;

    @Mock //(lenient = true)
    SpecialtyRepository specialtyRepository;

    @Test
    void deleteByIdTest() {
        //given - none

        //when
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);
        //then
        then(specialtyRepository).should(times(2)).deleteById(1L);
    }

    @Test
    void deleteByIdTest_never() {
        //given - none

        //when
        specialitySDJpaService.deleteById(1L);
        specialitySDJpaService.deleteById(1L);

        //then
        then(specialtyRepository).should(timeout(100).atLeastOnce()).deleteById(1L);
        then(specialtyRepository).should(never()).deleteById(5L);
    }

    @Test
    void deleteTest(){
        //given
        Speciality speciality = new Speciality();
        //when
        specialitySDJpaService.delete(speciality);
        //then
        then(specialtyRepository).should().delete(any(Speciality.class));
    }

    @Test
    void findById(){
        //given
        Speciality speciality = new Speciality();
        given(specialtyRepository.findById(1L)).willReturn(Optional.of(speciality));
        //when
        Speciality foundSpeciality = specialitySDJpaService.findById(1L);
        //then
        assertNotNull(foundSpeciality);
        then(specialtyRepository).should(timeout(100)).findById(1L);
        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteByObjectTest(){
        Speciality speciality = new Speciality();
        specialitySDJpaService.delete(speciality);
        verify(specialtyRepository).delete(any(Speciality.class));
    }

    @Test
    void testDoThrow(){
        doThrow(new RuntimeException("boom"))
                .when(specialtyRepository)
                .deleteById(1L);

        assertThrows(RuntimeException.class, () -> specialtyRepository.deleteById(1L));
        verify(specialtyRepository).deleteById(1L);
    }

    @Test
    void testDoThrowBDD(){
        given(specialtyRepository.findById(1L)).willThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> specialtyRepository.findById(1L));
        then(specialtyRepository).should().findById(1L);
    }

    @Test
    void testDoThrowBDD_voidMethods(){
        willThrow(new RuntimeException("boom")).given(specialtyRepository).delete(any(Speciality.class));

        assertThrows(RuntimeException.class, () -> specialtyRepository.delete(new Speciality()));
        then(specialtyRepository).should().findById(1L);
    }

    @Test
    void testSavedLambda(){
        //given
        final String MATCH_ME = "MATCH_ME";
        Speciality speciality = new Speciality();
        speciality.setDescription(MATCH_ME);

        Speciality savedSpeciality = new Speciality();
        savedSpeciality.setId(1L);

        //will return mock only if the arg will match
        given(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).willReturn(savedSpeciality);

        //when
        Speciality returnedSpeciality = specialtyRepository.save(speciality);

        //then
        assertThat((returnedSpeciality.getId()).equals(1L));
    }

    @Test
    void testSavedLambdaNoMatch(){
        //given
        final String MATCH_ME = "MATCH_ME";
        Speciality speciality = new Speciality();
        speciality.setDescription("Not a match");

        Speciality savedSpeciality = new Speciality();
        savedSpeciality.setId(1L);

        //will return mock only if the arg will match
        given(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).willReturn(savedSpeciality);

        //when
        Speciality returnedSpeciality = specialtyRepository.save(speciality);

        //then
        assertNull(returnedSpeciality);
    }
}