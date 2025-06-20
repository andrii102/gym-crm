package com.dre.gymapp.dao;

import com.dre.gymapp.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TrainerDaoTest {
    private TrainerDao trainerDao;
    private Map<String, Trainer> trainerMap;

    @BeforeEach
    public void setUp(){
        trainerMap = new HashMap<>();
        trainerDao = new TrainerDao();
        trainerDao.setTrainerMap(trainerMap);
    }

    @Test
    public void findByIdTest(){
        String id = "TR0";
        Trainer trainer = new Trainer(id, "John", "Doe");
        trainerMap.put(id, trainer);

        Trainer result = trainerDao.findById(id).orElse(null);

        assertNotNull(result);
        assertEquals(trainer.getUserId(), result.getUserId());
        assertEquals(trainer.getFirstName(), result.getFirstName());
        assertEquals(trainer.getLastName(), result.getLastName());
    }

    @Test
    public void findAllTest(){
        String id1 = "TR0";
        String id2 = "TR1";
        Trainer trainer1 = new Trainer(id1, "John", "Doe");
        Trainer trainer2 = new Trainer(id2, "Jane", "Doe");
        trainerMap.put(id1, trainer1);
        trainerMap.put(id2, trainer2);

        List<Trainer> result = trainerDao.findAll();

        assertEquals(2, result.size());
    }

    @Test
    public void saveTest() {
        Trainer newTrainer = new Trainer("TR0", "John", "Doe");
        Trainer result = trainerDao.save(newTrainer);

        assertNull(result);
        assertEquals(newTrainer, trainerMap.get("TR0"));
    }


    @Test
    public void updateExistingTrainerTest() {
        String id = "TR0";
        Trainer original = new Trainer(id, "John", "Doe");
        trainerMap.put(id, original);

        Trainer updated = new Trainer(id, "John", "Updated");

        Trainer result = trainerDao.update(updated);

        // It should return the old value (the one being replaced)
        assertEquals(original, result);

        // But now the map should contain the updated value
        assertEquals(updated, trainerMap.get(id));
        assertEquals("Updated", trainerMap.get(id).getLastName());
    }


    @Test
    public void updateNonExistingTrainerTest(){
        Trainer result = trainerDao.update(new Trainer("TR1", "NonExisting", "Guy"));

        assertNull(result);
    }

    @Test
    public void deleteByIdTest(){
        String id = "TR0";

        assertThrows(UnsupportedOperationException.class, () -> trainerDao.deleteById(id));

    }

    @Test
    void usernameExistsTrueTest() {
        Trainer t = new Trainer("TR8", "User", "Name");
        t.setUsername("User.Name");
        trainerMap.put("TR8", t);

        assertTrue(trainerDao.usernameExists("User.Name"));
    }

    @Test
    void usernameExistsFalseTest() {
        assertFalse(trainerDao.usernameExists("Ghost.User"));
    }

}
