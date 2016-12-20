package com.suntrans.xiaofang.activity;

/**
 * Created by Looney on 2016/12/17.
 */

public class dasdasdas {
    /*
    URL前缀：http://api.91yunpan.com/
client_id:6
client_secret: test
请求方法： post
token 有效期 30天
refresh_token 有效期 30天
-d '参数'

刷新令牌
/oauth/token -d 'grant_type= 'refresh_token'&'refresh_token' = 'the-refresh-token'&client_id=6&client_secret=test'

1获取token
/oauth/token -d 'grant_type= 'password'&client_id=6&client_secret=test'
结果
{
  "error": "invalid_client",
  "message": "Client authentication failed"
}
{
  "error": "invalid_credentials",
  "message": "The user credentials were incorrect."
}
{
  "token_type": "Bearer",
  "expires_in": 31536000,
  "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImFlY2I0YzI3ZTM2ODg5OTY0ZDVmODZmODRjODQxMzU2NzRhNzRjOTQ2Mjg4N2Y3ZjE4YjNlMmJiNTNlNzU3MmQxZjdiYmVhZTBkNzRhNmIxIn0.eyJhdWQiOiI2IiwianRpIjoiYWVjYjRjMjdlMzY4ODk5NjRkNWY4NmY4NGM4NDEzNTY3NGE3NGM5NDYyODg3ZjdmMThiM2UyYmI1M2U3NTcyZDFmN2JiZWFlMGQ3NGE2YjEiLCJpYXQiOjE0ODE3OTEwOTYsIm5iZiI6MTQ4MTc5MTA5NiwiZXhwIjoxNTEzMzI3MDk2LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.nQvNcNYI5IUNcLWwIGSHP-HKfHMk4S4x9c-FcsMMO5UBnIQlpIgYYvI-FiPQH6QE7UV1hTDAu1FmlDQrtmSrwx9CLJYYpU8wiBEQ2qeJQgslZB0ZoEKqmBXXqqGZnVt2pS5uV6Q6gZt5zp64k2c0YUFTBqEr47CZMcHOEpSkzMLe_56CtxaWq6YzunbxBkgQAkpsACTDFmtSREbkTag71vNrJRP0LvyyGezFhIo65Kceq9ILvsVcn4wyMZwb95jYWF4e3VkTsf08q_CVApXkB9rJLMvCzTCow_zfgHdR7j_G6rslWQM2D6V-HxtpTSEInj5YGqPxNJ-pdwdqG73XmxXqXbX8pnjXz3pGNVNXtlKt6Qke8Ax598MY7rVYwjdVP_3fNiiqotrlDkq-7N2BcD8elFcEfh9XhUaFHpQuHmWHzPFOC-ESVWWeaKeZTIVY6e7wwTxQPQzeaPMAQYjqsq4e9j7zyrCAgRzzFQPYWwOczzK3sOkvvxWazcDzcwckVtJV_l65Xer2aymhlbAtloV7zshHE794iXRQsM_VFgC18RrD9WO2fPECZpvVALg7FBwlaj2_VlEkq6nNdW7PZBQGvfXxHC2kR7_h9dWF_sXXiN_p6hSgapNRHeAjz_DQOX2zrhqxQseCKY9oyqS2xjx7jWzQsZkekMr1mJB75jM",
  "refresh_token": "iUcm0YIPYZn4FpZoC+ZMvaIKTlE/MPbeTCI71YX7Thw1BYR9+ojin+/zyYywaqe9/gM8AxGRIrd2LjuFxvsY6uH3kcgZkQ78d1Hu59dRB0EZiDbLVFokyqM1FUEFECG96fgK9EpROF4H+ouGrkh3uK+aiB5d6eXtQzhSgDYKX+yb5mog1kOXSv4KbVXPpim4uOsqJ6+Rwb9lxMHT8j70mouz7SNaxyRo6fQTVmi8CwwJQDx8vY2fu1BsMFnBD8/21oljd37aDyrPN9i1eHC79zPBDHtISkI6bCtNOc8skFjFCaX7sYAPN3LTCX+mNvYCvxwStUzMmdFCLjfDxDtPjy7bKq3ScIbunHlUG3yadrDdxO+Z70OQYYlUwuVVwXLNuorETlj1r4rv+9Jp4K0O8Okk4kbnV9ZYFhxNatbuCL/cmk49mAsKutdZKO8gCEcckjbdwsEoRTNGlrNDQd6MFA/21EHzYmTKhBL3qPF5BUXWAt9/OjeRJSWwcxuUARkQmvgUCX8Arvrl5yxxZVJeG3CgXS86LDOyK7PvuWT79hf1uD6QOFbHD37WPPNt29sTBBeGCMH9h1S8rlMZHG5YmZT8SitArJv9yN73F1o+fgmF107rW/K5rpgFYq/zTpCGx/hmWR8bs7aOmOr7FkeEnTnimf4IXegOlVXpMK/O5+I="
}

2.获得用户基本信息
api/v1/user/profile
{
  "status": 1,
  "result": {
    "id": 1,
    "username": "shengwei",
    "truename": "盛巍",
    "mobile": "13825821001",
    "sex": 0,
    "area_id": 3,
    "area1": "江汉",
    "area2": "唐家墩街",
    "area3": "唐家墩派出所",
    "area4": "",
    "role": "jdy",
    "active": 1,
    "uuid": null,
    "avatar": null,
    "login_num": 0,
    "last_ip": null,
    "setting_path": null,
    "setting_msg": 1,
    "setting_distance": 3,
    "views": 0,
    "likes": 0,
    "likeme": 0,
    "nolinks": 0,
    "signs": 0,
    "cards": 0,
    "imgs": 0,
    "isauth": null,
    "level": 0,
    "remark": null,
    "last_at": null,
    "created_at": null,
    "updated_at": "2016-12-16 01:23:38",
    "deleted_at": null
  }
}

2.通过手机号查询个人信息，手机号码,名字等
api/v1/user/search –d ‘phone=13811123109'

{
  "status": 0,
  "msg": "记录不存在"
}
{
  "status": 1,
  "result": {
    "id": 1,
    "username": "shengwei",
    "truename": "盛巍",
    "mobile": "13825821001",
    "sex": 0,
    "area_id": 3,
    "area1": "江汉",
    "area2": "唐家墩街",
    "area3": "唐家墩派出所",
    "area4": "",
    "role": "jdy",
    "active": 1,
    "uuid": null,
    "avatar": null,
    "login_num": 0,
    "last_ip": null,
    "setting_path": null,
    "setting_msg": 1,
    "setting_distance": 3,
    "views": 0,
    "likes": 0,
    "likeme": 0,
    "nolinks": 0,
    "signs": 0,
    "cards": 0,
    "imgs": 0,
    "isauth": null,
    "level": 0,
    "remark": null,
    "last_at": null,
    "created_at": null,
    "updated_at": "2016-12-07 10:30:46",
    "deleted_at": null
  }
}

3.通过单位id查询单位所有信息的接口
api/v1/company/detail –d ‘id=1’

{
  "status": 0,
  "msg": "记录不存在"
}
{
  "status": 1,
  "result": {
    "id": 1,
    "company_id": 1,
    "user_id": null,
    "name": "湖北丽江饭店有限公司",
    "addr": "武昌区洪山体育馆路5号",
    "incharge": null,
    "dangerlevel": 1,
    "exitnum": null,
    "stairnum": null,
    "hasfacility": "1",
    "mainattribute": "01000",
    "subattribute": "#01110#",
    "companyid": "42001204001975",
    "orgid": "42000265-1",
    "shortname": "",
    "usename": "湖北丽江饭店有限公司",
    "cmptype": null,
    "otherdisp": null,
    "leaderdepart": "",
    "postcode": "",
    "phone": "",
    "fax": "",
    "email": "",
    "personum": null,
    "economy": null,
    "foundtime": null,
    "areaname": "420106",
    "artiname": "盛国政",
    "artiid": "",
    "artiphone": "13397161889、87136889",
    "managername": "刘涛",
    "managerid": "",
    "managerphone": null,
    "responname": "盛国政",
    "responid": "420106196501217654",
    "responphone": "",
    "fund": 0,
    "staffnum": 0,
    "area": "0.00",
    "buildheight": null,
    "buildarea": "0.00",
    "gastype": null,
    "firemannum": 0,
    "elevatornum": null,
    "lanenum": null,
    "lanepos": "",
    "refugenum": null,
    "refugearea": null,
    "refugepos": "",
    "nearby": "",
    "east": "丽江青莲酒家",
    "south": "",
    "west": "",
    "north": "",
    "remark": "",
    "cmystate": null,
    "status": 0,
    "updated_at": null,
    "created_at": "2011-08-07 15:33:56",
    "deleted_at": null
  }
}

4.所有单位的信息（经纬度坐标，名称，类别，id）
4.1一定经纬度范围内单位
api/v1/company/range
{
  "status": 1,
  "result": [
    {
      "id": 1,
      "name": "湖北丽江饭店有限公司",
      "lng": "114.333639",
      "lat": "30.548329",
      "vtype": 0,
      "special": 1
    },
    {
      "id": 1266,
      "name": "楚天都市花园",
      "lng": "114.337040",
      "lat": "30.548288",
      "vtype": 0,
      "special": 0
    }	]  }
4.2所有单位
api/v1/company/all
{"status":  0, 'msg': '记录不存在'}
{
  "status": 1,
  "result": [
    {
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
    },
    {
      "id": 2,
      "name": "知音传媒集团",
      "uuid": null,
      "pinyin_short": null,
      "pinyin": null,
      "area_id": 0,
      "create_uid": null,
      "m_id": null,
      "attr_id": null,
      "location": "114.352771,30.552955",
      "lng": "114.352771",
      "lat": "30.552955",
      "vtype": 0,
      "special": 1,
      "address": "东湖路169号",
      "leader": "绍德良",
      "phone": "",
      "place": null,
      "created_at": null,
      "updated_at": null,
      "deleted_at": null
    }  ]
}

5.添加单位/修改单位信息接口
说明：文员具有在手机APP端添加，更新已审查的社会单位的信息的权限。上述三项操作针对数据库中的company和company_detail表进行操作。
5.1添加单位
api/v1/company/create
{
  "status": 1, "result": "添加记录成功"
};
{
  "status": 0, "msg": "更新公司信息,创建公司信息详情成功"
};
{
  "status": 0, "msg": "更新公司信息及详情成功"
};
5.2更新单位信息
api/v1/company/update
{
  "status": 0, "msg": "记录不存在"
};
{
  "status": 1,
  "result": "更新单位信息成功"
}

6.修改个人密码接口
/api/v1/user/password
{
  "status": 1
}

7.获取待审核所有单位的列表（id，名称）	权限：文员，admin
说明：微信端社会单位自己录入的单位信息存在socials表中，手机APP端文员对该表中的社会单位信息进行审查，审查通过后单位信息复制至company和company_detail表中，social表中该记录的状态变为已审查。
api/v1/social/all
{
  "status": 0,  "msg": "记录不存在"
}
{
  "status": 1,
  "result": [
    {
      "id": 19,
      "name": "武汉XXX网络科技有限公司"
    }
  ]
}

8.通过id查询待审核单位的所有信息。并配有通过审核的接口。 权限：文员，admin
8.1待审核单位详细信息
api/v1/social/detail
{
  "status": 1,
  "result": {
    "id": 19,
    "company_id": 694,
    "user_id": 14152,
    "name": "武汉XXX网络科技有限公司",
    "addr": "中国 湖北 武汉市江夏区阳光大道圣宝龙工业园3栋",
    "incharge": 4,
    "dangerlevel": "2",
    "exitnum": 0,
    "stairnum": 6,
    "hasfacility": "1,3",
    "mainattribute": null,
    "subattribute": null,
    "companyid": "694",
    "orgid": "",
    "shortname": null,
    "usename": null,
    "cmptype": null,
    "otherdisp": null,
    "leaderdepart": "",
    "postcode": null,
    "phone": "",
    "fax": null,
    "email": null,
    "personum": null,
    "economy": null,
    "foundtime": "2010-01-14",
    "areaname": null,
    "artiname": "115555",
    "artiid": "22555",
    "artiphone": "3333333",
    "managername": "",
    "managerid": "",
    "managerphone": "",
    "responname": "",
    "responid": "",
    "responphone": "",
    "fund": null,
    "staffnum": 0,
    "area": "0.00",
    "buildheight": "12.56",
    "buildarea": "664.22",
    "gastype": null,
    "firemannum": 0,
    "elevatornum": 4,
    "lanenum": 10,
    "lanepos": "1,2",
    "refugenum": 10,
    "refugearea": null,
    "refugepos": "6666",
    "nearby": "",
    "east": null,
    "south": null,
    "west": null,
    "north": null,
    "remark": "",
    "cmystate": 0,
    "status": 0,
    "updated_at": "2016-12-06 21:04:24",
    "created_at": "2016-12-02 02:18:29",
    "deleted_at": null
  }
}

8.2社会单位审查通过
api/v1/social/pass
{
  "status": 1,
  "result": "社会单位审查通过"
}
{
  "status": 0,
  "msg": "社会单位已通过审查"
}
{
  "status": 0,
  "msg": "社会单位不存在"
}

9.问题反馈的提交接口
api/v1/guestbook/create
{
  "status": 1,
  "result": "问题反馈成功"
}

10.通过名称某一字段搜索单位，返回单位列表。
api/v1/company/search
{  "status": 0,  "msg": "无匹配单位"}
{
  "status": 1,
  "result": [
    {
      "name": "武汉市新洲区欢乐谷音乐茶座"
    },
    {
      "name": "武汉市蔡甸区幼儿园"
    },
    {
      "name": "武汉市丽红商业有限公司"
    },
    {
      "name": "武汉回民小学"
    }]}

11.行政许可的建审接口	权限：监督员，消防窗口，admin
说明：name在company表中必须唯一，如果不唯一会创建失败并返回错误
api/v1/license/create
{
  "status": 1,
  "result": "添加记录成功"
}

12.行政许可的验收接口	权限：监督员，消防窗口，admin
api/v1/license/complete
{
  "status": 0,
  "msg": "id对应行政许可记录不存在"
}
{
  "status": 1,
  "result": "提交行政许可验收信息成功"
}

13.行政许可的开业前接口		权限：监督员，消防窗口，admin
api/v1/license/open
{
  "status": 0,
  "msg": "id对应行政许可记录不存在"
}
{
  "status": 1,
  "result": "提交行政许可开业信息成功"
}


CREATE TABLE `company_detail` (
  `id` bigint(15) unsigned NOT NULL AUTO_INCREMENT,
  `company_id` int(11) DEFAULT NULL,
  `user_id` int(10) DEFAULT NULL COMMENT '创建人',
  `name` varchar(255) DEFAULT NULL COMMENT '单位名称',
  `addr` varchar(255) DEFAULT NULL COMMENT '单位地址',
  `incharge` int(11) DEFAULT NULL COMMENT '消防管辖',
  `dangerlevel` tinyint(4) DEFAULT NULL COMMENT '火灾危险性',
  `exitnum` int(11) DEFAULT NULL COMMENT '安全出口数',
  `stairnum` int(11) DEFAULT NULL COMMENT '疏散楼梯数',
  `hasfacility` varchar(10) DEFAULT NULL COMMENT '自动消防设施',
  `mainattribute` varchar(100) DEFAULT NULL COMMENT '单位属性主类',
  `subattribute` varchar(100) DEFAULT NULL COMMENT '单位属性子类',
  `companyid` varchar(20) DEFAULT NULL COMMENT '单位编码',
  `orgid` varchar(20) DEFAULT NULL COMMENT '组织机构代码',
  `shortname` varchar(20) DEFAULT NULL COMMENT '拼音简称',
  `usename` varchar(255) DEFAULT NULL COMMENT '使用名称',
  `cmptype` varchar(50) DEFAULT NULL COMMENT '单位类型',
  `otherdisp` varchar(255) DEFAULT NULL COMMENT '单位其他情况',
  `leaderdepart` varchar(255) DEFAULT NULL COMMENT '上级单位',
  `postcode` varchar(20) DEFAULT NULL COMMENT '邮政编码',
  `phone` varchar(30) DEFAULT NULL COMMENT '单位电话',
  `fax` varchar(50) DEFAULT NULL COMMENT '单位传真',
  `email` varchar(50) DEFAULT NULL COMMENT 'E-Mail',
  `personum` int(11) DEFAULT NULL COMMENT '营业时最大人数',
  `economy` varchar(255) DEFAULT NULL COMMENT '经济所有制',
  `foundtime` date DEFAULT NULL COMMENT '单位成立时间',
  `areaname` varchar(50) DEFAULT NULL COMMENT '行政区域',
  `artiname` varchar(30) DEFAULT NULL COMMENT '法定代表人姓名 ',
  `artiid` varchar(30) DEFAULT NULL COMMENT '法定代表人身份证/其他证件',
  `artiphone` varchar(20) DEFAULT NULL COMMENT '法定代表人联系电话',
  `managername` varchar(30) DEFAULT NULL COMMENT '消防安全管理人姓名 ',
  `managerid` varchar(30) DEFAULT NULL COMMENT '消防安全管理人身份证/其他证件',
  `managerphone` varchar(20) DEFAULT NULL COMMENT '消防安全管理人联系电话',
  `responname` varchar(30) DEFAULT NULL COMMENT '消防安全责任人姓名 ',
  `responid` varchar(30) DEFAULT NULL COMMENT '消防安全责任人身份证/其他证件',
  `responphone` varchar(20) DEFAULT NULL COMMENT '消防安全责任人联系电话',
  `fund` int(11) DEFAULT NULL COMMENT '固定资产（万元）',
  `staffnum` int(11) DEFAULT NULL COMMENT '职工人数',
  `area` decimal(10,2) DEFAULT NULL COMMENT '占地面积（平方米）',
  `buildheight` decimal(10,2) DEFAULT NULL,
  `buildarea` decimal(10,2) DEFAULT NULL COMMENT '建筑面积（平方米）',
  `gastype` varchar(20) DEFAULT NULL COMMENT '燃气类型',
  `firemannum` int(11) DEFAULT NULL COMMENT '单位专职（志愿）消防员数',
  `elevatornum` int(11) DEFAULT NULL COMMENT '消防电梯数',
  `lanenum` int(11) DEFAULT NULL COMMENT '消防车道数',
  `lanepos` varchar(255) DEFAULT NULL COMMENT '消防车道位置',
  `refugenum` int(11) DEFAULT NULL COMMENT '避难层数',
  `refugearea` decimal(10,2) DEFAULT NULL COMMENT '避难层总面积',
  `refugepos` varchar(255) DEFAULT NULL COMMENT '避难层位置',
  `nearby` varchar(255) DEFAULT NULL COMMENT '单位毗邻情况',
  `east` varchar(255) DEFAULT NULL COMMENT '毗邻单位东',
  `south` varchar(255) DEFAULT NULL COMMENT '毗邻单位南',
  `west` varchar(255) DEFAULT NULL COMMENT '毗邻单位西',
  `north` varchar(255) DEFAULT NULL COMMENT '毗邻单位北',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `cmystate` int(11) DEFAULT NULL COMMENT '单位状态 0重点 1非重点 2非社会单位',
  `status` tinyint(1) DEFAULT '0' COMMENT '0 未审核 1 审核',
  `updated_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12290 DEFAULT CHARSET=utf8;

     */
}
