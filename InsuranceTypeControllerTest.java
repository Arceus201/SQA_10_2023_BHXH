package com.example.sqa_be_code_2023.controller;

import com.example.sqa_be_code_2023.model.entity.InsuranceType;
import com.example.sqa_be_code_2023.model.entity.User;
import com.example.sqa_be_code_2023.respository.InsuranceTypeResponsitory;
import com.example.sqa_be_code_2023.service.InsuranceTypeService;
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

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class InsuranceTypeControllerTest {

    @Autowired
    private InsuranceTypeController insuranceTypeController;

    @Autowired
    private InsuranceTypeService insuranceTypeService;

    @Autowired
    private InsuranceTypeResponsitory insuranceTypeResponsitory;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    User u1 = new User(1,"hung","hung");
    User u2 = new User(2,"huy","huy");

    InsuranceType bhxh1 = new InsuranceType("Bảo Hiểm Xã Hội Bắt Buộc - Việt Nam","2015-01-01",
            22.0,3.0,0.5,2.0,4.5,32.0,u1);
    InsuranceType bhxh2 = new InsuranceType("Bảo Hiểm Xã Hội Bắt Buộc - Nước Ngoài","2015-01-01",
            22.0,3.0,0.5,0.0,4.5,30.0,u2);
    InsuranceType bhxh3 = new InsuranceType("Bảo Hiểm Xã Hội Tự Nguyện","2015-01-01",
            0.0,0.0,0.0,0.0,0.0,22.0,u1);

    // test add + getall

    @Test
    @Rollback
    public void testgetAllInsuranceType() throws Exception {
        // kiểm tra có bao nhiêu phần tử InsuranceType trước khi test : expected:3
        System.out.println("Trước insert:" + insuranceTypeController.getAllInsuranceType().size());
        int before = insuranceTypeController.getAllInsuranceType().size();


        List<InsuranceType> typeList = new ArrayList<>();
        typeList.add(bhxh1);
        typeList.add(bhxh2);
        typeList.add(bhxh3);

        insuranceTypeController.addInsuranceTypes(typeList);

        Iterable<InsuranceType> list = insuranceTypeController.getAllInsuranceType();
        // kiểm tra có bao nhiêu phần tử InsuranceType trước sau khi addOne : expected:6
        System.out.println("sau insert:" + insuranceTypeController.getAllInsuranceType().size());
        int after = insuranceTypeController.getAllInsuranceType().size();

        //  kiểm tra xem đối tượng test được thêm vào có được trả về hay không
        int size = 0;
        for (InsuranceType insuranceType: list) {
            size=size+1;
            if (size==4) {
                Assert.assertEquals(insuranceType.getInsuranceName(), bhxh1.getInsuranceName());
                Assert.assertEquals(insuranceType.getTotal(), bhxh1.getTotal());
                Assert.assertEquals(insuranceType.getUser(), u1);
            }else if(size==5){
                Assert.assertEquals(insuranceType.getInsuranceName(), bhxh2.getInsuranceName());
                Assert.assertEquals(insuranceType.getTotal(), bhxh2.getTotal());
                Assert.assertEquals(insuranceType.getUser(), u2);
            }
            else if(size==6){
                Assert.assertEquals(insuranceType.getInsuranceName(), bhxh3.getInsuranceName());
                Assert.assertEquals(insuranceType.getTotal(), bhxh3.getTotal());
                Assert.assertEquals(insuranceType.getUser(), u1);
            }
        }
        // kiểm tra kích thước danh sách trả về
        Assert.assertTrue(before + 3 == after);
    }


    @Test
    @Rollback
    public void testgetInsuranceTypesById() throws Exception{
        // lấy ra ALL user
        Iterable<InsuranceType> list = insuranceTypeController.getAllInsuranceType();

        //lay ra UserDto1
        InsuranceType insuranceType1 = new InsuranceType();
        for(InsuranceType x: list){
            insuranceType1 = x;
            break;
        }


        // Lấy ra user có id = 1
        int id = 1;
        InsuranceType InsuranceType = insuranceTypeController.getInsuranceTypesById(id);

        Assert.assertEquals(insuranceType1.getInsuranceName(), InsuranceType.getInsuranceName());
        Assert.assertEquals(insuranceType1.getTotal(), InsuranceType.getTotal());
        Assert.assertEquals(insuranceType1.getUser(), InsuranceType.getUser());

    }

    @Test
    @Rollback
    public void updateInsuranceTypeByIdPass() {
        int id = 1;
        // thay doi gia tri quybhtn,va usingtime =>total
        InsuranceType bhxh4 = new InsuranceType(1,"Bảo Hiểm Xã Hội Bắt Buộc - Việt Nam","2019-01-01",
                22.0,3.0,0.5,5.0,4.5,35.0,u1);

        boolean status = insuranceTypeController.updateInsuranceTypeById(id,bhxh4);
//        System.out.println("status" + status);
        // Lấy ra user có id = 1

        InsuranceType insuranceType = insuranceTypeController.getInsuranceTypesById(1);


        Assert.assertEquals(insuranceType.getInsuranceName(), bhxh4.getInsuranceName());
        Assert.assertEquals(insuranceType.getQuybhtn(), bhxh4.getQuybhtn());
        Assert.assertEquals(insuranceType.getUsingTime(), bhxh4.getUsingTime());
        Assert.assertEquals(insuranceType.getTotal(), bhxh4.getTotal());
        Assert.assertEquals(insuranceType.getUser(), bhxh4.getUser());
        // ktra da update chuwa
    }

}



