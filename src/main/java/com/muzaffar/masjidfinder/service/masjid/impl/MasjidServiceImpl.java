package com.muzaffar.masjidfinder.service.masjid.impl;

import com.muzaffar.masjidfinder.ResourceNotFoundException;
import com.muzaffar.masjidfinder.bot.model.TgUserDTO;
import com.muzaffar.masjidfinder.domain.entity.Masjid;
import com.muzaffar.masjidfinder.domain.entity.UserMasjid;
import com.muzaffar.masjidfinder.domain.entity.enums.MasjidStatus;
import com.muzaffar.masjidfinder.domain.entity.enums.UserMasjidType;
import com.muzaffar.masjidfinder.domain.repository.MasjidRepo;
import com.muzaffar.masjidfinder.domain.repository.UserMasjidRepo;
import com.muzaffar.masjidfinder.domain.repository.UserRepo;
import com.muzaffar.masjidfinder.model.LocationDTO;
import com.muzaffar.masjidfinder.service.masjid.MasjidService;
import com.muzaffar.masjidfinder.service.masjid.model.MasjidDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MasjidServiceImpl implements MasjidService {

    private final MasjidRepo masjidRepo;
    private final UserMasjidRepo userMasjidRepo;
    private final UserRepo userRepo;


    private static final double EARTH_RADIUS = 6_371.00;

    @Override
    public List<MasjidDTO> getMasajidClosestToLocation(LocationDTO dto) {
        var masjids = masjidRepo.findAll();

        var sorted = orderMasjidsByDistanceAscending(masjids, dto);

        return sorted.subList(0, Math.min(sorted.size(), 5));
    }

    @Override
    public MasjidDTO getMasjid(Long id) {
        var masjid = masjidRepo.findById(id).orElseThrow(
                () -> {
                    log.error("Get masjid attempt --> Masjid with id: {} not found", id);
                    return new InputMismatchException("NOT FOUND");
                }
        );
        return new MasjidDTO(masjid);
    }

    @Override
    public List<MasjidDTO> getFavsByUserId(Long userId) {

        var favs = userMasjidRepo.findAllByUserIdAndDeletedIsFalse(userId);

        if (favs.isEmpty()) {
            return List.of();
        }

        var masajidIds = favs.stream()
                .map(UserMasjid::getMasjidId)
                .toList();

        var masajid = masjidRepo.findAllByIdIn(masajidIds);

        if (favs.isEmpty()) {
            return List.of();
        }

        return masajid.stream()
                .map(MasjidDTO::new)
                .toList();
    }

    @Override
    public MasjidDTO getDefaultMasjidByUserId(Long userId) {
        var def = userMasjidRepo.findByUserIdAndIsDefaultTrue(userId);

        if (def.isEmpty()) {
            return null;
        }

        var masjid = masjidRepo.findById(def.get().getMasjidId());

        return masjid.map(MasjidDTO::new).orElse(null);
    }

    @Override
    public MasjidDTO setMasjidAsFav(Long userId, Long masjidId) {
        UserMasjid dto = new UserMasjid();
        dto.setMasjidId(masjidId);
        dto.setUserId(userId);
        dto.setType(UserMasjidType.FAVORITE);
        userMasjidRepo.save(dto);
        return getMasjid(masjidId);
    }

    @Override
    public MasjidDTO removeMasjidFromFav(Long userId, Long masjidId) {
        log.info("Removing Masjid {} from User {} favorite list", masjidId, userId);
        var userMasjidOptional = userMasjidRepo.findByUserIdAndMasjidId(userId, masjidId);

        if (userMasjidOptional.isPresent()) {
            var userMasjid = userMasjidOptional.get();
            userMasjid.setDeleted(true);
            userMasjidRepo.save(userMasjid);
        }

        return getMasjid(masjidId);
    }

    @Override
    public List<MasjidDTO> findMasajidByName(String name) {
        return masjidRepo.findAllByNameContainingIgnoreCase(name)
                .stream()
                .map(MasjidDTO::new)
                .toList();
    }

    @Override
    public List<MasjidDTO> findMasjidByName(String name, Boolean isUnverified) {
        return masjidRepo.findAllByNameContainingIgnoreCase(name)
                .stream()
                .filter(m -> isUnverified ? Objects.equals(m.getStatus(), MasjidStatus.DRAFTED) : (Objects.equals(m.getStatus(), MasjidStatus.CONFIRMED) || Objects.equals(m.getStatus(), MasjidStatus.DRAFTED)))
                .map(MasjidDTO::new)
                .toList();
    }

    @Override
    public void updateMasjidName(TgUserDTO user, Long masjidId, String masjidName) {
        //TODO user is for logging purposes
        var masjid = masjidRepo.findById(masjidId).orElse(null);

        if (Objects.isNull(masjid)) {
            // ideally we should not reach this block
            throw new ResourceNotFoundException(String.format("Masjid with ID %s not found", masjidId));
        }

        masjid.setName(masjidName);
        masjidRepo.save(masjid);
    }

    @Override
    public MasjidDTO verifyMasjidById(TgUserDTO user, Long masjidId) {
        //TODO user is for logging purposes
        var masjid = masjidRepo.findById(masjidId).orElse(null);

        if (Objects.isNull(masjid)) {
            // ideally we should not reach this block
            throw new ResourceNotFoundException(String.format("Masjid with ID %s not found", masjidId));
        }

        masjid.setStatus(MasjidStatus.CONFIRMED);
        masjidRepo.save(masjid);
        return new MasjidDTO(masjid);
    }

    @Override
    public MasjidDTO updateMasjidPrayerTimes(TgUserDTO userDTO, Long masjidId, LocalTime bomdod, LocalTime peshin, LocalTime asr, LocalTime shom, LocalTime hufton) {
        //TODO user is for logging purposes
        var masjid = masjidRepo.findById(masjidId).orElse(null);

        if (Objects.isNull(masjid)) {
            // ideally we should not reach this block
            throw new ResourceNotFoundException(String.format("Masjid with ID %s not found", masjidId));
        }

        masjid.setFajr(bomdod);
        masjid.setDuhr(peshin);
        masjid.setAsr(asr);
        masjid.setMagrib(shom);
        masjid.setIsha(hufton);
        masjidRepo.save(masjid);
        return new MasjidDTO(masjid);
    }

    @Override
    public List<MasjidDTO> getUnverifiedMasajidClosestToLocation(LocationDTO locationDTO) {
        return List.of();
//        var masajid = masjidRepo.findAllByStatusIn(List.of(MasjidStatus.DRAFTED, MasjidStatus.DISABLED));
//        var sorted = orderMasjidsByDistanceAscending(masajid, locationDTO);
//        return sorted.subList(0, Math.min(sorted.size(), 10));
    }

    @Override
    public Pair<List<MasjidDTO>, Long> getFirst15UnverifiedMasajid() {
        Sort sort = Sort.by("id").ascending();
        Pageable pageable = PageRequest.of(0, 15, sort);
        List<MasjidStatus> statuses = List.of(MasjidStatus.DRAFTED, MasjidStatus.DISABLED);
        var masajid = masjidRepo.findAllByStatusIn(statuses, pageable);

        List<MasjidDTO> result = masajid.stream().map(MasjidDTO::new).collect(Collectors.toList());
        Long total = masjidRepo.countAllByStatusIn(statuses);
        return Pair.of(result, total);
    }

    @Override
    public List<MasjidDTO> getMasajidClosestToLocation(LocationDTO dto, Boolean isUnverified) {
        var masjids = masjidRepo.findAll();

        var sorted = orderMasjidsByDistanceAscending(masjids, dto)
                .stream()
                .filter(m -> isUnverified ?
                        m.status() == MasjidStatus.DRAFTED
                        : (m.status() == MasjidStatus.CONFIRMED) || m.status() == MasjidStatus.DRAFTED)
                .toList();

        return sorted.subList(0, Math.min(sorted.size(), 5));
    }

    @Override
    public List<MasjidDTO> getAssignedMasjid(TgUserDTO userDTO) {
        var user = userRepo.findByTelegramId(userDTO.telegramId()).orElse(null);
        if (Objects.isNull(user)) {
            return List.of();
        }

        var masjidList = userMasjidRepo.findAllByUserIdAndType(user.getId(), UserMasjidType.ASSIGNED);
        return masjidRepo.findAllByIdIn(masjidList.stream().map(UserMasjid::getMasjidId).toList())
                .stream()
                .map(MasjidDTO::new)
                .toList();
    }

    private List<MasjidDTO> orderMasjidsByDistanceAscending(List<Masjid> masjids, LocationDTO dto) {

        List<MasjidDTO> result = new ArrayList<>();

        for (Masjid masjid : masjids) {
            Double distance = Math.round(calculateDistance(masjid, dto) * 100.0) / 100.0;
            result.add(new MasjidDTO(masjid, distance));
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


    public void init() {
        Masjid masjid1 = getMasjid("Abu Sahiy", 69.16538754002481, 41.248203831616344);
        Masjid masjid2 = getMasjid("Shayx Muhammad Sodiq Muhammad Yusuf", 69.18636274051873, 41.25960654276252);
        Masjid masjid3 = getMasjid("Chosh peta", 69.18803655078626, 41.23775954919874);
        Masjid masjid4 = getMasjid("Hasanhonqori", 69.20317558957414, 41.29702214624629);
        Masjid masjid5 = getMasjid("Dombirabot", 69.20937508212428, 41.26228990264712);
        Masjid masjid6 = getMasjid("Qatortol", 69.19567639991095, 41.267248199400385);
        Masjid masjid7 = getMasjid("Ko'zi ojizlar", 69.19712478977603, 41.29090674121106);
        Masjid masjid8 = getMasjid("Ubay ibn Kaab", 69.18773174268055, 41.26417868996122);
        Masjid masjid9 = getMasjid("Nosirhon", 69.20060634588116, 41.25726694928192);
        Masjid masjid10 = getMasjid("Muhsinhon tora", 69.19920623265239, 41.2522863324597);
        Masjid masjid11 = getMasjid("Bota buva masjidi", 69.1719167680633, 41.24061265791863);
        Masjid masjid12 = getMasjid("Novza", 69.22685185783973, 41.29431493630495);
        Masjid masjid13 = getMasjid("Qozirabot", 69.21566963369364, 41.27973347962563);
        Masjid masjid14 = getMasjid("Maruf ota", 69.20042127410578, 41.243348500068414);
        Masjid masjid15 = getMasjid("Sobithon Hoji", 69.23099045342889, 41.291901449654645);
        Masjid masjid16 = getMasjid("Tinchlik", 69.17380034967394, 41.265612159600394);

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
        masjid.setStatus(MasjidStatus.DRAFTED);
        return masjid;

    }
}
