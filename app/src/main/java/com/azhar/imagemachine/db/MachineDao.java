package com.azhar.imagemachine.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.azhar.imagemachine.models.Machine;

import java.util.List;


@Dao
public interface MachineDao {

    @Insert
    void insert(Machine machine);

    @Query("SELECT machineId, machine_name, machine_type, maintenance_date, machineId ||2022 AS qr_code FROM tb_machine_data order by machine_name asc")
    List<Machine> getAllMachineByName();

    @Query("SELECT * FROM tb_machine_data order by machine_type asc")
    List<Machine> getAllMachineByType();

    @Update
    void update(Machine machine);

    @Delete
    void delete(Machine machine);

}