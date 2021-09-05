//package pt.ist.socialsoftware.edition.game.api.textDto;
//
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import org.joda.time.LocalDate;
//import pt.ist.socialsoftware.edition.game.utils.CustomDateTimeDeserializer;
//import pt.ist.socialsoftware.edition.game.utils.PrecisionType;
//
//
//public class LdoDDateDto {
//
//    public  enum DateType {
//        YEAR, MONTH, DAY
//    }
//
//    private DateType type;
//    private LocalDate date;
//    private PrecisionType precisionType;
//
//
//
//    public LdoDDateDto() {
//        super();
//    }
//
//    public String print() {
//        switch (getType()) {
//            case YEAR:
//                return getDate().toString("yyyy");
//            case DAY:
//                return getDate().toString("dd-MM-yyyy");
//            case MONTH:
//                return getDate().toString("MM-yyyy");
//            default:
//                return "";
//        }
//    }
//
//    public DateType getType() {
//        return this.type;
//    }
//
//    public void setType(DateType type) {
//        this.type = type;
//    }
//
//    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
//    public LocalDate getDate() {
//        return this.date;
//    }
//
//    public void setDate(LocalDate date) {
//        this.date = date;
//    }
//
//    public PrecisionType getPrecision() {
//        return precisionType;
//    }
//
//    public void setPrecision(PrecisionType precisionType) {
//        this.precisionType = precisionType;
//    }
//}
