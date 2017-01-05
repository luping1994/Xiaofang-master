package com.suntrans.xiaofang.model.company;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Looney on 2016/12/17.
 */

public class CompanyDetailnfoResult {
    public  String status;
    @SerializedName("result")
    public CompanyDetailnfo info;

    public  String msg;


    /*
     "id": 1000,
        "company_id": "1000",
        "user_id": null,
        "name": "武汉晴川假日酒店",
        "addr": "武汉市汉阳区洗马长街88号",
        "incharge": null,
        "dangerlevel": "1",
        "exitnum": "5",
        "stairnum": "2",
        "hasfacility": "1",
        "mainattribute": "01000",
        "subattribute": "#01110#",
        "companyid": "42001217000248",
        "orgid": "70680128-0",
        "shortname": "",
        "usename": "武汉晴川假日酒店",
        "cmptype": null,
        "otherdisp": null,
        "leaderdepart": "湖北省国资委",
        "postcode": "430050",
        "phone": "027-84716688",
        "fax": "027-84716181",
        "email": "",
        "personum": "600",
        "economy": null,
        "foundtime": "1999-04-01",
        "areaname": "420105",
        "artiname": "谢思训",
        "artiid": "",
        "artiphone": "",
        "managername": "毛庆汉",
        "managerid": "420102610225281",
        "managerphone": null,
        "responname": "吴锦培",
        "responid": "H0916844900",
        "responphone": "027-84716688",
        "fund": "20000",
        "staffnum": "290",
        "area": "5900.00",
        "buildheight": null,
        "buildarea": "30000.00",
        "gastype": null,
        "firemannum": "2",
        "elevatornum": "2",
        "lanenum": "1",
        "lanepos": "酒店环形车道",
        "refugenum": null,
        "refugearea": null,
        "refugepos": "",
        "nearby": "",
        "east": "南岸嘴、武汉城投",
        "south": "长江",
        "west": "晴川阁",
        "north": "洗马长街",
        "remark": "",
        "cmystate": null,
        "status": "0",
        "updated_at": null,
        "created_at": "2011-01-30 16:37:03",
        "deleted_at": null
     */
}
