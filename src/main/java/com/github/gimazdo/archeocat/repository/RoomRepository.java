package com.github.gimazdo.archeocat.repository;

import com.github.gimazdo.archeocat.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}