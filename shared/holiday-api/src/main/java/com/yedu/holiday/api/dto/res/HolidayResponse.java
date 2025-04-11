package com.yedu.holiday.api.dto.res;

import jakarta.xml.bind.annotation.*;
import java.util.List;
import lombok.ToString;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
public class HolidayResponse {

  @XmlElement(name = "header")
  private Header header;

  @XmlElement(name = "body")
  private Body body;

  @XmlAccessorType(XmlAccessType.FIELD)
  @ToString
  public static class Header {
    @XmlElement(name = "resultCode")
    private String resultCode;

    @XmlElement(name = "resultMsg")
    private String resultMsg;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @ToString
  public static class Body {
    @XmlElement(name = "items")
    private Items items;

    @XmlElement(name = "numOfRows")
    private Integer numOfRows;

    @XmlElement(name = "pageNo")
    private Integer pageNo;

    @XmlElement(name = "totalCount")
    private Integer totalCount;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @ToString
  public static class Items {
    @XmlElement(name = "item")
    private List<Item> item;
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @ToString
  public static class Item {
    @XmlElement(name = "dateKind")
    private String dateKind;

    @XmlElement(name = "dateName")
    private String dateName;

    @XmlElement(name = "isHoliday")
    private String isHoliday;

    @XmlElement(name = "locdate")
    private Integer locdate;

    @XmlElement(name = "seq")
    private Integer seq;
  }
}
