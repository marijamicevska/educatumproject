package project.educatum.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "casovi", schema = "project")
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cas", nullable = false)
    private Integer id;

    @Column(name = "vreme_pocetok")
    private LocalDateTime beginningTime;

    @Column(name = "tema", nullable = false, length = 100)
    private String topic;

    @ManyToOne
    @JoinColumn(name = "id_nastavnik")
    private Teacher idTeacher;

    @ManyToOne
    @JoinColumn(name = "id_predmet")
    private Subject subjectID;

    public Class(LocalDateTime beginningTime, String topic, Teacher idTeacher, Subject subjectID) {
        this.beginningTime = beginningTime;
        this.topic = topic;
        this.idTeacher = idTeacher;
        this.subjectID = subjectID;
    }

    public Class() {

    }

    @Override
    public String toString() {
        return topic + "\t" + beginningTime.getDayOfMonth() + "." + beginningTime.getMonthValue() + "."
                + beginningTime.getYear() + " " + beginningTime.getHour() + ":" +
                beginningTime.getMinute() + "\n";
    }

}