package com.runtracker.repository;//Repository = the layer that talks to the database.

import com.runtracker.entity.RunRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunRepository extends JpaRepository<RunRecord, Long> { //An interface is a contract — it defines WHAT methods exist but not HOW they work.



    List<RunRecord> findByUserId(String userId);

    List<RunRecord> findByRunId(String runId);
}
//model      → data classes
//entity     → database table classes
//producer   → Kafka sending
//consumer   → Kafka receiving
//repository → database operations  ← we are here

//import com.runtracker.entity.RunRecord
//Your own RunRecord entity class. Repository needs to know which table it's working with.
//import org.springframework.data.jpa.repository.JpaRepository
//Spring Data's built-in interface. Contains all standard database operations already written for you. You just inherit them.
//import org.springframework.stereotype.Repository
//Marks this as a Spring Bean — same concept as @Service and @Component but specifically for database layer.
//import java.util.List
//Java's built-in List class. Used because findByUserId returns multiple records — a list of RunRecords.


//@Repository
//Tells Spring:
//
//Manage this as a Bean ✅
//Add database exception translation ✅
//
//Exception translation means: if MySQL throws a database-specific error, Spring converts it to a standard Spring exception. Your code doesn't need to handle MySQL-specific errors.


//So who implements RunRepository?
//Spring Data does it automatically at runtime! When your app starts, Spring sees RunRepository extends JpaRepository and says:
//"I'll create a complete implementation of this interface automatically — with all database operations working."
//You write the interface. Spring writes the implementation. You never see it but it's there working perfectly.