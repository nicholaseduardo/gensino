/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util.types;

import ensino.helpers.DateHelper;
import java.io.Serializable;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author nicho
 */
@Embeddable
public class Periodo implements Serializable {

    @Column(name = "periodoDe", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date de;

    @Column(name = "periodoAte", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date ate;

    public Periodo() {
        this.de = new Date();
        this.ate = new Date();
    }

    /**
     * Cria um objeto da classe periodo
     *
     * @param de Data no formato dd/MM/yyyy
     * @param ate Data no formato dd/MM/yyyy
     * @throws java.text.ParseException
     */
    public Periodo(String de, String ate) throws ParseException {
        this.de = DateHelper.stringToDate(de, "dd/MM/yyyy");
        this.ate = DateHelper.stringToDate(ate, "dd/MM/yyyy");
    }

    public Periodo(Date de, Date ate) {
        this.de = de;
        this.ate = ate;
    }

    public Date getDe() {
        return de;
    }

    public String getDeText() {
        return DateHelper.dateToString(de, "dd/MM/yyyy");
    }

    public void setDe(Date de) {
        this.de = de;
    }

    public Date getAte() {
        return ate;
    }

    public String getAteText() {
        return DateHelper.dateToString(ate, "dd/MM/yyyy");
    }

    public void setAte(Date ate) {
        this.ate = ate;
    }

    public boolean isMesmaData() {
        return de.equals(ate);
    }

    /**
     * Retorna o número de dias existente entre as datas.
     * Considera a data inicial mas não considera a data final.
     *
     * @return
     */
    public Integer getDiasEntrePeriodo() {
        Period p = getPeriod();
        return p.getDays();
    }
    
    public Integer getMesesEntrePeriodo() {
        Period p = getPeriod();
        return p.getMonths();
    }
    
    private Period getPeriod() {
        LocalDate lde, late;
        lde = Instant.ofEpochMilli(de.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        late = Instant.ofEpochMilli(ate.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return Period.between(lde, late);
    }

    public Boolean contemData(Date date) {
        if (date.equals(de) || date.equals(ate)) {
            return Boolean.TRUE;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(de);
        if (!isMesmaData()) {
            do {
                cal.add(Calendar.DATE, 1);
                if (date.equals(cal.getTime())) {
                    return Boolean.TRUE;
                }
            } while (cal.getTime().compareTo(ate) < 0);
        }

        return Boolean.FALSE;
    }

    /**
     * Retorna o número de semanas existente entre as duas datas
     *
     * @return
     */
    public Long getSemanas() {
        LocalDateTime lde, late;
        lde = Instant.ofEpochMilli(de.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        late = Instant.ofEpochMilli(ate.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        // Existirá ao menos 1 semana
        return ChronoUnit.WEEKS.between(lde, late) + 1;
    }

    /**
     * Retorna uma instancia de enunm do <code>MesesDeAno</code> referente a
     * data De.
     *
     * @return
     */
    public MesesDeAno getMesDoAno() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(de);
        return MesesDeAno.of(cal.get(Calendar.MONTH));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Periodo other = (Periodo) obj;
        if (!Objects.equals(this.de, other.de)) {
            return false;
        }
        if (!Objects.equals(this.ate, other.ate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        int mesDe, anoDe, mesAte, anoAte;
        Calendar cal = Calendar.getInstance();
        cal.setTime(de);
        mesDe = cal.get(Calendar.MONTH);
        anoDe = cal.get(Calendar.YEAR);
        cal.setTime(ate);
        mesAte = cal.get(Calendar.MONTH);
        anoAte = cal.get(Calendar.YEAR);

        String formatDe = "";
        if (mesDe == mesAte) {
            formatDe = "dd";
        } else if (anoDe == anoAte) {
            formatDe = "dd/MM";
        } else {
            formatDe = "dd/MM/yyyy";
        }
        StringBuilder sb = new StringBuilder();
        if (!de.equals(ate)) {
            sb.append(DateHelper.dateToString(de, formatDe));
            sb.append(" a ");
            sb.append(DateHelper.dateToString(ate, "dd/MM/yyyy"));
            return sb.toString();
        }
        return DateHelper.dateToString(de, "dd/MM/yyyy");
    }

//    public static void main(String args[]) {
//        try {
//            Periodo p = new Periodo();
//            p.setDe(DateHelper.stringToDate("11/02/2019", "dd/MM/yyyy"));
//            p.setAte(DateHelper.stringToDate("05/07/2019", "dd/MM/yyyy"));
//            System.out.println(p);
//            System.out.println("Dias: " + p.getDiasEntrePeriodo());
//            System.out.println("Semanas: " + p.getSemanas());
//        } catch (ParseException ex) {
//            java.util.logging.Logger.getLogger(Periodo.class.getName()).log(
//                    java.util.logging.Level.SEVERE, null, ex);
//        }
//    }
}
