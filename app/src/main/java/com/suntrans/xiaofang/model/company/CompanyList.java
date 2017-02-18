package com.suntrans.xiaofang.model.company;

/**
 * Created by Looney on 2016/12/16.
 */

public class CompanyList {
    /*
     "id": 1,
      "name": "湖北丽江饭店有限公司",
      "uuid": null,
      "pinyin_short": null,
      "pinyin": null,
      "area_id": 0,
      "create_uid": null,
      "m_id": null,
      "attr_id": null,
      "location": "114.333639,30.548329",
      "lng": "114.333639",
      "lat": "30.548329",
      "vtype": 0,
      "special": 1,
      "address": "武昌区洪山体育馆路5号",
      "leader": "盛国政",
      "phone": "",
      "place": null,
      "created_at": null,
      "updated_at": null,
      "deleted_at": null

     */

    public String id;
    public String name;
    public int vtype;
    public String location;
    public String lng;
    public String lat;
    public String special;
    public String address;
    public String leader;
    public String source_id;

    @Override
    public String toString() {
        return "CompanyList{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", vtype=" + vtype +
                ", location='" + location + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", special=" + special +
                ", address='" + address + '\'' +
                ", leader='" + leader + '\'' +
                '}';
    }
}
