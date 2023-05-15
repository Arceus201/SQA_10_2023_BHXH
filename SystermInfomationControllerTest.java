package com.example.sqa_be_code_2023.controller;

import com.example.sqa_be_code_2023.model.entity.SystermInformation;
import com.example.sqa_be_code_2023.model.entity.User;
import com.example.sqa_be_code_2023.respository.SystermInformationResponsitory;
import com.example.sqa_be_code_2023.service.SystermInfomationService;
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
import java.util.List;


@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class SystermInfomationControllerTest {

    @Autowired
    private SystermInfomationController systermInfomationController;

    @Autowired
    private SystermInfomationService systermInfomationService;


    @Autowired
    private SystermInformationResponsitory systermInformationResponsitory;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    User user = new User(2,"vietnb7","vietnb7","giam doc BHXH","nguyen ba viet",
            "ha noi","0963775146",
            "vietnb7@gmail.com",null,"bhxh");

    SystermInformation sys = new SystermInformation(2,"033283348","Số 7 Tràng Thi, Q.Hoàn Kiếm, TP.Hà Nội",
            "Bảo hiểm xã hội Việt Nam","253/GP-TTĐT, cấp ngày 01/08/2017",user);

    SystermInformation sys2 = new SystermInformation(2,"033283348","Số 7 Tràng Thi, Q.Hoàn Kiếm, TP.Hà Nội",
            "Bảo hiểm xã hội Việt Nam","253/GP-TTĐT, cấp ngày 01/08/2017",null);

    //Test getall và add list SystermInformation
    @Test
    @Rollback
    public void testgetAllSystermInformation() throws Exception {
        // kiểm tra có bao nhiêu phần tử SystermInformation trước khi test : expected:1
        System.out.println("Trước insert:" + systermInfomationController.getALl().size());
        int before =  systermInfomationController.getALl().size();


        systermInfomationController.createOne(sys);
        Iterable<SystermInformation> list = systermInfomationController.getALl();

        // kiểm tra có bao nhiêu phần tử SystermInformation trước khi test : expected:2
        System.out.println("sau insert:" + ((List<SystermInformation>) list).size());
        int after = ((List<SystermInformation>) list).size();

        //  kiểm tra xem đối tượng test được thêm vào có được trả về hay không
        int size = 0;
        for (SystermInformation x : list) {
            size=size+1;
            if (size==2) {
                Assert.assertEquals(x.getPhoneNumber(), sys.getPhoneNumber());
                Assert.assertEquals(x.getUser(), user);

            }

        }
        // kiểm tra kích thước danh sách trả về
        Assert.assertTrue(before + 1 == after);

    }


    //Test add Fail SystemInformation với user null
    @Test
    @Rollback
    public void testaddSystermInformationFail() throws Exception {
        // kiểm tra có bao nhiêu phần tử SystermInformation trước khi test : expected:1
        System.out.println("Trước insert:" + systermInfomationController.getALl().size());
        int before =  systermInfomationController.getALl().size();


        systermInfomationController.createOne(sys2);
        Iterable<SystermInformation> list = systermInfomationController.getALl();

        // kiểm tra có bao nhiêu phần tử SystermInformation trước khi test : expected:1
        System.out.println("sau insert:" + ((List<SystermInformation>) list).size());
        int after = ((List<SystermInformation>) list).size();


        //  kiểm tra xem đối tượng test được thêm vào có được trả về hay không
        int size = 0;
        for (SystermInformation x : list) {
            size=size+1;
            if (size==2) {
                Assert.assertEquals(x.getPhoneNumber(), sys2.getPhoneNumber());
                Assert.assertEquals(x.getUser(),null);
                System.out.println("vẫn có thể insert vào database trong TH Fail");
            }

        }
        // kiểm tra kích thước danh sách trả về
        Assert.assertTrue(before  == after);

    }

    //không có update - 1 hệ thống chỉ nên có 1 bản ghi information duy nhất

}
