package com.smoothstack.branchservice.serviceTest;

import com.smoothstack.branchservice.dao.BankerRepository;
import com.smoothstack.branchservice.dao.BranchRepository;
import com.smoothstack.branchservice.mapper.BankerMapper;
import com.smoothstack.branchservice.service.BankerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

    @ExtendWith(MockitoExtension.class)
//    @SpringBootTest
    class BankerServiceTest {

        @InjectMocks
        private BankerService bankerService;

        @Mock
        private BranchRepository branchRepository;

        @Mock
        private BankerRepository bankerRepository;

        @Mock
        private BankerMapper bankerMapper;

        private static final Logger logger = LoggerFactory.getLogger(BankerService.class);

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.openMocks(this);
        }

//        @Test
//        public void testGetAllBankers() {
//            // Arrange
//            Banker banker1 = new Banker();
////            banker1.setBankerId(1);
//            banker1.setBranchId(1);
//            banker1.setFirstName("John");
//            banker1.setLastName("Doe");
//            banker1.setPhoneNumber("123-456-7890");
//            banker1.setEmail("test@test.com");
//            banker1.setJobTitle("Banker");
//
//            Banker banker2 = new Banker();
////            banker2.setBankerId(2);
//            banker2.setBranchId(2);
//            banker2.setFirstName("Jim");
//            banker2.setLastName("Smith");
//            banker2.setPhoneNumber("321-654-0987");
//            banker2.setEmail("test2@test.com");
//            banker2.setJobTitle("Banker2");
//
//            List<Banker> bankers = Arrays.asList(banker1, banker2);
//
//            Mockito.when(bankerRepository.findAll()).thenReturn(bankers);
//
//            BankerDTO bankerDTO1 = new BankerDTO();
////            bankerDTO1.setBankerId(1);
//            bankerDTO1.setBranchId(1);
//            bankerDTO1.setFirstName("John");
//            bankerDTO1.setLastName("Doe");
//            bankerDTO1.setPhoneNumber("123-456-7890");
//            bankerDTO1.setEmail("test@test.com");
//            bankerDTO1.setJobTitle("Banker");
//
//            BankerDTO bankerDTO2 = new BankerDTO();
////            bankerDTO2.setBankerId(2);
//            bankerDTO2.setBranchId(2);
//            bankerDTO2.setFirstName("Jim");
//            bankerDTO2.setLastName("Smith");
//            bankerDTO2.setPhoneNumber("321-654-0987");
//            bankerDTO2.setEmail("test2@test.com");
//            bankerDTO2.setJobTitle("Banker2");
//
//            List<BankerDTO> expectedBankerDTOs = Arrays.asList(bankerDTO1, bankerDTO2);
//
//            when(bankerMapper.bankerToBankerDTO(banker1)).thenReturn(bankerDTO1);
//            when(bankerMapper.bankerToBankerDTO(banker2)).thenReturn(bankerDTO2);
//
//            // Act
//            List<BankerDTO> result = bankerService.getAllBankers();
//            List<Banker> actualBankers = bankerRepository.findAll();
//            logger.info("Actual bankers: {}", actualBankers);
//            logger.info("Result: {}", result);
//
//            // Assert
//            assertEquals(expectedBankerDTOs.size(), result.size());
//            for (int i = 0; i < expectedBankerDTOs.size(); i++) {
//                assertEquals(expectedBankerDTOs.get(i), result.get(i));
//            }
//
//            verify(bankerRepository, times(1)).findAll();
//            verify(bankerMapper, times(2)).bankerToBankerDTO(any(Banker.class));
//        }
//
//        @Test
//        public void createBankerTest_Success() {
//            // Arrange
//            BankerDTO bankerDTO = new BankerDTO();
////            bankerDTO.setBankerId(1);
//            bankerDTO.setBranchId(1);
//            bankerDTO.setFirstName("John");
//            bankerDTO.setLastName("Doe");
//            bankerDTO.setPhoneNumber("123-456-7890");
//            bankerDTO.setEmail("test@test.com");
//            bankerDTO.setJobTitle("Banker");
//
//            Banker banker = Banker.builder()
////                    .bankerId(bankerDTO.getBankerId())
//                    .branchId(bankerDTO.getBranchId())
//                    .firstName(bankerDTO.getFirstName())
//                    .lastName(bankerDTO.getLastName())
//                    .phoneNumber(bankerDTO.getPhoneNumber())
//                    .email(bankerDTO.getEmail())
//                    .jobTitle(bankerDTO.getJobTitle())
//                    .build();
//
//            Branch branch = new Branch();
//            branch.setBranchId(bankerDTO.getBranchId());
//            when(branchRepository.findById(bankerDTO.getBranchId())).thenReturn(Optional.of(branch));
//
//            when(bankerRepository.findByBankerId(bankerDTO.getBankerId())).thenReturn(Optional.empty());
//            when(bankerMapper.bankerDTOToBanker(bankerDTO)).thenReturn(banker);
//            logger.info("bankerDTO: {}", bankerDTO);
//            logger.info("banker: {}", banker);
//            when(bankerRepository.save(banker)).thenReturn(banker);
//            when(bankerMapper.bankerToBankerDTO(banker)).thenReturn(bankerDTO);
//
//            // Act
//            BankerDTO result = bankerService.createBanker(bankerDTO);
//            logger.info("Result: {}", result);
//
//            // Assert
//            assertNotNull(result);
//            verify(bankerRepository).findByBankerId(bankerDTO.getBankerId());
//            verify(bankerRepository).save(banker);
//        }



        @Test
        void deleteBankerById_WithValidId_ShouldDeleteBanker() {
            // Arrange
            Integer bankerId = 1;
//            lenient().doNothing().when(bankerRepository).deleteById(bankerId);

            // Act
            bankerService.deleteBankerById(bankerId);
        }
    }