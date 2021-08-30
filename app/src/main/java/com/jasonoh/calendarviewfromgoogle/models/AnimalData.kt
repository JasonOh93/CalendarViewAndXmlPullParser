package com.jasonoh.calendarviewfromgoogle.models

data class AnimalData(var cityName: String = "Seoul", var cityCode: String = "000010", var aaa: String = "000010") {
}

data class AnimalDataFromCity(var cityName: String = "Seoul", var cityCode: String = "000010")

data class AnimalDataFull(
    var age: String = "age",
    var careAddr: String = "careAddr",
    var careNm: String = "careNm",
    var careTel: String = "careTel",
    var chargeNm: String = "chargeNm",
    var colorCd: String = "colorCd",
    var desertionNo: String = "desertionNo",
    var filename: String = "filename",
    var happenDt: String = "happenDt",
    var happenPlace: String = "happenPlace",
    var kindCd: String = "kindCd",
    var neuterYn: String = "neuterYn",
    var noticeEdt: String = "noticeEdt",
    var noticeNo: String = "noticeNo",
    var noticeSdt: String = "noticeSdt",
    var officetel: String = "officetel",
    var orgNm: String = "orgNm",
    var popfile: String = "popfile",
    var processState: String = "processState",
    var sexCd: String = "sexCd",
    var specialMark: String = "specialMark",
    var weight: String = "weight"
)