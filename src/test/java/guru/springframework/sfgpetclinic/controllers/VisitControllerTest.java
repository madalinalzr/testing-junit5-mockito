package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;
import guru.springframework.sfgpetclinic.services.map.PetMapService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VisitControllerTest {

    @InjectMocks
    VisitController visitController;

    @Mock
    VisitService visitService;

    @Spy
    PetMapService petService;


    @Test
    void loadPetWithVisit() {
        //given
        Long petId = 1L;
        Map<String, Object> model = new HashMap<>();
        Pet pet = new Pet(petId);

        petService.save(pet);
        given(petService.findById(anyLong())).willCallRealMethod();
        //when
        Visit visitResult = visitController.loadPetWithVisit(petId, model);
        //then
        assertNotNull(visitResult);
        assertThat(visitResult.getPet().getId()).isEqualTo(1L);
    }
}