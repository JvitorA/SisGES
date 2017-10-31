/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package Util;

import de.jollyday.Holiday;
import de.jollyday.HolidayManager;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import net.objectlab.kit.datecalc.common.DateCalculator;
import net.objectlab.kit.datecalc.common.DefaultHolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayHandlerType;
import net.objectlab.kit.datecalc.joda.LocalDateKitCalculatorsFactory;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.chrono.ISOChronology;

/**
 *
 * @author Alessandro
 */
public class CalendarFormat implements Serializable {

    /**
     * função para extrair a hora HH
     *
     * @param data
     * @return int
     */
    public static int getHora(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        int hora = cal.get(Calendar.HOUR_OF_DAY);
        return hora;
    }

    /**
     * função para calcular dias de estagio
     *
     * @param dataInicial
     * @param horasPorDia
     * @param horasObrigatorias
     * @return Date
     */
    public static Date getDiasDeEstagio(Date dataInicial, int horasPorDia, int horasObrigatorias) {
        //usado para arredondar para cima 2.5 para 3
        double dias = 0.0;
        double horasPorDiaTemp = (double) horasPorDia;
        double horasObrigatoriasTemp = (double) horasObrigatorias;
        int diasSemana = 7;
        //achar numero de dias corridos necessarios 
        //para cumprir a carga horaria obrigatoria
        dias = horasObrigatoriasTemp / horasPorDiaTemp;
        //somar os dias que não contam sabado e domingo
        //a cada 7 dias soma 2 dias para frente
        dias = dias + ((dias / 7) * 2);

        dias++;
        Date dataFinal = getDataFinal(dataInicial, (int) dias);
        return dataFinal;
    }

    /**
     * função para somar um X dias na dataInicial
     *
     * @param dataInicial
     * @param dias
     * @return int
     */
    public static Date getDataFinal(Date dataInicial, int dias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataInicial);

        int numDias = 1;
        int numFimDeSemana = 0;
        while (numDias <= dias) {
            cal.add(Calendar.DATE, numDias);
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                numFimDeSemana++;
            }
            numDias++;
        }
        cal.setTime(dataInicial);
        dias = dias + numFimDeSemana;
        cal.add(Calendar.DATE, dias);
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            dias += 2;
            cal.setTime(dataInicial);
            cal.add(Calendar.DATE, dias);
        } else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            dias += 1;
            cal.setTime(dataInicial);
            cal.add(Calendar.DATE, dias);
        }
        return cal.getTime();
    }

    public static void getFeriados(DateTime dataInicial, DateTime dataFinal) {

        HolidayManager gerenciadorDeFeriados = HolidayManager.getInstance(de.jollyday.HolidayCalendar.BRAZIL);
        Set<Holiday> feriados = gerenciadorDeFeriados.getHolidays(new DateTime().getYear());
        Set<LocalDate> dataDosFeriados = new HashSet<>();
        for (Holiday h : feriados) {
            dataDosFeriados.add(new LocalDate(h.getDate(), ISOChronology.getInstance()));
        }

        // popula com os feriados brasileiros
        DefaultHolidayCalendar<LocalDate> calendarioDeFeriados = new DefaultHolidayCalendar<>(dataDosFeriados);
        //HolidayCalendar<LocalDate> calendarioDeFeriados = new DefaultHolidayCalendar<>(dataDosFeriados);

        //calendarioDeFeriados
        LocalDateKitCalculatorsFactory.getDefaultInstance().registerHolidays("BR", calendarioDeFeriados);

        DateCalculator<LocalDate> calendario = LocalDateKitCalculatorsFactory.getDefaultInstance().getDateCalculator("BR", HolidayHandlerType.FORWARD);

        // true - sábado
        calendario.isNonWorkingDay(new LocalDate(dataInicial));
        // false
        calendario.isNonWorkingDay(new LocalDate(dataFinal));
        // true - natal
        calendario.isNonWorkingDay(new LocalDate(feriados));

        int diasNaoUteis = 0;

        LocalDate dataInicialTemporaria = new LocalDate(dataInicial);
        LocalDate dataFinalTemporaria = new LocalDate(dataFinal);

        while (!dataInicialTemporaria.isAfter(dataFinalTemporaria)) {
            if (calendario.isNonWorkingDay(dataInicialTemporaria)) {
                diasNaoUteis++;
            }
            dataInicialTemporaria = dataInicialTemporaria.plusDays(1);
        }
        BigDecimal minutos = new BigDecimal(Minutes.minutesBetween(dataInicial, dataFinal).getMinutes());
        BigDecimal horas = minutos.divide(new BigDecimal("60"), 2, RoundingMode.HALF_UP);
        System.out.println("Horas finais líquidas: " + horas.subtract(new BigDecimal(24 * diasNaoUteis)));
    }

    /**
     * função para calcular diferença de horas
     *
     * @param dataInicial
     * @param dataFinal
     * @return int
     */
    public static int getNumHoras(DateTime dataInicial, DateTime dataFinal) {
        int horas = Hours.hoursBetween(dataInicial, dataFinal).getHours();
        return horas;
    }

    /**
     * função para calcular quantos dias existem entre uma data e outra DateTime
     * dataInicial = new DateTime(2012, 12, 1, 12, 0); DateTime dataFinal = new
     * DateTime(2012, 12, 28, 12, 30); DateTime feriado = new DateTime(2012, 12,
     * 25, 12, 0);
     *
     * @param dataInicial
     * @param dataFinal
     * @return int
     */
    public static int getNumDias(DateTime dataInicial, DateTime dataFinal) {
        int dias = Days.daysBetween(dataInicial, dataFinal).getDays();
        return dias;
    }

    /**
     * função para retorna hora do sistema em formato date
     *
     * @return Time
     */
    public static Time getTimeSO() {
        Time time;
        time = parseTime(getTimeSOToString());
        return time;
    }

    /**
     * função para retorna hora do sistema em formato string
     *
     * @return String
     */
    public static String getTimeSOToString() {
        GregorianCalendar calendar = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("HH:mm");
        return s.format(calendar.getTime());
    }

    /**
     * função para retorna data e horario do sistema em formato date
     *
     * @return Date
     */
    public static Date getDataHorarioSO() {
        Date data = null;
        try {
            data = new Date();
            SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(formatador.format(data));
        } catch (ParseException e) {
            e.getMessage();
        }
        return data;
    }

    public static String getDataHorarioSOToString() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar c = Calendar.getInstance();
        return dateFormat.format(c.getTime());
    }

    public static String getAnoToString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Calendar c = Calendar.getInstance();
        return dateFormat.format(c.getTime());
    }

    /**
     * função para retorna data do sistema em formato date
     *
     * @return Date
     */
    public static Date getDataSO() {
        Date data = null;
        try {
            data = new Date();
            SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
            data = new SimpleDateFormat("dd/MM/yyyy").parse(formatador.format(data));
        } catch (ParseException e) {
            e.getMessage();
        }
        return data;
    }

    /**
     * função para retorna data do sistema em formato string
     *
     * @return String
     */
    public static String getDataSOToString() {
        SimpleDateFormat formatador;
        formatador = new SimpleDateFormat("dd/MM/yyyy");
        return formatador.format(CalendarFormat.getDataSO());
    }

    /**
     * função para converte string para dateTime
     *
     * @param hora
     * @return Date
     */
    public static Date parseDateTime(String hora) {
        Date horario = null;
        DateFormat formato = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        try {
            horario = (Date) formato.parse(hora);
            DateFormat writeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            String format = writeFormat.format(horario);
            horario = (Date) writeFormat.parse(format);

        } catch (ParseException e) {
            e.getMessage();
        }
        return horario;
    }

    /**
     * função para converte date to time string
     *
     * @param hora
     * @return Time
     */
    public static String parseDateToTimeString(Date hora) {
        String horario;
        //DateFormat formato = new SimpleDateFormat("HH:mm:ss");
        DateFormat formato = new SimpleDateFormat("HH:mm");
        horario = formato.format(hora);
        return horario;
    }

    /**
     * função para converte string para time
     *
     * @param hora
     * @return Time
     */
    public static Time parseTime(String hora) {
        Time horario = null;
        //DateFormat formato = new SimpleDateFormat("HH:mm:ss");
        DateFormat formato = new SimpleDateFormat("HH:mm");
        try {
            horario = new java.sql.Time(formato.parse(hora).getTime());
        } catch (ParseException e) {
            e.getMessage();
        }
        return horario;
    }

    /**
     * função para converte time para string
     *
     * @param hora
     * @return String
     */
    public static String parseTimeString(Time hora) {
        return hora.toString();
    }

    /**
     * função para converte data no formato string para dateISO
     *
     * @param dataStr
     * @return String
     */
    public static Date getDataISO(String dataStr) {
        Date data = null;
        try {
            data = new SimpleDateFormat("dd/MM/yyyy").parse(dataStr);
            String dataBanco = new SimpleDateFormat("yyyy-MM-dd").format(data);
            data = new SimpleDateFormat("yyyy-MM-dd").parse(dataBanco);
        } catch (ParseException e) {
            e.getMessage();
        }
        return data;
    }

    /**
     * função para converte data no formato string para dateBR
     *
     * @param dataStr
     * @return String
     */
    public static Date getDataBR(String dataStr) {
        Date data = null;
        try {
            data = new SimpleDateFormat("yyyy-MM-dd").parse(dataStr);
            String dataBanco = new SimpleDateFormat("yyyy-MM-dd").format(data);
            data = new SimpleDateFormat("dd/MM/yyyy").parse(dataBanco);
        } catch (ParseException e) {
            e.getMessage();
        }
        return data;
    }

    /**
     * função para converte data no formato string para dateBRA em string
     *
     * @param dataStr
     * @return String
     */
    public static String getDataBRtoString(String dataStr) {
        String dataBanco = "";
        try {
            Date data = new SimpleDateFormat("yyyy-MM-dd").parse(dataStr);
            dataBanco = new SimpleDateFormat("dd/MM/yyyy").format(data);
        } catch (ParseException e) {
            e.getMessage();
        }
        return dataBanco;
    }

    /**
     * função para converte data no formato Date para dateBRA em String
     *
     * @param data
     * @return String
     */
    public static String getDataBRtoDate(Date data) {
        String dataBanco = "";
        try {
            dataBanco = new SimpleDateFormat("dd/MM/yyyy").format(data);
        } catch (Exception e) {
            e.getMessage();
        }
        return dataBanco;
    }

    /**
     * função para calcular idade
     *
     * @param dataNascInput
     * @return int
     */
    public static int calculaIdade(Date dataNascInput) {
        Calendar dateOfBirth = new GregorianCalendar();
        dateOfBirth.setTime(dataNascInput);
        // Cria um objeto calendar com a data atual
        Calendar today = Calendar.getInstance();
        // Obtém a idade baseado no ano
        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
        dateOfBirth.add(Calendar.YEAR, age);
        return age;
    }

    /**
     * função para subtração entre duas horas
     *
     * @param hora
     * @param hora2
     * @return String
     */
    public static String subtraiHora(String hora, String hora2) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        long min_1 = getMinutos(hora, formatter);
        long min_2 = getMinutos(hora2, formatter);
        long result = (min_1 - min_2) * 60 * 1000;
        Date data = new Date(result);
        return formatter.format(data);
    }

    /**
     * função para retornar os minutos
     *
     * @return long
     */
    private static long getMinutos(String hora, SimpleDateFormat formatter) {
        Date data;
        try {
            data = formatter.parse(hora);
        } catch (ParseException e) {
            e.getMessage();
            return 0;
        }
        long minutos = data.getTime() / 1000 / 60;
        return minutos;
    }

    /**
     * Retorna a data passada como parametro por extenso
     *
     * @param data
     * @return
     */
    public static String getDataPorExtenso(Date data) {
        DateFormat dfmt = new SimpleDateFormat("d 'de' MMMM 'de' yyyy");
        return dfmt.format(data);
    }
}
