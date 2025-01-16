package com.muzaffar.masjidfinder.service.masjid;

import com.muzaffar.masjidfinder.domain.entity.Masjid;
import com.muzaffar.masjidfinder.domain.repository.MasjidRepo;
import com.muzaffar.masjidfinder.domain.repository.UserMasjidRepo;
import com.muzaffar.masjidfinder.model.LocationDTO;
import com.muzaffar.masjidfinder.service.masjid.impl.MasjidServiceImpl;
import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MasjidServiceTest {

    private MasjidService underTest;

    @Mock
    private MasjidRepo masjidRepo;
    @Mock
    private UserMasjidRepo userMasjidRepo;

    //DUMMY VARIABLES
    private Masjid masjid;
    private MasjidDTO masjidDTO;
    private List<Masjid> masjids;
    private LocationDTO locationDTO;

    @BeforeEach
    void setUp() {
        underTest = new MasjidServiceImpl(masjidRepo, userMasjidRepo);
        masjids = initsMasjids();
        masjid = initMasjid();
        masjidDTO = initMasjidDTO(masjid);
        locationDTO = new LocationDTO(69.207769, 41.343151);
    }

    @Test
    void getMasajidClosestToLocation() {
        // Given
        when(masjidRepo.findAll()).thenReturn(masjids);

        // When
        var actual = underTest.getMasajidClosestToLocation(locationDTO);

        //Then
        var siroj = getMasjidDTO(getMasjid("Siroj-solih", 69.2190667,41.3704571));
        assertThat(actual.getFirst().longitude()).isEqualTo(siroj.longitude());
        assertThat(actual.getFirst().latitude()).isEqualTo(siroj.latitude());
        assertThat(actual.getFirst().name()).isEqualTo(siroj.name());
    }

    @Test
    void getMasjidTest() {
        // Given
        long id = 10;
        when(masjidRepo.findById(id)).thenReturn(Optional.of(masjid));

        // When
        var actual = underTest.getMasjid(id);

        //Then
        assertThat(actual).isEqualTo(masjidDTO);
    }

    private List<Masjid> initsMasjids() {
        Masjid masjid1 = getMasjid("Abu Sahiy", 69.16538754002481, 41.248203831616344);
        Masjid masjid2 = getMasjid("Shayx Muhammad Sodiq Muhammad Yusuf", 69.18636274051873, 41.25960654276252);
        Masjid masjid3 = getMasjid("Chosh peta", 69.18803655078626, 41.23775954919874);
        Masjid masjid4 = getMasjid("Hasanhonqori", 69.20317558957414, 41.29702214624629);
        Masjid masjid5 = getMasjid("Siroj-solih", 69.2190667, 41.3704571);

        List<Masjid> list = new ArrayList<>();
        list.add(masjid1);
        list.add(masjid2);
        list.add(masjid3);
        list.add(masjid4);
        list.add(masjid5);
        return list;
    }
    private MasjidDTO initMasjidDTO(Masjid masjid) {
        return new MasjidDTO(masjid);
    }
    private Masjid initMasjid () {
        var masjid = new Masjid();
        masjid.setName("Abu Sahiy");
        masjid.setLongitude(69.16538754002481);
        masjid.setLatitude(41.248203831616344);
        masjid.setId(10L);
        return masjid;
    }
    private Masjid getMasjid(String name, Double ln, Double lat) {

        var masjid = new Masjid();
        masjid.setName(name);
        masjid.setLongitude(ln);
        masjid.setLatitude(lat);
        return masjid;

    }
    private MasjidDTO getMasjidDTO(Masjid masjid) {
        return new MasjidDTO(masjid.getId(),
                masjid.getName(),
                masjid.getAddress(),
                masjid.getLatitude(),
                masjid.getLongitude(),
                masjid.getStatus(),
                (Math.random() * 10) + 1,
                masjid.getFajr(),
                masjid.getDuhr(),
                masjid.getAsr(),
                masjid.getMagrib(),
                masjid.getIsha()
                );
    }

}