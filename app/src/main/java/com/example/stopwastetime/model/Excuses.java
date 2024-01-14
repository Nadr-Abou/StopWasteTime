package com.example.stopwastetime.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "excuses")
public class Excuses {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "excuses")
    String scusa;
    @ColumnInfo(name = "tempo_utilizzo")
    int tempoUtilizzo;
    @ColumnInfo(name = "blocco")
    int blocco;

    public Excuses(String scusa, int tempoUtilizzo, int blocco) {
        this.scusa = scusa;
        this.tempoUtilizzo = tempoUtilizzo;
        this.blocco = blocco;
    }

    public String getScusa() {
        return scusa;
    }

    public void setScusa(String scusa) {
        this.scusa = scusa;
    }

    public int getTempoUtilizzo() {
        return tempoUtilizzo;
    }

    public void setTempoUtilizzo(int tempoUtilizzo) {
        this.tempoUtilizzo = tempoUtilizzo;
    }

    public int getBlocco() {
        return blocco;
    }

    public void setBlocco(int blocco) {
        this.blocco = blocco;
    }
}
