package com.muzaffar.masjidfinder.service.masjid.impl;

import com.muzaffar.masjidfinder.domain.entity.Masjid;
import com.muzaffar.masjidfinder.domain.repository.MasjidRepo;
import com.muzaffar.masjidfinder.model.LocationDTO;
import com.muzaffar.masjidfinder.service.masjid.MasjidService;
import com.muzaffar.masjidfinder.service.masjid.mapper.MasjidMapper;
import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MasjidServiceImpl implements MasjidService {

    private final MasjidRepo masjidRepo;
    private final MasjidMapper masjidMapper;
    private static final double EARTH_RADIUS = 6_371.00;

    @Override
    public List<MasjidDTO> getMasjidsClosestToLocation(LocationDTO dto) {
        var masjids = masjidRepo.findAll();

        var sorted = orderMasjidsByDistanceAscending(masjids, dto);

        return sorted.subList(0, Math.min(sorted.size(), 5));
    }

    @Override
    public MasjidDTO getMasjid(Long id) {
        var masjid = masjidRepo.findById(id).orElseThrow(
                () -> {
                    log.error("Get masjid attempt --> Masjid with id: {} not found", id);
                    throw new InputMismatchException("NOT FOUND");
                }
        );
        return masjidMapper.toMasjidDTO(masjid);
    }

    private List<MasjidDTO> orderMasjidsByDistanceAscending(List<Masjid> masjids, LocationDTO dto) {

        List<MasjidDTO> result = new ArrayList<>();

        for (Masjid masjid : masjids) {
            Double distance = Math.round(calculateDistance(masjid, dto) * 100.0) / 100.0;
            var obj = masjidMapper.toMasjidDTO(masjid, distance);
            result.add(obj);
        }

        return result.stream().sorted(Comparator.comparingDouble(MasjidDTO::distance)).toList();
    }

    private double calculateDistance(Masjid masjid, LocationDTO dto) {
        return calculateDistance(dto.lat(), dto.lon(), masjid.getLatitude(), masjid.getLongitude());
    }

    private double calculateDistance(double startLat, double startLong, double endLat, double endLong) {
        double dLat = Math.toRadians(endLat - startLat);
        double dLong = Math.toRadians(endLong - startLong);

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(startLat) * Math.cos(endLat)
                *Math.sin(dLong/2) * Math.sin(dLong/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    //    //TODO for testing purposes
    @PostConstruct
    public void init() {
        Masjid masjid1 = getMasjid("Abu_Sahiy_masjidi", 69.16538754002481, 41.248203831616344);
        Masjid masjid2 = getMasjid("Shayx_Muhammad_Sodiq_Muhammad_Yusuf", 69.18636274051873, 41.25960654276252);
        Masjid masjid3 = getMasjid("Chosh_peta_masjidi", 69.18803655078626, 41.23775954919874);
        Masjid masjid4 = getMasjid("Hasanhonqori_masjidi", 69.20317558957414, 41.29702214624629);
        Masjid masjid5 = getMasjid("Dombirabot_masjidi", 69.20937508212428, 41.26228990264712);
        Masjid masjid6 = getMasjid("qatortol_masjidi", 69.19567639991095, 41.267248199400385);
        Masjid masjid7 = getMasjid("Ko'zi_ojizlar_masjidi", 69.19712478977603, 41.29090674121106);
        Masjid masjid8 = getMasjid("Ubay ibn Kaab_masjidi", 69.18773174268055, 41.26417868996122);
        Masjid masjid9 = getMasjid("Nosirhon masjidi", 69.20060634588116, 41.25726694928192);
        Masjid masjid10 = getMasjid("Muhsinhon tora masjidi", 69.19920623265239, 41.2522863324597);
        Masjid masjid11 = getMasjid("Bota_buva masjidi", 69.1719167680633, 41.24061265791863);
        Masjid masjid12 = getMasjid("Novza masjidi", 69.22685185783973, 41.29431493630495);
        Masjid masjid13 = getMasjid("Qozirabot masjidi", 69.21566963369364, 41.27973347962563);
        Masjid masjid14 = getMasjid("Maruf ota masjidi", 69.20042127410578, 41.243348500068414);
        Masjid masjid15 = getMasjid("Sobithon hoji masjidi", 69.23099045342889, 41.291901449654645);
        Masjid masjid16 = getMasjid("Tinchik masjidi", 69.17380034967394, 41.265612159600394);

        List<Masjid> list = new ArrayList<>();
        list.add(masjid1);
        list.add(masjid2);
        list.add(masjid3);
        list.add(masjid4);
        list.add(masjid5);
        list.add(masjid6);
        list.add(masjid7);
        list.add(masjid8);
        list.add(masjid9);
        list.add(masjid10);
        list.add(masjid11);
        list.add(masjid12);
        list.add(masjid13);
        list.add(masjid14);
        list.add(masjid15);
        list.add(masjid16);

        masjidRepo.saveAll(list);

    }

    private Masjid getMasjid(String name, Double ln, Double lat) {

        var masjid = new Masjid();
        masjid.setName(name);
        masjid.setLongitude(ln);
        masjid.setLatitude(lat);
        return masjid;

    }
}
