package com.example.sqa_be_code_2023.controller;
import com.example.sqa_be_code_2023.controller.UserController;
import com.example.sqa_be_code_2023.model.entity.User;
import com.example.sqa_be_code_2023.model.dto.UserDto;
import com.example.sqa_be_code_2023.model.mapper.UserMapper;
import com.example.sqa_be_code_2023.respository.UserResponsitory;
import com.example.sqa_be_code_2023.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import com.fasterxml.jackson.core.type.TypeReference;
//20:
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)

public class UserControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    @Autowired
    private UserResponsitory userResponsitory;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;


    UserDto userDto1 = new UserDto(1, "user1", "password1", "position1", "fullName1", "address1", "phoneNumber1", "email1", null, "unit1");
    UserDto userDto2 = new UserDto(2, "user2", "password2", "position2", "fullName2", "address2", "phoneNumber2", "email2", null, "unit2");

    User user = new User(1, "user1", "password1", "position1", "fullName1", "address1", "phoneNumber1", "email1", null, "unit1");

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }
    // Test getAllUser()
    @Test
    public void testGetAllUser() throws Exception {
        List<UserDto> userDtos = Arrays.asList(userDto1, userDto2);

        Mockito.when(userService.getAllUser()).thenReturn(userDtos);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is("user1")))
                .andExpect(jsonPath("$[0].password", is("password1")))
                .andExpect(jsonPath("$[0].position", is("position1")))
                .andExpect(jsonPath("$[0].fullName", is("fullName1")))
                .andExpect(jsonPath("$[0].address", is("address1")))
                .andExpect(jsonPath("$[0].phoneNumber", is("phoneNumber1")))
                .andExpect(jsonPath("$[0].email", is("email1")))
                .andExpect(jsonPath("$[0].dob", is(nullValue())))
                .andExpect(jsonPath("$[0].unit", is("unit1")))

                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].username", is("user2")))
                .andExpect(jsonPath("$[1].password", is("password2")))
                .andExpect(jsonPath("$[1].position", is("position2")))
                .andExpect(jsonPath("$[1].fullName", is("fullName2")))
                .andExpect(jsonPath("$[1].address", is("address2")))
                .andExpect(jsonPath("$[1].phoneNumber", is("phoneNumber2")))
                .andExpect(jsonPath("$[1].email", is("email2")))
                .andExpect(jsonPath("$[1].dob", is(nullValue())))
                .andExpect(jsonPath("$[1].unit", is("unit2")));

        verify(userService, times(1)).getAllUser();
        verifyNoMoreInteractions(userService);
    }
    // Test AddOneUser()
    @Test
    public void createOneTest() throws Exception{
        init();

        Mockito.when(userService.addUser(user)).thenReturn(user);
        // chuyen Object sang Json
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        User response = objectMapper.readValue(responseString, new TypeReference<User>() {});


        //Kiểm tra response trả về là một đối tượng User:
        assertThat(response).isInstanceOf(User.class);
        //Kiểm tra response trả về có giá trị giống với đối tượng ban đầu:
        assertThat(response.getUsername()).isEqualTo(user.getUsername());
        assertThat(response.getPassword()).isEqualTo(user.getPassword());
        assertThat(response.getPosition()).isEqualTo(user.getPosition());
    }
// test AddUsers()
    @Test
    public void testAddUsers() throws Exception{
        init();
        List<User> userList = new ArrayList<>();
        userList.add(new User("username1", "password1", "position1", "fullName1", "address1",
                "phoneNumber1", "email1", null, "unit1"));
        userList.add(new User("username2", "password2", "position2", "fullName2", "address2",
                "phoneNumber2", "email2", null, "unit2"));

        Mockito.when(userService.addUsers(userList)).thenReturn(userList);
        // chuyen Object sang Json
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userList)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        List<User> response = objectMapper.readValue(responseString, new TypeReference<List<User>>() {});


        //Kiểm tra số lượng phần tử của đối tượng response có bằng với số lượng phần tử trong danh sách
        assertThat(response).hasSize(userList.size());
        //kiểm tra xem danh sách response có chứa đủ 2 phần tử đầu tiên của danh sách
        assertThat(response).contains(userList.get(0), userList.get(1));
    }

    // Test getUserById
    @Test
    public void testgetUserById() throws Exception{
        //UserDto userDto = new UserDto(1, "user1", "password1", "position1", "fullName1", "address1", "phoneNumber1", "email1", null, "unit1");
        int id = 1;
        Mockito.when(userService.getById(1)).thenReturn(userDto1);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userDto1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(userDto1.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value(userDto1.getPassword()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.position").value(userDto1.getPosition()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value(userDto1.getFullName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value(userDto1.getAddress()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(userDto1.getPhoneNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userDto1.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dob").value(userDto1.getDob()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.unit").value(userDto1.getUnit()))
                .andReturn();

        // Then
        String response = result.getResponse().getContentAsString();
        assertNotNull(response);
        assertEquals(response, objectMapper.writeValueAsString(userDto1));
        verify(userService, times(1)).getById(id);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void testuserLoginPass() {
        String validUsername = "huylm6";
        String validPassword = "huylm6";
        when(userService.getUserByUsernameAndPassword(validUsername, validPassword)).thenReturn(true);

        assertTrue(userController.userLogin(validUsername, validPassword));
    }

    @Test
    public void testuserLoginFail() {
        String invalidUsername = "foo";
        String invalidPassword = "bar";
        when(userService.getUserByUsernameAndPassword(anyString(), anyString())).thenReturn(false);

        assertFalse(userController.userLogin(invalidUsername, invalidPassword));
    }


    @Test
    public void testDeleteById(){
        int id = 1;
        String expected = "Xóa thành công bản ghi " + id + "!";
        Mockito.when(userService.deleteById(id)).thenReturn(expected);
        String result = userController.deleteUserById(id);
        assertEquals(expected, result);
        verify(userService, times(1)).deleteById(id);
        verifyNoMoreInteractions(userService);
    }



}
