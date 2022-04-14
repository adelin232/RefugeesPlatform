package com.project;

import com.project.models.Room;
import com.project.repositories.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ProjectApplicationTests {

	@Autowired
	RoomRepository roomRepository;

//	@BeforeEach
//	void setUp() {
//		room = new Room();
//	}

	@Test
	@DisplayName("Insert room should work")
	void insertRoomTest() {
		Room room = new Room(1L, 26L, "First Floor", 2L, true,
				"https://studioinsign.ro/wp-content/uploads/2016/08/Proiecte-hoteluri-design-interior-camera-hotel-stil-loft-industrial-newyorkez-1.png",
				"Str. Pietroasele, Nr. 18", 46.0125, 25.0567);
		roomRepository.save(room);

		assertThat(roomRepository.findById(room.getId())).hasValue(room);
	}
}
