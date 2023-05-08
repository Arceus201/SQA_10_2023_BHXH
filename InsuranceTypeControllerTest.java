package com.example.sqa_be_code_2023.controller;

import com.example.sqa_be_code_2023.model.entity.InsuranceType;
import com.example.sqa_be_code_2023.model.entity.SystermInformation;
import com.example.sqa_be_code_2023.model.entity.User;
import com.example.sqa_be_code_2023.respository.InsuranceTypeResponsitory;
import com.example.sqa_be_code_2023.service.InsuranceTypeService;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class InsuranceTypeControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private InsuranceTypeController insuranceTypeController;

    @Mock
    private InsuranceTypeService insuranceTypeService;

    @Mock
    @Autowired
    private InsuranceTypeResponsitory insuranceTypeResponsitory;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(insuranceTypeController).build();
        objectMapper = new ObjectMapper();
    }

    User u1 = new User(1,"hung","hung");
    User u2 = new User(2,"huy","huy");

    InsuranceType bhxh1 = new InsuranceType(1,"Bảo Hiểm Xã Hội Bắt Buộc - Việt Nam","2015-01-01",
            22.0,3.0,0.5,2.0,4.5,32.0,u1);
    InsuranceType bhxh2 = new InsuranceType(2,"Bảo Hiểm Xã Hội Bắt Buộc - Nước Ngoài","2015-01-01",
            22.0,3.0,0.5,0.0,4.5,30.0,u2);
    InsuranceType bhxh3 = new InsuranceType(3,"Bảo Hiểm Xã Hội Tự Nguyện","2015-01-01",
            0.0,0.0,0.0,0.0,0.0,22.0,u1);

    @Test
    public void testgetAllInsuranceType() throws Exception {
        List<InsuranceType> typeList = new ArrayList<>();
        typeList.add(bhxh1);
        typeList.add(bhxh2);
        typeList.add(bhxh3);

        Mockito.when(insuranceTypeService.getAllInsuranceType()).thenReturn(typeList);

        mockMvc.perform(get("/insuranceType"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].insuranceName", is("Bảo Hiểm Xã Hội Bắt Buộc - Việt Nam")))
                 .andExpect(jsonPath("$[0].total", is(32.0)))
                .andExpect(jsonPath("$[0].user.id", is(1)))

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].insuranceName", is("Bảo Hiểm Xã Hội Bắt Buộc - Nước Ngoài")))
                .andExpect(jsonPath("$[1].total", is(30.0)))
                .andExpect(jsonPath("$[1].user.id", is(2)))

                .andExpect(jsonPath("$[2].id", is(3)))
                .andExpect(jsonPath("$[2].insuranceName", is("Bảo Hiểm Xã Hội Tự Nguyện")))
                .andExpect(jsonPath("$[2].total", is(22.0)))
                .andExpect(jsonPath("$[2].user.id", is(1)));



        verify(insuranceTypeService, times(1)).getAllInsuranceType();
        verifyNoMoreInteractions(insuranceTypeService);
    }

    @Test
    public void testaddInsuranceTypes() throws Exception{
        List<InsuranceType> typeList = new ArrayList<>();
        typeList.add(bhxh1);
        typeList.add(bhxh2);
        typeList.add(bhxh3);

        Mockito.when(insuranceTypeService.addInsuranceTypes(typeList)).thenReturn(typeList);
        // chuyen Object sang Json
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/insuranceTypes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(typeList)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        List<InsuranceType> response = objectMapper.readValue(responseString, new TypeReference<List<InsuranceType>>() {});

        List <InsuranceType> response2 = new ArrayList<>();

        for(InsuranceType x : response){
            x.setInsuranceName(new String(x.getInsuranceName().getBytes("ISO-8859-1"), "UTF-8"));
            response2.add(x);
        }

        //Kiểm tra số lượng phần tử của đối tượng response có bằng với số lượng phần tử trong danh sách
        assertThat(response2).hasSize(typeList.size());
        //kiểm tra xem danh sách response có chứa đủ 2 phần tử đầu tiên của danh sách
        assertThat(response2).contains(typeList.get(0), typeList.get(1),typeList.get(2));
    }

    @Test
    public void testgetInsuranceTypesById() throws Exception{
        int id = 1;
        Mockito.when(insuranceTypeService.getInsuranceTypesById(id)).thenReturn(bhxh1);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/insuranceType/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.insuranceName", is("Bảo Hiểm Xã Hội Bắt Buộc - Việt Nam")))
                .andExpect(jsonPath("$.total", is(32.0)))
                .andExpect(jsonPath("$.user.id", is(1)))
                .andReturn();

        // Then
        String response = result.getResponse().getContentAsString();
        assertNotNull(response);
        String [] s = response.split(",");
        String response2 = "";
        int i=0;
        for(String x: s){
            if(i==1) response2 = response2 + new String(x.getBytes("ISO-8859-1"), "UTF-8")+",";
            else response2 = response2 + x + ",";
        i++;
        }
        response2 = response2.substring(0,response2.length()-1);
        //System.out.println(response2);

        assertEquals(response2, objectMapper.writeValueAsString(bhxh1));
        verify(insuranceTypeService, times(1)).getInsuranceTypesById(id);
        verifyNoMoreInteractions(insuranceTypeService);
    }

    @Test
    public void updateInsuranceTypeByIdPass() {
        int id = 1;
        InsuranceType bhxh1 = new InsuranceType(1,"Bảo Hiểm Xã Hội Bắt Buộc - Việt Nam","2015-01-01",
                22.0,3.0,0.5,5.0,4.5,35.0,u1);
        when(insuranceTypeService.updateInsuranceTypeById(id,bhxh1)).thenReturn(true);

        assertTrue(insuranceTypeController.updateInsuranceTypeById(id,bhxh1));
    }
    @Test
    public void updateInsuranceTypeByIdPail() {
        int id = 2;
        InsuranceType bhxh1 = new InsuranceType(1,"Bảo Hiểm Xã Hội Bắt Buộc - Việt Nam","2015-01-01",
                22.0,3.0,0.5,5.0,4.5,35.0,u1);

        when(insuranceTypeService.updateInsuranceTypeById(id,bhxh1)).thenReturn(false);

        assertFalse(insuranceTypeController.updateInsuranceTypeById(id,bhxh1));
    }


}



