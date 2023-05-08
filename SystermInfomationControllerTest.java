package com.example.sqa_be_code_2023.controller;


import com.example.sqa_be_code_2023.model.entity.SystermInformation;
import com.example.sqa_be_code_2023.model.entity.Unit;
import com.example.sqa_be_code_2023.model.entity.User;
import com.example.sqa_be_code_2023.respository.SystermInformationResponsitory;
import com.example.sqa_be_code_2023.service.SystermInfomationService;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class SystermInfomationControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private SystermInfomationController systermInfomationController;

    @Mock
    private SystermInfomationService systermInfomationService;

    @Mock
    @Autowired
    private SystermInformationResponsitory systermInformationResponsitory;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(systermInfomationController).build();
        objectMapper = new ObjectMapper();
    }

    SystermInformation sys1 =  new SystermInformation(1,"19009068","Số 7 Tràng Thi, Q.Hoàn Kiếm, TP.Hà Nội",
            "Bảo hiểm xã hội Việt Nam","253/GP-TTĐT, cấp ngày 01/08/2017",null);
    User user = new User(2,"vietnb7","vietnb7","giam doc BHXH","nguyen ba viet",
            "ha noi","0963775146",
            "vietnb7@gmail.com",null,"bhxh");
    SystermInformation sys2 = new SystermInformation(2,"033283348","Số 7 Tràng Thi, Q.Hoàn Kiếm, TP.Hà Nội",
            "Bảo hiểm xã hội Việt Nam","253/GP-TTĐT, cấp ngày 01/08/2017",user);

    @Test
    public void testgetAllUnit() throws Exception {
        List<SystermInformation> sysList = new ArrayList<>();
        sysList.add(sys1);
        sysList.add(sys2);

        Mockito.when(systermInfomationService.getAllSystermInformation()).thenReturn(sysList);

        mockMvc.perform(get("/systermInfomation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].phoneNumber", is("19009068")))

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].phoneNumber", is("033283348")))
                .andExpect(jsonPath("$[1].user.id", is(2)));



        verify(systermInfomationService, times(1)).getAllSystermInformation();
        verifyNoMoreInteractions(systermInfomationService);
    }

    @Test
    public void testsystermInfomations() throws Exception{
        List<SystermInformation> sysList = new ArrayList<>();
        sysList.add(sys1);
        sysList.add(sys2);

        Mockito.when(systermInfomationService.addSystermInformations(sysList)).thenReturn(sysList);
        // chuyen Object sang Json
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/systermInfomations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sysList)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        List<SystermInformation> response = objectMapper.readValue(responseString, new TypeReference<List<SystermInformation>>() {});

        List <SystermInformation> response2 = new ArrayList<>();

        for(SystermInformation x : response){
            x.setAddress(new String(x.getAddress().getBytes("ISO-8859-1"), "UTF-8"));
            x.setAgency(new String(x.getAgency().getBytes("ISO-8859-1"), "UTF-8"));
            x.setLicenseNumber(new String(x.getLicenseNumber().getBytes("ISO-8859-1"), "UTF-8"));
            response2.add(x);
        }

        //Kiểm tra số lượng phần tử của đối tượng response có bằng với số lượng phần tử trong danh sách
        assertThat(response2).hasSize(sysList.size());
        //kiểm tra xem danh sách response có chứa đủ 2 phần tử đầu tiên của danh sách
        assertThat(response2).contains(sysList.get(0), sysList.get(1));
    }

    @Test
    public void testcreateOne() throws Exception{

        Mockito.when(systermInfomationService.addSystermInformation(sys1)).thenReturn(sys1);
        // chuyen Object sang Json
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/systermInfomation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sys1)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        SystermInformation response = objectMapper.readValue(responseString, new TypeReference<SystermInformation>() {});

        response.setAddress(new String(response.getAddress().getBytes("ISO-8859-1"), "UTF-8"));
        response.setAgency(new String(response.getAgency().getBytes("ISO-8859-1"), "UTF-8"));
        response.setLicenseNumber(new String(response.getLicenseNumber().getBytes("ISO-8859-1"), "UTF-8"));

        //Kiểm tra response trả về là một đối tượng SystemInformation:
        assertThat(response).isInstanceOf(SystermInformation.class);
        //Kiểm tra response trả về có giá trị giống với đối tượng ban đầu:


        assertThat(response.getId()).isEqualTo(sys1.getId());
        assertThat(response.getPhoneNumber()).isEqualTo(sys1.getPhoneNumber());
        assertThat(response.getAddress()).isEqualTo(sys1.getAddress());
        assertThat(response.getAgency()).isEqualTo(sys1.getAgency());
        assertThat(response.getLicenseNumber()).isEqualTo(sys1.getLicenseNumber());
        assertThat(response.getUser()).isEqualTo(sys1.getUser());

    }


}
