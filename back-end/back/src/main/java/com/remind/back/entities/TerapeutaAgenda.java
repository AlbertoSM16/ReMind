package com.remind.back.entities;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data; 
import lombok.NoArgsConstructor; 
import lombok.AllArgsConstructor; 


@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class TerapeutaAgenda {

    @Id
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terapeuta_id", nullable = false) 
    private Integer terapeutaId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agenda_id", nullable = false) 
    private Integer agendaId;
}
