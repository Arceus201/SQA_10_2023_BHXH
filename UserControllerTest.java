package com.example.sqa_be_code_2023.controller;
import com.example.sqa_be_code_2023.model.entity.User;
import com.example.sqa_be_code_2023.model.dto.UserDto;
import com.example.sqa_be_code_2023.respository.UserResponsitory;
import com.example.sqa_be_code_2023.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional

public class UserControllerTest {

    @Autowired
    private UserResponsitory userResponsitory;

    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;


    UserDto userDto1 = new UserDto( "user1", "password1", "position1", "fullName1", "address1", "phoneNumber1", "email1", null, "unit1");
    UserDto userDto2 = new UserDto("user2", "password2", "position2", "fullName2", "address2", "phoneNumber2", "email2", null, "unit2");


    User user1 = new User("user1", "password1", "position1", "fullName1", "address1", "phoneNumber1", "email1", null, "unit1");
    User user2 = new User("user2", "password2", "position2", "fullName2", "address2", "phoneNumber2", "email2", null, "unit2");

    User user3 = new User(null,null, "position3", "fullName3", "address3", "phoneNumber3", "email3", null, "unit3");
    User user4 = new User(null,null, "position3", "fullName3", "address3", "phoneNumber3", "email3", null, "unit3");

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    // Test getAllUser() + addOneUser
    @Test
    @Rollback
    public void testGetAllUser1() throws Exception {
        // kiểm tra có bao nhiêu phần tử User trước khi test : expected:4
        System.out.println("Trước insert:" + userController.getAllUser().size());
        int before = userController.getAllUser().size();

        //test add OneUser
        userController.createOne(user1);

        Iterable<UserDto> list = userController.getAllUser();
        // kiểm tra có bao nhiêu phần tử User trước sau khi addOne : expected:5
        System.out.println("sau insert:" + userController.getAllUser().size());
        int after = userController.getAllUser().size();


        //  kiểm tra xem đối tượng test được thêm vào có được trả về hay không
        int size = 0;
        for (UserDto userDto : list) {
            size=size+1;
            if (size==5) {
                Assert.assertEquals(userDto.getUsername(), userDto1.getUsername());
                Assert.assertEquals(userDto.getPassword(), userDto1.getPassword());
            }

        }
        // kiểm tra kích thước danh sách trả về
        Assert.assertTrue(before + 1 == after);

    }

    // Test getAllUser() + addOneUser + Fail
    @Test
    @Rollback
    public void testGetAllUserFail1() throws Exception {
        // kiểm tra có bao nhiêu phần tử User trước khi test : expected:4
        System.out.println("Trước insert:" + userController.getAllUser().size());
        int before = userController.getAllUser().size();

        //test add OneUser
        userController.createOne(user3);

        // kiểm tra có bao nhiêu phần tử User trước sau khi addOne : expected:4
        System.out.println("sau insert:" + userController.getAllUser().size());
        int after = userController.getAllUser().size();

        // kiểm tra kích thước danh sách trả về
        Assert.assertTrue(before  == after);

    }



    // Test getAllUser() + addListUser
    @Test
    @Rollback
    public void testGetAllUser2() throws Exception {
        // kiểm tra có bao nhiêu phần tử User trước khi test : expected:4
        System.out.println("Trước insert:" + userController.getAllUser().size());

        int before = userController.getAllUser().size();

        //test add ListUser
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userController.addUsers(userList);


        Iterable<UserDto> list = userController.getAllUser();
        // kiểm tra có bao nhiêu phần tử User trước sau khi addOne : expected:6
        System.out.println("sau insert:" + userController.getAllUser().size());
        int after = userController.getAllUser().size();


        //  kiểm tra xem đối tượng test được thêm vào có được trả về hay không
        int size = 0;
        for (UserDto userDto : list) {
            size=size+1;
            if (size==5) {
                Assert.assertEquals(userDto.getUsername(), userDto1.getUsername());
                Assert.assertEquals(userDto.getPassword(), userDto1.getPassword());
            }else if(size==6){
                Assert.assertEquals(userDto.getUsername(), userDto2.getUsername());
                Assert.assertEquals(userDto.getPassword(), userDto2.getPassword());
            }
        }
        // kiểm tra kích thước danh sách trả về
        Assert.assertTrue(before + 2 == after);
    }

    // Test getAllUser() + addListUser + Fail
    @Test
    @Rollback
    public void testGetAllUserFail2() throws Exception {
        // kiểm tra có bao nhiêu phần tử User trước khi test : expected:4
        System.out.println("Trước insert:" + userController.getAllUser().size());

        int before = userController.getAllUser().size();

        //test add ListUser
        List<User> userList = new ArrayList<>();
        userList.add(user3);
        userList.add(user4);
        userController.addUsers(userList);


        // kiểm tra có bao nhiêu phần tử User trước sau khi addOne : expected:4
        System.out.println("sau insert:" + userController.getAllUser().size());
        int after = userController.getAllUser().size();



        // kiểm tra kích thước danh sách trả về
        Assert.assertTrue(before  == after);
    }



    // Test getUserById Pass
    @Test
    @Rollback
    public void testgetUserByIdPass() throws Exception{
        // lấy ra ALL user
        Iterable<UserDto> list = userController.getAllUser();

        //lay ra UserDto1
        UserDto userDto1 = new UserDto();
        for(UserDto x: list){
            userDto1 = x;
            break;
        }


        // Lấy ra user có id = 1
        int id = 1;
        UserDto userDto = userController.getUserById(id);

        // Kiểm tra có đúng là UserDto1 không
        Assert.assertEquals(userDto.getUsername(), userDto1.getUsername());
        Assert.assertEquals(userDto.getPassword(), userDto1.getPassword());

    }

    // Test getUserById fail
    @Test
    @Rollback
    public void testgetUserByIdFail() throws Exception{
        // lấy ra ALL user
        Iterable<UserDto> list = userController.getAllUser();

        //kiểm tra phần tử cuối cùng có id  = ? =>4
        int end = userController.getAllUser().size();

        for(UserDto x: list){
            userDto1 = x;
            break;
        }

        // Lấy ra user có id = phần tử cuối+1
        int id = end+1;
        UserDto userDto = userController.getUserById(id);

    }


    @Test
    @Rollback
    public void testuserLoginPass() {
        // lấy ra ALL user
        Iterable<UserDto> list = userController.getAllUser();

        //lay ra UserDto1
        UserDto userDto1 = new UserDto();
        for(UserDto x: list){
            userDto1 = x;
            break;
        }

        boolean status = userController.userLogin(userDto1.getUsername(),userDto1.getPassword());

        Assert.assertEquals(status, true);
    }

    @Test
    @Rollback
    public void testuserLoginFail() {
        // lấy ra ALL user
        Iterable<UserDto> list = userController.getAllUser();

        //lay ra UserDto1
        UserDto userDto1 = new UserDto();
        for(UserDto x: list){
            userDto1 = x;
            break;
        }

        boolean status = userController.userLogin(userDto1.getUsername()+"abcd",userDto1.getPassword());

        Assert.assertEquals(status, false);
    }


    @Test
    @Rollback
    public void testDeleteByIdPass(){
        // lấy ra ALL user
        Iterable<UserDto> list = userController.getAllUser();
        int size = userController.getAllUser().size();

        //lay ra UserDto1
        UserDto userDto1 = new UserDto();
        for(UserDto x: list){
            userDto1 = x;
            break;
        }
        //test login
        boolean status = userController.userLogin(userDto1.getUsername(),userDto1.getPassword());

        Assert.assertEquals(status, true);

        //xoa userdto1
        userController.deleteUserById(userDto1.getId());

        //kiem tra lai xem co dang nhap bang user1 nua khong
        boolean status2 = userController.userLogin(userDto1.getUsername(),userDto1.getPassword());

        Assert.assertEquals(status2, false);
    }


}
