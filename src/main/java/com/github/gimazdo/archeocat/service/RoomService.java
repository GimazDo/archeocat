package com.github.gimazdo.archeocat.service;

import com.github.gimazdo.archeocat.entity.Room;
import com.github.gimazdo.archeocat.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;

    public String getDescriptionById(Long id){
        Optional<Room> roomOpt = roomRepository.findById(id);
        if(roomOpt.isPresent()){
            log.info("Found room with id {} \n {}", id, roomOpt.get());
            return roomOpt.get().getDescription();
        }
        else {
            log.error("Cannot find room with id {}", id);
            return "Нет информации о зале №" + id;
        }
    }

    public Room add(Room room){
        return roomRepository.save(room);
    }

    public List<Room> getAll(){
        return roomRepository.findAll();
    }

}
