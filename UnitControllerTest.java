package com.example.sqa_be_code_2023.controller;


import com.example.sqa_be_code_2023.model.dto.UserDto;
import com.example.sqa_be_code_2023.model.entity.Unit;
import com.example.sqa_be_code_2023.model.entity.User;
import com.example.sqa_be_code_2023.respository.UnitResponsitory;
import com.example.sqa_be_code_2023.service.UnitService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.Assert;
import org.junit.Before;
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
public class UnitControllerTest {

    @Autowired
    private UnitController unitController;

    @Autowired
    private UnitService unitService;



    @Autowired
    private UnitResponsitory unitResponsitory;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    Unit unit1 = new Unit("Công ty TNHH Phần mềm FPT");
    Unit unit2 = new Unit("Công ty công nghệ thông tin CMC");

    Unit unit3 = new Unit();
    // Test getALL và addUnits
    @Test
    @Rollback
    public void testgetAllUnit() throws Exception {
        // kiểm tra có bao nhiêu phần tử Unit trước khi test : expected:10
        System.out.println("Trước insert:" + unitController.getAllUnit().size());
        int before = unitService.getAllUnit().size();

        List<Unit> unitList = new ArrayList<>();
        unitList.add(unit1);
        unitList.add(unit2);
        unitController.addUnits(unitList);

        Iterable<Unit> list = unitController.getAllUnit();

        // kiểm tra có bao nhiêu phần tử Unit sau khi addlist : expected:12
        System.out.println("sau insert:" + ((List<Unit>) list).size());
        int after = ((List<Unit>) list).size();


       //  kiểm tra xem đối tượng test được thêm vào có được trả về hay không
        int size = 0;
        for (Unit unit : list) {
            size=size+1;
            if (size==11) {
                Assert.assertEquals(unit1.getUnitName(), unit.getUnitName());
            }
            else if (size==12) {
                Assert.assertEquals(unit2.getUnitName(), unit.getUnitName());
            }
        }
        // kiểm tra kích thước danh sách trả về
        Assert.assertTrue(before + 2 == after);
    }

    @Test
    @Rollback
    public void testaddUnitsFail() throws Exception {
        // kiểm tra có bao nhiêu phần tử Unit trước khi test : expected:10
        System.out.println("Trước insert:" + unitController.getAllUnit().size());
        int before = unitController.getAllUnit().size();

        List<Unit> unitList2 = new ArrayList<>();
        unitList2.add(unit3);

        Iterable<Unit> list = unitController.getAllUnit();

        // kiểm tra có bao nhiêu phần tử Unit trước khi test : expected:10
        System.out.println("sau insert:" + ((List<Unit>) list).size());
        int after = ((List<Unit>) list).size();

        // kiểm tra kích thước danh sách trả về
        Assert.assertTrue(before  == after);
    }


}
