package com.example.sqa_be_code_2023.controller;


import com.example.sqa_be_code_2023.model.dto.UserDto;
import com.example.sqa_be_code_2023.model.entity.Unit;
import com.example.sqa_be_code_2023.model.entity.User;
import com.example.sqa_be_code_2023.respository.UnitResponsitory;
import com.example.sqa_be_code_2023.service.UnitService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UnitControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private UnitController unitController;

    @Mock
    private UnitService unitService;


    @Mock
    @Autowired
    private UnitResponsitory unitResponsitory;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(unitController).build();
        objectMapper = new ObjectMapper();
    }


    Unit unit1 = new Unit(1,"Công ty TNHH Phần mềm FPT");
    Unit unit2 = new Unit(2,"Công ty công nghệ thông tin CMC");

    @Test
    public void testgetAllUnit() throws Exception {
        List<Unit> unitList = new ArrayList<>();
        unitList.add(unit1);
        unitList.add(unit2);

        Mockito.when(unitService.getAllUnit()).thenReturn(unitList);

        mockMvc.perform(get("/unit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].unitName", is("Công ty TNHH Phần mềm FPT")))

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].unitName", is("Công ty công nghệ thông tin CMC")));



        verify(unitService, times(1)).getAllUnit();
        verifyNoMoreInteractions(unitService);
    }

    @Test
    public void testaddUnits() throws Exception{
        List<Unit> unitList2 = new ArrayList<>();
        unitList2.add(unit1);
        unitList2.add(unit2);

        Mockito.when(unitService.addUnits(unitList2)).thenReturn(unitList2);
        // chuyen Object sang Json
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(unitList2)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        List<Unit> response = objectMapper.readValue(responseString, new TypeReference<List<Unit>>() {});

        List <Unit> response2 = new ArrayList<>();
        for(Unit x : response){
            x.setUnitName(new String(x.getUnitName().getBytes("ISO-8859-1"), "UTF-8"));
            response2.add(x);
        }

        //Kiểm tra số lượng phần tử của đối tượng response có bằng với số lượng phần tử trong danh sách
        assertThat(response2).hasSize(unitList2.size());
        //kiểm tra xem danh sách response có chứa đủ 2 phần tử đầu tiên của danh sách
        assertThat(response2).contains(unitList2.get(0), unitList2.get(1));
    }



}
