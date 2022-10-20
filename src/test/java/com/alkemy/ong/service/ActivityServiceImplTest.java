package com.alkemy.ong.service;

import com.alkemy.ong.dto.ActivityDTO;
import com.alkemy.ong.entity.Activity;
import com.alkemy.ong.mapper.ActivityMapper;
import com.alkemy.ong.repository.ActivityRepository;
import com.alkemy.ong.service.implement.ActivityServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static com.alkemy.ong.util.ActivityMocksUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ActivityServiceImpl.class)
class ActivityServiceImplTest {

    @SpyBean
    private ActivityMapper activityMapper;

    @MockBean
    private ActivityRepository activityRepository;

    private ActivityServiceImpl activityService;

    String existingActivityById = "1";

    private static String activityNameIsNull = null;
    private static String activityContentIsNull = null;
    private static String activityImageIsNull = null;

    @BeforeEach
    void setUp(){
        activityService = new ActivityServiceImpl(activityRepository, activityMapper);
    }

    @Nested
    class ActivityTest {

        @Nested
        class saveActivityTest {
            @Test
            @DisplayName("Successful save")
            void test1() {
                ActivityDTO expectedResponse = generateANewActivityDTO();
                Activity activity = activityMapper.activityDTO2Entity(expectedResponse);

                given(activityRepository.save(activity)).willReturn(activity);
                given(activityMapper.activityDTO2Entity(expectedResponse)).willReturn(activity);
                given(activityMapper.activityEntity2DTO(activity)).willReturn(expectedResponse);
                activityService.createActivity(expectedResponse);

                assertThat(activity.getImage()).isEqualTo(expectedResponse.getImage());
                Mockito.verify(activityRepository, Mockito.times(1)).save(any());
            }

            @Test
            @DisplayName("Name is null")
            void test3() {
                Activity activityEntity = generateMockActivity();
                ActivityDTO activityDTO = activityMapper.activityEntity2DTO(activityEntity);
                activityDTO.setName(activityNameIsNull);

                when(activityRepository.save(activityEntity)).thenReturn(activityEntity);

                assertThrows(
                        NullPointerException.class,
                        () -> {
                            ActivityDTO result = activityService.createActivity(activityDTO);
                            assertNull(result, "Result object is null.");
                            assertEquals(activityDTO.getName(), result.getName(), "The name attribute was not created.");
                        }
                        , "The service did not throw any exception."
                );
            }

            @Test
            @DisplayName("Content is null")
            void test5() {
                Activity activityEntity = generateMockActivity();
                ActivityDTO activityDTO = activityMapper.activityEntity2DTO(activityEntity);
                activityDTO.setName(activityContentIsNull);

                when(activityRepository.save(activityEntity)).thenReturn(activityEntity);

                assertThrows(
                        NullPointerException.class,
                        () -> {
                            ActivityDTO result = activityService.createActivity(activityDTO);
                            assertNull(result, "Result object is null.");
                            assertEquals(activityDTO.getContent(), result.getContent(), "The content attribute was not created.");
                        }
                        , "The service did not throw any exception."
                );
            }

            @Test
            @DisplayName("Image is null")
            void test6() {
                Activity activityEntity = generateMockActivity();
                ActivityDTO activityDTO = activityMapper.activityEntity2DTO(activityEntity);
                activityDTO.setName(activityImageIsNull);

                when(activityRepository.save(activityEntity)).thenReturn(activityEntity);

                assertThrows(
                        NullPointerException.class,
                        () -> {
                            ActivityDTO result = activityService.createActivity(activityDTO);
                            assertNull(result, "Result object is null.");
                            assertEquals(activityDTO.getImage(), result.getImage(), "The image attribute was not created.");
                        }
                        , "The service did not throw any exception."
                );
            }
        }

        @Nested
        class updateActivityTest {
            @Test
            @DisplayName("Successful edit")
            void test1() {
                Activity activityEntity = generateMockActivity();
                ActivityDTO activityDTOUpdated = activityMapper.activityEntity2DTO(activityEntity);
                String newName = "Name updated 0";
                activityDTOUpdated.setName(newName);

                when(activityRepository.save(activityEntity)).thenReturn(activityEntity);
                when(activityRepository.findById("1")).thenReturn(Optional.of(activityEntity));

                assertDoesNotThrow(
                        () -> {
                            ActivityDTO result = activityService.updateActivity(activityDTOUpdated, existingActivityById);
                            assertEquals(newName, result.getName(), "Attribute has expected updated value.");
                        }    , "The service did not throw any exception."
                );
                Mockito.verify(activityRepository, times(1)).findById("1");
            }

            @Test
            @DisplayName("Activity not found")
            void test2() {
                Activity activityEntity = generateMockActivity();
                when(activityRepository.findById(any())).thenReturn(Optional.empty());

                ActivityDTO activityDTO = activityMapper.activityEntity2DTO(activityEntity);

                assertThrows(
                        AssertionError.class,
                        () -> {
                            ActivityDTO result = activityService.updateActivity(activityDTO, any());
                        }
                        , "The service did not throw any exception."
                );

                verify(activityRepository, never()).save(any());
            }

            @Test
            @DisplayName("Name is null")
            void test3() {
                Activity activityEntity = generateMockActivity();
                ActivityDTO activityDTO = activityMapper.activityEntity2DTO(activityEntity);
                activityDTO.setName(activityNameIsNull);

                when(activityRepository.save(activityEntity)).thenReturn(activityEntity);
                when(activityRepository.findById("1")).thenThrow(NullPointerException.class);

                assertThrows(
                        NullPointerException.class,
                        () -> {
                            ActivityDTO result = activityService.updateActivity(activityDTO, existingActivityById);
                            assertNull(result, "Result object is null.");
                            assertEquals(activityDTO.getName(), result.getName(), "The name attribute was not updated.");
                        }
                        , "The service did not throw any exception."
                );

                verify(activityRepository, never()).save(any());
            }

            @Test
            @DisplayName("Content is null")
            void test4() {
                Activity activityEntity = generateMockActivity();
                ActivityDTO activityDTO = activityMapper.activityEntity2DTO(activityEntity);
                activityDTO.setName(activityContentIsNull);

                when(activityRepository.save(activityEntity)).thenReturn(activityEntity);
                when(activityRepository.findById("1")).thenThrow(NullPointerException.class);

                assertThrows(
                        NullPointerException.class,
                        () -> {
                            ActivityDTO result = activityService.updateActivity(activityDTO, existingActivityById);
                            assertNull(result, "Result object is null.");
                            assertEquals(activityDTO.getContent(), result.getContent(), "The content attribute was not updated.");
                        }
                        , "The service did not throw any exception."
                );
            }

            @Test
            @DisplayName("Image is null")
            void test5() {
                Activity activityEntity = generateMockActivity();
                ActivityDTO activityDTO = activityMapper.activityEntity2DTO(activityEntity);
                activityDTO.setName(activityImageIsNull);

                when(activityRepository.save(activityEntity)).thenReturn(activityEntity);
                when(activityRepository.findById("1")).thenThrow(NullPointerException.class);

                assertThrows(
                        NullPointerException.class,
                        () -> {
                            ActivityDTO result = activityService.updateActivity(activityDTO, existingActivityById);
                            assertNull(result, "Result object is null.");
                            assertEquals(activityDTO.getImage(), result.getImage(), "The image attribute was not updated.");
                        }
                        , "The service did not throw any exception."
                );

                verify(activityRepository, never()).save(any());
            }
        }
    }
}