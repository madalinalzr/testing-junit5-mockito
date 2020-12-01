package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.fauxspring.WebDataBinder;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @InjectMocks
    OwnerController controller;

    @Mock
    OwnerService service;

    @Mock
    BindingResult bindingResult;

    @Mock
    Model model;

    @Captor
    ArgumentCaptor<String> captor;

    @BeforeEach
    void setUp() {
        given(service.findAllByLastNameLike(captor.capture())).willAnswer(innvocation ->
        {
            List<Owner> ownerList = new ArrayList<>();
            String name = innvocation.getArgument(0);
            if(name.equals("%Kent%")){
               ownerList.add(new Owner(1L, "Joe", "Kent"));
               return ownerList;
            }
            throw new RuntimeException("invalid argument!");
        });
    }

    @Test
    void processFindFromWildcardStringAnnotation(){
        //given
        Owner owner = new Owner(1L, "Joe", "Kent");
        //when
        String valueName = controller.processFindForm(owner, bindingResult, null);
        //then
        assertThat("%Kent%").isEqualToIgnoringCase(captor.getValue());
        assertEquals("redirect:/owners/1", valueName);
    }

    @Test
    void processCreationForm_whenHasErrror() {
        //given
        Owner owner = new Owner(1L, "Marin", "Stefan");
        given(bindingResult.hasErrors()).willReturn(true);

        //when
        String result = controller.processCreationForm(owner, bindingResult);

        //then
        assertEquals("owners/createOrUpdateOwnerForm", result);
        then(service).should(never()).save(owner);
    }

    @Test
    void processCreationForm_whenNoErrors() {
        //given
        Owner owner = new Owner(5L, "Marin", "Stefan");
        given(bindingResult.hasErrors()).willReturn(false);
        given(service.save(any(Owner.class))).willReturn(owner);

        //when
        String result = controller.processCreationForm(owner, bindingResult);

        //then
        assertThat(result).isEqualToIgnoringCase("redirect:/owners/5");
        then(service).should(times(1)).save(owner);
    }

    @Test
    void processFindFromWildcardString(){
        //given
        Owner owner = new Owner(1L, "Joe", "Kent");
        InOrder inOrder = inOrder(service, model);
        given(service.findAllByLastNameLike("%"+ owner.getLastName() + "%")).willReturn(Arrays.asList(owner, owner));
        //when
        String valueName = controller.processFindForm(owner, bindingResult, model);

        //then
        assertThat("%Kent%").isEqualToIgnoringCase(captor.getValue());

        inOrder.verify(service).findAllByLastNameLike(anyString());
        inOrder.verify(model).addAttribute(anyString(), anyList());

    }

}